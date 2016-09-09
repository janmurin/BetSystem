/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_cleanDB_2;

import data.TipovaciDen;
import data.VysledokTipovaciDen;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import data.CleanBetEvent;
import data.Tiket;

/**
 *
 * @author Janco1
 */
public class Spustac2 {

    private static final String DATABAZA_CLEAN_FOLDER = "C:\\Users\\jan.murin\\Google Drive\\databaza_clean";
    public static DecimalFormat df = new DecimalFormat("###.##");
    public static final double VKLAD = 1.0;
    private static final int MAX_VYHRA = 30000;

    public void execute() {
        // STRATEGIA: postupne nacitavame po mesiacoch eventy a pre kazdy den v mesiaci dostaneme k dispozicii zoznam eventov
        // ziskame najprv mesiace
        File[] mesiaceFiles = new File(DATABAZA_CLEAN_FOLDER).listFiles();
        // prechadzame mesiace a nacitavame postupne databazu kurzov k danemu mesiacu
//        List<VysledokTipovaciDen> stg1_vysledky = new ArrayList<>();
        List<VysledokTipovaciDen> stg2_vysledky = new ArrayList<>();
        List<VysledokTipovaciDen> stg3_vysledky = new ArrayList<>();
        int vsetkych = 0;
        //for (int i = mesiaceFiles.length - 1; i < mesiaceFiles.length; i++) {
        for (int i = 0; i < mesiaceFiles.length; i++) {
            List<VysledokTipovaciDen> stg1_vysledky = new ArrayList<>();
           // List<VysledokTipovaciDen> stg2_vysledky = new ArrayList<>();
            //List<VysledokTipovaciDen> stg3_vysledky = new ArrayList<>();
            File mesiacFile = mesiaceFiles[i];
            System.out.println("");
            System.out.println("===========================================================================================");
            System.out.println("analyzujem mesiac [" + mesiacFile.getName().substring(0, 7) + "]");
            // nacitame si jednotlive dni mesiaca so svojimi eventami do zoznamu
            List<TipovaciDen> tipovacieDni = getTipovacieDniFromFile(mesiacFile);
            // prejdeme jednotlive tipovacie dni a skusame rozne strategie
            System.out.println("tipovacie dni size: " + tipovacieDni.size());
            for (TipovaciDen td : tipovacieDni) {
                //System.out.println("analyzujem den [" + td.den + "], pocet eventov: " + td.eventy.size());
                vsetkych += td.eventy.size();
                // tu sa budu skusat rozne tipovacie strategie
                //
                // strategia 1: budem tipovat kombinacie zapasov s kurzami do 1,5
                // vysledkom je stav penazenky, pocet vyhratych a prehratych tiketov
                //if (td.den.equals("2015-06-30")) {
                // stg1_vysledky.add(stg1_zapasyNizkeKurzy(td.eventy));
                //}
                VysledokTipovaciDen vysl = stg2_zapasyVysokeKurzy(td.eventy);
                stg2_vysledky.add(vysl);
//                stg3_vysledky.add(vysl);
                //stg3_vysledky.add(stg3_zapasyVysokeKurzy(td.eventy));
            }
            // teraz vypiseme uspesnot jednotlivych strategii
            //vypisUspesnost_stg1(stg1_vysledky, "kombinacie zapasov s nizkymi kurzami");
            vypisUspesnost_stg1(stg2_vysledky, "kombinacie zapasov s vysokymi kurzami");
//            vypisUspesnost_stg1(stg3_vysledky, "CELKOVA USPESNOST: kombinacie zapasov s vysokymi kurzami");
        }

        System.out.println("vsetkych eventov: " + vsetkych);
        vypisUspesnost_stg1(stg2_vysledky, "kombinacie zapasov s vysokymi kurzami");
    }

    private List<TipovaciDen> getTipovacieDniFromFile(File mesiacFile) {
        System.out.println("nacitavam subor: " + mesiacFile.getName());
        long start2 = System.currentTimeMillis();
        // nacitame z jsona vsetky eventy
        List<TipovaciDen> tipovacieDni = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<CleanBetEvent> eventyMesiaca = new ArrayList<>();
        try {
            eventyMesiaca = mapper.readValue(mesiacFile, new TypeReference<List<CleanBetEvent>>() {
            });
        } catch (IOException ex) {
            Logger.getLogger(Spustac2.class.getName()).log(Level.SEVERE, null, ex);
        }
        // roztriedime eventy do jednotlivych dni
        for (CleanBetEvent cbe : eventyMesiaca) {
            insertIntoTipovaciDen(cbe, tipovacieDni);
        }
        Collections.sort(tipovacieDni);
        System.out.println("subor " + mesiacFile.getName() + " nacitany za " + (System.currentTimeMillis() - start2) + " ms");
        return tipovacieDni;
    }

    private void insertIntoTipovaciDen(CleanBetEvent cbe, List<TipovaciDen> tipovacieDni) {
        // najdeme aktualnemu eventu tipovaci den kam patri a priradime ho k nemu
        for (TipovaciDen td : tipovacieDni) {
            if (cbe.date.equals(td.den)) {
                td.eventy.add(cbe);
                return;
            }
        }
        // vytvorime novy tipovaci den
        TipovaciDen td = new TipovaciDen(cbe.date);
        td.eventy.add(cbe);
        tipovacieDni.add(td);
    }

    public static void main(String[] args) {
        Spustac2 spustac = new Spustac2();
        spustac.execute();
    }

    private VysledokTipovaciDen stg1_zapasyNizkeKurzy(List<CleanBetEvent> eventy) {
        VysledokTipovaciDen vysledok = new VysledokTipovaciDen();
        /*
         taktika: 
         1. vytiahneme si zo zoznamu tie eventy, ktore su typ zapas a maju kurz do 1.5
         2. robime tikety z 5 zapasovych kombinacii
         */
        //=========================================================================================
        Collections.shuffle(eventy);
        Queue<CleanBetEvent> vyhovujuce = new LinkedList<>();
        for (CleanBetEvent cbe : eventy) {
            if (cbe.typEventu.equals("Zápas") && cbe.kurz <= 1.5) {
                vyhovujuce.add(cbe);
            }
        }
        System.out.println("stg1_zapasyNizkeKurzy den: " + eventy.get(0).date + " size:" + vyhovujuce.size());

        // TODO: z vyhovujucich povyhadzovat najviac rizikove pomocou nejakeho machine learning, branim do uvahy minule vysledky
        // System.out.println("vyhovujuce size: " + vyhovujuce.size());
        // vytahujeme z vyhovujucich po tolko kolko je urcena velkost mnoziny a robime kombinacie
        List<CleanBetEvent> vybrane;
        int ZAPASOV_NA_TIKETE = 5;
        while (!vyhovujuce.isEmpty()) {
            // vytiahneme si z vyhovujucich tolko kolko nam treba eventov
            vybrane = new ArrayList<>();
            for (int i = 0; i < ZAPASOV_NA_TIKETE; i++) {
                if (vyhovujuce.peek() != null) {
                    vybrane.add(vyhovujuce.poll());
                } else {
                    break;
                }
            }
            if (vybrane.size() != ZAPASOV_NA_TIKETE) {
                break;
            }

            // urobime z nich kombinacie a spocitame uspesnost
            int[][] kombinacie = getKombinacieZapasov(vybrane, ZAPASOV_NA_TIKETE);
            Map<String, Double> vysledokTiketov;
            try {
                vysledokTiketov = getVysledokTiketov(vybrane, kombinacie);
                vysledok.vyhranych += vysledokTiketov.get("vyhranych");
                vysledok.prehranych += vysledokTiketov.get("prehranych");
                vysledok.penazenka += vysledokTiketov.get("penazenka");
            } catch (Exception e) {
                System.out.println("VYNIMKA GET VYSLEDOK TIKETOV");
            }
        }

        return vysledok;
    }

    private int[][] getKombinacieZapasov(List<CleanBetEvent> dnesneEventy, int maxZapasov) {
        List<Integer> cisla = new ArrayList<>();
        for (int i = 0; i < dnesneEventy.size(); i++) {
            cisla.add(i);
        }
        // Create the initial vector
        ICombinatoricsVector<Integer> initialVector = Factory.createVector(cisla);

        // Create a simple combination generator to generate maxZapasov-combinations of the initial vector
        Generator<Integer> gen = Factory.createSimpleCombinationGenerator(initialVector, maxZapasov);

        int[][] vysledok = new int[(int) ArithmeticUtils.binomialCoefficient(dnesneEventy.size(), maxZapasov)][maxZapasov];
        // Print all possible combinations
        int i = 0;
        for (ICombinatoricsVector<Integer> combination : gen) {
            for (int j = 0; j < maxZapasov; j++) {
                vysledok[i][j] = combination.getValue(j);
            }
            i++;
        }

        return vysledok;
    }

    private Map<String, Double> getVysledokTiketov(List<CleanBetEvent> dnesneEventy, int[][] kombinacie) {
        // zistime stav penazenky, a pocty vyhranych a prehranych
        Map<String, Double> vysledok = new HashMap<>();
        //System.out.println("VYHODNOTENIE " + kombinacie.length + " TIKETOV");
        int vyhranychTiketov = 0;
        int prehranychTiketov = 0;
        double penazenka = 0;

        // prechadzam tikety
        for (int i = 0; i < kombinacie.length; i++) {
            //System.out.println("TIKET: " + i);
            boolean jeVyhranyTiket = true;
            double vyskaKurzu = 1;
            Tiket t = new Tiket();

            // prechadzam jednotlive zapasy na tikete
            for (int j = 0; j < kombinacie[0].length; j++) {
                int cisloZapasu = kombinacie[i][j];
                t.zapasy.add(dnesneEventy.get(cisloZapasu));

                vyskaKurzu *= dnesneEventy.get(cisloZapasu).kurz;
                if (!dnesneEventy.get(cisloZapasu).isVyherny()) {
                    jeVyhranyTiket = false;
                    //System.out.print("PREHRA   ");
                    //break;
                } else {
                    //System.out.print("vyhra    ");
                }
                //System.out.println(dnesneEventy.get(cisloZapasu));
            }

            t.jeVyherny = jeVyhranyTiket;
            t.kurz = vyskaKurzu;
            //vybraneTikety.add(t);
            if (jeVyhranyTiket) {
                vyhranychTiketov++;
                penazenka += vyskaKurzu * VKLAD - VKLAD;
                System.out.println(t);
            } else {
                prehranychTiketov++;
                penazenka -= VKLAD;
            }
            if (jeVyhranyTiket) {
                for (int j = 0; j < kombinacie[0].length; j++) {
                    int cisloZapasu = kombinacie[i][j];
                    //System.out.println(dnesneEventy.get(cisloZapasu));
                }
            }
            //System.out.println("kurz: " + t.kurz + " penazenka: " + penazenka);
            //System.out.println("");
        }

        vysledok.put(
                "vyhranych", new Double(vyhranychTiketov));
        vysledok.put(
                "prehranych", new Double(prehranychTiketov));
        vysledok.put(
                "penazenka", penazenka);
//        if (vyhranychTiketov == (int) ArithmeticUtils.binomialCoefficient(dnesneEventy.size(), kombinacie[0].length)) {
//            vsetkyUhadnute++;
//        }
//        if (vyhranychTiketov == (int) ArithmeticUtils.binomialCoefficient(dnesneEventy.size() - 1, kombinacie[0].length)) {
//            jedenZle++;
//        }
//        if (vyhranychTiketov == 0) {
//            vsetkyZle++;
//        }
//        if (vyhranychTiketov == 1) {
//            dveZle++;
//        }
        //System.out.println(vysledok);
        //System.out.println("- - - - - - - - - - - - - - - - ");
        return vysledok;
    }

    private void vypisUspesnost_stg1(List<VysledokTipovaciDen> stg1_vysledky, String popis) {
        System.out.println("===============================================");
        System.out.println("vypisujem uspesnost strategie: " + popis);
        int vyhranych = 0;
        int prehranych = 0;
        double penazenka = 0;
        for (VysledokTipovaciDen vtd : stg1_vysledky) {
            vyhranych += vtd.vyhranych;
            prehranych += vtd.prehranych;
            penazenka += vtd.penazenka;
        }
        double uspesnost = vyhranych / ((double) vyhranych + prehranych);
        System.out.println("vyhranych: " + vyhranych);
        System.out.println("prehranych: " + prehranych);
        System.out.println("PENAZENKA: " + df.format(penazenka));
        System.out.println("uspesnost: " + df.format(uspesnost));
        System.out.println("");
    }

    private VysledokTipovaciDen stg2_zapasyVysokeKurzy(List<CleanBetEvent> eventy) {
        VysledokTipovaciDen vysledok = new VysledokTipovaciDen();
        /*
         taktika: 
         1. vytiahneme si zo zoznamu tie eventy, ktore su typ zapas a maju kurz do 1.5
         2. robime tikety z 5 zapasovych kombinacii
         */
        //=========================================================================================
        Collections.shuffle(eventy);
        Queue<CleanBetEvent> vyhovujuce = new LinkedList<>();
        for (CleanBetEvent cbe : eventy) {
            if (cbe.typEventu.equals("1.polčas") && cbe.kurz >= 5 && cbe.kurz <= 10) {
                vyhovujuce.add(cbe);
            }
        }
        //System.out.println("stg2_zapasyVysokeKurzy den: " + eventy.get(0).date + " size:" + vyhovujuce.size());

        // TODO: z vyhovujucich povyhadzovat najviac rizikove pomocou nejakeho machine learning, branim do uvahy minule vysledky
        // System.out.println("vyhovujuce size: " + vyhovujuce.size());
        // vytahujeme z vyhovujucich po tolko kolko je urcena velkost mnoziny a robime kombinacie
        List<CleanBetEvent> vybrane;
        int ZAPASOV_NA_TIKETE = 6;
        while (!vyhovujuce.isEmpty()) {
            // vytiahneme si z vyhovujucich tolko kolko nam treba eventov
            vybrane = new ArrayList<>();
            for (int i = 0; i < ZAPASOV_NA_TIKETE; i++) {
                if (vyhovujuce.peek() != null) {
                    vybrane.add(vyhovujuce.poll());
                } else {
                    break;
                }
            }
            if (vybrane.size() != ZAPASOV_NA_TIKETE) {
                break;
            }

            // urobime z nich kombinacie a spocitame uspesnost
            int[][] kombinacie = getKombinacieZapasov(vybrane, ZAPASOV_NA_TIKETE - 2);
            Map<String, Double> vysledokTiketov;
            try {
                vysledokTiketov = getVysledokTiketov(vybrane, kombinacie);
                vysledok.vyhranych += vysledokTiketov.get("vyhranych");
                vysledok.prehranych += vysledokTiketov.get("prehranych");
                vysledok.penazenka += vysledokTiketov.get("penazenka");
            } catch (Exception e) {
                System.out.println("VYNIMKA GET VYSLEDOK TIKETOV");
            }
        }

        return vysledok;
    }

    private VysledokTipovaciDen stg3_zapasyVysokeKurzy(List<CleanBetEvent> eventy) {
        VysledokTipovaciDen vysledok = new VysledokTipovaciDen();
        /*
         taktika: 
         1. vytiahneme si zo zoznamu tie eventy, ktore su typ zapas a maju kurz do 1.5
         2. robime tikety z 5 zapasovych kombinacii
         */
        //=========================================================================================
        Collections.shuffle(eventy);
        Queue<CleanBetEvent> vyhovujuce = new LinkedList<>();
        for (CleanBetEvent cbe : eventy) {
            if (cbe.typEventu.equals("Zápas") && cbe.kurz >= 1.7 && cbe.kurz <= 3) {
                vyhovujuce.add(cbe);
            }
        }
        System.out.println("stg3_zapasyVysokeKurzy den: " + eventy.get(0).date + " size:" + vyhovujuce.size());

        // TODO: z vyhovujucich povyhadzovat najviac rizikove pomocou nejakeho machine learning, branim do uvahy minule vysledky
        // System.out.println("vyhovujuce size: " + vyhovujuce.size());
        // vytahujeme z vyhovujucich po tolko kolko je urcena velkost mnoziny a robime kombinacie
        List<CleanBetEvent> vybrane;
        int ZAPASOV_NA_TIKETE = 3;
        while (!vyhovujuce.isEmpty()) {
            // vytiahneme si z vyhovujucich tolko kolko nam treba eventov
            vybrane = new ArrayList<>();
            for (int i = 0; i < ZAPASOV_NA_TIKETE; i++) {
                if (vyhovujuce.peek() != null) {
                    vybrane.add(vyhovujuce.poll());
                } else {
                    break;
                }
            }
            if (vybrane.size() != ZAPASOV_NA_TIKETE) {
                break;
            }

            // urobime z nich kombinacie a spocitame uspesnost
            int[][] kombinacie = getKombinacieZapasov(vybrane, ZAPASOV_NA_TIKETE);
            Map<String, Double> vysledokTiketov;
            try {
                vysledokTiketov = getVysledokTiketov(vybrane, kombinacie);
                vysledok.vyhranych += vysledokTiketov.get("vyhranych");
                vysledok.prehranych += vysledokTiketov.get("prehranych");
                vysledok.penazenka += vysledokTiketov.get("penazenka");
            } catch (Exception e) {
                System.out.println("VYNIMKA GET VYSLEDOK TIKETOV");
            }
        }

        return vysledok;
    }

}
