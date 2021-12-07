package com.mec.studybuddy;

import android.content.Intent;

public class Etut_Olustur {
    private String etut_baslik ;
    private String etut_pp ;

    private String  etut_saat ;
    private String etut_olusturan ;
    private String etut_olusturan_id ;
    private String etut_key ;
    private String etut_dakika ;
    private String tenefus_dakika ;
    private String etut_tekrar ;
    private String etut_tarih ;


    public Etut_Olustur() {
    }

    public Etut_Olustur(String etut_baslik, String etut_pp, String etut_saat, String etut_olusturan, String etut_olusturan_id, String etut_key, String etut_dakika, String tenefus_dakika, String etut_tekrar, String etut_tarih) {

        this.etut_baslik = etut_baslik;
        this.etut_pp = etut_pp;
        this.etut_saat = etut_saat;
        this.etut_olusturan = etut_olusturan;
        this.etut_olusturan_id = etut_olusturan_id;
        this.etut_key = etut_key;
        this.etut_dakika = etut_dakika;
        this.tenefus_dakika = tenefus_dakika;
        this.etut_tekrar = etut_tekrar;
        this.etut_tarih = etut_tarih;
    }

    public String getEtut_baslik() {
        return etut_baslik;
    }

    public void setEtut_baslik(String etut_baslik) {
        this.etut_baslik = etut_baslik;
    }

    public String getEtut_pp() {
        return etut_pp;
    }

    public void setEtut_pp(String etut_pp) {
        this.etut_pp = etut_pp;
    }

    public String getEtut_saat() {
        return etut_saat;
    }

    public void setEtut_saat(String etut_saat) {
        this.etut_saat = etut_saat;
    }

    public String getEtut_olusturan() {
        return etut_olusturan;
    }

    public void setEtut_olusturan(String etut_olusturan) {
        this.etut_olusturan = etut_olusturan;
    }

    public String getEtut_olusturan_id() {
        return etut_olusturan_id;
    }

    public void setEtut_olusturan_id(String etut_olusturan_id) {
        this.etut_olusturan_id = etut_olusturan_id;
    }

    public String getEtut_key() {
        return etut_key;
    }

    public void setEtut_key(String etut_key) {
        this.etut_key = etut_key;
    }

    public String getEtut_dakika() {
        return etut_dakika;
    }

    public void setEtut_dakika(String etut_dakika) {
        this.etut_dakika = etut_dakika;
    }

    public String getTenefus_dakika() {
        return tenefus_dakika;
    }

    public void setTenefus_dakika(String tenefus_dakika) {
        this.tenefus_dakika = tenefus_dakika;
    }

    public String getEtut_tekrar() {
        return etut_tekrar;
    }

    public void setEtut_tekrar(String etut_tekrar) {
        this.etut_tekrar = etut_tekrar;
    }

    public String getEtut_tarih() {
        return etut_tarih;
    }

    public void setEtut_tarih(String etut_tarih) {
        this.etut_tarih = etut_tarih;
    }
}
