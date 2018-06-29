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
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chymes
 */
public enum MapToolZipType {
    CMPGN, MTMACSET, MTPROPS, MTTABLE, RPTOK;
    private static final Logger LOG = Logger.getLogger(MapToolZipType.class.getName());
    public static final String[] EXTENSIONS = extensions();
    public static final String[] EXTENSIONS_PLUS = extensions_plus();

    public static MapToolZipType valueOfFileName(String fileNameIn) {
        return valueOf(Paths.get(fileNameIn));
    }

    public static MapToolZipType valueOf(Path path) {
        String extension = "";
        String fileName = path.getFileName().toString();
        int i = fileName.lastIndexOf('.');

        if (i > 0) {
            extension = fileName.substring(i + 1).toUpperCase(Locale.US);
        } else {
            throw new IllegalArgumentException("File does not end with a \".\" extension.");
        }
        MapToolZipType result = valueOf(extension);
        return result;
    }

    public static boolean isMapToolZip(String fileNameIn) {
        return isMapToolZip(Paths.get(fileNameIn.toLowerCase(Locale.US)));
    }

    public static boolean isMapToolZip(Path asPath) {
        boolean result = false;
        if (asPath.getFileName().toString().toLowerCase(Locale.US).endsWith(".zip")) {
            return true;
        }
        try {
            result = valueOf(asPath) != null;
        } catch (IllegalArgumentException iae) {
            LOG.log(Level.FINER, null, iae);
        }
        return result;
    }

    public static String[] extensions() {
        String result[] = new String[values().length];
        int i = 0;
        for (MapToolZipType val : values()) {
            result[i++] = val.asExtension();
        }
        return result;
    }

    public static String[] extensions_plus() {
        String result[] = new String[values().length + 1];
        int i = 0;
        for (MapToolZipType val : values()) {
            result[i++] = val.asExtension();
        }
        result[i] = ".zip";
        return result;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.US);
    }

    public String asExtension() {
        return "." + this.toString().toLowerCase(Locale.US);
    }

}
