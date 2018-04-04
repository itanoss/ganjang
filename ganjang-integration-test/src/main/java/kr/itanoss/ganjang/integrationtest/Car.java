package kr.itanoss.ganjang.integrationtest;

import kr.itanoss.ganjang.NotNull;
import kr.itanoss.ganjang.Valid;

@Valid
public class Car {
    @NotNull
    public String modelName;

    @NotNull
    public Color color;
}
