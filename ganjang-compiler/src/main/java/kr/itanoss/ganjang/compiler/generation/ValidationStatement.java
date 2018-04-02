package kr.itanoss.ganjang.compiler.generation;


import kr.itanoss.ganjang.compiler.analysis.TargetField;

class ValidationStatement {
    private final TargetField targetField;
    private final String assertionMethodName;
    private final String targetFieldName;

    ValidationStatement(TargetField targetField) {
        this.targetField = targetField;
        this.assertionMethodName = "shouldNotNull";
        this.targetFieldName = targetField.getName();
    }

    String getAssertionMethodName() {
        return assertionMethodName;
    }

    String getFieldName() {
        return targetFieldName;
    }
}
