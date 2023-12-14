package com.example.epilog;

import android.content.Context;

public class friendrequestlist {
    String nickname;
    String email;
    String profile;
    String uid;

    public friendrequestlist() {
    }

    public friendrequestlist(String nickname, String email, String profile, String uid) {
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfile() {
        return profile;
    }

    public String getUid() {
        return uid;
    }
}
