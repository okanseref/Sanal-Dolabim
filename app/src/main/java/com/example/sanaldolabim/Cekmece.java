package com.example.sanaldolabim;

import java.util.ArrayList;
import java.util.List;

public class Cekmece {
    private String cekmeceAdi;
    int ID;
    public Cekmece(String cekmeceAdi) {
        this.cekmeceAdi = cekmeceAdi;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCekmeceAdi() {
        return cekmeceAdi;
    }
}
