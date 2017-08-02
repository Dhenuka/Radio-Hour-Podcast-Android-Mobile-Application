package com.example.ranga.group12_hw07;

import java.io.Serializable;

/**
 * Created by ranga on 3/10/2017.
 */

public class Podcast implements Serializable {

    String title,des,pdate,image,duration,mp3;




    @Override
    public String toString() {
        return "Podcast{" +
                "des='" + des + '\'' +
                ", title='" + title + '\'' +
                ", pdate='" + pdate + '\'' +
                ", image='" + image + '\'' +
                ", duration='" + duration + '\'' +
                ", mp3='" + mp3 + '\'' +
                '}';
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
