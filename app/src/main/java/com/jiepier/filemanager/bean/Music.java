package com.jiepier.filemanager.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by panruijie on 17/1/12.
 * Email : zquprj@gmail.com
 */

public class Music implements Parcelable {

    private int id;
    private String title;
    private String album;
    private String artist;
    private String url;
    private int duration;
    private int size;

    public Music() {
    }

    public Music(String title, String album, String artist, String url, int duration, int size) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.url = url;
        this.duration = duration;
        this.size = size;
    }

    public Music(int id,String title, String album, String artist, String url, int duration, int size) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.url = url;
        this.duration = duration;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public Music setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Music setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public Music setAlbum(String album) {
        this.album = album;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public Music setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Music setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Music setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public int getSize() {
        return size;
    }

    public Music setSize(int size) {
        this.size = size;
        return this;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", url='" + url + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.album);
        dest.writeString(this.artist);
        dest.writeString(this.url);
        dest.writeInt(this.duration);
        dest.writeInt(this.size);
    }

    protected Music(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.album = in.readString();
        this.artist = in.readString();
        this.url = in.readString();
        this.duration = in.readInt();
        this.size = in.readInt();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };
}
