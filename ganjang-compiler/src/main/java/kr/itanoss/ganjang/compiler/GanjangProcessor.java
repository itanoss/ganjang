package kr.itanoss.ganjang.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import kr.itanoss.ganjang.NotNull;
import kr.itanoss.ganjang.Valid;
import kr.itanoss.ganjang.ValidationException;
import kr.itanoss.ganjang.Validator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "kr.itanoss.ganjang.Valid"
})
public class GanjangProcessor extends AbstractProcessor {

    public static final String POSTFIX = "_Validator";
    private Filer filer;
    private Elements elementUtils;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Valid.class)) {
            if(annotatedElement.getKind().isClass()) {
                try {
                    generateValidator(annotatedElement);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private void generateValidator(Element element) throws IOException {
        String qualifiedName = element.getSimpleName().toString();
        String packageName = elementUtils.getPackageOf(element).toString();
        String fullyQualifiedName = packageName + "." + qualifiedName;
        TypeName elementTypeName = typeName(element);
        List<String> fieldElements = elementUtils.getAllMembers((TypeElement) element)
                .stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .filter(e -> e.getAnnotation(NotNull.class) != null)
                .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
                .map(Element::getSimpleName)
                .map(Object::toString)
                .collect(Collectors.toList());

        MethodSpec validateMethod = newValidateMethod(elementTypeName, fieldElements);

        TypeSpec validatorClass = TypeSpec.classBuilder(qualifiedName + POSTFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterfaces(asList(
                        ParameterizedTypeName.get(className(Validator.class), elementTypeName)
                )).addMethods(asList(validateMethod))
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, validatorClass).build();

        JavaFileObject javaFileObject = filer.createSourceFile(fullyQualifiedName + POSTFIX);
        Writer writer = javaFileObject.openWriter();
        javaFile.writeTo(writer);

        writer.close();
    }

    private TypeName typeName(Element element) {
        return TypeName.get(element.asType());
    }

    private ClassName className(Class<?> clazz) {
        return ClassName.get(clazz);
    }

    private MethodSpec newValidateMethod(TypeName elementTypeName, List<String> fieldNames) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("validate")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(elementTypeName, "target")
                .addException(ValidationException.class)
                .returns(void.class);

        fieldNames.stream()
                .forEach(n -> builder.addStatement("shouldNotNull(target.$N)", n));

        return builder.build();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
