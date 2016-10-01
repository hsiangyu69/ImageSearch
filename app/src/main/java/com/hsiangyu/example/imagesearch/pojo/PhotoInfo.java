package com.hsiangyu.example.imagesearch.pojo;

import java.io.Serializable;

/**
 * Created by HsiangYu on 2016/9/30.
 */
public class PhotoInfo implements Serializable{
    private String tags;
    private String previewURL;
    private String webformatURL;

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public String getTags() {
        return tags;
    }

    public String getWebformatURL() {
        return webformatURL;
    }
}
