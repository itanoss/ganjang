package kr.itanoss.ganjang.compiler.generation;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import kr.itanoss.ganjang.ValidationException;
import kr.itanoss.ganjang.Validator;
import kr.itanoss.ganjang.compiler.analysis.TargetClass;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static kr.itanoss.ganjang.compiler.Kong.className;

public class ValidatorClass {
    private static final String POSTFIX = "_Validator";

    private final TargetClass targetClass;
    private final String name;
    private final List<ValidationStatement> statements;

    public ValidatorClass(TargetClass targetClass) {
        this.targetClass = targetClass;
        this.name = targetClass.getQualifiedName() + POSTFIX;
        this.statements = targetClass.getFields()
                .stream()
                .map(ValidationStatement::new)
                .collect(toList());
    }

    public static ValidatorClass of(TargetClass targetClass) {
        return new ValidatorClass(targetClass);
    }

    TypeSpec build() {
        MethodSpec methodSpec = buildMethodSpec();
        return buildTypeSpec(methodSpec);
    }

    private TypeSpec buildTypeSpec(MethodSpec methodSpec) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterfaces(singletonList(
                        ParameterizedTypeName.get(className(Validator.class), targetClass.getTypeName())
                )).addMethods(singletonList(methodSpec))
                .build();
    }

    private MethodSpec buildMethodSpec() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("validate")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(targetClass.getTypeName(), "target")
                .addException(ValidationException.class)
                .returns(void.class);

        statements.forEach(s -> builder.addStatement(s.getAssertionMethodName() + "(target.$N)", s.getFieldName()));
        return builder.build();
    }

    String getFullyQualifiedName() {
        return targetClass.getPackageName() + "." + name;
    }

    String getPackageName() {
        return targetClass.getPackageName();
    }
}
