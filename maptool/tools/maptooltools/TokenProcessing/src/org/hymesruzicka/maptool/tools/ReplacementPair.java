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

/**
 *
 * @author chymes
 */
public class ReplacementPair {

    private final Path _replacement;
    private final Path _original;

    public ReplacementPair(Path original, Path replacement) {
        this._replacement = replacement;
        this._original = original;
    }

    /**
     * Get the value of _replacement
     *
     * @return the value of _replacement
     */
    public Path getReplacement() {
        return _replacement;
    }

    /**
     * Get the value of _original
     *
     * @return the value of _original
     */
    public Path getOriginal() {
        return _original;
    }

}
