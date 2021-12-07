package com.mec.studybuddy;

public class etut_gecmisi_kayit {
    String etut_adi ;
    String etut_calisma_saat ;

    public etut_gecmisi_kayit() {
    }

    public etut_gecmisi_kayit(String etut_adi, String etut_calisma_saat) {
        this.etut_adi = etut_adi;
        this.etut_calisma_saat = etut_calisma_saat;
    }

    public String getEtut_adi() {
        return etut_adi;
    }

    public void setEtut_adi(String etut_adi) {
        this.etut_adi = etut_adi;
    }

    public String getEtut_calisma_saat() {
        return etut_calisma_saat;
    }

    public void setEtut_calisma_saat(String etut_calisma_saat) {
        this.etut_calisma_saat = etut_calisma_saat;
    }
}
