/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated;

/**
 *
 * @author Janco1
 */
public class KurzTyp implements Comparable<KurzTyp> {

    String typEventu;
    Kurz kurz;

    public double getPomerZarobokPocet() {
        return kurz.zarobok / kurz.pocetVyskytov;
    }

    @Override
    public int compareTo(KurzTyp o) {
//        if (kurz.zarobok / kurz.pocetVyskytov > o.kurz.zarobok / o.kurz.pocetVyskytov) {
//            return -1;
//        }else{
//            return 1;
//        }
        return Double.compare(kurz.zarobok / kurz.pocetVyskytov, o.kurz.zarobok / o.kurz.pocetVyskytov);
    }
}
