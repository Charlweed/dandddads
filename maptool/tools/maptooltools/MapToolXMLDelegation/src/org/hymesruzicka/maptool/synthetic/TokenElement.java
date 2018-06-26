package org.hymesruzicka.maptool.synthetic;

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
import org.hymesruzicka.maptool.annotation.faces.TokenPropertyAttribute;
import org.hymesruzicka.maptool.annotation.faces.TokenPropertyElement;

/**
 * This Class is used by the AnnotationProcessor
 * <code>org.hymesruzicka.maptool.annotation.process.MapToolAnnotationProcessor</code>
 * to generate the source file
 * <code>"org.hymesruzicka.maptool.synthetic.TokenElementDelegate"</code>. The
 * fields annotated with <code>TokenPropertyAttribute</code> generate java
 * source code to process XML Attributes, and the fields annotated with
 * <code>TokenPropertyElement</code> generate java source code to process XML
 * Elements. Everything else is ignored.
 *
 * @author chymes
 */
public class TokenElement {

    @TokenPropertyAttribute
    private String reference;
    @TokenPropertyElement
    private String exposedAreaGUID;
    @TokenPropertyElement
    private String id;
    @TokenPropertyElement
    private String beingImpersonated;
    @TokenPropertyElement
    private String height;
    @TokenPropertyElement
    private String width;
    @TokenPropertyElement
    private String propertyType;
    @TokenPropertyElement
    private String imageAssetMap;
    @TokenPropertyElement
    private String visibleOnlyToOwner;
    @TokenPropertyElement
    private String ownerList;
    @TokenPropertyElement
    private String ownerType;
    @TokenPropertyElement
    private String lastPath;
    @TokenPropertyElement
    private String x;
    @TokenPropertyElement
    private String y;
    @TokenPropertyElement
    private String state;
    @TokenPropertyElement
    private boolean isFlippedX;
    @TokenPropertyElement
    private String sightType;
    @TokenPropertyElement
    private String tokenShape;
    @TokenPropertyElement
    private String tokenType;
    @TokenPropertyElement
    private String z;
    @TokenPropertyElement
    private String layer;
    @TokenPropertyElement
    private String snapToScale;
    @TokenPropertyElement
    private String scaleX;
    @TokenPropertyElement
    private String name;
    @TokenPropertyElement
    private String macroPropertiesMap;
    @TokenPropertyElement
    private String snapToGrid;
    @TokenPropertyElement
    private String hasSight;
    @TokenPropertyElement
    private String speechMap;
    @TokenPropertyElement
    private String scaleY;
    @TokenPropertyElement
    private String portraitImage;
    @TokenPropertyElement
    private boolean isFlippedY;
    @TokenPropertyElement
    private String sizeMap;
    @TokenPropertyElement
    private String anchorX;
    @TokenPropertyElement
    private String anchorY;
    @TokenPropertyElement
    private String lastY;
    @TokenPropertyElement
    private String facing;
    @TokenPropertyElement
    private String propertyMapCI;
    @TokenPropertyElement
    private String sizeScale;
    @TokenPropertyElement
    private String lastX;
    @TokenPropertyElement
    private String charsheetImage;
    @TokenPropertyElement
    private boolean isVisible;
}
