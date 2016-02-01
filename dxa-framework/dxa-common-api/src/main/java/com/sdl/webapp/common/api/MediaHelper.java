package com.sdl.webapp.common.api;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

/**
 * <p>MediaHelper interface.</p>
 *
 * @author azarakovskiy
 * @version 1.3-SNAPSHOT
 */
public interface MediaHelper {

    /**
     * <p>getResponsiveWidth.</p>
     *
     * @param widthFactor   a {@link java.lang.String} object.
     * @param containerSize a int.
     * @return a int.
     */
    int getResponsiveWidth(String widthFactor, int containerSize);

    /**
     * <p>getResponsiveHeight.</p>
     *
     * @param widthFactor   a {@link java.lang.String} object.
     * @param aspect        a double.
     * @param containerSize a int.
     * @return a int.
     */
    int getResponsiveHeight(String widthFactor, double aspect, int containerSize);

    /**
     * <p>getResponsiveImageUrl.</p>
     *
     * @param url a {@link java.lang.String} object.
     * @param widthFactor a {@link java.lang.String} object.
     * @param aspect a double.
     * @param containerSize a int.
     * @return a {@link java.lang.String} object.
     */
    String getResponsiveImageUrl(String url, String widthFactor, double aspect, int containerSize);

    /**
     * <p>getGridSize.</p>
     *
     * @return a int.
     */
    int getGridSize();

    /**
     * <p>getScreenWidth.</p>
     *
     * @return a {@link com.sdl.webapp.common.api.ScreenWidth} object.
     */
    ScreenWidth getScreenWidth();

    /**
     * <p>getSmallScreenBreakpoint.</p>
     *
     * @return a int.
     */
    int getSmallScreenBreakpoint();

    /**
     * <p>getMediumScreenBreakpoint.</p>
     *
     * @return a int.
     */
    int getMediumScreenBreakpoint();

    /**
     * <p>getLargeScreenBreakpoint.</p>
     *
     * @return a int.
     */
    int getLargeScreenBreakpoint();

    /**
     * <p>getDefaultMediaAspect.</p>
     *
     * @return a double.
     */
    double getDefaultMediaAspect();

    /**
     * <p>getDefaultMediaFill.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getDefaultMediaFill();

    /**
     * DXA Model uses MediaHelper internally which is not nice though. Also, Model classes are not managed by Spring,
     * so IoC doesn't work which is again not nice.
     * <p>
     * Anyway, in order to support multiple implementations of MediaHelper interface this interface is created.
     * It's intended for calls on ApplicationContext:
     * {@code ApplicationContextHolder.getContext().getBean(MediaHelperFactory.class).getMediaHelperInstance()}
     * </p>
     */
    interface MediaHelperFactory {
        MediaHelper getMediaHelperInstance();
    }

    /**
     * Intended to help to build CD-version-specific, format-specific, CID-existence specific URL.
     */
    abstract class ResponsiveMediaUrlBuilder {

        public abstract ResponsiveMediaUrlBuilder.Builder newInstance();

        public interface HostsNamesProvider {
            String getHostname();

            String getCidHostname();
        }

        @Component
        public static class StubHostsNamesProvider implements HostsNamesProvider {

            @Override
            public String getHostname() {
                throw new UnsupportedOperationException("Usage of stub implementation.");
            }

            @Override
            public String getCidHostname() {
                throw new UnsupportedOperationException("Usage of stub implementation.");
            }
        }

        @Setter
        @Getter
        @Accessors(chain = true)
        public static abstract class Builder {
            private String baseUrl, width, height;
            private boolean zeroAspect;

            public String build() {
                if (baseUrl == null || width == null || height == null) {
                    throw new NullPointerException("All basic (baseUrl, width, height) String parameters " +
                            "for ResponsiveMediaUrlBuilder should be not null!");
                }
                return buildInternal();
            }

            protected abstract String buildInternal();
        }
    }
}
