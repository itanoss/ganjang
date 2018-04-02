package kr.itanoss.ganjang;

import java.lang.annotation.Annotation;

public interface ValidatorRegistry {

    void register(Annotation annotation, Object validator);
}
