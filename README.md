# 간장 Ganjang

[![Build Status](https://travis-ci.org/itanoss/ganjang.svg?branch=master)](https://travis-ci.org/itanoss/ganjang) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=ganjang-parent)](https://sonarcloud.io/dashboard/index/ganjang-parent)

*Ganjang* is Java Validation framework with **Annotation Processing** technology.

## Example

There is a sample value object like the following.

```java
public class Car {
    public String modelName;
}
```

We can check easily whether `modelName` is not null with *Ganjang*:

```java
@Valid
public class Car {
    @NotNull
    public String modelName;
}
```

To validate Car instance:

```java
Car car = new Car();
Validator<Car> validator = new Car_Validator(); // auto generated from annotation processor
validator.validate(car);    // will throw a ValidationException
```

## TODO

 - [x] Attach travis
 - [ ] Attach jacoco and sonarcloud
 - [ ] Support getter and setter
 - [ ] Support private field
 - [ ] Inflate basic validation rules: `NotEmpty`, `IsNumeric`, `SatisfiesRegex` and so on
 - [ ] Support custom validation annotation
 - [ ] Design complex validation logic
 - [ ] Deploy to maven central
 - [ ] Support JSR-330