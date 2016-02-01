package com.sdl.webapp.common.api.mapping.semantic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>SemanticProperty class.</p>
 *
 * @author azarakovskiy
 * @version 1.3-SNAPSHOT
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SemanticProperty {

    String propertyName() default "";

    String value() default "";
}
