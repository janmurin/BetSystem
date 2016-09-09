/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import make_cleanDB.*;


public class CleanBetEvent implements Comparable<CleanBetEvent>{
    
    public String date;
    public String competitors;
//    String result;
    public String sport;
    public String liga;
    public String typEventu;
    public String poznamka;
    public double kurz;
    public boolean vyherny;
    //int id;
//    double kurz_1;
//    double kurz_X;
//    double kurz_2;

    @Override
    public String toString() {
        return "{"+"\"sport\":\""+sport+"\","
                +"\"liga\":\""+liga+"\","
                +"\"typEventu\":\""+typEventu+"\","
                + "\"date\":\""+date+"\","
                + "\"competitors\":\""+competitors+"\","
                + "\"kurz\":\""+kurz+"\","
                + "\"vyherny\":\""+vyherny+"\","
                + "\"poznamka\":\""+poznamka+"\"}\n";
//                + "\"kurz_2\":\""+kurz_2+"\"}";
    }

    public String getDate() {
        return date;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    
    public void setDate(String date) {
        this.date = date;
    }

    public String getCompetitors() {
        return competitors;
    }

    public void setCompetitors(String competitors) {
        this.competitors = competitors;
    }

    public double getKurz() {
        return kurz;
    }

    public void setKurz(double kurz) {
        this.kurz = kurz;
    }

    public boolean isVyherny() {
        return vyherny;
    }

    public void setVyherny(boolean vyherny) {
        this.vyherny = vyherny;
    }
    


    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getLiga() {
        return liga;
    }

    public void setLiga(String liga) {
        this.liga = liga;
    }

    public String getTypEventu() {
        return typEventu;
    }

    public void setTypEventu(String typEventu) {
        this.typEventu = typEventu;
    }

    @Override
    public int compareTo(CleanBetEvent o) {
       return Double.compare(kurz, o.kurz);
    }
    
    
}
