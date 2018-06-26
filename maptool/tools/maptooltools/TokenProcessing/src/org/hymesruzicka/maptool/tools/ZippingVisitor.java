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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author deftDeveloper
 */
public class ZippingVisitor extends SimpleFileVisitor<Path> implements java.lang.AutoCloseable {

    private static final Logger LOG = Logger.getLogger(ZippingVisitor.class.getName());
    public static final int BUFFER_SIZE = 4096;

    public static void zipWalking(Path source, Path target) {
        try (ZippingVisitor zippingVisitor = new ZippingVisitor(source, target)) {
            Files.walkFileTree(source, zippingVisitor);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, null, ioe);
        }
    }
    
    private final Path _source;
    private final Path _target;
    private final FileOutputStream _fos;
    private final ZipOutputStream _zos;

    public ZippingVisitor(Path source, Path target) throws FileNotFoundException {
        this._source = source;
        this._target = target;
        _fos = new FileOutputStream(_target.toFile());
        _zos = new ZipOutputStream(_fos);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (!Files.exists(file)) {
            throw new IOException("File " + file.toString() + " not found.");
        }
        Path zipEntryPath = _source.relativize(file);
        LOG.log(Level.FINE, zipEntryPath.toString());
        byte[] buffer = new byte[BUFFER_SIZE];
        try (FileInputStream fis = new FileInputStream(file.toFile())) {
            _zos.putNextEntry(new ZipEntry(zipEntryPath.toString()));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                _zos.write(buffer, 0, length);
            }
            _zos.closeEntry();
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, null, ioe);
        }
        return CONTINUE;
    }

    @Override
    public void close() throws IOException {
        _zos.close();
        _fos.close();
    }
}
