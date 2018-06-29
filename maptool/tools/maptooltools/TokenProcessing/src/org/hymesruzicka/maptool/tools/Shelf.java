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
package org.hymesruzicka.maptool.tools;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 *
 * @author chymes
 */
public class Shelf {

    private static final Logger LOG = Logger.getLogger(Shelf.class.getName());
    /**
     * Prefix for processed file name
     */
    public static final String PROCESSED_PREFIX = "content_";
    /**
     * Postfix a.k.a Suffix for processed file name
     */
    public static final String PROCESSED_POSTFIX = ".xml";
    /**
     * Postfix a.k.a Suffix for processed file name
     */
    public static final String DTD_POSTFIX = ".dtd";
    /**
     * The name of the MapTool XML Element "Token"
     */
    public static final String TOKEN_NODE_NAME = "net.rptools.maptool.model.Token";

    /**
     * The name of the MapTool XML Element "MacroButton"
     */
    public static final String MACRO_BUTTON_NODE_NAME = "net.rptools.maptool.model.MacroButtonProperties";
    
    /**
     * The name of the MapTool XML Element "MacroButton"
     */
    public static final String INDEX_NODE_NAME = "index";
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
    public static final String PROPERTY_NAME_MARKER = "Marker";
    public static final String DEFAULT_CONTENT_XML_FILENAME = "content.xml";
    public static final String SQUISHME_DIR_NAME = "danddads_processed";
    public static final String DEFAULT_ZIP_FILE_NAME = "dnddads.cmpgn";
    public static final int BUFFER_SIZE = 4096;
    public static final String REPLACEMENT_FILES_GLOB_STRING = PROCESSED_PREFIX + "\\" + PROCESSED_POSTFIX;
    public static final List<String> OBSOLETE_FILES = Collections.unmodifiableList(Arrays.asList(new String[]{DEFAULT_CONTENT_XML_FILENAME, "*" + DTD_POSTFIX}));
    private static final String PROPERTY_NAMES_DANDDDADS_ARRAY[] = {PROPERTY_NAME_CHARACTER, PROPERTY_NAME_NPC};
    private static final String PROPERTY_NAMES_STOCK_ARRAY[] = {PROPERTY_NAME_BASIC, PROPERTY_NAME_MARKER};
    public static final List<String> PROPERTY_NAMES_DANDDDADS = Collections.unmodifiableList(Arrays.asList(PROPERTY_NAMES_DANDDDADS_ARRAY));
    public static final List<String> PROPERTY_NAMES_STOCK = Collections.unmodifiableList(Arrays.asList(PROPERTY_NAMES_STOCK_ARRAY));
    public static final String WORK_DIR_NAME_DEFAULT;
    public static final String MAPTOOL_INPUT_FILE_DIR_NAME;
    public static final String XML_PROCESSED_DIR_NAME;
    public static final Path PROPERTIES_NPC_CIVILIAN_FILE_DEFAULT;
    public static final Path MACROS_TOKEN_NPC_CIVILIAN_FILE_DEFAULT;

    static {
        String workDirName = Paths.get(System.getProperty("user.home", ""), "Documents", "maptool_work").toString();
        String campaignOrigionalDirName = Paths.get(System.getProperty("user.home", ""), ".maptool", "backup",DEFAULT_ZIP_FILE_NAME).toString();
        String xml_processedDirName = Paths.get(System.getProperty("user.home", ""), "Documents", "token_processor").toString();
        String osName = System.getProperty("os.name").toLowerCase(Locale.US);
        String userName = System.getProperty("user.name").toLowerCase(Locale.US);
        Path authorPrefix = Paths.get(System.getProperty("user.home"), "projects", "hymerfania");
        if (osName.contains("win")) {
            if (userName.contains("hymes")) {
                workDirName = Paths.get("R:", userName, "TEMP", "maptool_work").toString();
                campaignOrigionalDirName = Paths.get(System.getProperty("user.home", ""), "projects", "hymerfania", "dandddads_garage", "DummyApplication", "src", "brine_and_venom_compilation_hackme").toString();
                xml_processedDirName = Paths.get("R:", userName, "TEMP", "token_processor").toString();
            }
        }
        if (osName.contains("nix")) {
            workDirName = Paths.get(System.getProperty("java.io.tmpdir"), "maptool_work").toString();
            campaignOrigionalDirName = Paths.get(System.getProperty("user.home", ""), ".maptool", "backup",DEFAULT_ZIP_FILE_NAME).toString();
            xml_processedDirName = Paths.get(System.getProperty("java.io.tmpdir"), "token_processor").toString();
            if (userName.contains("hymes")) {
                campaignOrigionalDirName = Paths.get(System.getProperty("user.home", ""), "projects", "hymerfania", "dandddads_garage", "DummyApplication", "src", "brine_and_venom_compilation_hackme").toString();
            }
        }
        if (osName.contains("mac")) {
            workDirName = Paths.get(System.getProperty("java.io.tmpdir"), "maptool_work").toString();
            campaignOrigionalDirName = Paths.get(System.getProperty("user.home", ""), ".maptool", "backup", DEFAULT_ZIP_FILE_NAME).toString();
            xml_processedDirName = Paths.get(System.getProperty("java.io.tmpdir"), "token_processor").toString();
            if (userName.contains("hymes")) {
                campaignOrigionalDirName = Paths.get(System.getProperty("user.home", ""), "projects", "hymerfania", "dandddads_garage", "DummyApplication", "src", "brine_and_venom_compilation_hackme").toString();
            }
        }
        Path defaultNPCCivilianFile = Paths.get(System.getProperty("user.dir", ""), "danddads_properties_npc_civilian.xml");
        if (userName.contains("hymes")) {
            defaultNPCCivilianFile = Paths.get(authorPrefix.toString(), "MapToolTools", "TokenProcessing", "src", "rptools", "maptool", "campaign", "content", "danddads_properties_npc_civilian.xml");
        }
        Path macrosetTokenNPCCivilianFile = Paths.get(System.getProperty("user.dir", ""), "danddads_macros_token_npc_civilian.xml");
        if (userName.contains("hymes")) {
            macrosetTokenNPCCivilianFile = Paths.get(authorPrefix.toString(), "MapToolTools", "TokenProcessing", "src", "rptools", "maptool", "campaign", "content", "danddads_macros_token_npc_civilian.xml");
        }
        WORK_DIR_NAME_DEFAULT = workDirName;
        MAPTOOL_INPUT_FILE_DIR_NAME = campaignOrigionalDirName;
        XML_PROCESSED_DIR_NAME = xml_processedDirName;
        PROPERTIES_NPC_CIVILIAN_FILE_DEFAULT = defaultNPCCivilianFile;
        MACROS_TOKEN_NPC_CIVILIAN_FILE_DEFAULT = macrosetTokenNPCCivilianFile;
    }

    private Shelf() {
    }
}
