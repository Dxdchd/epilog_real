package com.example.epilog;

import android.widget.ImageView;

public class MyBlogRecyclerList {
    private String date;
    private String title;
    private String mainimg;
    private String location;
    private int feel;
    private int weather;

    public MyBlogRecyclerList(String date, String title, String mainimg, String location, int feel, int weather){
        this.date = date;
        this.title = title;
        this.mainimg = mainimg;
        this.location = location;
        this.feel = feel;
        this.weather = weather;
    }

    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public int getWeather() {
        return weather;
    }

    public String getDate() {
        return date;
    }

    public int getFeel() {
        return feel;
    }

    public String getMainimg() {
        return mainimg;
    }
}
