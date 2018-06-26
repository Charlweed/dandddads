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
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chymes
 */
public class DeletionVisitor extends SimpleFileVisitor<Path> {

    private static final Logger LOG = Logger.getLogger(DeletionVisitor.class.getName());

    public static void deleteWalking(Path start) throws IOException {
        if (Files.exists(start)) {
            Files.walkFileTree(start, new DeletionVisitor());
        }
    }

    public static void deleteWalking(Path start, String glob) throws IOException {
        if (Files.exists(start)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(start, glob)) {
                for (Path entry : stream) {
                    Files.walkFileTree(entry, new DeletionVisitor());
                }
            } catch (IOException ioe) {
                throw ioe;
            }
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (Files.exists(file)) {
            LOG.log(Level.FINE, "Deleting {0}", new String[]{file.toString()});
            Files.delete(file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
        if (e == null) {
            LOG.log(Level.FINE, "Deleting {0}", new String[]{dir.toString()});
            if (Files.exists(dir)) {
                Files.delete(dir);
            }
            return FileVisitResult.CONTINUE;
        } else {
            LOG.log(Level.SEVERE, "Problem deleting {0}", new String[]{dir.toString()});
            throw e;
        }
    }
}
