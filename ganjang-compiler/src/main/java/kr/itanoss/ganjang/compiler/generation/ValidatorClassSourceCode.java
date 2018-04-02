package kr.itanoss.ganjang.compiler.generation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public class ValidatorClassSourceCode {
    private final ValidatorClass theClass;

    public ValidatorClassSourceCode(ValidatorClass theClass) {
        this.theClass = theClass;
    }

    public JavaFile build() {
        TypeSpec spec = theClass.build();
        return JavaFile.builder(getPackageName(), spec).build();
    }

    private String getPackageName() {
        return theClass.getPackageName();
    }

    public String getFileName() {
        return theClass.getFullyQualifiedName();
    }
}
