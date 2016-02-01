package com.sdl.webapp.common.impl.taglib.dxa;

import com.sdl.webapp.common.markup.PluggableMarkupRegistry;
import com.sdl.webapp.common.markup.html.HtmlNode;
import com.sdl.webapp.common.markup.html.ParsableHtmlNode;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Define Pluggable Markup Tag
 *
 * @author nic
 * @version 1.3-SNAPSHOT
 */
public class DefinePluggableMarkupTag extends BodyTagSupport {

    // TODO: Define a common abstract base class for all tags dealing with pluggable markup

    private PluggableMarkupRegistry registry;

    private String label;

    /**
     * <p>Setter for the field <code>label</code>.</p>
     *
     * @param label a {@link java.lang.String} object.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int doAfterBody() throws JspException {

        BodyContent bodyContent = this.getBodyContent();
        HtmlNode markup = new ParsableHtmlNode(bodyContent.getString());
        this.getPluggableMarkupRegistry().registerContextualPluggableMarkup(this.label, markup);
        return SKIP_BODY;
    }

    /**
     * <p>getPluggableMarkupRegistry.</p>
     *
     * @return a {@link com.sdl.webapp.common.markup.PluggableMarkupRegistry} object.
     */
    protected PluggableMarkupRegistry getPluggableMarkupRegistry() {
        if (this.registry == null) {
            this.registry = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext())
                    .getBean(PluggableMarkupRegistry.class);
        }
        return this.registry;
    }

}
