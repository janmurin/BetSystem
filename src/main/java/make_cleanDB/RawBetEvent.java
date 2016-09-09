/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package make_cleanDB;

/**
 *
 * @author student
 */
public class RawBetEvent {
    
    String date;
    String competitors;
//    String result;
    String sport;
    String liga;
    String typEventu;
    String poznamka;
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
                + "\"poznamka\":\""+poznamka+"\"}";
//                + "\"kurz_1\":\""+kurz_1+"\","
//                + "\"kurz_X\":\""+kurz_X+"\","
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
    
    

//    public String getResult() {
//        return result;
//    }
//
//    public void setResult(String result) {
//        this.result = result;
//    }
//
//    public double getKurz_1() {
//        return kurz_1;
//    }
//
//    public void setKurz_1(double kurz_1) {
//        this.kurz_1 = kurz_1;
//    }
//
//    public double getKurz_X() {
//        return kurz_X;
//    }
//
//    public void setKurz_X(double kurz_X) {
//        this.kurz_X = kurz_X;
//    }
//
//    public double getKurz_2() {
//        return kurz_2;
//    }
//
//    public void setKurz_2(double kurz_2) {
//        this.kurz_2 = kurz_2;
//    }
//
//    public int compareTo(RawBetEvent o) {
//        if (Math.min(kurz_1, kurz_2) < Math.min(o.kurz_1, o.kurz_2)) {
//            return -1;
//        } else {
//            return 1;
//        }
//    }

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
    
    
}
