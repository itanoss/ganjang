package kr.itanoss.ganjang.compiler.peripheral;

import com.squareup.javapoet.JavaFile;
import kr.itanoss.ganjang.compiler.generation.ValidatorClassSourceCode;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public class CodeWriter {
    private final Filer filer;

    public CodeWriter(Filer filer) {
        this.filer = filer;
    }

    public void write(ValidatorClassSourceCode sourceCode) throws IOException {
        JavaFile content = sourceCode.build();
        CharSequence fileName = sourceCode.getFileName();

        JavaFileObject fileObject = filer.createSourceFile(fileName);
        try (Writer writer = fileObject.openWriter()) {
            content.writeTo(writer);
        }
    }
}
