package com.sdl.dxa.tridion.caching;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.Test;

import javax.cache.Cache;

import static javax.cache.Caching.getCachingProvider;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.jsr107.Eh107Configuration.fromEhcacheCacheConfiguration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class CopyingCacheTest {


    @Test
    public void shouldReturnItemFromCache_IfExists_CopyItem() {
        //given 
        TestingCache testingCache = new TestingCache();
        TestingValue initial = new TestingValue(1);

        //when
        TestingValue added = testingCache.getOrAdd(() -> initial, "key");
        TestingValue fromCache = testingCache.getOrAdd(() -> new TestingValue(2), "key");

        //then
        assertEquals(initial, added);
        assertEquals(initial, fromCache);
        assertEquals(added, fromCache);
        assertNotSame(initial, added);
        assertNotSame(initial, fromCache);
        assertNotSame(added, fromCache);
    }

    @Test
    public void shouldReturnValue_IfCachingDisabled() {
        //given
        CopyingCache<TestingValue> cache = new CopyingCache<TestingValue>() {
            @Override
            public Cache<Object, TestingValue> getCache() {
                return null;
            }
        };
        TestingValue value = new TestingValue(1);

        //when
        TestingValue first = cache.getOrAdd(() -> value, "key");
        TestingValue second = cache.getOrAdd(() -> value, "key");

        //then
        assertSame(second, value);
        assertSame(second, first);
    }

    private static class TestingCache extends CopyingCache<TestingValue> {

        private final static Cache<Object, TestingValue> cache = getCachingProvider().getCacheManager()
                .createCache("test", fromEhcacheCacheConfiguration(
                        newCacheConfigurationBuilder(Object.class, TestingValue.class,
                                ResourcePoolsBuilder.heap(10)).build()));

        @Override
        public Cache<Object, TestingValue> getCache() {
            return cache;
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private static class TestingValue {

        int field;
    }
}