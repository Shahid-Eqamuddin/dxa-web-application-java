package com.sdl.webapp.common.api.model.entity;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.w3c.dom.Node;

import com.google.common.base.Strings;
import com.sdl.webapp.common.api.mapping.annotations.SemanticEntity;
import com.sdl.webapp.common.api.mapping.annotations.SemanticProperty;
import com.sdl.webapp.common.markup.html.HtmlElement;

import static com.sdl.webapp.common.api.mapping.config.SemanticVocabulary.SCHEMA_ORG;

@SemanticEntity(entityName = "MediaObject", vocabulary = SCHEMA_ORG, prefix = "s", public_ = true)
public abstract class MediaItem extends AbstractEntityModel {

    @SemanticProperty("s:contentUrl")
    @JsonProperty("Url")
    private String url;

    @JsonProperty("IsEmbedded")
    private Boolean isEmbedded;

    @JsonProperty("FileName")
    private String fileName;

    @SemanticProperty("s:contentSize")
    @JsonProperty("FileSize")
    private int fileSize;

    @JsonProperty("MimeType")
    private String mimeType;

    public String getUrl() {
        return url;
    }

    public void setIsEmbedded(Boolean isEmbedded) {
        this.isEmbedded = isEmbedded;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getIsEmbedded() {
        return this.isEmbedded;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toHtml() {
        return this.toHtml("100%");
    }

    @JsonIgnore
    public String getIconClass() {
        String fileType = FontAwesomeMimeTypeToIconClassMapping.containsKey(this.getMimeType()) ? FontAwesomeMimeTypeToIconClassMapping.get(this.getMimeType()) : null;
        return fileType != null ? String.format("fa-file-%s-o", fileType) : "fa-file";
    }

    @JsonIgnore
    public String getFriendlyFileSize() {
        String[] sizes = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        double len = getFileSize();
        int order = 0;
        while (len >= 1024 && order + 1 < sizes.length) {
            order++;
            len = len / 1024;
        }

        return String.format("%s %s", Math.ceil(len), sizes[order]);
    }

    /**
     * Renders an HTML representation of the Item.
     *
     * @param widthFactor The factor to apply to the width - can be % (eg "100%") or absolute (eg "120")
     * @return The HTML representation
     */
    public abstract String toHtml(String widthFactor);

    /**
     * Renders an HTML representation of the Item.
     *
     * @param widthFactor The factor to apply to the width - can be % (eg "100%") or absolute (eg "120")
     * @param aspect The aspect ratio to apply
     * @param cssClass Optional CSS class name(s) to apply
     * @param containerSize The size (in grid column units) of the containing element
     * @return The HTML representation
     */
    public abstract String toHtml(String widthFactor, double aspect, String cssClass, int containerSize);

    public abstract HtmlElement toHtmlElement(String widthFactor, double aspect, String cssClass, int containerSize, String contextPath);

    /**
     * Read properties from XHTML element.
     */
    public void readFromXhtmlElement(Node xhtmlElement) {
        // Return the Item (Reference) ID part of the TCM URI.
        this.setId(xhtmlElement.getAttributes().getNamedItem("xlink:href").getNodeValue().split("-")[1]);
        this.setUrl(xhtmlElement.getAttributes().getNamedItem("src").getNodeValue());
        this.setFileName(xhtmlElement.getAttributes().getNamedItem("data-multimediaFileName").getNodeValue());
        String fileSize = xhtmlElement.getAttributes().getNamedItem("data-multimediaFileSize").getNodeValue();
        if (!Strings.isNullOrEmpty(fileSize)) {
            this.setFileSize(Integer.parseInt(fileSize));
        }
        this.setMimeType(xhtmlElement.getAttributes().getNamedItem("data-multimediaMimeType").getNodeValue());
        this.setIsEmbedded(true);
    }

    private static final HashMap<String, String> FontAwesomeMimeTypeToIconClassMapping;

    static {
        FontAwesomeMimeTypeToIconClassMapping = new HashMap<String, String>();

        FontAwesomeMimeTypeToIconClassMapping.put("application/ms-excel", "excel");
        FontAwesomeMimeTypeToIconClassMapping.put("application/pdf", "pdf");
        FontAwesomeMimeTypeToIconClassMapping.put("application/x-wav", "audio");
        FontAwesomeMimeTypeToIconClassMapping.put("audio/x-mpeg", "audio");
        FontAwesomeMimeTypeToIconClassMapping.put("application/msword", "word");
        FontAwesomeMimeTypeToIconClassMapping.put("text/rtf", "word");
        FontAwesomeMimeTypeToIconClassMapping.put("application/zip", "archive");
        FontAwesomeMimeTypeToIconClassMapping.put("image/gif", "image");
        FontAwesomeMimeTypeToIconClassMapping.put("image/jpeg", "image");
        FontAwesomeMimeTypeToIconClassMapping.put("image/png", "image");
        FontAwesomeMimeTypeToIconClassMapping.put("image/x-bmp", "image");
        FontAwesomeMimeTypeToIconClassMapping.put("text/plain", "text");
        FontAwesomeMimeTypeToIconClassMapping.put("text/css", "code");
        FontAwesomeMimeTypeToIconClassMapping.put("application/x-javascript", "code");
        FontAwesomeMimeTypeToIconClassMapping.put("application/ms-powerpoint", "powerpoint");
        FontAwesomeMimeTypeToIconClassMapping.put("video/vnd.rn-realmedia", "video");
        FontAwesomeMimeTypeToIconClassMapping.put("video/quicktime", "video");
        FontAwesomeMimeTypeToIconClassMapping.put("video/mpeg", "video");
    }
}