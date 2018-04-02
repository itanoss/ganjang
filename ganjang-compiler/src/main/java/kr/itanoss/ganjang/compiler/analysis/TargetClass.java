package kr.itanoss.ganjang.compiler.analysis;

import com.squareup.javapoet.TypeName;
import kr.itanoss.ganjang.NotNull;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.itanoss.ganjang.compiler.Kong.typeName;

public class TargetClass {
    private final TypeElement element;
    private final String qualifiedName;
    private final TypeName typeName;
    private List<TargetField> fields;

    private String packageName;

    public TargetClass(TypeElement element) {
        this.element = element;
        this.qualifiedName = element.getSimpleName().toString();
        this.typeName = typeName(element);
        this.fields = new LinkedList<>();
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

    public void investigate(Elements elementUtil) {
        investigateSupplements(elementUtil);
        investigateFields(elementUtil);
    }

    private void investigateSupplements(Elements elementUtil) {
        this.packageName = elementUtil.getPackageOf(element).toString();
    }

    private void investigateFields(Elements elementUtil) {
        this.fields = elementUtil.getAllMembers(element)
                .stream()
                .filter(e -> e.getKind().isField())
                .filter(e -> e.getAnnotation(NotNull.class) != null)
                .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
                .map(e -> new TargetField((VariableElement) e))
                .collect(Collectors.toList());
    }

    public List<TargetField> getFields() {
        return fields;
    }
}
