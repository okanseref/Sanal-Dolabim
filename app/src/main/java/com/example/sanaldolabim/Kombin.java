package com.example.sanaldolabim;

import java.io.Serializable;

public class Kombin implements Serializable {
    private String basustu,surat,ustbeden,altbeden,ayakkabi,ID;

    public Kombin(String basustu, String surat, String ustbeden, String altbeden, String ayakkabi) {
        this.basustu = basustu;
        this.surat = surat;
        this.ustbeden = ustbeden;
        this.altbeden = altbeden;
        this.ayakkabi = ayakkabi;
    }

    public String getBasustu() {
        return basustu;
    }

    public void setBasustu(String basustu) {
        this.basustu = basustu;
    }

    public String getSurat() {
        return surat;
    }

    public void setSurat(String surat) {
        this.surat = surat;
    }

    public String getUstbeden() {
        return ustbeden;
    }

    public void setUstbeden(String ustbeden) {
        this.ustbeden = ustbeden;
    }

    public String getAltbeden() {
        return altbeden;
    }

    public void setAltbeden(String altbeden) {
        this.altbeden = altbeden;
    }

    public String getAyakkabi() {
        return ayakkabi;
    }

    public void setAyakkabi(String ayakkabi) {
        this.ayakkabi = ayakkabi;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
