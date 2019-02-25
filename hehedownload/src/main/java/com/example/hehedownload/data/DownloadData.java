package com.example.hehedownload.data;

import android.os.Parcel;
import android.os.Parcelable;

import static com.example.hehedownload.data.Consts.NONE;
import static com.example.hehedownload.data.Consts.START;


public class DownloadData implements Parcelable {

    private String url;
    private String path;
    private String name;
    private int currentLength;
    private int totalLength;
    private float percentage;
    private int status = NONE;
    private int childTaskCount;
    private long date;

    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(int currentLength) {
        this.currentLength = currentLength;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChildTaskCount() {
        return childTaskCount;
    }

    public void setChildTaskCount(int childTaskCount) {
        this.childTaskCount = childTaskCount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    private String lastModify;

    public DownloadData() {

    }

    public DownloadData(String url, String path, String name) {
        this.url = url;
        this.path = path;
        this.name = name;
    }

    public DownloadData(String url, String path, String name, int childTaskCount) {
        this.url = url;
        this.path = path;
        this.name = name;
        this.childTaskCount = childTaskCount;
    }

    public DownloadData(String url, String path, int childTaskCount, String name, int currentLength, int totalLength, String lastModify, long date) {
        this.url = url;
        this.path = path;
        this.childTaskCount = childTaskCount;
        this.currentLength = currentLength;
        this.status = START;
        this.name = name;
        this.totalLength = totalLength;
        this.lastModify = lastModify;
        this.date = date;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(path);
        parcel.writeString(name);
        parcel.writeInt(currentLength);
        parcel.writeInt(totalLength);
        parcel.writeFloat(percentage);
        parcel.writeInt(status);
        parcel.writeInt(childTaskCount);
        parcel.writeLong(date);
        parcel.writeString(lastModify);
    }

    protected DownloadData(Parcel in) {
        url = in.readString();
        path = in.readString();
        name = in.readString();
        currentLength = in.readInt();
        totalLength = in.readInt();
        percentage = in.readFloat();
        status = in.readInt();
        childTaskCount = in.readInt();
        date = in.readLong();
        lastModify = in.readString();
    }

    public static final Creator<DownloadData> CREATOR = new Creator<DownloadData>() {
        @Override
        public DownloadData createFromParcel(Parcel in) {return new DownloadData(in);}

        @Override
        public DownloadData[] newArray(int size) {return new DownloadData[size];}
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
