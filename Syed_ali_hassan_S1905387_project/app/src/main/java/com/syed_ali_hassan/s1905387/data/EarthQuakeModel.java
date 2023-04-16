//Syed_Ali_Hassan_S1905387
package com.syed_ali_hassan.s1905387.data;

import java.io.Serializable;

public class EarthQuakeModel implements Serializable {
     String title;
    String description;
    String link;
    String date;
    String lat;
    String lng;
    String category;

    public EarthQuakeModel(String title, String description, String link, String date, String lat, String lng, String category) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.lat = lat;
        this.lng = lng;
        this.category = category;
    }

    public EarthQuakeModel() {
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String pubDate) {
        this.date = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
