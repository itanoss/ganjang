package kr.itanoss.ganjang.compiler.input;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import static kr.itanoss.ganjang.compiler.Kong.typeName;

public class TargetClass {
    /**
     * TODO Replace with subclass
     */
    private final Element element;
    private final String qualifiedName;
    private TypeName typeName;

    public TargetClass(Element element) {
        this.element = element;
        this.qualifiedName = element.getSimpleName().toString();
        this.typeName = typeName(element);
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public String getPackageName(Elements elements) {
        return elements.getPackageOf(element).toString();
    }

    public String getFullyQualifiedName(Elements elements) {
        return getPackageName(elements);
    }
}
