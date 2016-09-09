/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_cleanDB_2;

import data.CleanBetEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author janmu
 */
public class Simulacia_81_Kombinacii {

    public static DecimalFormat df = new DecimalFormat("###.##");
    private static final double K_1 = 2.33;
    private static final double K_2 = 3.3;
    private static final double K_X = 3.3;

    private String vypisKombinaciu(Kombinacia k) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k.eventy81.length; i++) {
            sb.append(k.eventy81[i].eventsCBE[k.zvoleneTipy[i]] + "\n");
        }
        return sb.toString();
    }

    private class Event81 implements Comparable<Event81> {

        private final CleanBetEvent eventsCBE[] = new CleanBetEvent[3];
        private double distance;

        public void pridajEvent(CleanBetEvent cbe) {
            if (cbe.poznamka.equalsIgnoreCase("1") || cbe.poznamka.equalsIgnoreCase("0") || cbe.poznamka.equalsIgnoreCase("2")) {
                if (cbe.poznamka.equalsIgnoreCase("1")) {
                    eventsCBE[0] = cbe;
                }
                if (cbe.poznamka.equalsIgnoreCase("2")) {
                    eventsCBE[2] = cbe;
                }
                if (cbe.poznamka.equalsIgnoreCase("0")) {
                    eventsCBE[1] = cbe;
                }
                if (mame_1_0_2()) {
                    distance = Math.sqrt((K_1 - eventsCBE[0].kurz) * (K_1 - eventsCBE[0].kurz)
                            + (K_2 - eventsCBE[2].kurz) * (K_2 - eventsCBE[2].kurz)
                            + (K_X - eventsCBE[1].kurz) * (K_X - eventsCBE[1].kurz));
                }
            } else {
                // pridavame iny kurz ako 1,0 alebo 2 ktore nas nezaujimaju
            }
        }

        public boolean mame_1_0_2() {
            return eventsCBE[0] != null && eventsCBE[1] != null && eventsCBE[2] != null;
        }

        @Override
        public int compareTo(Event81 o) {
            return Double.compare(distance, o.distance);
        }

//        @Override
//        public String toString() {
//            return "Event81{" + "distance= " + distance + ", kurz_1=" + kurz_1 + ", kurz_2=" + kurz_2 + ", kurz_x=" + kurz_x + ", events=\n   " + events.get(0) + "\n  " + events.get(1) + "\n  " + events.get(2) + ",  k_1=" + k_1 + ", k_2=" + k_2 + ", k_x=" + k_x + '}';
//        }
    }

    private class Kombinacia implements Comparable<Kombinacia> {

        private final int zvoleneTipy[] = new int[4]; // 3 moznosti
        Event81 eventy81[] = new Event81[4];
        private double vyslednaKombinacia = 1;

        /**
         *
         * @param ev
         * @param tip_pos- ci je to kurz 1,0 alebo 2
         * @param kombinacia_pos poradove cislo zapasu v 4-kombinacii
         */
        public void addKombinacia(Event81 ev, int tip_pos, int kombinacia_pos) {
            vyslednaKombinacia *= ev.eventsCBE[tip_pos].kurz;
            eventy81[kombinacia_pos] = ev;
            zvoleneTipy[kombinacia_pos] = tip_pos; // aby sme vedeli z eventu zistit ktoru moznost sme zvolili pre zistenie vyhernosti
        }

        public double getVyslednaKombinacia() {
            return vyslednaKombinacia;
        }

        @Override
        public int compareTo(Kombinacia o) {
            return Double.compare(vyslednaKombinacia, o.vyslednaKombinacia);
        }

        @Override
        public String toString() {
            return "Kombinacia{" + "zvolenyTip=" + Arrays.toString(zvoleneTipy) + ", vyslednaKombinacia=" + vyslednaKombinacia + ", eventy81=" + Arrays.toString(eventy81) + '}';
        }

    }

    public double simulacia_81moznosti(List<CleanBetEvent> eventy) {
        String INDENT = "     ";
        /*
        - prechadzame vsetky eventy a vyberieme z nich tie, ktore su typ zapas a maju vsetky kurzy definovane: 1,0 a 2
        - vybrane udalosti usporiadame podla toho ako blizko su k zadanym kriteriam: 2.33, 3 a 3
         */
        // 1. zgrupenie a vybratie vybranych zapasov
        List<Event81> vybrane = new ArrayList<>();
        int idx = 0;
        while (idx < eventy.size()) {
            CleanBetEvent cbe = eventy.get(idx);
            if (cbe.typEventu.equalsIgnoreCase("zápas") || cbe.typEventu.equalsIgnoreCase("1X2")) {
                // nasli sme nas typ eventu, ideme vytvorit zgrupenie
                Event81 novy = new Event81();
                novy.pridajEvent(cbe);
                // zistime idcko
                int id = getIDFromCompetitors(cbe.competitors);
                // najdeme mu kamaratov
                idx++;
                while (idx < eventy.size()) {
                    if (id == getIDFromCompetitors(eventy.get(idx).competitors)) {
                        novy.pridajEvent(eventy.get(idx));
                    } else {
                        break;
                    }
                    idx++;
                }
                // nasli sme vsetkych potencionalnych kamaratov, pozrieme sa ci su vsetci
                if (novy.mame_1_0_2()) {
                    vybrane.add(novy);
                    //            System.out.println(INDENT + "" + novy);
                }
            }
            idx++;
        }
        Collections.sort(vybrane);
//        for (Event81 ev : vybrane) {
//            System.out.println(INDENT + "" + ev);
//        }
        System.out.println(INDENT + " vybranych potencionalnych: " + vybrane.size());
        // vyberieme zo zoznamu prvych 4
        if (vybrane.size() < 4) {
            return 0;
        }
        // 2. KOMBINOVANIE 4 VYBRANYCH
        List<Kombinacia> kombinacie = new ArrayList<>();
//        System.out.println(Arrays.toString(vybrane.get(0).kurzy));
//        System.out.println(Arrays.toString(vybrane.get(1).kurzy));
//        System.out.println(Arrays.toString(vybrane.get(2).kurzy));
//        System.out.println(Arrays.toString(vybrane.get(3).kurzy));
        // vygenerujeme kombinacie zapasov z tychto 4 tiketov
        for (int i1 = 0; i1 < 3; i1++) {
            for (int i2 = 0; i2 < 3; i2++) {
                for (int i3 = 0; i3 < 3; i3++) {
                    for (int i4 = 0; i4 < 3; i4++) {
                        Kombinacia k = new Kombinacia();
                        k.addKombinacia(vybrane.get(0), i1, 0);
                        k.addKombinacia(vybrane.get(1), i2, 1);
                        k.addKombinacia(vybrane.get(2), i3, 2);
                        k.addKombinacia(vybrane.get(3), i4, 3);
                        kombinacie.add(k);
                    }
                }
            }
        }
        Collections.sort(kombinacie);
//        System.out.println("Kombinacie: ");
//        for (Kombinacia k : kombinacie) {
//            System.out.println(jeVyherna(k) + "      " + k);
//        }
        // 3. ZVOLENIE 26 VYBRANYCH KOMBINACII A VYPOCET ZMENY STAVU PENAZENKY
        double suma = 0;
        int TIKETOV = 13;
//        for (int i = 0; i < 13; i++) {
//            Kombinacia k = kombinacie.get(i);
//            if (jeVyherna(k)) {
//                System.out.println("nasli sme vyhernu kombinaciu: +" + df.format(k.vyslednaKombinacia)+"-"+TIKETOV+" = +"+df.format(k.vyslednaKombinacia - TIKETOV));
//                System.out.print(vypisKombinaciu(k));
//                return k.vyslednaKombinacia - TIKETOV; //tolko sme vlozili
//            }
//        }
        for (int i = 81 - 13; i < 81; i++) {
            Kombinacia k = kombinacie.get(i);
            if (jeVyherna(k)) {
                System.out.println("nasli sme vyhernu kombinaciu: +" + df.format(k.vyslednaKombinacia) + "-" + TIKETOV + " = +" + df.format(k.vyslednaKombinacia - TIKETOV));
                System.out.print(vypisKombinaciu(k));
                return k.vyslednaKombinacia - TIKETOV; //tolko sme vlozili
            }
        }
        System.out.println("NENASLI sme vyhernu kombinaciu: -" + TIKETOV);
        return -TIKETOV; // stavili sme na kombinacie ale nevyhrali sme nic
    }

    private int getIDFromCompetitors(String competitors) {
        return Integer.parseInt(competitors.substring(0, competitors.indexOf(" ")));
    }

    private boolean jeVyherna(Kombinacia k) {
        for (int i = 0; i < k.eventy81.length; i++) {
            if (!k.eventy81[i].eventsCBE[k.zvoleneTipy[i]].vyherny) {
                return false;
            }
        }
        return true;
    }
}
