package kr.itanoss.ganjang.compiler.output;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import kr.itanoss.ganjang.Validator;
import kr.itanoss.ganjang.compiler.input.TargetClass;

import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static kr.itanoss.ganjang.compiler.Kong.className;

public class ValidatorClass {
    public static final String POSTFIX = "_Validator";

    private final TargetClass targetClass;
    private final String name;
    private final List<ValidationStatement> statements;

    public ValidatorClass(TargetClass targetClass) {
        this.targetClass = targetClass;
        this.name = targetClass.getQualifiedName() + POSTFIX;
        this.statements = new LinkedList<>();
    }

    public static ValidatorClass of(TargetClass targetClass) {
        return new ValidatorClass(targetClass);
    }

    public TypeSpec build() {
        List<MethodSpec> methods = this.statements.stream()
                .map(ValidationStatement::build)
                .collect(toList());

        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterfaces(asList(
                        ParameterizedTypeName.get(className(Validator.class), targetClass.getTypeName())
                )).addMethods(methods)
                .build();
    }

    public String getFullyQualifiedName(Elements elements) {
        return targetClass.getPackageName(elements) + name;
    }

    public String getPackageName(Elements elements) {
        return targetClass.getPackageName(elements);
    }
}
