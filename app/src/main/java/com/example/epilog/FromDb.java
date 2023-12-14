package com.example.epilog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FromDb {
    private String content;
    private String date;
    private int emotion;
    private ArrayList<String> imgUrls;
    private String location;
    private String title;
    private String uid;
    private int weather;
    private String timestamp;
    private List<String> likes;
    private String now;
    private List<Map<String, String>> comments;
    private String path;

    public FromDb() {
    }

    public FromDb(String content, String date, int emotion, ArrayList<String> imgUrls, String location, String now,String path,String timestamp, String title, String uid, int weather, List<String> likes, List<Map<String, String>> comments) {
        this.content = content;
        this.date = date;
        this.emotion = emotion;
        this.imgUrls = new ArrayList<>(imgUrls);
        this.location = location;
        this.title = title;
        this.timestamp = timestamp;
        this.uid = uid;
        this.weather = weather;
        this.likes = likes;
        this.comments = comments;
        this.now = now;
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public int getEmotion() {
        return emotion;
    }

    public int getWeather() {
        return weather;
    }

    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public String getUid() {
        return uid;
    }

    public ArrayList<String> getImgUrls() {
        return imgUrls;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<String> getLikes() {
        return likes;
    }

    public List<Map<String, String>> getComments() {
        return comments;
    }

    public String getNow() {
        return now;
    }

    public String getPath() {
        return path;
    }
}
