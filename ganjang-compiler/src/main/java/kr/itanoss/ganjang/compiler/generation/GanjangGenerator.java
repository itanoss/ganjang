package kr.itanoss.ganjang.compiler.generation;

import com.squareup.javapoet.*;
import kr.itanoss.ganjang.ValidationException;
import kr.itanoss.ganjang.Validator;
import kr.itanoss.ganjang.compiler.generation.model.ValidationStatement;
import kr.itanoss.ganjang.compiler.generation.model.ValidatorClass;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;
import static kr.itanoss.ganjang.compiler.Kong.className;

public class GanjangGenerator {
    public JavaFile generate(ValidatorClass validatorClass) {
        String packageName = validatorClass.getPackageName();
        TypeSpec spec = typeSpecOf(validatorClass);
        return JavaFile.builder(packageName, spec).build();
    }

    private TypeSpec typeSpecOf(ValidatorClass validatorClass) {
        TypeName targetClassTypeName = validatorClass.getTargetClassTypeName();

        List<MethodSpec> methods = singletonList(
                methodSpecOf(validatorClass));

        List<ParameterizedTypeName> superinterfaces = singletonList(
                ParameterizedTypeName.get(className(Validator.class), targetClassTypeName)
        );

        return TypeSpec.classBuilder(validatorClass.getName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterfaces(superinterfaces)
                .addMethods(methods)
                .build();
    }

    private MethodSpec methodSpecOf(ValidatorClass validatorClass) {
        TypeName targetClassTypeName = validatorClass.getTargetClassTypeName();

        MethodSpec.Builder builder = MethodSpec.methodBuilder("validate")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(targetClassTypeName, "target")
                .addException(ValidationException.class)
                .returns(void.class);

        validatorClass.iteratorOfStatements().forEachRemaining(addStatement(builder));

        return builder.build();
    }

    private Consumer<ValidationStatement> addStatement(MethodSpec.Builder builder) {
        return s -> builder.addStatement(s.getAssertionMethodName() + "(target.$N)", s.getFieldName());
    }
}
