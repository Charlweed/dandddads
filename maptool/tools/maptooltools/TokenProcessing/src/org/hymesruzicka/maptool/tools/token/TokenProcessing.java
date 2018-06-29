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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import org.hymesruzicka.maptool.synthetic.TokenElementDelegate;
import org.hymesruzicka.maptool.tools.MapToolXOM;
import org.hymesruzicka.maptool.tools.Shelf;
import static org.hymesruzicka.maptool.tools.Shelf.PROCESSED_POSTFIX;
import static org.hymesruzicka.maptool.tools.Shelf.PROCESSED_PREFIX;
import static org.hymesruzicka.maptool.tools.Shelf.PROPERTY_NAME_BASIC;
import static org.hymesruzicka.maptool.tools.Shelf.PROPERTY_NAME_CHARACTER;
import static org.hymesruzicka.maptool.tools.Shelf.PROPERTY_NAME_NPC;
import static org.hymesruzicka.maptool.tools.Shelf.TOKEN_NODE_NAME;
import static org.hymesruzicka.maptool.tools.Utility.elementsByName;
import static org.hymesruzicka.maptool.tools.token.TokenProcessing.TokenProcess.REPLACE_PROPERTY_SET;
import rptools.maptool.campaign.content.DandDDadsMacroSetTokenNPCText;
import rptools.maptool.campaign.content.DandDDadsProperiesText;

/**
 *
 * @author chymes
 */
public class TokenProcessing {

    private static final Logger LOG = Logger.getLogger(TokenProcessing.class.getName());

    /*Could be equals, but I may upgreade this later.*/
    private static boolean isModifyTarget(String propertyTypeName) {
        boolean result = false;
        switch (propertyTypeName) {
            case PROPERTY_NAME_BASIC: {
                result = true;
                break;
            }
            case PROPERTY_NAME_CHARACTER:
            case PROPERTY_NAME_NPC: {
                break;
            }
            default: {
                LOG.log(Level.WARNING, "unrecognized propertyType {0} ", new String[]{propertyTypeName});
                break;
            }
        }
        return result;
    }

    private DandDDadsProperiesText _replacementSource;
    private DandDDadsMacroSetTokenNPCText _macroSetSource;
    private Path _destDirPath = Paths.get(Shelf.WORK_DIR_NAME_DEFAULT);
    private MapToolXOM _campaignXOM;
    private Path _processedFile = null;

    /**
     * Processes a MapTool XML file, by calling the <code>process</code> method
     * on its content.
     *
     * @param originalXMLFileName The MapTool XML file to modify.
     * @param replacementXMLFileName The source of the replacement XML content.
     * @param macroSetXMLFileName The source of the macroSet XML content.
     * @throws nu.xom.ParsingException
     * @throws IOException
     *
     */
    public TokenProcessing(String originalXMLFileName, String replacementXMLFileName, String macroSetXMLFileName) throws ParsingException, IOException {
        readRelacementPropertiesSource(replacementXMLFileName);
        if (getReplacementSource() == null) {
            throw new ParsingException("Replacement source not loaded.");
        }

        readMacrosetSource(macroSetXMLFileName);
        if (getMacroSetSource() == null) {
            throw new ParsingException("macroSet source not loaded.");
        }

        if (originalXMLFileName != null && !originalXMLFileName.isEmpty()) {
            _campaignXOM = new MapToolXOM(originalXMLFileName);
        } else {
            _campaignXOM = new MapToolXOM();
        }
    }

    /**
     * Getter for the replacementSource property, as set during construction.
     * This object provides an instance of the XOM document of the replacement
     * XML.
     *
     * @return replacementSource value.
     */
    public final DandDDadsProperiesText getReplacementSource() {
        return _replacementSource;
    }

    /**
     * Getter for the macroSetSource property, as set during construction. This
     * object provides an instance of the XOM document of the macroSet XML.
     *
     * @return macroSetSource value.
     */
    public final DandDDadsMacroSetTokenNPCText getMacroSetSource() {
        return _macroSetSource;
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
    public final void setDestDirPath(Path destDirPath) {
        this._destDirPath = destDirPath;
    }

    /**
     * Getter for campaignXOM document property.
     *
     * @return
     */
    private final MapToolXOM getCampaignXOM() {
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
     * @param processList the value of processEnum
     */
    public void process(List<TokenProcessing.TokenProcess> processList) {
        TokenElementDelegate campaignToken;
        for (Element tokenElement : getTokenElements()) {
            campaignToken = new TokenElementDelegate(tokenElement);
            String reference = campaignToken.getReference();
            String tokenName = campaignToken.getName();
            if ((reference == null || reference.isEmpty())
                    && tokenName != null
                    && (!tokenName.isEmpty())
                    && (!tokenName.contains("Lib:"))) {
                if (isModifyTarget(campaignToken.getPropertyType())) {
                    for (TokenProcess currentProcess : processList) {
                        switch (currentProcess) {
                            case REPLACE_PROPERTY_SET:
                                LOG.log(Level.FINE, "Performing {0} on \"{1}\"", new String[]{currentProcess.toString(), tokenName});
                                replaceTokenProperties(campaignToken);
                                break;
                            case REPLACE_MACRO_SET:
                                LOG.log(Level.FINE, "Performing {0} on \"{1}\"", new String[]{currentProcess.toString(), tokenName});
                                replaceTokenMacros(campaignToken);
                                break;
                            default:
                                throw new AssertionError(currentProcess.name());
                        }
                    }
                    assignTokenPropertyType(campaignToken, PROPERTY_NAME_NPC);
                }
            }
        }
        try {
            if (Files.exists(getDestDirPath())) {
                if (!Files.isDirectory(getDestDirPath())) {
                    throw new IOException("Existing " + getDestDirPath().toString() + " is not a directory.");
                }
            } else {
                boolean mkdirs = getDestDirPath().toFile().mkdirs(); //test return value later.
            }
            StringBuilder freshName = new StringBuilder(PROCESSED_PREFIX);
            Long now = new Timestamp(System.currentTimeMillis()).getTime();//Yeah, redundant.
            String nowString = Long.toString(now);
            freshName.append(nowString);
            freshName.append(PROCESSED_POSTFIX);
            setProcessedFile(getDestDirPath().resolve(freshName.toString()));
            /*Surprised that failure to write here does not throw exception.*/
            try (FileOutputStream destOutputStream = new FileOutputStream(getProcessedFile().toFile())) {
                Serializer outputter = new Serializer(destOutputStream);
                outputter.setIndent(4);
                outputter.write(getCampaignXOM().getXomDocument());
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Assigns the propertyTypeName to the provided TokenElementDelegate.
     *
     * @param campaignToken The TokenElementDelegate who's value will be
     * changed.
     * @param propertyTypeName The propertyTypeName value, I.E. Basic "NPC",
     * "Charater", etc.
     * @return The same TokenElementDelegate that was provided.
     */
    public TokenElementDelegate assignTokenPropertyType(TokenElementDelegate campaignToken, String propertyTypeName) {
        String tokenName = campaignToken.getName();
        LOG.log(Level.FINE, "Attempting to set \"propertyType\" of {0} to {1}", new String[]{tokenName, propertyTypeName});
        campaignToken.setPropertyType(propertyTypeName);
        campaignToken.readAll();
        if (!propertyTypeName.equals(campaignToken.getPropertyType())) {
            LOG.log(Level.SEVERE, " \"propertyType\" of {0} is now  {1}", new String[]{tokenName, campaignToken.getPropertyType()});
        }
        return campaignToken;
    }

    /**
     * <p>
     * The replacement is obtained from the <code>getPropertyMapCIElement</code>
     * method of a <code>DandDDadsProperiesText</code> instance.</p>
     *
     * @param campaignToken The token whose propertyMapCI XML Element will be
     * replaced.
     * @return The replacement propertyMapCI XML Element.
     */
    protected Element replaceTokenProperties(TokenElementDelegate campaignToken) {
        String tokenName = campaignToken.getName();
        LOG.log(Level.FINE, "replacing propertyMapCI Element of {0}", new Object[]{tokenName});
        Element tokenXMLElement = campaignToken.getSourceElement();
        Element propertyMapCIElement_basic = campaignToken.getPropertyMapCIElement();
        int childPos = tokenXMLElement.indexOf(propertyMapCIElement_basic);
        if (childPos < 0) {
            throw new IllegalStateException("Could not correctly determine position of propertyMapCIElement in " + tokenName);
        }
        tokenXMLElement.removeChild(propertyMapCIElement_basic);
        Element propertyMapCIElement_civilian = getReplacementSource().getPropertyMapCIElement();
        tokenXMLElement.insertChild(propertyMapCIElement_civilian, childPos);
        campaignToken.readAll();
        return propertyMapCIElement_civilian;
    }

    /**
     * <p>
     * The replacement is obtained from the <code>getMacroPropertiesMap</code>
     * method of a <code>DandDDadsMacroSetTokenNPCText</code> instance.</p>
     *
     * @param campaignToken The token that gets the insertion.
     * @return The fresh MacroSetElement XML Element.
     */
    protected Element replaceTokenMacros(TokenElementDelegate campaignToken) {
        String tokenName = campaignToken.getName();
        LOG.log(Level.FINE, "inserting macroSetElement Element into {0}", new String[]{tokenName});
        Element tokenXMLElement = campaignToken.getSourceElement();
        Element macrosElement_empty = campaignToken.getMacroPropertiesMapElement();
        int childPos = tokenXMLElement.indexOf(macrosElement_empty);
        if (childPos < 0) {
            throw new IllegalStateException("Could not correctly determine position of macroPropertiesMapElement in " + tokenName);
        }
        tokenXMLElement.removeChild(macrosElement_empty);
        Element macroPropertiesMapElement = getMacroSetSource().getMacroPropertiesMap();
        tokenXMLElement.insertChild(macroPropertiesMapElement, childPos);
        campaignToken.readAll();
        return macroPropertiesMapElement;
    }

    /**
     * Finds token Elements of this campaignXOM by recursively searching for
     * nodes named with <code>TOKEN_NODE_NAME</code>. Currently
     * "net.rptools.maptool.model.Token"
     *
     * @return
     */
    public final List<Element> getTokenElements() {
        if (getCampaignXOM().getXomDocument() == null) {
            getCampaignXOM().build();
        }
        if (getCampaignXOM().getXomDocument() == null) {
            throw new IllegalStateException("Failed to build Campaign XML Document from " + getCampaignXOM().getInputFilePath().toString());
        }
        Element root = getCampaignXOM().getXomDocument().getRootElement();
        return elementsByName(root, TOKEN_NODE_NAME);
    }

    /**
     * Getter for processedFile property.After calling process, this property
     * contains the file generated by <code>process</code>. If it is still null
     * after calling <code>process</code>, there was an error during processing.
     *
     * @return The processedFile
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

    private void readRelacementPropertiesSource(String replacementXMLFileName) throws IOException, ParsingException {
        DandDDadsProperiesText replacementSource;
        if (replacementXMLFileName == null || replacementXMLFileName.isEmpty()) {
            throw new IllegalArgumentException("replacementXMLFileName cannot be null nor empty.");
        }
        Path replacementSourceFile = Paths.get(replacementXMLFileName);
        if (!Files.exists(replacementSourceFile)) {
            throw new IOException("Could not find" + replacementXMLFileName);
        }
        replacementSource = new DandDDadsProperiesText(replacementSourceFile);

        _replacementSource = replacementSource;
    }

    private void readMacrosetSource(String macroSetXMLFileName) throws IOException, ParsingException {
        DandDDadsMacroSetTokenNPCText macroSetSource;
        if (macroSetXMLFileName == null || macroSetXMLFileName.isEmpty()) {
            throw new IllegalArgumentException("macroSetXMLFileName cannot be null nor empty.");
        }

        Path macroSetSourceFile = Paths.get(macroSetXMLFileName);
        if (!Files.exists(macroSetSourceFile)) {
            throw new IOException("Could not find" + macroSetXMLFileName);
        }
        macroSetSource = new DandDDadsMacroSetTokenNPCText(macroSetSourceFile);

        _macroSetSource = macroSetSource;
    }

    public static enum TokenProcess {
        REPLACE_PROPERTY_SET, REPLACE_MACRO_SET;
        public static final List<TokenProcess> PROPERTIES = Collections.unmodifiableList(Arrays.asList(new TokenProcess[]{REPLACE_PROPERTY_SET}));
        public static final List<TokenProcess> MACROS = Collections.unmodifiableList(Arrays.asList(new TokenProcess[]{REPLACE_MACRO_SET}));
        public static final List<TokenProcess> ALL_PROCESSES = Collections.unmodifiableList(Arrays.asList(values()));
    }

}
