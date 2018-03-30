package kr.itanoss.ganjang.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import kr.itanoss.ganjang.NotNull;
import kr.itanoss.ganjang.Valid;
import kr.itanoss.ganjang.ValidationException;
import kr.itanoss.ganjang.compiler.input.TargetClass;
import kr.itanoss.ganjang.compiler.output.ValidatorClass;
import kr.itanoss.ganjang.compiler.output.ValidatorClassSourceCode;
import kr.itanoss.ganjang.compiler.peripheral.CodeWriter;
import kr.itanoss.ganjang.compiler.peripheral.MessageReporter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kr.itanoss.ganjang.compiler.Kong.typeName;

@AutoService(Processor.class)
public class GanjangProcessor extends AbstractProcessor {

    private Filer fileUtils;
    private Elements elementUtils;
    private MessageReporter reporter;
    private CodeWriter writer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementUtils = processingEnv.getElementUtils();
        this.fileUtils = processingEnv.getFiler();
        this.reporter = new MessageReporter(processingEnv.getMessager());
        this.writer = new CodeWriter(fileUtils, elements);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean processResult = roundEnv.getElementsAnnotatedWith(Valid.class).stream()
                .filter(this::isClass)
                .map(this::generateValidator)
                .anyMatch(r -> !r);

        return processResult;
    }

    private boolean isClass(Element e) {
        boolean result = e.getKind().isClass();

        if(!result) {
            reporter.error(e, "Only classes can be annotated with @%s", Valid.class.getSimpleName());
        }

        return result;
    }

    private boolean generateValidator(Element element) {
        String qualifiedName = element.getSimpleName().toString();
        String packageName = elementUtils.getPackageOf(element).toString();
        String fullyQualifiedName = packageName + "." + qualifiedName;
        TypeName elementTypeName = typeName(element);
        List<String> fieldElements = elementUtils.getAllMembers((TypeElement) element)
                .stream()
                .filter(e -> e.getKind().isField())
                .filter(e -> e.getAnnotation(NotNull.class) != null)
                .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
                .map(Element::getSimpleName)
                .map(Object::toString)
                .collect(Collectors.toList());

        MethodSpec.Builder builder = MethodSpec.methodBuilder("validate")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(elementTypeName, "target")
                .addException(ValidationException.class)
                .returns(void.class);

        fieldElements.stream()
                .forEach(n -> builder.addStatement("shouldNotNull(target.$N)", n));

        TargetClass targetClass = new TargetClass(element);

        ValidatorClass validatorClass = new ValidatorClass(targetClass);

        ValidatorClassSourceCode sourceCode = new ValidatorClassSourceCode(validatorClass, targetClass.getPackageName(elementUtils));

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
