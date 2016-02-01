package com.sdl.webapp.tridion.fields.exceptions;

import org.dd4t.contentmodel.FieldType;

/**
 * Thrown by {@code FieldConverterRegistry} when there is no {@code FieldConverter} for the specified field type.
 *
 * @author azarakovskiy
 * @version 1.3-SNAPSHOT
 */
public class UnsupportedFieldTypeException extends FieldConverterException {

    /**
     * <p>Constructor for UnsupportedFieldTypeException.</p>
     *
     * @param fieldType a {@link org.dd4t.contentmodel.FieldType} object.
     */
    public UnsupportedFieldTypeException(FieldType fieldType) {
        super("No field converter available for field type: " + fieldType);
    }
}
