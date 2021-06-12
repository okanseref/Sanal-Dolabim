package com.example.sanaldolabim;

import java.io.Serializable;

public class Kiyafet  implements Serializable {
    private String kiyafetTuru,renk,desen,tarih,fiyat,cekmeceAdi;
    private int ID;
    private byte[] foto;

    public Kiyafet(String kiyafetTuru, String renk, String desen, String tarih, String fiyat, byte[] foto,String cekmeceAdi) {
        this.kiyafetTuru = kiyafetTuru;
        this.renk = renk;
        this.desen = desen;
        this.tarih = tarih;
        this.fiyat = fiyat;
        this.foto = foto;
        this.cekmeceAdi = cekmeceAdi;
    }

    public String getCekmeceAdi() {
        return cekmeceAdi;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getKiyafetTuru() {
        return kiyafetTuru;
    }

    public void setKiyafetTuru(String kiyafetTuru) {
        this.kiyafetTuru = kiyafetTuru;
    }

    public String getRenk() {
        return renk;
    }

    public void setRenk(String renk) {
        this.renk = renk;
    }

    public String getDesen() {
        return desen;
    }

    public void setDesen(String desen) {
        this.desen = desen;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
