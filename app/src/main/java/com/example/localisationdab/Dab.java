package com.example.localisationdab;

import android.database.Cursor;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Dab {
    private int id;
    private Double latitude;
    private Double longitude;
    private String adresse;
    private String etat;
    private String nomInstitut;


    public Dab(int id, Double latitude, Double longitude, String adresse, String etat, String nomInstitut) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresse = adresse;
        this.etat = etat;
        this.nomInstitut = nomInstitut;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getNomInstitut() {
        return nomInstitut;
    }

    public void setNomInstitut(String nomInstitut) {
        this.nomInstitut = nomInstitut;
    }
}