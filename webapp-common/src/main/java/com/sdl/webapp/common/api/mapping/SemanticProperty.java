package com.sdl.webapp.common.api.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SemanticProperty {

    String propertyName() default "";

    String value() default "";

    boolean ignoreMapping() default false;
}
