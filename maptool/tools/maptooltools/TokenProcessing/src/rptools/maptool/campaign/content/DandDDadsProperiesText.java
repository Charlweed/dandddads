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
import java.nio.file.Paths;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

/**
 *
 * @author chymes
 */
public class DandDDadsProperiesText {

    private static final Logger LOG = Logger.getLogger(DandDDadsProperiesText.class.getName());
    public static final Path DEFAULT_NPC_CIVILIAN_FILE;
    private Document _xomDocument = null;
    private final Path _replacementXMLSourceFile;

    static {
        Path defaultNPCCivilianFile = Paths.get(System.getProperty("user.dir", ""), "danddads_properties_npc_civilian.xml");
        String osName = System.getProperty("os.name").toLowerCase(Locale.US);
        String userName = System.getProperty("user.name").toLowerCase(Locale.US);
        if (userName.contains("hymes")) {
            Path authorPrefix = Paths.get(System.getProperty("user.home"), "projects", "hymerfania");
            defaultNPCCivilianFile = Paths.get(authorPrefix.toString(), "MapToolTools", "TokenProcessing", "src", "rptools", "maptool", "campaign", "content", "danddads_properties_npc_civilian.xml");
        }
        DEFAULT_NPC_CIVILIAN_FILE = defaultNPCCivilianFile;
    }

    public DandDDadsProperiesText(Path replacementXMLSourceFile) throws ParsingException, ValidityException, IOException {
        _replacementXMLSourceFile = replacementXMLSourceFile;
        build();
        if (_xomDocument == null) {
            throw new IllegalStateException("Failed to parse XML snip file.");
        }
    }

    private final DandDDadsProperiesText build() throws ParsingException, ValidityException, IOException {
        if (_xomDocument != null) {
            throw new IllegalStateException("Campaign XOM my only be built once.");
        }
        Builder parser = new Builder();
        _xomDocument = parser.build(getReplacementXMLSourceFile().toFile());
        LOG.log(Level.FINER, "{0} parsed.", getReplacementXMLSourceFile().toString());
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

    public Element getPropertyMapCIElement() {
        return new Element(_xomDocument.getRootElement());
    }

    /**
     * @return the _replacementXMLSourceFile
     */
    public final Path getReplacementXMLSourceFile() {
        return _replacementXMLSourceFile;
    }
}
