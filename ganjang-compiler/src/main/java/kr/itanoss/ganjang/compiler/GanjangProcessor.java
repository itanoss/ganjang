package kr.itanoss.ganjang.compiler;

import com.google.auto.service.AutoService;
import kr.itanoss.ganjang.Valid;
import kr.itanoss.ganjang.compiler.analysis.TargetClass;
import kr.itanoss.ganjang.compiler.generation.ValidatorClass;
import kr.itanoss.ganjang.compiler.generation.ValidatorClassSourceCode;
import kr.itanoss.ganjang.compiler.peripheral.CodeWriter;
import kr.itanoss.ganjang.compiler.peripheral.MessageReporter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class GanjangProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private MessageReporter reporter;
    private CodeWriter writer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementUtils = processingEnv.getElementUtils();
        this.reporter = new MessageReporter(processingEnv.getMessager());
        this.writer = new CodeWriter(processingEnv.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(Valid.class).stream()
                .filter(this::isClass)
                .map(e -> (TypeElement) e)
                .map(this::generateValidator)
                .anyMatch(r -> !r);
    }

    private boolean isClass(Element e) {
        boolean result = e.getKind().isClass();

        if(!result) {
            reporter.error(e, "Only classes can be annotated with @%s", Valid.class.getSimpleName());
        }

        return result;
    }

    private boolean generateValidator(TypeElement element) {
        TargetClass targetClass = new TargetClass(element);
        targetClass.investigate(elementUtils);

        ValidatorClass validatorClass = new ValidatorClass(targetClass);
        ValidatorClassSourceCode sourceCode = new ValidatorClassSourceCode(validatorClass);

        try {
            writer.write(sourceCode);
        } catch (IOException e) {
            reporter.error(e.getLocalizedMessage());
            return false;
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(Valid.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
