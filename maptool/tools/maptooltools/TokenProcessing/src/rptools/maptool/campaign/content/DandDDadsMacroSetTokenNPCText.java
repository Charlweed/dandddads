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
package rptools.maptool.campaign.content;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.hymesruzicka.maptool.tools.Shelf;
import static org.hymesruzicka.maptool.tools.Shelf.INDEX_NODE_NAME;
import static org.hymesruzicka.maptool.tools.Utility.elementsByName;

/**
 *
 * @author chymes
 */
public class DandDDadsMacroSetTokenNPCText {

    private static final Logger LOG = Logger.getLogger(DandDDadsMacroSetTokenNPCText.class.getName());
    private Document _xomDocument = null;
    private final Path _XMLSourceFile;

    public DandDDadsMacroSetTokenNPCText(Path xmlSourceFile) throws ParsingException, ValidityException, IOException {
        _XMLSourceFile = xmlSourceFile;
        build();
        if (_xomDocument == null) {
            throw new IllegalStateException("Failed to parse XML snip file.");
        }
    }

    private final DandDDadsMacroSetTokenNPCText build() throws ParsingException, ValidityException, IOException {
        if (_xomDocument != null) {
            throw new IllegalStateException("Campaign XOM my only be built once.");
        }
        Builder parser = new Builder();
        _xomDocument = parser.build(getXMLSourceFile().toFile());
        LOG.log(Level.FINER, "{0} parsed.", getXMLSourceFile().toString());
        return this;
    }

    /**
     * Get the value of _xomDocument
     *
     * @return the value of _xomDocument
     */
    public Document getXomDocument() {
        return _xomDocument;
    }

    public Element getMacroSetListElement() {
        return new Element(_xomDocument.getRootElement());
    }

    public Element getMacroPropertiesMap(){
        Element result = new Element("macroPropertiesMap");
        for(Element entry : getMacroEntries()){
            result.appendChild(entry);
        }        
        return result;
    }
    
    public List<Element> getMacroEntries() {
        List<Element> raws = getMacrosRaw();
        List<Element> result = new ArrayList<>(raws.size());
        for(Element raw : raws){
            result.add(asEntry(raw));
        }
        return result;
    }

    private List<Element> getMacrosRaw() {
        if (getXomDocument() == null) {
            try {
                build();
            } catch (ParsingException | IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        if (getXomDocument() == null) {
            throw new IllegalStateException("Failed to build Campaign XML Document from " + getXMLSourceFile().toString());
        }
        Element root = getXomDocument().getRootElement();
        return elementsByName(root, Shelf.MACRO_BUTTON_NODE_NAME);
    }

    public Element asEntry(Element macroElement) {
        Element intElement = new Element("int");
        List<Element> indexElements = elementsByName(macroElement, INDEX_NODE_NAME);
        if (indexElements.isEmpty()) {
            throw new IllegalStateException("Could not read index of element.");
        }
        String indexValue = indexElements.get(0).getValue();
//        int index = Integer.parseInt(indexValue);
        intElement.appendChild(indexValue);
        Element entryElement = new Element("entry");
        entryElement.appendChild(intElement);
        entryElement.appendChild(new Element(macroElement));
        return entryElement;
    }

    /**
     * @return the _XMLSourceFile
     */
    public final Path getXMLSourceFile() {
        return _XMLSourceFile;
    }
}
