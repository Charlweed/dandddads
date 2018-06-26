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
package org.hymesruzicka.maptool.annotation.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.hymesruzicka.maptool.annotation.faces.MapToolAnnotation;
import org.hymesruzicka.maptool.annotation.faces.TokenPropertyElement;

/**
 *
 * @author chymes
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"org.hymesruzicka.maptool.annotation.faces.*"})
public class MapToolAnnotationProcessor extends AbstractProcessor {

    private static final Logger LOG = Logger.getLogger(MapToolAnnotationProcessor.class.getName());
    public static final String NL = System.lineSeparator();
    public static final String FQN = "org.hymesruzicka.maptool.annotation.process.MapToolAnnotationProcessor";

    public static String getFunctionsText() {
        String result = ""
                + "  public void replace(Attribute doomed, Attribute replacement) {\n"
                + "        replace(getSourceElement(), doomed, replacement);\n"
                + "    }\n"
                + "\n"
                + "    /**\n"
                + "     * Returns true if provided Element has no children, and a null or empty\n"
                + "     * value.\n"
                + "     *\n"
                + "     * @param element\n"
                + "     * @return True if provided Element has no children, and a null or empty\n"
                + "     * value.\n"
                + "     *\n"
                + "     */\n"
                + "    public static boolean isEmpty(Element element) {\n"
                + "        boolean result = true;\n"
                + "        int kidCount = element.getChildCount();\n"
                + "        boolean noKids = (kidCount <= 0);\n"
                + "        result = noKids && (element.getValue() == null || element.getValue().isEmpty());\n"
                + "        return result;\n"
                + "    }\n"
                + "\n"
                + "    public static boolean isEmpty(Attribute attribute) {\n"
                + "        boolean result = true;\n"
                + "        int kidCount = attribute.getChildCount();\n"
                + "        boolean noKids = (kidCount <= 0);\n"
                + "        result = noKids && (attribute.getValue() == null || attribute.getValue().isEmpty());\n"
                + "        return result;\n"
                + "    }\n"
                + "\n"
                + "    /**\n"
                + "     * Removes the existing Element <code>doomed</code>, and inserts the\n"
                + "     * existing, non-empty Element <code>replacement</code> in its place. If\n"
                + "     * doomed is non-null, it is always removed. If it is null,\n"
                + "     * <code>replacement</code> is appended to the end of this Token's children\n"
                + "     * Elements. <code>eplacement</code> must be non-null and not empty to be\n"
                + "     * inserted or appended.\n"
                + "     *\n"
                + "     * @param doomed\n"
                + "     * @param replacement\n"
                + "     *\n"
                + "     */\n"
                + "    public static void replace(Element ancestor, Element doomed, Element replacement) {\n"
                + "        int doomedIndex;\n"
                + "        ParentNode parent;\n"
                + "        if (doomed != null) {\n"
                + "            parent = doomed.getParent();\n"
                + "            doomedIndex = parent.indexOf(doomed);\n"
                + "            parent.removeChild(doomed);\n"
                + "            if (replacement != null && (!isEmpty(replacement))) {\n"
                + "                parent.insertChild(replacement, doomedIndex);\n"
                + "            }\n"
                + "        } else {\n"
                + "            if (replacement != null && (!isEmpty(replacement))) {\n"
                + "                ancestor.appendChild(replacement);\n"
                + "            }\n"
                + "        }\n"
                + "    }\n"
                + "\n"
                + "    public static void replace(Element ancestor, Attribute doomed, Attribute replacement) {\n"
                + "        int doomedIndex;\n"
                + "        ParentNode parent;\n"
                + "        if (doomed != null) {\n"
                + "            parent = doomed.getParent();\n"
                + "            doomedIndex = parent.indexOf(doomed);\n"
                + "            parent.removeChild(doomed);\n"
                + "            if (replacement != null && (!isEmpty(replacement))) {\n"
                + "                parent.insertChild(replacement, doomedIndex);\n"
                + "            }\n"
                + "        } else {\n"
                + "            if (replacement != null && (!isEmpty(replacement))) {\n"
                + "                ancestor.appendChild(replacement);\n"
                + "            }\n"
                + "        }\n"
                + "    }\n"
                + "";
        return result;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<Element> allAnnotatedElements = new HashSet<>(2);
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            allAnnotatedElements.addAll(annotatedElements);
        }
        if (!allAnnotatedElements.isEmpty()) {
            String className = enclosingClassName(allAnnotatedElements);
            try {
                writeBuilderFile(className, fields(allAnnotatedElements));
            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    public String enclosingClassName(Set<? extends Element> annotatedElements) {
        return enclosingClass(annotatedElements).getQualifiedName().toString();
    }

    public TypeElement enclosingClass(Set<? extends Element> annotatedElements) {
        TypeElement result = null;

        if (!annotatedElements.isEmpty()) {
            Element first = null;
            for (Element element : annotatedElements) {
                first = element;
                break;
            }
            if (first != null) {
                result = (TypeElement) first.getEnclosingElement();
            }
        }
        return result;
    }

    public List<VariableElement> fields(Set<? extends Element> annotatedElements) {
        Map<Boolean, List<Element>> annotatedFields = annotatedElements
                .stream().collect(
                        Collectors.partitioningBy(element -> element.getKind().equals(ElementKind.FIELD))
                );
        List<Element> superFields = annotatedFields.get(true);
        List<VariableElement> fields = superFields.stream().map(element -> (VariableElement) element).collect(Collectors.toList());
        return fields;
    }

    private String capitalize(VariableElement field) {
        return capitalize(field.getSimpleName());
    }

    private String capitalize(Name noun) {
        return capitalize(noun.toString());
    }

    private String capitalize(String noun) {
        return noun.substring(0, 1).toUpperCase(Locale.US) + noun.substring(1);
    }

    private void writeBuilderFile(String className, List<VariableElement> fields) throws IOException {

        String packageName = "org.hymesruzicka.maptool.synthetic";
        int lastDot = className.lastIndexOf('.');
        String builderClassName = className + "Delegate";
        String builderSimpleClassName = builderClassName.substring(lastDot + 1);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(builderClassName);

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

            out.print("package ");
            out.print(packageName);
            out.println(";");
            out.println();

            out.println("import nu.xom.Attribute;");
            out.println("import nu.xom.Element;");
            out.println("import nu.xom.ParentNode;");
            out.println();
            out.println();

            out.print("public class ");
            out.print(builderSimpleClassName);
            out.println(" {");
            out.println();
            out.println();

            out.println("    private final Element _sourceElement;");
            for (VariableElement field : fields) {
                out.print("    private ");
                out.print(field.asType().toString());
                out.print(" _");
                out.print(field.getSimpleName());
                out.println(";");
            }

            StringBuilder constructorText = new StringBuilder("    public ");
            constructorText.append(builderSimpleClassName);
            constructorText.append("(Element sourceElement) {").append(NL);
            constructorText.append("     this._sourceElement = sourceElement;").append(NL);
            constructorText.append("     readAll();").append(NL);
            constructorText.append("    }").append(NL);
            out.println(constructorText);

            out.println("    public void replace(Element doomed, Element replacement){replace(getSourceElement(), doomed, replacement);}");
            out.println(getFunctionsText());

            StringBuilder readAllText = new StringBuilder("    public final void ");
            readAllText.append("readAll");
            readAllText.append("() {").append(NL);
            for (VariableElement field : fields) {
                readAllText.append("    read");
                readAllText.append(capitalize(field));
                readAllText.append("();").append(NL);
            }
            readAllText.append("    }").append(NL);
            out.println(readAllText);

            out.println("    public Element getSourceElement() {return _sourceElement;}");

            for (VariableElement field : fields) {
                switch (MapToolAnnotation.which(field)) {
                    case ATTRIBUTE:
                        out.println(getAttributeText(field));
                        break;
                    case ELEMENT:
                        out.println(getElementText(field));
                        break;
                    case BOGUS:
                    default:
                        throw new AssertionError(MapToolAnnotation.which(field.getAnnotation(TokenPropertyElement.class)).name());
                }
            }

            for (VariableElement field : fields) {
                out.println(getReaderText(field));
            }

            for (VariableElement field : fields) {
                out.println(getSetterText(field));
            }

            for (VariableElement field : fields) {
                out.println(getSetter0Text(field));
            }

            for (VariableElement field : fields) {
                out.println(getGetterText(field));
            }
            out.println("}");
        }
    }

    public String getElementText(VariableElement field) {
        StringBuilder text = new StringBuilder(NL);
        text.append("    ");
        text.append("public Element ");
        text.append(getElementGetterIDText(field));
        text.append("() {").append(NL);
        text.append("        ");
        text.append("return getSourceElement().getFirstChildElement(\"");
        text.append(field.getSimpleName());
        text.append("\");").append(NL);
        text.append("    ");
        text.append("}").append(NL);
        return text.toString();
    }

    public String getAttributeText(VariableElement field) {
        StringBuilder text = new StringBuilder(NL);
        text.append("    ");
        text.append("public Attribute ");
        text.append(getAttributeGetterIDText(field));
        text.append("() {").append(NL);
        text.append("        ");
        text.append("return getSourceElement().getAttribute(\"");
        text.append(field.getSimpleName());
        text.append("\");").append(NL);
        text.append("    ");
        text.append("}").append(NL);
        return text.toString();
    }

    public String getGetterIDText(VariableElement field) {
        StringBuilder text = new StringBuilder();
        if (field.asType().toString().contains("boolean")) {
            text.append("is");
            text.append(capitalize(field).replaceFirst("Is", ""));
        } else {
            text.append("get");
            text.append(capitalize(field));
        }
        return text.toString();
    }

    public String getElementGetterIDText(VariableElement field) {
        StringBuilder text = new StringBuilder("get");
        text.append(capitalize(field));
        text.append("Element");
        return text.toString();
    }

    public String getAttributeGetterIDText(VariableElement field) {
        StringBuilder text = new StringBuilder("get");
        text.append(capitalize(field));
        text.append("Attribute");
        return text.toString();
    }

    public String getSetterIDText(VariableElement field) {
        StringBuilder text = new StringBuilder("set");
        if (field.asType().toString().contains("boolean")) {
            text.append(capitalize(field).replaceFirst("Is", ""));
        } else {
            text.append(capitalize(field));
        }
        return text.toString();
    }

    public String getReaderIDText(VariableElement field) {
        StringBuilder text = new StringBuilder("read");
        text.append(capitalize(field));
        return text.toString();
    }

    public String getSetter0Text(VariableElement field) {
        StringBuilder text = new StringBuilder("    private void ");
        text.append(getSetterIDText(field));
        text.append("0(");
        text.append(field.asType().toString());
        text.append(" ");
        text.append(field.getSimpleName());
        text.append(") {").append(NL);
        text.append("        this._");
        text.append(field.getSimpleName());
        text.append(" = ");
        text.append(field.getSimpleName());
        text.append(";").append(NL);
        text.append("    ");
        text.append("}").append(NL);
        return text.toString();
    }

    public String getReaderText(VariableElement field) {
        boolean isBool = field.asType().toString().contains("boolean");
        StringBuilder text = new StringBuilder("    public final void ");
        text.append(getReaderIDText(field));
        text.append("() {").append(NL);

        switch (MapToolAnnotation.which(field)) {
            case ATTRIBUTE:
                text.append("        Attribute child = ");
                text.append(getAttributeGetterIDText(field));
                text.append("();").append(NL);
                break;
            case ELEMENT:
                text.append("        Element child = ");
                text.append(getElementGetterIDText(field));
                text.append("();").append(NL);
                break;
            case BOGUS:
            default:
                throw new AssertionError(MapToolAnnotation.which(field.getAnnotation(TokenPropertyElement.class)).name());
        }

        text.append("        if (child != null) {").append(NL);
        text.append("            ");
        text.append("String value = child.getValue();").append(NL);
        text.append("            ");
        text.append(getSetterIDText(field));
        text.append("0(");
        if (isBool) {
            text.append("Boolean.parseBoolean(value)");
        } else {
            text.append("value");
        }
        text.append(");").append(NL);
        text.append("    ");
        text.append("}").append(NL);
        text.append("        else {").append(NL);
        text.append("        ");
        text.append(getSetterIDText(field));
        text.append("0(");
        if (isBool) {
            text.append("false");
        } else {
            text.append("null");
        }
        text.append(");").append(NL);
        text.append("    ");
        text.append("}").append(NL);
        text.append("    ");
        text.append("}").append(NL);
        return text.toString();
    }

    public String getGetterText(VariableElement field) {
        StringBuilder text = new StringBuilder();
        text.append("    ");
        text.append("public ");
        text.append(field.asType().toString());
        text.append(" ");
        text.append(getGetterIDText(field));
        text.append("() {").append(NL);
        text.append("        ");
        text.append("return _");
        text.append(field.getSimpleName());
        text.append(";").append(NL);
        text.append("    ");
        text.append("}").append(NL);
        return text.toString();
    }

    public String getSetterText(VariableElement field) {

        StringBuilder text = new StringBuilder("    public final void ");
        text.append(getSetterIDText(field));
        text.append("(");
        text.append(field.asType().toString());
        text.append(" ");
        text.append(field.getSimpleName());
        text.append(") {").append(NL);
        //Don't try to test for primitive
        boolean isBool = field.asType().toString().endsWith("boolean");
        boolean isInt = field.asType().toString().endsWith("int");
        boolean isDouble = field.asType().toString().endsWith("double");
        if (isBool || isInt || isDouble) {
            text.append("        if ( this._");
            text.append(field.getSimpleName());
            text.append(" == ");
            text.append(field.getSimpleName());
            text.append("){return;}").append(NL);
        } else {
            text.append("        if ( this._");
            text.append(field.getSimpleName());
            text.append(" == null && ");
            text.append(field.getSimpleName());
            text.append(" == null){return;}").append(NL);

            text.append("        if ( this._");
            text.append(field.getSimpleName());
            text.append(" != null && ");
            text.append(field.getSimpleName());
            text.append(" != null && this._");
            text.append(field.getSimpleName());
            text.append(".equals(");
            text.append(field.getSimpleName());
            text.append(")){return;}").append(NL);
        }
        //Creation of fresh whatever
        text.append("        ");
        switch (MapToolAnnotation.which(field)) {
            case ATTRIBUTE:
                text.append("Attribute fresh = new Attribute(");
                text.append("\"");
                text.append(field.getSimpleName());
                text.append("\",");
                if (isBool || isInt || isDouble) {
                    if (isBool) {
                        text.append("Boolean");
                    }
                    if (isInt) {
                        text.append("Long");
                    }
                    if (isDouble) {
                        text.append("Dounle");
                    }
                    text.append(".toString(");
                    text.append(field.getSimpleName());
                    text.append(")");
                } else {
                    text.append(field.getSimpleName());
                }
                text.append(");").append(NL);
                break;
            case ELEMENT:
                text.append("Element fresh = new Element(");
                text.append("\"");
                text.append(field.getSimpleName());
                text.append("\");").append(NL);
                //Set value of new Element
                text.append("        ");
                text.append("fresh.appendChild(");
                if (isBool || isInt || isDouble) {
                    if (isBool) {
                        text.append("Boolean");
                    }
                    if (isInt) {
                        text.append("Long");
                    }
                    if (isDouble) {
                        text.append("Dounle");
                    }
                    text.append(".toString(");
                    text.append(field.getSimpleName());
                    text.append(")");
                } else {
                    text.append(field.getSimpleName());
                }
                text.append(");").append(NL);
                break;
            case BOGUS:
            default:
                throw new AssertionError(MapToolAnnotation.which(field.getAnnotation(TokenPropertyElement.class)).name());
        }
        //Call inner setter.        
        text.append("        ");
        text.append(getSetterIDText(field));
        text.append("0(");
        text.append(field.getSimpleName());
        text.append(");").append(NL);

        text.append("        replace(");
        switch (MapToolAnnotation.which(field)) {
            case ATTRIBUTE:
                text.append(getAttributeGetterIDText(field));
                break;
            case ELEMENT:
                text.append(getElementGetterIDText(field));
                break;
            case BOGUS:
            default:
                throw new AssertionError(MapToolAnnotation.which(field.getAnnotation(TokenPropertyElement.class)).name());
        }
        text.append("(), fresh);").append(NL);
        text.append("    ");
        text.append("}").append(NL);
        return text.toString();
    }
}
