package kr.itanoss.ganjang.compiler.analysis;

import javax.lang.model.element.VariableElement;

public class TargetField {
    private final VariableElement element;
    private final String name;

    TargetField(VariableElement element) {
        this.element = element;

        this.name =  element.getSimpleName().toString();
    }

    public String getName() {
        return name;
    }
}
