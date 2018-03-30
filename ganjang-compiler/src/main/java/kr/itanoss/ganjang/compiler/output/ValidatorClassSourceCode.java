package kr.itanoss.ganjang.compiler.output;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.util.Elements;

public class ValidatorClassSourceCode {
    private final ValidatorClass theClass;
    private final String packageName;
    private CharSequence fileName;

    public ValidatorClassSourceCode(ValidatorClass theClass, String packageName) {
        this.theClass = theClass;
        this.packageName = packageName;
    }

    public JavaFile build() {
        TypeSpec spec = theClass.build();
        return JavaFile.builder(getPackageName(), spec).build();
    }

    public String getPackageName(Elements elements) {
        return theClass.getPackageName(elements);
    }

    public String getFileName(Elements elements) {
        return theClass.getFullyQualifiedName(elements);
    }
}
