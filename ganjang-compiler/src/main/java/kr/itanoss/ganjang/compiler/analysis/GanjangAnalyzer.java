package kr.itanoss.ganjang.compiler.analysis;

import com.squareup.javapoet.TypeName;
import kr.itanoss.ganjang.NotNull;
import kr.itanoss.ganjang.compiler.analysis.model.TargetClass;
import kr.itanoss.ganjang.compiler.analysis.model.TargetField;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kr.itanoss.ganjang.compiler.Kong.typeName;

public class GanjangAnalyzer {
    private final Elements tool;

    public GanjangAnalyzer(Elements tool) {
        this.tool = tool;
    }

    public TargetClass toTargetClass(TypeElement element) {
        final List<TargetField> fields = this.findTargetFields(element);

        final String qualifiedName = element.getSimpleName().toString();
        final String packageName = tool.getPackageOf(element).toString();
        final TypeName typeName = typeName(element);
        return new TargetClass(qualifiedName, typeName, packageName, fields);
    }

    private List<TargetField> findTargetFields(TypeElement element) {
        return findFieldElementsToValidate(element)
                .map(this::toTargetField)
                .collect(Collectors.toList());
    }

    private TargetField toTargetField(VariableElement element) {
        String name = element.getSimpleName().toString();
        return new TargetField(name);
    }

    /**
     * TODO Refactor here, which can be like {@code GanjangInvestigator}
     */
    private Stream<VariableElement> findFieldElementsToValidate(TypeElement element) {
        return tool.getAllMembers(element)
                .stream()
                .filter(e -> e.getKind().isField())
                .filter(e -> e.getAnnotation(NotNull.class) != null)
                .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
                .map(e -> (VariableElement) e);
    }
}
