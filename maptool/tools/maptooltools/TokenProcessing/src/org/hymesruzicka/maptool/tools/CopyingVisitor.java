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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chymes
 */
public class CopyingVisitor extends SimpleFileVisitor<Path> {

    private static final Logger LOG = Logger.getLogger(CopyingVisitor.class.getName());

    public static void copyWalking(Path source, Path target) throws IOException {
        Files.walkFileTree(source, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new CopyingVisitor(source, target));
    }
    
    public static void copyWalking(Path source, Path target, String glob) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source, glob)) {
            for (Path entry : stream) {
                Path leaf = entry.getFileName();
               copyWalking(entry,target.resolve(leaf));
            }
        } catch (IOException ioe) {
            throw ioe;
        }        
    }
    private final Path _source;
    private final Path _target;

    public CopyingVisitor(Path source, Path target) {
        this._source = source;
        this._target = target;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path targetdir = _target.resolve(_source.relativize(dir));
        LOG.log(Level.FINE, "From {0} -> {1}", new String[]{_source.toString(), targetdir.toString()});
        try {
            Files.copy(dir, targetdir);
        } catch (FileAlreadyExistsException e) {
            if (!Files.isDirectory(targetdir)) {
                throw e;
            }
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path targ = _target.resolve(_source.relativize(file));
        LOG.log(Level.FINE, "From {0} -> {1}", new String[]{_source.toString(), targ.toString()});
        Files.copy(file, targ);
        return CONTINUE;
    }
}
