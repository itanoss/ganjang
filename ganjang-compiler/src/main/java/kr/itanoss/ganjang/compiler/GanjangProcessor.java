package kr.itanoss.ganjang.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import kr.itanoss.ganjang.NotNull;
import kr.itanoss.ganjang.Valid;
import kr.itanoss.ganjang.ValidationException;
import kr.itanoss.ganjang.Validator;
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

import static java.util.Arrays.asList;

@AutoService(Processor.class)
public class GanjangProcessor extends AbstractProcessor {

    public static final String POSTFIX = "_Validator";
    private Filer fileUtils;
    private Elements elementUtils;
    private MessageReporter reporter;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementUtils = processingEnv.getElementUtils();
        this.fileUtils = processingEnv.getFiler();
        this.reporter = new MessageReporter(processingEnv.getMessager());
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

        MethodSpec validateMethod = builder.build();

        TypeSpec validatorClass = TypeSpec.classBuilder(qualifiedName + POSTFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterfaces(asList(
                        ParameterizedTypeName.get(className(Validator.class), elementTypeName)
                )).addMethods(asList(validateMethod))
                .build();

        final JavaFile javaFile = JavaFile.builder(packageName, validatorClass).build();
        final String filename = fullyQualifiedName + POSTFIX;

        try(CodeWriter writer = new CodeWriter(fileUtils, filename)) {
            writer.write(javaFile);
        } catch (IOException e) {
            reporter.error(e.getLocalizedMessage());
            return false;
        }

        return true;
    }

    private TypeName typeName(Element element) {
        return TypeName.get(element.asType());
    }

    private ClassName className(Class<?> clazz) {
        return ClassName.get(clazz);
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
