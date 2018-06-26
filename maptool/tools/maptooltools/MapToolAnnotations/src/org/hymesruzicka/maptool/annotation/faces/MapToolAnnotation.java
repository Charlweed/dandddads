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
package org.hymesruzicka.maptool.annotation.faces;

import java.lang.annotation.Annotation;
import javax.lang.model.element.VariableElement;

/**
 *
 * @author chymes
 */
public enum MapToolAnnotation{
    ATTRIBUTE(TokenPropertyAttribute.class), ELEMENT(TokenPropertyElement.class), BOGUS(java.lang.annotation.Annotation.class);
    
    private <A extends Annotation> MapToolAnnotation(Class<A>annotationClazz){
        _annotationClazz = annotationClazz;
    }
    
    private final Class<? extends Annotation>_annotationClazz;
    
    @SuppressWarnings("unchecked")
    public <A extends Annotation> Class<A> getAnnotationClass(){
        return (Class<A>)_annotationClazz;
    }

    public static MapToolAnnotation which(VariableElement varElement) {
        MapToolAnnotation result = BOGUS;
        for(MapToolAnnotation mpAnno : values()){
            if(varElement.getAnnotation(mpAnno.getAnnotationClass()) != null){
                result = mpAnno;
                break;
            }
        }
        return result;
    }
    
    public static MapToolAnnotation which(TokenPropertyAttribute annotation) {
        return ATTRIBUTE;
    }

    public static MapToolAnnotation which(TokenPropertyElement annotation) {
        return ELEMENT;
    }

    public static MapToolAnnotation which(Object annotation) {
        MapToolAnnotation result = BOGUS;
        if (annotation != null) {
            result = which(annotation.getClass());
        }
        return result;
    }

    public static MapToolAnnotation which(Class<?> clazz) {
        MapToolAnnotation result = BOGUS;
        if (clazz != null) {
            if (clazz == TokenPropertyAttribute.class) {
                result = ATTRIBUTE;
            } else {
                if (clazz == TokenPropertyElement.class) {
                    result = ELEMENT;
                }
            }
        }
        return result;
    }
}
