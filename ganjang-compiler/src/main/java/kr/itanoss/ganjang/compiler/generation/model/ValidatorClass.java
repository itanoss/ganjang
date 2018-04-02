package kr.itanoss.ganjang.compiler.generation.model;

import com.squareup.javapoet.TypeName;
import kr.itanoss.ganjang.compiler.analysis.model.TargetClass;

import java.util.Iterator;
import java.util.List;

public class ValidatorClass {
    private final TargetClass targetClass;
    private final String name;
    private final List<ValidationStatement> statements;

    public ValidatorClass(TargetClass targetClass, String name, List<ValidationStatement> statements) {
        this.targetClass = targetClass;
        this.name = name;
        this.statements = statements;
    }

    public String getName() {
        return name;
    }

    public Iterator<ValidationStatement> iteratorOfStatements() {
        return statements.iterator();
    }

    public TypeName getTargetClassTypeName() {
        return targetClass.getTypeName();
    }

    private String getFullyQualifiedName() {
        return targetClass.getPackageName() + "." + name;
    }

    public String getPackageName() {
        return targetClass.getPackageName();
    }

    public String getFileName() {
        return getFullyQualifiedName();
    }
}
