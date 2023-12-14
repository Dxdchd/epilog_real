package com.example.epilog;

import java.util.ArrayList;

public class User {
    String nickname;
    String email;
    ArrayList<String> friends;
    String profile;
    String uid;

    public User() {
    }

    public User(String nickname, String email, ArrayList<String> friends, String profile,String uid) {
        this.email = email;
        this.nickname = nickname;
        this.friends = friends;
        this.profile = profile;
        this.uid = uid;
    }


    public ArrayList<String> getFriends() {
        return friends;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile() {
        return profile;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUid() {
        return uid;
    }
}
