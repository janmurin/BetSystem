/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated;

import java.text.DecimalFormat;

/**
 *
 * @author Janco1
 */
public class Vysledok implements Comparable<Vysledok> {

    double kurz;
    double minKurz;
    int pocetDni;
    int pocetZapasovNaTikete;
    int pocetTiketov;
    double penazenka;
    double uspesnost;
    double ziskovost;
    int velkostMnoziny;
    DecimalFormat df=new DecimalFormat("##.##");
    int vyhranych;

    public int compareTo(Vysledok o) {
//        if (penazenka == Double.NEGATIVE_INFINITY || penazenka == Double.POSITIVE_INFINITY || penazenka == Double.NaN
//                || o.penazenka == Double.NEGATIVE_INFINITY || o.penazenka == Double.POSITIVE_INFINITY || o.penazenka == Double.NaN){
//            System.out.println("PENAZENKA MA ZLU HODNOTU");
//        }
        if (penazenka > o.penazenka) {
            return -1;
        }
        if (penazenka == o.penazenka) {
            return 0;
        }
        if (penazenka < o.penazenka) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        String s=String.format("zapasov=%4s kurz: %7s  tiketov: %5s  vyhranych: %5s  penazenka: %10s   uspesnost: %5s   ziskovost: %7s   velkost mnoziny: %2s",
                pocetZapasovNaTikete,(df.format(minKurz) + "-" + df.format(kurz)),pocetTiketov,vyhranych,df.format(penazenka),df.format(uspesnost),df.format(ziskovost),velkostMnoziny);
//        return //"pocetDni=" + pocetDni + " "
//                 "zapasov=" + pocetZapasovNaTikete + " "
//                + "kurz: " + df.format(minKurz) + "-" + df.format(kurz) + " "
//                + "tiketov: " + pocetTiketov + " "
//                + "vyhranych: " + vyhranych + " "
//                + "penazenka: " + df.format(penazenka) + " "
//                + "uspesnost: " + df.format(uspesnost) + "  "
//                + "ziskovost: " + df.format(ziskovost)+"  "
//                +"velkost mnoziny: "+velkostMnoziny;
        return s;
    }

}
