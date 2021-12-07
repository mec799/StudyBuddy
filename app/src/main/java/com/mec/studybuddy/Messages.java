package com.mec.studybuddy;

public class Messages {
    private String kullanıcı_mesaj ;
    private String key ;

    public Messages() {
    }

    public Messages(String kullanıcı_mesaj, String key) {
        this.kullanıcı_mesaj = kullanıcı_mesaj;
        this.key = key;
    }

    public String getKullanıcı_mesaj() {
        return kullanıcı_mesaj;
    }

    public void setKullanıcı_mesaj(String kullanıcı_mesaj) {
        this.kullanıcı_mesaj = kullanıcı_mesaj;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}