package com.android.myapplication.coldpod.persistence;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.android.myapplication.coldpod.network.data.Enclosure;
import com.android.myapplication.coldpod.network.data.ItemImage;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Root(name = "item", strict = false)
public class Item implements Parcelable {

    private String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }


    @Path("title") //this is not the only way , we could have added @Element
    @Text(required = false)
    private String mTitle;

    @Path("description")//this is not the only way , we could have added @Element
    @Text(required = false)
    private String mDescription;

    @Element(name = "summary", required = false)
    private String mITunesSummary;

    @Element(name = "pubDate", required = false)
    private String mPubDate;

    @Element(name = "duration", required = false)
    private String mITunesDuration;

    @ElementList(inline = true, name = "enclosure", required = false)
    private List<Enclosure> mEnclosures;

    @ElementList(inline = true, name = "image", required = false)
    private List<ItemImage> mItemImages;



    public Item() {
    }
    public Item(String title, String description, String iTunesSummary, String pubDate,
                String duration, List<Enclosure> enclosures , List<ItemImage> itemImages) {
        mTitle = title;
        mDescription = description;
        mITunesSummary = iTunesSummary;
        mPubDate = pubDate;
        mITunesDuration = duration;
        mEnclosures = enclosures;
        mItemImages = itemImages;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(mTitle, item.mTitle) &&
                Objects.equals(mDescription, item.mDescription) &&
                Objects.equals(mITunesSummary, item.mITunesSummary) &&
                Objects.equals(mPubDate, item.mPubDate) &&
                Objects.equals(mITunesDuration, item.mITunesDuration) &&
                Objects.equals(mEnclosures, item.mEnclosures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mTitle, mDescription, mITunesSummary, mPubDate, mITunesDuration, mEnclosures);
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getITunesSummary() {
        return mITunesSummary;
    }

    public void setITunesSummary(String iTunesSummary) {
        mITunesSummary = iTunesSummary;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public void setPubDate(String pubDate) {
        mPubDate = pubDate;
    }

    public String getITunesDuration() {
        return mITunesDuration;
    }

    public void setITunesDuration(String iTunesDuration) {
        mITunesDuration = iTunesDuration;
    }

    public List<Enclosure> getEnclosures() {
        return mEnclosures;
    }

    public void setEnclosures(List<Enclosure> enclosures) {
        mEnclosures = enclosures;
    }

    public List<ItemImage> getItemImages() {
        return mItemImages;
    }
    public void setItemImages(List<ItemImage> itemImages) {
        mItemImages = itemImages;
    }
    protected Item(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
        mITunesSummary = in.readString();
        mPubDate = in.readString();
        mITunesDuration = in.readString();
        if (in.readByte() == 0x01) {
            mEnclosures = new ArrayList<>();
            in.readList(mEnclosures, Enclosure.class.getClassLoader());
        }        if (in.readByte() == 0x01) {
            mItemImages = new ArrayList<>();
            in.readList(mItemImages, ItemImage.class.getClassLoader());
        }
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mITunesSummary);
        dest.writeString(mPubDate);
        dest.writeString(mITunesDuration);
        if (mEnclosures == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mEnclosures);
        }
        if (mItemImages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mItemImages);
        }
    }
}

