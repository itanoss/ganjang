package kr.itanoss.ganjang.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * Utility class
 */
public class Kong {

    public static TypeName typeName(Element element) {
        return TypeName.get(element.asType());
    }

    public static ClassName className(Class<?> clazz) {
        return ClassName.get(clazz);
    }
}
