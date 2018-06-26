/*
 * Copyright (C) 2018 chymes.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.hymesruzicka.maptool.tools.token;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import org.hymesruzicka.maptool.synthetic.TokenElementDelegate;
import org.hymesruzicka.maptool.tools.CampaignXOM;
import rptools.maptool.campaign.content.DandDDadsProperiesText;

/**
 *
 * @author chymes
 */
public class TokenProcessing {

    private static final Logger LOG = Logger.getLogger(TokenProcessing.class.getName());
    private static final DandDDadsProperiesText DEFAULT_REPLACEMENT_SOURCE;

    /**
     * The name of the MapTool XML Element "Token"
     */
    public static final String TOKEN_NODE_NAME = "net.rptools.maptool.model.Token";

    /**
     * The name of the DandDDads propertyType "Character"
     */
    public static final String PROPERTY_NAME_CHARACTER = "Character";

    /**
     * The name of the DandDDads propertyType "NPC"
     */
    public static final String PROPERTY_NAME_NPC = "NPC";

    /**
     * The name of the MapTool propertyType "Basic"
     */
    public static final String PROPERTY_NAME_BASIC = "Basic";
    private static final String PROPERTY_NAME_MARKER = "Marker";
    private static final String PROPERTY_NAMES_DANDDDADS[] = {PROPERTY_NAME_CHARACTER, PROPERTY_NAME_NPC};
    private static final String PROPERTY_NAME_STOCK[] = {PROPERTY_NAME_BASIC, PROPERTY_NAME_MARKER};

    /**
     * Prefix for processed file name
     */
    public static final String PROCESSED_PREFIX = "content_";

    /**
     * Postfix a.k.a Suffix for processed file name
     */
    public static final String PROCESSED_POSTFIX = ".xml";

    static {
        DandDDadsProperiesText source = null;
        try {
            source = new DandDDadsProperiesText(DandDDadsProperiesText.DEFAULT_NPC_CIVILIAN_FILE);
        } catch (ParsingException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        DEFAULT_REPLACEMENT_SOURCE = source;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TokenProcessing tokenProcessor;
        try {
            tokenProcessor = new TokenProcessing(null, null);
            tokenProcessor.process();
        } catch (ParsingException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private final DandDDadsProperiesText _replacementSource;
    private String _destDirName = "";
    private Path _destDirPath = Paths.get(_destDirName);
    private CampaignXOM _campaignXOM;
    private Path _processedFile = null;

    /**
     * Processes a MapTool XML file, by calling the <code>process</code> method
     * on its content.
     *
     * @param originalXMLFileName The MapTool XML file to modify.
     * @param replacementXMLFileName The source of the replacement XML content.
     * @throws nu.xom.ParsingException
     * @throws IOException
     *
     */
    public TokenProcessing(String originalXMLFileName, String replacementXMLFileName) throws ParsingException, IOException {
        DandDDadsProperiesText replacementSource = null;
        if (replacementXMLFileName != null && !replacementXMLFileName.isEmpty()) {
            Path replacementSourceFile = Paths.get(replacementXMLFileName);
            if (!Files.exists(replacementSourceFile)) {
                throw new IOException("Could not find" + replacementXMLFileName);
            }
            replacementSource = new DandDDadsProperiesText(replacementSourceFile);
        } else {
            replacementSource = DEFAULT_REPLACEMENT_SOURCE;
        }
        _replacementSource = replacementSource;

        if (getReplacementSource() == null) {
            throw new ParsingException("Replacement source not loaded.");
        }
        if (originalXMLFileName != null && !originalXMLFileName.isEmpty()) {
            _campaignXOM = new CampaignXOM(originalXMLFileName);
        } else {
            _campaignXOM = new CampaignXOM();
        }
    }

    /**
     * Getter for the replacementSource property, as set during construction. This
     * object provides an instance of the XOM document of the replacement XML.
     *
     * @return replacementSource value. 
     */
    public final DandDDadsProperiesText getReplacementSource() {
        return _replacementSource;
    }

    /**
     * Get the value of _destDirPath
     *
     * @return the value of _destDirPath
     */
    public Path getDestDirPath() {
        return _destDirPath;
    }

    /**
     * Set the value of _destDirPath
     *
     * @param destDirPath new value of _destDirPath
     */
    public void setDestDirPath(Path destDirPath) {
        this._destDirPath = destDirPath;
    }

    /**
     * Get the value of _destDirName
     *
     * @return the value of _destDirName
     */
    public String getDestDirName() {
        return _destDirName;
    }

    /**
     * Set the value of _destDirName
     *
     * @param destDirName new value of _destDirName
     */
    public void setDestDirName(String destDirName) {
        this._destDirName = destDirName;
    }

    /**
     * Getter for campaignXOM document property.
     *
     * @return
     */
    private final CampaignXOM getCampaignXOM() {
        return _campaignXOM;
    }

    /**
     * <p>
     * Searches for all XML Elements named "net.rptools.maptool.model.Token",
     * where:</p>
     * <ul>
     * <li>The "reference" attribute is null or empty.</li>
     * <li>There is a "name" child Element with a value</li>
     * <li>The "name" value does not contain the string "Lib:"</li>
     * </ul>
     * <p>
     * If that Token has a child Element "propertyType" with a value of "Basic",
     * then any existing "propertyMapCI" Element is replaced.</p><p>
     */
    public void process() {
        TokenElementDelegate campaignToken;
        for (Element tokenElement : getTokenElements()) {
            campaignToken = new TokenElementDelegate(tokenElement);
            String reference = campaignToken.getReference();
            String tokenName = campaignToken.getName();
            if ((reference == null || reference.isEmpty())
                    && tokenName != null
                    && (!tokenName.isEmpty())
                    && (!tokenName.contains("Lib:"))) {

                String propertyTypeName = campaignToken.getPropertyType();
                switch (propertyTypeName) {
                    case PROPERTY_NAME_BASIC: {
                        replace(campaignToken);
                        break;
                    }
                    case PROPERTY_NAME_CHARACTER:
                    case PROPERTY_NAME_NPC: {
                        LOG.log(Level.FINE, "ignoring {0}", new Object[]{tokenName});
                        break;
                    }
                    default: {
                        LOG.log(Level.WARNING, "unrecognized propertyType {0} in {1}", new Object[]{propertyTypeName, tokenName});
                        break;
                    }
                }
            }
        }

        StringBuilder freshName = new StringBuilder(PROCESSED_PREFIX);
        Long now = new Timestamp(System.currentTimeMillis()).getTime();//Yeah, redundant.
        String nowString = Long.toString(now);
        freshName.append(nowString);
        freshName.append(PROCESSED_POSTFIX);
        setProcessedFile(getDestDirPath().resolve(freshName.toString()));
        try (FileOutputStream destOutputStream = new FileOutputStream(getProcessedFile().toFile())) {
            Serializer outputter = new Serializer(destOutputStream);
            outputter.setIndent(4);
            outputter.write(getCampaignXOM().getXomDocument());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * <p>
     * The replacement is obtained from the <code>getPropertyMapCIElement</code>
     * method of a <code>DandDDadsProperiesText</code> instance, and that XML
     * currently has the properties of an unarmed civilian NPC.</p>
     *
     * @param campaignToken The token whose propertyMapCI XML Element will be
     * replaced.
     * @return The replacement propertyMapCI XML Element.
     */
    protected Element replace(TokenElementDelegate campaignToken) {
        String tokenName = campaignToken.getName();
        LOG.log(Level.FINE, "replacing propertyMapCI Element of {0}", new Object[]{tokenName});
        Element tokenXMLElement = campaignToken.getSourceElement();
        Element propertyMapCIElement_basic = campaignToken.getPropertyMapCIElement();
        Element propertyMapCIElement_civilian = getReplacementSource().getPropertyMapCIElement();
        int childPos = tokenXMLElement.indexOf(propertyMapCIElement_basic);
        if (childPos < 0) {
            throw new IllegalStateException("Could not correctly determine position of propertyMapCIElement_basic in " + tokenName);
        }
        tokenXMLElement.removeChild(propertyMapCIElement_basic);
        tokenXMLElement.insertChild(propertyMapCIElement_civilian, childPos);
        LOG.log(Level.FINE, "Attempting to set \"propertyType\" of {0} to {1}", new String[]{tokenName, PROPERTY_NAME_NPC});
        campaignToken.setPropertyType(PROPERTY_NAME_NPC);
        campaignToken.readAll();
        LOG.finer(campaignToken.getPropertyMapCI());
        LOG.log(Level.FINE, " \"propertyType\" of {0} is now  {1}", new String[]{tokenName, campaignToken.getPropertyType()});
        if (!PROPERTY_NAME_NPC.equals(campaignToken.getPropertyType())) {
            LOG.log(Level.SEVERE, " \"propertyType\" of {0} is now  {1}", new String[]{tokenName, campaignToken.getPropertyType()});
        }
        return propertyMapCIElement_civilian;
    }

    /**
     * Finds token Elements of this campaignXOM by recursively searching for
     * nodes named with <code>TOKEN_NODE_NAME</code>. Currently
     * "net.rptools.maptool.model.Token"
     *
     * @return
     */
    public final List<Element> getTokenElements() {
        List<Element> result;
        if (getCampaignXOM().getXomDocument() == null) {
            getCampaignXOM().build();
        }
        if (getCampaignXOM().getXomDocument() == null) {
            throw new IllegalStateException("Failed to build Campaign XML Document from " + getCampaignXOM().getInputFilePath().toString());
        }
        result = new ArrayList<>();
        Element current = getCampaignXOM().getXomDocument().getRootElement();
        getTokenElements(result, current, 1);
        return result;
    }

    private List<Element> getTokenElements(List<Element> resultIn, Element current, int depth) {
        if (TOKEN_NODE_NAME.equalsIgnoreCase(current.getLocalName())) {
            LOG.log(Level.FINE, "{0}", current.getQualifiedName());
            resultIn.add(current);
        } else {
            LOG.log(Level.FINER, "{0}", current.getQualifiedName());
        }
        Elements childElements = current.getChildElements();
        if (childElements.size() > 0) {
            int size = childElements.size();
            LOG.log(Level.FINER, "{0} has {1} children.", new Object[]{current.getQualifiedName(), Integer.toString(size)});
            for (int i = 0; i < size; i++) {
                Element child = childElements.get(i);
                getTokenElements(resultIn, child, depth + 1);
            }
        } else {
            LOG.log(Level.FINER, "No child nodes in {0}", current.getQualifiedName());
        }
        return resultIn;
    }

    /**
     * Getter for processedFile property. After calling process, this property
     * contains the file generated by <code>process</code>. If it is still null
     * after calling <code>process</code>, there was an error during processing.
     */
    public final Path getProcessedFile() {
        return this._processedFile;
    }

    /**
     * @param processedFile the _processedFile to set
     */
    private void setProcessedFile(Path processedFile) {
        this._processedFile = processedFile;
    }

}
