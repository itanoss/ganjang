package kr.itanoss.ganjang.compiler.peripheral;

import com.squareup.javapoet.JavaFile;
import kr.itanoss.ganjang.compiler.generation.GanjangGenerator;
import kr.itanoss.ganjang.compiler.generation.model.ValidatorClass;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public class CodeWriter {
    private final Filer filer;
    private final GanjangGenerator generator;

    public CodeWriter(Filer filer, GanjangGenerator generator) {
        this.filer = filer;
        this.generator = generator;
    }

    public void write(ValidatorClass validatorClass) throws IOException {
        JavaFile content = generator.generate(validatorClass);
        CharSequence fileName = validatorClass.getFileName();

        JavaFileObject fileObject = filer.createSourceFile(fileName);
        try (Writer writer = fileObject.openWriter()) {
            content.writeTo(writer);
        }
    }
}
