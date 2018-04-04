package kr.itanoss.ganjang.compiler;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class TestCompileFailure {

    @Test
    public void ValidAnnotationShouldBeOnlyOnClassLevel() {
        JavaFileObject validFieldSourceCode = JavaFileObjects.forResource("failure/Valid_FieldLevelAnnotationShouldBeFailed.java");

        Compilation compilation = javac()
                .withProcessors(new GanjangProcessor())
                .compile(validFieldSourceCode);

        assertThat(compilation).failed();
    }

    @Test
    public void ValidationRuleAnnotationShouldBeOnClassLevel() {
        JavaFileObject notNullSourceCode = JavaFileObjects.forResource("failure/NotNull_ClassLevelAnnotationShouldBeFailed.java");
        Compilation compilation = javac()
                .withProcessors(new GanjangProcessor())
                .compile(notNullSourceCode);

        assertThat(compilation).failed();
    }
}
