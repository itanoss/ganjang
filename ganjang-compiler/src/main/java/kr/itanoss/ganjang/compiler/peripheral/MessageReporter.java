package kr.itanoss.ganjang.compiler.peripheral;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class MessageReporter {
    private final javax.annotation.processing.Messager real;

    public MessageReporter(javax.annotation.processing.Messager real) {
        this.real = real;
    }

    /**
     * Prints a <strong>error</strong> message.
     *
     * @param e         Involved {@code Element} instance
     * @param msgFormat Formatted string to show message
     * @param arguments arguments injected into {@code msgFormat}
     */
    public void error(Element e, String msgFormat, Object... arguments) {
        real.printMessage(Diagnostic.Kind.ERROR, String.format(msgFormat, arguments), e);
    }

    /**
     * Prints a <strong>error</strong> message.
     *
     * @param msgFormat Formatted string to show message
     * @param arguments arguments injected into {@code msgFormat}
     */
    public void error(String msgFormat, Object... arguments) {
        real.printMessage(Diagnostic.Kind.ERROR, String.format(msgFormat, arguments));
    }
}
