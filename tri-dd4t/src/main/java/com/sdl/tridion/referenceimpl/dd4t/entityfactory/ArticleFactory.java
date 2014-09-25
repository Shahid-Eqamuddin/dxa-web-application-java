package com.sdl.tridion.referenceimpl.dd4t.entityfactory;

import com.sdl.tridion.referenceimpl.common.ContentProviderException;
import com.sdl.tridion.referenceimpl.common.model.Entity;
import com.sdl.tridion.referenceimpl.common.model.entity.Article;
import org.dd4t.contentmodel.ComponentPresentation;
import org.springframework.stereotype.Component;

@Component
public class ArticleFactory implements EntityFactory {

    private static final Class<?>[] SUPPORTED_ENTITY_TYPES = { Article.class };

    @Override
    public Class<?>[] supportedEntityTypes() {
        return SUPPORTED_ENTITY_TYPES;
    }

    @Override
    public Entity createEntity(ComponentPresentation componentPresentation, Class<?> entityType)
            throws ContentProviderException {
        // TODO
        return new Article();
    }
}
