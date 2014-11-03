package com.sdl.webapp.dd4t.fieldconverters;

import com.sdl.webapp.common.api.mapping.SemanticMapper;
import com.sdl.webapp.common.api.mapping.SemanticMappingException;
import com.sdl.webapp.common.api.mapping.config.SemanticField;
import com.sdl.webapp.common.api.model.Entity;
import com.sdl.webapp.dd4t.DD4TSemanticFieldDataProvider;
import org.dd4t.contentmodel.FieldSet;
import org.dd4t.contentmodel.FieldType;
import org.dd4t.contentmodel.impl.BaseField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmbeddedFieldConverter implements FieldConverter {

    private static final FieldType[] SUPPORTED_FIELD_TYPES = { FieldType.Embedded };

    private final SemanticMapper semanticMapper;

    @Autowired
    public EmbeddedFieldConverter(SemanticMapper semanticMapper) {
        this.semanticMapper = semanticMapper;
    }

    @Override
    public FieldType[] supportedFieldTypes() {
        return SUPPORTED_FIELD_TYPES;
    }

    @Override
    public Object getFieldValue(SemanticField semanticField, BaseField field, TypeDescriptor targetType,
                                DD4TSemanticFieldDataProvider semanticFieldDataProvider) throws FieldConverterException {
        final List<FieldSet> embeddedValues = field.getEmbeddedValues();

        if (semanticField.isMultiValue()) {
            final TypeDescriptor elementType = targetType.getElementTypeDescriptor();

            final List<Object> fieldValues = new ArrayList<>();
            for (FieldSet fieldSet : embeddedValues) {
                final Object fieldValue = getFieldValue(semanticField, fieldSet, elementType, semanticFieldDataProvider);
                if (fieldValue != null) {
                    fieldValues.add(fieldValue);
                }
            }

            return fieldValues;
        } else {
            return embeddedValues.isEmpty() ? null : getFieldValue(semanticField, embeddedValues.get(0), targetType,
                    semanticFieldDataProvider);
        }
    }

    private Object getFieldValue(SemanticField semanticField, FieldSet fieldSet, TypeDescriptor targetType,
                                 DD4TSemanticFieldDataProvider semanticFieldDataProvider) throws FieldConverterException {
        final Class<?> targetClass = targetType.getObjectType();

        if (Entity.class.isAssignableFrom(targetClass)) {
            try {
                semanticFieldDataProvider.pushEmbeddingLevel(fieldSet.getContent());

                return semanticMapper.createEntity(targetClass.asSubclass(Entity.class),
                        semanticField.getEmbeddedFields(), semanticFieldDataProvider);
            } catch (SemanticMappingException e) {
                throw new FieldConverterException("Exception while creating entity for embedded field: " +
                        semanticField.getPath(), e);
            } finally {
                semanticFieldDataProvider.popEmbeddingLevel();
            }
        } else {
            throw new UnsupportedTargetTypeException(targetType);
        }
    }
}