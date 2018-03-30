package kr.itanoss.ganjang.compiler.input;

import javax.lang.model.element.VariableElement;

public class Field {
    private final VariableElement element;
    private final String name;

    public Field(VariableElement element) {
        this.element = element;

        this.name =  element.getSimpleName().toString();
    }


}
