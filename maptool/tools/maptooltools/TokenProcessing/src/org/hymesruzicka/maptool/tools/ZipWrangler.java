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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import static org.hymesruzicka.maptool.tools.CopyingVisitor.copyWalking;
import static org.hymesruzicka.maptool.tools.DeletionVisitor.deleteWalking;
import static org.hymesruzicka.maptool.tools.MapToolZipType.isMapToolZip;
import static org.hymesruzicka.maptool.tools.Shelf.BUFFER_SIZE;
import static org.hymesruzicka.maptool.tools.Shelf.DEFAULT_ZIP_FILE_NAME;
import static org.hymesruzicka.maptool.tools.Shelf.MAPTOOL_INPUT_FILE_DIR_NAME;
import static org.hymesruzicka.maptool.tools.Shelf.OBSOLETE_FILES;
import static org.hymesruzicka.maptool.tools.Shelf.SQUISHME_DIR_NAME;
import static org.hymesruzicka.maptool.tools.Shelf.WORK_DIR_NAME_DEFAULT;
import static org.hymesruzicka.maptool.tools.ZippingVisitor.zipWalking;

/**
 *
 * @author chymes
 */
public class ZipWrangler {

    private static final Logger LOG = Logger.getLogger(ZipWrangler.class.getName());

    public static Path replace(ReplacementPair pair, String cmpgnOrigionalDirNameIn, String workDirNameIn) {
        String cmpgnOrigionalDirName, workDirName;
        if (cmpgnOrigionalDirNameIn == null || cmpgnOrigionalDirNameIn.isEmpty()) {
            cmpgnOrigionalDirName = MAPTOOL_INPUT_FILE_DIR_NAME;
        } else {
            cmpgnOrigionalDirName = cmpgnOrigionalDirNameIn;
        }
        if (workDirNameIn == null || workDirNameIn.isEmpty()) {
            workDirName = WORK_DIR_NAME_DEFAULT;
        } else {
            workDirName = workDirNameIn;
        }
        Path squishedCampaignDir, cmpgnOrigionalDir, targetZipFile;
        cmpgnOrigionalDir = Paths.get(cmpgnOrigionalDirName);
        targetZipFile = Paths.get(workDirName, DEFAULT_ZIP_FILE_NAME);
        squishedCampaignDir = Paths.get(workDirName, SQUISHME_DIR_NAME);
        try {
            //Clear squishedCampaignDir
            deleteWalking(targetZipFile);
            deleteWalking(squishedCampaignDir);
            Files.createDirectories(squishedCampaignDir);
            //Recursively copy contents of cmpgnOrigional (NOT cmpgnOrigional itself) to squishedCampaignDir
            copyWalking(cmpgnOrigionalDir, squishedCampaignDir);
            //Delete obsolete xml file(s) listed in doomed
            for (String doomedFileName : OBSOLETE_FILES) {
                deleteWalking(squishedCampaignDir, doomedFileName);
            }
            //Delete COPY of original file
            Path leaf = squishedCampaignDir.resolve(pair.getOriginal().getFileName());
            LOG.log(Level.FINE, "leaf={0}", new String[]{leaf.toString()});
            deleteWalking(leaf);
            //Copy replacemnt xml files from replacement to squishedCampaignDir            
            Files.copy(pair.getReplacement(), leaf);
            //Zip squishedCampaignDir into targetZipFile
            zipWalking(squishedCampaignDir, targetZipFile);
        } catch (IOException ex) {
            Logger.getLogger(ZipWrangler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return targetZipFile;
    }

    static public void extractZipFolder(String zipFileName, Path destDir) throws ZipException, IOException {
        int BUFFER = BUFFER_SIZE;
        File file = new File(zipFileName);

        ZipFile javaZipFile = new ZipFile(file);
        Path newPath = destDir.normalize();
        boolean mkdir = newPath.toFile().mkdir();
        Enumeration<? extends ZipEntry> zipFileEntries = javaZipFile.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements()) {
            // grab a zip file entry
            ZipEntry entry = zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath.toFile(), currentEntry);
            //destFile = new File(newPath, destFile.getName());
            File destinationParent = destFile.getParentFile();

            // create the parent directory structure if needed
            boolean mkdirs = destinationParent.mkdirs();
            if (!entry.isDirectory()) {
                try (BufferedInputStream is = new BufferedInputStream(javaZipFile.getInputStream(entry))) {
                    int currentByte;
                    // establish buffer for writing file
                    byte data[] = new byte[BUFFER];

                    // write the current file to disk
                    FileOutputStream fos = new FileOutputStream(destFile);
                    // read and write until last byte is encountered
                    try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)) {
                        // read and write until last byte is encountered
                        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, currentByte);
                        }
                        dest.flush();
                    }
                }
            }
            if (isMapToolZip(currentEntry)) {
                // found a zip file, try to open
                extractZipFolder(destFile.getAbsolutePath(), destFile.getAbsoluteFile().toPath());
            }
        }

    }

    private ZipWrangler() {
    }

}
