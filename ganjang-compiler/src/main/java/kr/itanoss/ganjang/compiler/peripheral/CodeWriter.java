package kr.itanoss.ganjang.compiler.peripheral;

import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public class CodeWriter implements AutoCloseable {
    private final String filename;
    private final Filer filer;
    private final JavaFileObject fileObject;
    private final Writer real;

    public CodeWriter(Filer filer, String filename) throws IOException {
        this.filer = filer;
        this.filename = filename;
        this.fileObject = filer.createSourceFile(filename);
        this.real = this.fileObject.openWriter();
    }

    public void write(JavaFile file) throws IOException {
        file.writeTo(real);
    }

    @Override
    public void close() throws IOException {
        if(real != null) {
            real.close();
        }
    }
}
