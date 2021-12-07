package com.mec.studybuddy;

public class Kullanici_kayit {
    private String bio ;
    private String id ;
    private String nickName ;

    public Kullanici_kayit() {
    }

    public Kullanici_kayit(String bio, String id, String nickName) {
        this.bio = bio;
        this.id = id;
        this.nickName = nickName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
