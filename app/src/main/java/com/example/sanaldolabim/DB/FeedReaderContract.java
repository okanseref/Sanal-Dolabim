package com.example.sanaldolabim.DB;

import android.provider.BaseColumns;

public class FeedReaderContract {
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class CekmeceDB implements BaseColumns {
        //username,surname,password,email,phone,bdate;
        public static final String TABLE_NAME = "cekmeceler";
        public static final String CEKMECEADI = "cekmeceadi";
    }
    /* Inner class that defines the table contents */
    public static class KiyafetDB implements BaseColumns {
        //username,surname,password,email,phone,bdate;
        public static final String TABLE_NAME = "kiyafetler";
        public static final String turu = "turu";
        public static final String renk = "renk";
        public static final String desen = "desen";
        public static final String tarih = "tarih";
        public static final String fiyat = "fiyat";
        public static final String foto = "foto";
        public static final String cekmeceAdi = "cekmeceAdi";
    }
    /* Inner class that defines the table contents */
    public static class KombinDB implements BaseColumns {
        //username,surname,password,email,phone,bdate;
        public static final String TABLE_NAME = "kombinler";
        public static final String basustu = "basustu";
        public static final String surat = "surat";
        public static final String ustbeden = "ustbeden";
        public static final String altbeden = "altbeden";
        public static final String ayakkabi = "ayakkabi";
    }
    /* Inner class that defines the table contents */
    public static class EtkinlikDB implements BaseColumns {
        //username,surname,password,email,phone,bdate;
        public static final String TABLE_NAME = "etkinlikler";
        public static final String isim = "isim";
        public static final String turu = "turu";
        public static final String tarihi = "tarihi";
        public static final String lokasyon = "lokasyon";
        public static final String kombin = "kombin";
    }

}
