package kr.itanoss.ganjang.compiler.peripheral;

import com.squareup.javapoet.JavaFile;
import kr.itanoss.ganjang.compiler.output.ValidatorClassSourceCode;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public class CodeWriter {
    private final Filer filer;
    private final Elements elements;

    public CodeWriter(Filer filer, Elements elements) {
        this.filer = filer;
        this.elements = elements;
    }

    public void write(ValidatorClassSourceCode sourceCode) throws IOException {
        JavaFile file = sourceCode.build();

        CharSequence fileName = sourceCode.getFileName(elements);
        JavaFileObject fileObject = filer.createSourceFile(fileName);
        try (Writer writer = fileObject.openWriter()) {
            file.writeTo(writer);
        }
    }
}
