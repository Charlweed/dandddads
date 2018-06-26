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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

/**
 *
 * @author chymes
 */
public class CampaignXOM {

    public static final String DEFAULT_CONTENT_XML_FILENAME = "content.xml";
    private static final Logger LOG = Logger.getLogger(CampaignXOM.class.getName());
    private Document _xomDocument = null;
    private Path _inputFilePath;

    public CampaignXOM() {
        this(DEFAULT_CONTENT_XML_FILENAME);
    }

    public CampaignXOM(String inputFileName) {
        if (inputFileName == null) {
            throw new NullPointerException("InputFileName cannot be null.");
        }
        if (inputFileName.isEmpty()) {
            throw new IllegalArgumentException("InputFileName cannot be empty.");
        }
        _inputFilePath = Paths.get(inputFileName);
    }

    /**
     * Get the value of _xomDocument
     *
     * @return the value of _xomDocument
     */
    public Document getXomDocument() {
        return _xomDocument;
    }

    /**
     * <p>Parses the campaign XML file, and sets the xomDocument to the result. The
     * xomDocument is null if the source file could not be parsed.</p>
     * This method my only called once on any instance of CampaignXOM.
     */
    public final CampaignXOM build() {
        if (_xomDocument != null) {
            throw new IllegalStateException("Campaign XOM my only be built once.");
        }

        Builder parser = new Builder();

        try {
            _xomDocument = parser.build(getInputFilePath().toFile());
            LOG.log(Level.FINE, "{0} parsed.", getInputFilePath().toString());
        } catch (ParsingException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return this;
    }

    /**
     * Get the value of _inputFilePath
     *
     * @return the value of _inputFilePath
     */
    public final Path
            getInputFilePath() {
        return _inputFilePath;

    }

}
