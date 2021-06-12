package com.example.sanaldolabim;

import java.io.Serializable;

public class Etkinlik implements Serializable {
    private String isim,turu,tarih,lokasyon,kombinId,ID;

    public Etkinlik(String isim, String turu, String tarih, String lokasyon, String kombinId) {
        this.isim = isim;
        this.turu = turu;
        this.tarih = tarih;
        this.lokasyon = lokasyon;
        this.kombinId = kombinId;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getTuru() {
        return turu;
    }

    public void setTuru(String turu) {
        this.turu = turu;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getLokasyon() {
        return lokasyon;
    }

    public void setLokasyon(String lokasyon) {
        this.lokasyon = lokasyon;
    }

    public String getKombinId() {
        return kombinId;
    }

    public void setKombinId(String kombinId) {
        this.kombinId = kombinId;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
