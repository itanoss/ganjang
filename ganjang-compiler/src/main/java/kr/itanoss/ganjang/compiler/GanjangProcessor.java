package kr.itanoss.ganjang.compiler;

import com.google.auto.service.AutoService;
import kr.itanoss.ganjang.Valid;
import kr.itanoss.ganjang.compiler.analysis.GanjangAnalyzer;
import kr.itanoss.ganjang.compiler.analysis.model.TargetClass;
import kr.itanoss.ganjang.compiler.generation.GanjangComposer;
import kr.itanoss.ganjang.compiler.generation.GanjangGenerator;
import kr.itanoss.ganjang.compiler.generation.model.ValidatorClass;
import kr.itanoss.ganjang.compiler.peripheral.CodeWriter;
import kr.itanoss.ganjang.compiler.peripheral.MessageReporter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class GanjangProcessor extends AbstractProcessor {

    private GanjangAnalyzer analyzer;
    private GanjangComposer composer;
    private MessageReporter reporter;
    private CodeWriter writer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.analyzer = new GanjangAnalyzer(processingEnv.getElementUtils());
        this.composer = new GanjangComposer();
        this.reporter = new MessageReporter(processingEnv.getMessager());
        this.writer = new CodeWriter(processingEnv.getFiler(), new GanjangGenerator());
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
        TargetClass targetClass = analyzer.toTargetClass(element);

        ValidatorClass validatorClass = composer.create(targetClass);

        try {
            writer.write(validatorClass);
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
