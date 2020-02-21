package com.android.myapplication.coldpod.network.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "enclosure", strict = false)
public class Enclosure implements Parcelable {

    @Attribute(name = "url", required = false)
    private String mUrl;

    @Attribute(name = "type", required = false)
    private String mType;

    @Attribute(name = "length", required = false)
    private String mLength;

    public Enclosure() {
    }

    protected Enclosure(Parcel in) {
        mUrl = in.readString();
        mType = in.readString();
        mLength = in.readString();
    }

    public static final Creator<Enclosure> CREATOR = new Creator<Enclosure>() {
        @Override
        public Enclosure createFromParcel(Parcel in) {
            return new Enclosure(in);
        }

        @Override
        public Enclosure[] newArray(int size) {
            return new Enclosure[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getLength() {
        return mLength;
    }

    public void setLength(String length) {
        mLength = length;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeString(mType);
        dest.writeString(mLength);
    }
}