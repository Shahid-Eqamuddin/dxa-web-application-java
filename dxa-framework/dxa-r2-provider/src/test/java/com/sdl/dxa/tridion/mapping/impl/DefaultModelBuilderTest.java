package com.sdl.dxa.tridion.mapping.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.sdl.dxa.api.datamodel.DataModelSpringConfiguration;
import com.sdl.dxa.api.datamodel.model.EntityModelData;
import com.sdl.dxa.api.datamodel.model.PageModelData;
import com.sdl.dxa.api.datamodel.model.RegionModelData;
import com.sdl.dxa.api.datamodel.model.ViewModelData;
import com.sdl.dxa.tridion.mapping.ModelBuilderPipeline;
import com.sdl.dxa.tridion.mapping.PageInclusion;
import com.sdl.dxa.tridion.mapping.converter.SourceConverterFactory;
import com.sdl.dxa.tridion.mapping.converter.StringConverter;
import com.sdl.webapp.common.api.WebRequestContext;
import com.sdl.webapp.common.api.content.LinkResolver;
import com.sdl.webapp.common.api.localization.Localization;
import com.sdl.webapp.common.api.mapping.semantic.SemanticMapper;
import com.sdl.webapp.common.api.mapping.semantic.SemanticMappingRegistry;
import com.sdl.webapp.common.api.mapping.semantic.config.EntitySemantics;
import com.sdl.webapp.common.api.mapping.semantic.config.FieldSemantics;
import com.sdl.webapp.common.api.mapping.semantic.config.SemanticField;
import com.sdl.webapp.common.api.mapping.semantic.config.SemanticSchema;
import com.sdl.webapp.common.api.mapping.views.AbstractInitializer;
import com.sdl.webapp.common.api.mapping.views.RegisteredViewModel;
import com.sdl.webapp.common.api.mapping.views.RegisteredViewModels;
import com.sdl.webapp.common.api.model.EntityModel;
import com.sdl.webapp.common.api.model.PageModel;
import com.sdl.webapp.common.api.model.RegionModel;
import com.sdl.webapp.common.api.model.ViewModel;
import com.sdl.webapp.common.api.model.ViewModelRegistry;
import com.sdl.webapp.common.api.model.entity.AbstractEntityModel;
import com.sdl.webapp.common.api.model.page.DefaultPageModel;
import com.sdl.webapp.common.api.model.region.RegionModelImpl;
import com.sdl.webapp.common.impl.mapping.SemanticMapperImpl;
import com.sdl.webapp.common.impl.mapping.SemanticMappingRegistryImpl;
import com.sdl.webapp.common.impl.model.ViewModelRegistryImpl;
import com.sdl.webapp.common.util.ApplicationContextHolder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.IOException;
import java.util.Collections;

import static com.sdl.webapp.common.api.mapping.semantic.config.SemanticVocabulary.SDL_CORE_VOCABULARY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
public class DefaultModelBuilderTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DefaultModelBuilder modelBuilder;

    @Autowired
    private Localization localization;

    @Autowired
    private ViewModelRegistry viewModelRegistry;

    @Before
    public void initMocks() {

    }

    @Test
    public void shouldBuildPageModel_OutOfModelDataR2() throws IOException {
        //given
        PageModelData pageModelData = objectMapper.readValue(new ClassPathResource("home_page_json_full.json").getFile(), PageModelData.class);

        //when
        PageModel pageModel = modelBuilder.buildPageModel(null, pageModelData, PageInclusion.INCLUDE);

        //then
        // page.Id
        assertEqualsAndNotNull(pageModelData.getId(), pageModel.getId());

        // page.Title
        assertEqualsAndNotNull(pageModelData.getTitle() + "|My Site", pageModel.getTitle());

        // page.Meta
        assertEqualsAndNotNull(pageModelData.getMeta().get("sitemapKeyword"), pageModel.getMeta().get("sitemapKeyword"));

        // region (0). region (0) -> Header
        RegionModelData regionModelData = pageModelData.getRegions().get(0);
        RegionModel headerRegion = pageModel.getRegions().get("Header");
        assertEqualsAndNotNull(regionModelData.getName(), headerRegion.getName());

        // Header region (0) -> Info
        RegionModelData subRegionModelData = regionModelData.getRegions().get(0);
        RegionModel infoRegion = headerRegion.getRegions().get("Info");
        assertEqualsAndNotNull(subRegionModelData.getName(), infoRegion.getName());

        // Info .entity (0)
        EntityModelData entityModelData = subRegionModelData.getEntities().get(0);
        EntityModel infoRegionEntities = infoRegion.getEntities().get(0);
        assertEqualsAndNotNull(entityModelData.getId(), infoRegionEntities.getId());

        // TODO
        // region(0).region(0).entity(0).Content
        //assertEqualsAndNotNull(entityModelData.getContent().get("headline"), ((EntityModel) infoRegionEntities).getContent().get("headline"));

        // region(0).region(0).entity(0).MvcData
        assertEqualsAndNotNull(entityModelData.getMvcData().getViewName(), infoRegionEntities.getMvcData().getViewName());

        // TODO
        // region(0).region(0).entity(0).xpmMetadata
//        assertXpmMetadata(entityModelData, infoRegionEntities, "ComponentID");
//        assertXpmMetadata(entityModelData, infoRegionEntities, "ComponentModified");
//        assertXpmMetadata(entityModelData, infoRegionEntities, "ComponentTemplateID");
//        assertXpmMetadata(entityModelData, infoRegionEntities, "ComponentTemplateModified");
//        assertXpmMetadata(entityModelData, infoRegionEntities, "IsRepositoryPublished");

        // TODO
        // region(0).region(0).entity(0).schemaId
        //assertEqualsAndNotNull(entityModelData.getSchemaId(), ((EntityModel) infoRegionEntities).getSchemaId());

        // region(0).region(0).MvcData
        assertEqualsAndNotNull(subRegionModelData.getMvcData().getViewName(), infoRegion.getMvcData().getViewName());

        // TODO
        // region(0).IncludePageUrl
        //assertEqualsAndNotNull(regionModelData.getIncludePageUrl(), ((RegionModel) headerRegion).getIncludePageUrl());

        // region(0).MvcData
        assertEqualsAndNotNull(regionModelData.getMvcData().getViewName(), headerRegion.getMvcData().getViewName());

        // TODO
        // region(0).XpmMetadata
//        assertXpmMetadata(regionModelData, headerRegion, "IncludedFromPageId");
//        assertXpmMetadata(regionModelData, headerRegion, "IncludedFromPageTitle");
//        assertXpmMetadata(regionModelData, headerRegion, "IncludedFromPageFileName");

        // page.getMvcData
        assertEqualsAndNotNull(pageModelData.getMvcData().getViewName(), pageModel.getMvcData().getViewName());

        // TODO
        // page.getXpmMetadata
//        assertXpmMetadata(pageModelData, pageModel, "PageID");
//        assertXpmMetadata(pageModelData, pageModel, "PageModified");
//        assertXpmMetadata(pageModelData, pageModel, "PageTemplateID");
//        assertXpmMetadata(pageModelData, pageModel, "PageTemplateModified");

        // TODO
        // page.Metadata
        //noinspection unchecked
        //assertEqualsAndNotNull(((Map<String, Object>) pageModelData.getMetadata().get("sitemapKeyword")).get("Id"), ((Map<String, Object>) pageModel.getMetadata().get("sitemapKeyword")).get("Id"));
        //noinspection unchecked
        //assertEqualsAndNotNull(((Map<String, Object>) pageModelData.getMetadata().get("sitemapKeyword")).get("Title"), ((Map<String, Object>) pageModel.getMetadata().get("sitemapKeyword")).get("Title"));
        //noinspection unchecked
        //assertEqualsAndNotNull(((Map<String, Object>) pageModelData.getMetadata().get("Description")).get("Id"), ((Map<String, Object>) pageModel.getMetadata().get("sitemapKeyword")).get("Description"));
        //noinspection unchecked
        //assertEqualsAndNotNull(((Map<String, Object>) pageModelData.getMetadata().get("Key")).get("Id"), ((Map<String, Object>) pageModel.getMetadata().get("sitemapKeyword")).get("Key"));
        //noinspection unchecked
        //assertEqualsAndNotNull(((Map<String, Object>) pageModelData.getMetadata().get("sitemapKeyword")).get("TaxonomyId"), ((Map<String, Object>) pageModel.getMetadata().get("sitemapKeyword")).get("TaxonomyId"));

        // TODO
        // page.SchemaId
        //assertEqualsAndNotNull(pageModelData.getSchemaId(), pageModel.getSchemaId());

//        ((ItemList) pageModel.getRegions().get("Hero").getEntity("1472"))
    }

    private void assertEqualsAndNotNull(Object expected, Object actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    public void assertXpmMetadata(ViewModelData modelData, ViewModel model, String key) {
        assertEqualsAndNotNull(modelData.getXpmMetadata().get(key), model.getXpmMetadata().get(key));
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TestEntity extends AbstractEntityModel {

        private String headline;
    }

    @RegisteredViewModels({
            @RegisteredViewModel(modelClass = TestEntity.class, viewName = "TestClassView"),
            @RegisteredViewModel(modelClass = DefaultPageModel.class, viewName = "GeneralPage"),
            @RegisteredViewModel(viewName = "Header", modelClass = RegionModelImpl.class),
            @RegisteredViewModel(viewName = "Info", modelClass = RegionModelImpl.class)
    })
    private static class TestClassInitializer extends AbstractInitializer {

        @Override
        protected String getAreaName() {
            return "Core";
        }
    }

    @Configuration
    public static class SpringConfigurationContext {

        @Bean
        public WebRequestContext webRequestContext() {
            WebRequestContext mock = mock(WebRequestContext.class);
            when(mock.getLocalization()).thenReturn(localization());
            return mock;
        }

        @Bean
        public Localization localization() {
            Localization localization = mock(Localization.class);
            when(localization.getSemanticSchemas()).thenReturn(ImmutableMap.<Long, SemanticSchema>builder()
                    .put(10015L, new SemanticSchema(10015L, "NotImportant", Collections.emptySet(), Collections.emptyMap()))
                    .put(2737L, new SemanticSchema(2737L, "TestEntity",
                            Sets.newHashSet(new EntitySemantics(SDL_CORE_VOCABULARY, "TestEntity")),
                            ImmutableMap.of(
                                    new FieldSemantics(SDL_CORE_VOCABULARY, "TestEntity", "headline"),
                                    new SemanticField("headline", "/TestEntity/headline", false, Collections.emptyMap())
                            ))).build());

            when(localization.getResource(eq("core.pageTitleSeparator"))).thenReturn("|");
            when(localization.getResource(eq("core.pageTitlePostfix"))).thenReturn("My Site");

            return localization;
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new DataModelSpringConfiguration().dxaR2ObjectMapper();
        }

        @Bean
        public DefaultModelBuilder defaultModelBuilder() {
            return new DefaultModelBuilder();
        }

        @Bean
        public TestClassInitializer testClassInitializer() {
            return new TestClassInitializer();
        }

        @Bean
        public SemanticMapper semanticMapper() {
            return new SemanticMapperImpl(semanticMappingRegistry());
        }

        @Bean
        public SemanticMappingRegistry semanticMappingRegistry() {
            return new SemanticMappingRegistryImpl();
        }

        @Bean
        public ApplicationContextHolder applicationContextHolder() {
            return new ApplicationContextHolder();
        }

        @Bean
        public SourceConverterFactory sourceConverterFactory() {
            return new SourceConverterFactory();
        }

        @Bean
        public LinkResolver linkResolver() {
            return mock(LinkResolver.class);
        }

        @Bean
        public StringConverter stringConverter() {
            return new StringConverter();
        }

        @Bean
        public ModelBuilderPipeline modelBuilderPipeline() {
            return new ModelBuilderPipelineImpl();
        }

        @Bean
        public ViewModelRegistry viewModelRegistryImpl() {
            return new ViewModelRegistryImpl();
        }
    }

}