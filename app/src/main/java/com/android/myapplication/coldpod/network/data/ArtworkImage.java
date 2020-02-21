package com.android.myapplication.coldpod.network.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "image", strict = false)
public class ArtworkImage {

    @Attribute(name = "href", required = false)
    private String mHref;

    @Element(name = "url", required = false)
    private String mUrl;

    public ArtworkImage() {
    }

    public String getImageHref() {
        return mHref;
    }

    public void setImageHref(String href) {
        mHref = href;
    }

    public String getImageUrl() {
        return mUrl;
    }

    public void setImageUrl(String url) {
        mUrl = url;
    }
}
