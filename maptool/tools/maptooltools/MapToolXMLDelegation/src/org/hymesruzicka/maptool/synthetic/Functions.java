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
package org.hymesruzicka.maptool.synthetic;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.ParentNode;

/**
 *
 * @author chymes
 */
public class Functions {

    private Functions() {
    }

    /**
     * Returns true if provided Element has no children, and a null or empty
     * value.
     *
     * @param element
     * @return True if provided Element has no children, and a null or empty
     * value.
     *
     */
    public static boolean isEmpty(Element element) {
        boolean result = true;
        int kidCount = element.getChildCount();
        boolean noKids = (kidCount <= 0);
        result = noKids && (element.getValue() == null || element.getValue().isEmpty());
        return result;
    }

    public static boolean isEmpty(Attribute attribute) {
        boolean result = true;
        int kidCount = attribute.getChildCount();
        boolean noKids = (kidCount <= 0);
        result = noKids && (attribute.getValue() == null || attribute.getValue().isEmpty());
        return result;
    }

    /**
     * Removes the existing Element <code>doomed</code>, and inserts the
     * existing, non-empty Element <code>replacement</code> in its place. If
     * doomed is non-null, it is always removed. If it is null,
     * <code>replacement</code> is appended to the end of this Token's children
     * Elements. <code>eplacement</code> must be non-null and not empty to be
     * inserted or appended.
     *
     * @param doomed
     * @param replacement
     *
     */
    public static void replace(Element ancestor, Element doomed, Element replacement) {
        int doomedIndex;
        ParentNode parent;
        if (doomed != null) {
            parent = doomed.getParent();
            doomedIndex = parent.indexOf(doomed);
            parent.removeChild(doomed);
            if (replacement != null && (!isEmpty(replacement))) {
                parent.insertChild(replacement, doomedIndex);
            }
        } else {
            if (replacement != null && (!isEmpty(replacement))) {
                ancestor.appendChild(replacement);
            }
        }
    }

    public static void replace(Element ancestor, Attribute doomed, Attribute replacement) {
        int doomedIndex;
        ParentNode parent;
        if (doomed != null) {
            parent = doomed.getParent();
            doomedIndex = parent.indexOf(doomed);
            parent.removeChild(doomed);
            if (replacement != null && (!isEmpty(replacement))) {
                parent.insertChild(replacement, doomedIndex);
            }
        } else {
            if (replacement != null && (!isEmpty(replacement))) {
                ancestor.appendChild(replacement);
            }
        }
    }

}
