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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Element;
import nu.xom.Elements;
/**
 *
 * @author chymes
 */
public class Utility {

    private static final Logger LOG = Logger.getLogger(Utility.class.getName());
//    public static  java.util.function.Predicate<String> nameIs = localName -> (localName.equals(element.getLocalName())); 
    private Utility() {
    }

    /**
     * Search for Elements with the localname equal to the specified value.
     *
     * @param rootElement element to search
     * @param localName name to match
     * @return
     */
    public static List<Element> elementsByName(Element rootElement, String localName) {
        List<Element> result = new ArrayList<>();
        Element current = rootElement;
        elementsByName(result, current, localName, 1);
        return result;
    }

    public static  List<Element> elementsByName(List<Element> resultIn, Element current, String localName, int depth) {
        if (localName.equalsIgnoreCase(current.getLocalName())) {
            LOG.log(Level.FINE, "{0}", current.getQualifiedName());
            resultIn.add(current);
        } else {
            LOG.log(Level.FINER, "{0}", current.getQualifiedName());
        }
        Elements childElements = current.getChildElements();
        if (childElements.size() > 0) {
            int size = childElements.size();
            LOG.log(Level.FINER, "{0} has {1} children.", new Object[]{current.getQualifiedName(), Integer.toString(size)});
            for (int i = 0; i < size; i++) {
                Element child = childElements.get(i);
                elementsByName(resultIn, child, localName, depth + 1);
            }
        } else {
            LOG.log(Level.FINER, "No child nodes in {0}", current.getQualifiedName());
        }
        return resultIn;
    }


}
