package kr.itanoss.ganjang.compiler.generation;

import kr.itanoss.ganjang.compiler.analysis.model.TargetClass;
import kr.itanoss.ganjang.compiler.analysis.model.TargetField;
import kr.itanoss.ganjang.compiler.generation.model.ValidationStatement;
import kr.itanoss.ganjang.compiler.generation.model.ValidatorClass;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class GanjangComposer {
    private static final String POSTFIX = "_Validator";

    public ValidatorClass create(TargetClass targetClass) {
        final List<ValidationStatement> statements = targetClass.getFields()
                .stream()
                .map(this::create)
                .collect(toList());

        final String name = targetClass.getQualifiedName() + POSTFIX;
        return new ValidatorClass(targetClass, name, statements);
    }

    private ValidationStatement create(TargetField targetField) {
        String targetFieldName = targetField.getName();
        String assertionMethodName = "shouldNotNull";
        return new ValidationStatement(targetFieldName, assertionMethodName);
    }
}
