package com.sdl.webapp.common.api.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SemanticEntity {

    String entityName() default "";

    String vocab() default "";

    String prefix() default "";

    boolean pub() default false;
}
