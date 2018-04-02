package kr.itanoss.ganjang.compiler.generation.model;


public class ValidationStatement {
    private final String assertionMethodName;
    private final String targetFieldName;

    public ValidationStatement(String targetFieldName, String assertionMethodName) {
        this.targetFieldName = targetFieldName;
        this.assertionMethodName = assertionMethodName;
    }

    public String getAssertionMethodName() {
        return assertionMethodName;
    }

    public String getFieldName() {
        return targetFieldName;
    }
}
