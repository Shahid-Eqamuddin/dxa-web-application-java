package com.sdl.dxa.tridion.annotations.impl;

import com.sdl.dxa.tridion.annotations.AnnotationFetcherForValue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ValueAnnotationLogger implements AnnotationFetcherForValue {

    @Override
    public String fetchAllValues(InitializingBean bean) throws Exception {
        if (bean == null) return "";
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        StringBuilder result = new StringBuilder(bean.getClass().getCanonicalName() + " has ");
        if (declaredFields.length == 0) result.append(" no @Value annotations");
        for (Field field : declaredFields){
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                field.setAccessible(true);
                result.append("\t@Value(\"" + valueAnnotation.value() + "\")\n\t" + field.getGenericType().getTypeName());
                result.append(" " + field.getName() + " = ");
                result.append(" " + field.get(bean));
                result.append("\n");
            }
        }
        return result.toString();
    }
}
