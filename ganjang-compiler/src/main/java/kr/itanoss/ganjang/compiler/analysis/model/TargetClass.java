package kr.itanoss.ganjang.compiler.analysis.model;

import com.squareup.javapoet.TypeName;

import java.util.List;

public class TargetClass {
    private final String qualifiedName;
    private final TypeName typeName;
    private final String packageName;
    private final List<TargetField> fields;

    public TargetClass(String qualifiedName, TypeName typeName, String packageName, List<TargetField> fields) {
        this.qualifiedName = qualifiedName;
        this.typeName = typeName;
        this.packageName = packageName;
        this.fields = fields;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public String getPackageName() {
        return packageName;
    }

    public List<TargetField> getFields() {
        return fields;
    }
}
