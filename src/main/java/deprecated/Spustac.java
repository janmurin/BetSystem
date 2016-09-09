/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated;

import data.Tiket;
import data.CleanBetEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.apache.commons.math3.util.ArithmeticUtils;

/**
 *
 * @author Janco1
 */
public class Spustac {

    public static final double VKLAD = 1.0;
    private static final int MAX_VYHRA = 10000;
    public static DecimalFormat df = new DecimalFormat("###.##");
    private static ArrayList<KurzTyp> kurzyTypy;
    private static int pocetMaloKombinacii;

    public static void main(String[] args) throws IOException {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

        ArrayList<CleanBetEvent> eventy = getEventyFromCleanDB();
        List<TypEventu> typyEventov = getTypyEventov(eventy);
        Collections.sort(typyEventov);

//        // pre kazdy typ eventu robime nejake vypocty a strategie
//        // vypiseme vsetky kurzy vsetkych typov a 10 najlepsich
//        kurzyTypy = new ArrayList<KurzTyp>();
//        for (TypEventu te : typyEventov) {
////            System.out.println("");;
////            System.out.println("====================" + te.nazov + " (size: " + te.eventy.size() + ")=====================");
//            List<Kurz> kurzy = vypocet2_uspesnostKurzov(te.eventy);
//            for (Kurz k : kurzy) {
//                KurzTyp kt = new KurzTyp();
//                kt.typEventu = te.nazov;
//                kt.kurz = k;
//                kurzyTypy.add(kt);
//            }
//        }
//        Collections.sort(kurzyTypy);
//        System.out.println("10 NAJUSPESNEJSICH KURZOV");
//        for (KurzTyp kt : kurzyTypy) {
//            if (kt.kurz.pocetVyskytov > 200) {
//                System.out.println(kt.typEventu+"  (" + df.format(kt.kurz.min) + " - " + df.format(kt.kurz.max) + ")       "
//                        + "" + kt.kurz.pocetVyskytov + "         "
//                        + "" + df.format(kt.kurz.getOcakavanaUspesnost()) + "         "
//                        + "" + df.format(kt.kurz.getRealnaUspesnost()) + "               "
//                        + "" + df.format(kt.kurz.zarobok) + "      " + kt.getPomerZarobokPocet());
//            }
//        }
        // vyskusame stavovat ze prvych 15 minut nepadne gol 0-15 minĂşta zĂˇpasu
        List<CleanBetEvent> mojeVybrane = new ArrayList<>();
        List<VysledokTypEventu> vysledkyEventov = new ArrayList<>();
        for (TypEventu te : typyEventov) {
            // prejdeme len specialne vybrany typ eventu s urcenymi parametrami
            if (te.nazov.equals("Víťaz vrátane predľženia")) {
                mojeVybrane = te.eventy;
                System.out.println("******************************************************************************");
                System.out.println("strategia: stavujeme na eventy [" + te.nazov + "] size: " + mojeVybrane.size());
                List<Vysledok> vysledky = new ArrayList<>();
                vysledky.add(getVysledkyKombinacii(mojeVybrane, 4, 5, 10, 8));
                System.out.println("");
                System.out.println("USPORIADANY VYPIS PODLA ZAROBKU");
                for (int i = 0; i < vysledky.size(); i++) {
                    if (vysledky.get(i).pocetTiketov > 10) {
                        double ziskovost = (vysledky.get(i).penazenka / (VKLAD)) / vysledky.get(i).pocetTiketov;
                        vysledky.get(i).ziskovost = ziskovost;
                        System.out.println(vysledky.get(i));
                        VysledokTypEventu vte = new VysledokTypEventu();
                        vte.typEventu = te.nazov;
                        vte.vysledok = vysledky.get(i);
                        vysledkyEventov.add(vte);
                    }
                }
            }
//            if (te.nazov.equals("Handicap 1.5:0")) {
//                mojeVybrane = te.eventy;
//                System.out.println("******************************************************************************");
//                System.out.println("strategia: stavujeme na eventy [" + te.nazov + "] size: " + mojeVybrane.size());
//                vysledkyEventov.addAll(strategia8_vysledkyKombinacieZapasov(mojeVybrane, te.nazov));
//                System.out.println("");
//            }
        }
        Collections.sort(vysledkyEventov);
        System.out.println("");
        System.out.println("NAJLEPSIE PARAMETRE: ZISKOVOST");
        for (int i = 0; i < vysledkyEventov.size(); i++) {
            VysledokTypEventu vte = vysledkyEventov.get(i);
            System.out.printf("[%30s]: ", vte.typEventu);
            System.out.println(vte.vysledok);
        }

        Collections.sort(vysledkyEventov, new VysledokPodlaPenazenkyKomparator());
        System.out.println("");
        System.out.println("NAJLEPSIE PARAMETRE: PENAZENKA");
        for (int i = 0; i < vysledkyEventov.size(); i++) {
            VysledokTypEventu vte = vysledkyEventov.get(i);
            System.out.printf("[%30s]: ", vte.typEventu);
            System.out.println(vte.vysledok);
        }

    }

    private static List<VysledokTypEventu> strategia8_vysledkyKombinacieZapasov(List<CleanBetEvent> events, String typEventu) {
//        System.out.println("STRATEGIA 8: Vysledky roznych kombinacii zapasov");
//        System.out.println("iba na informativne ucely, lebo pocet pocetTiketov na den sa lisi, takze tu nie je pevne dana strategia");
//        System.out.println("je to len ukazka ake vysledky sa mozu dosahovat ak sa spravne prisposobuje trendom a formam ");
        List<Vysledok> vysledky = new ArrayList<>();
        double[][] kurzy = {{1, 1.5}, {1.5, 2}, {2, 2.4}, {2.4, 2.8}, {2.8, 3.2}, {3.2, 5}, {5, 10}, {10, 20}, {20, 100}};
        // skusame tuto strategiu s roznymi parametrami
        // na kombinaciu kolkych zapasov chceme tipovat
        for (int pocetZapasov = 2; pocetZapasov < 10; pocetZapasov++) {
            // na ake kurzy chceme tipovat
            for (int i = 0; i < kurzy.length; i++) {
                // z akej velkej mnoziny robime kombinacie
                int obmedzenie = 10;
                double priemerKurzov = (kurzy[i][1] + kurzy[i][0]) / 2.0;
                for (int velkostMnoziny = pocetZapasov; velkostMnoziny < 10; velkostMnoziny++) {
                    if (Math.pow(priemerKurzov, pocetZapasov) > MAX_VYHRA) {
                        break;
                    }
                    //System.out.print("zapasov=" + pocetZapasovNaTikete + " pocetDni=" + pocetDni + " kurz: " + df.format(kurz) + "-" + df.format(kurz + 0.4) + "   ");
                    vysledky.add(getVysledkyKombinacii(events, pocetZapasov, kurzy[i][0], kurzy[i][1], velkostMnoziny));
                }
            }
            //System.out.println("");
        }
        List<VysledokTypEventu> vysledkyEventu = new ArrayList<>();
        //System.out.println(vysledky);
        Collections.sort(vysledky);
        System.out.println("");
        System.out.println("USPORIADANY VYPIS PODLA ZAROBKU");
        for (int i = 0; i < 10; i++) {
            if (vysledky.get(i).pocetTiketov > 10) {
                double ziskovost = (vysledky.get(i).penazenka / (VKLAD)) / vysledky.get(i).pocetTiketov;
                vysledky.get(i).ziskovost = ziskovost;
                System.out.println(vysledky.get(i));
                VysledokTypEventu vte = new VysledokTypEventu();
                vte.typEventu = typEventu;
                vte.vysledok = vysledky.get(i);
                vysledkyEventu.add(vte);
            }
        }

//        VysledokPodlaUspesnostiKomparator podlaUspesnosti = new VysledokPodlaUspesnostiKomparator();
//        Collections.sort(vysledky, podlaUspesnosti);
//        System.out.println("");
//        System.out.println("USPORIADANY VYPIS PODLA USPESNOSTI");
//        for (int i = 0; i < 10; i++) {
//            System.out.println(vysledky.get(i) + "  max velkostMnoziny: " + vysledky.get(i).velkostMnoziny);
//        }
//        System.out.println("");
//        System.out.println("PRIKLAD OPTIMALNEHO TIKETU");
//        for (BetEvent be : prikladTiket) {
//            System.out.println(be);
//        }
        return vysledkyEventu;
    }

    private static ArrayList<CleanBetEvent> getEventyFromCleanDB() throws IOException {
        System.out.println("loadujem eventy z adresara: databaza_clean");
        File dir = new File("databaza_clean");
        File[] subory = dir.listFiles();
        long start = System.currentTimeMillis();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<CleanBetEvent> events = new ArrayList<CleanBetEvent>();
        for (File subor : subory) {
            System.out.println("-----------------------------------------------------");
            System.out.print("loadujem subor: " + subor.getName());
            long start2 = System.currentTimeMillis();
            List<CleanBetEvent> aktEvents = mapper.readValue(subor, new TypeReference<List<CleanBetEvent>>() {
            });
            events.addAll(aktEvents);
            System.out.println("  events loaded time (ms): " + (System.currentTimeMillis() - start2));
            System.out.println("events.size=" + aktEvents.size());
        }

        System.out.println("TOTAL events loaded time (ms): " + (System.currentTimeMillis() - start));
        System.out.println("TOTAL events.size=" + events.size());
        return events;
    }

    private static List<Kurz> vypocet2_uspesnostKurzov(ArrayList<CleanBetEvent> events) {
//        System.out.println("VYPOCET 1:  Uspesnost kurzov ");
        //System.out.println("==============================");
        // vygenerujeme si zoznam kurzov
        ArrayList<Kurz> kurzy = new ArrayList<Kurz>();

        // vygeneruje 10 rozsahov
        for (double i = 1; i < 2; i += 0.1) {
            Kurz novy = new Kurz();
            novy.min = i;
            novy.max = i + 0.1;
            kurzy.add(novy);
        }
        for (double i = 2; i < 3; i += 0.2) {
            Kurz novy = new Kurz();
            novy.min = i;
            novy.max = i + 0.2;
            kurzy.add(novy);
        }
        // vygeneruje 3 rozsahov
        for (double i = 3; i < 4; i += 0.33) {
            Kurz novy = new Kurz();
            novy.min = i;
            novy.max = i + 0.33;
            kurzy.add(novy);
        }
        // vygeneruje 0.5 rozsahov
        for (double i = 4; i < 15; i += 0.5) {
            Kurz novy = new Kurz();
            novy.min = i;
            novy.max = i + 0.5;
            kurzy.add(novy);
        }
        Kurz novy = new Kurz();
        novy.min = 15;
        novy.max = 100;
        kurzy.add(novy);

        // vypocitame uspesnost jednotlivych kurzov
        for (CleanBetEvent be : events) {
            // zatriedime event podla kurzu do kategorie
            for (Kurz k : kurzy) {
                if (k.min <= be.kurz && k.max > be.kurz) {
                    if (be.isVyherny()) {
                        k.pocetUspechov++;
                        k.zarobok += VKLAD * be.kurz - VKLAD;
                    } else {
                        k.zarobok -= VKLAD;
                    }
                    k.sucetKurzov += be.kurz;
                    k.pocetVyskytov++;
                    break;
                }
            }
        }

//        System.out.println("KURZ    pocetVyskytov   ocakavanaUspesnost  realnaUspesnost     zarobok");
//        for (Kurz k : kurzy) {
//            System.out.println("(" + df.format(k.min) + " - " + df.format(k.max) + ")       "
//                    + "" + k.pocetVyskytov + "         "
//                    + "" + df.format(k.getOcakavanaUspesnost()) + "         "
//                    + "" + df.format(k.getRealnaUspesnost()) + "               "
//                    + "" + df.format(k.zarobok));
//        }
        return kurzy;
    }

    private static List<TypEventu> getTypyEventov(List<CleanBetEvent> eventy) {
        System.out.println("triedim eventy podla typu");
        long start = System.currentTimeMillis();
        List<TypEventu> typyEventov = new ArrayList<>();
        // zatriedime vsetky eventy podla typu
        for (CleanBetEvent cbe : eventy) {
            TypEventu typ = getTypEventuFromTypyEventov(cbe.typEventu, typyEventov);
            typ.eventy.add(cbe);
        }
        System.out.println("typyEventov size: " + typyEventov.size() + "  " + (System.currentTimeMillis() - start) + "ms");
        return typyEventov;
    }

    private static TypEventu getTypEventuFromTypyEventov(String typEventu, List<TypEventu> typyEventov) {
        // vratime typEventu ktoreho nzov je zhodny so stringom
        for (TypEventu typ : typyEventov) {
            if (typ.nazov.equals(typEventu)) {
                return typ;
            }
        }
        TypEventu typ = new TypEventu(typEventu);
        typyEventov.add(typ);
        return typ;
    }

    private static Vysledok strategia7888_ziskajNticeZapasov(List<CleanBetEvent> events, int pocetZapasovNaTikete, int pocetDni, double minKurz, double maxKurz, int velkostMnoziny) {
        //System.out.println("STRATEGIA 7:  Kombinacie zapasov na tikete");
        int pocetTiketov = 0;
        Vysledok vysledok = new Vysledok();
        vysledok.pocetDni = pocetDni;
        vysledok.pocetZapasovNaTikete = pocetZapasovNaTikete;
        vysledok.kurz = maxKurz;
        vysledok.minKurz = minKurz;
        vysledok.velkostMnoziny = velkostMnoziny;
        pocetMaloKombinacii = 0;
        /*
         taktika: 
         1. kazde 1-3 dni podavame tikety na zapasy z tychto dni
         2. tuto mnozinu zapasov redukujeme podla nejakych kriterii
         3. z redukovanej podmnoziny, v ktorej by mali byt uz iba "zaruceni" favoriti,
         vytvarame tikety o 2 az n zapasoch na tiket

         3 parametre: - pocet dni z ktorych obdobia zoberieme zapasy
         - filter (napriklad vyska kurzu u favorita)
         - pocet zapasov na tikete
         */
        //=========================================================================================
        // PRIPRAVNA FAZA
        // utriedime si testovacie data
        PodlaDatumuKomparator pdk = new PodlaDatumuKomparator();
        List<CleanBetEvent> utriedeneEventy = new ArrayList<CleanBetEvent>(events);
        Collections.sort(utriedeneEventy, pdk);
        //System.out.println("prvy event: " + utriedeneEventy.get(0));
//        System.out.println("posledny event: "+utriedeneEventy.get(utriedeneEventy.size()-1));

        // nastavime kalendar na pridavanie datumu
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(sdf.parse(utriedeneEventy.get(0).date));

        } catch (ParseException ex) {
            Logger.getLogger(Spustac.class.getName()).log(Level.SEVERE, null, ex);
        }
        int vyhranych = 0;
        int prehranych = 0;
        double penazenka = 0;
        int aktEvent = 0;
        int mnozinMaloZapasov = 0;

        // zacneme simulaciu so zadanymi parametrami a vratime vysledok simulacie
        while (true) {
            //=========================================================================================
            // VYTVARAME MNOZINU ZAPASOV        
            int aktPocetDni = 0; //z kolkych hracich dni mame zapasy v aktualnom zozname
            // najdeme vsetky "dnesne" eventy
            List<CleanBetEvent> dnesneEventy = new ArrayList<>();
            while (aktEvent < utriedeneEventy.size()) {
                // pytame sa ci aktualny event je este dnesny
                if (jeDnesny(utriedeneEventy.get(aktEvent), c)) {
                    dnesneEventy.add(utriedeneEventy.get(aktEvent));
                } else {
                    // uz nie su ziadne nove dnesne zapasy, tak zmenime dnesny datum
                    try {
                        c.setTime(sdf.parse(utriedeneEventy.get(aktEvent).date));

                    } catch (ParseException ex) {
                        Logger.getLogger(Spustac.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // mame v zozname zapasy z dalsieho hracieho dna
                    aktPocetDni++;
                    if (aktPocetDni == pocetDni) {
                        // vybrali sme zapasy z pocetDni hracich dni, tak breakneme
                        break;
                    } else {
                        // nie je dnesny ale mozeme pridavat zapasy z dalsieho dna
                        dnesneEventy.add(utriedeneEventy.get(aktEvent));
                    }
                }
                aktEvent++;
            }
            System.out.println(dnesneEventy.get(0).date + " dnesne eventy size: " + dnesneEventy.size());
            if (dnesneEventy.size() == 0) {
                break;
            }

//=================================================================================================
            // REDUKUJEME MNOZINU ZAPASOV PODLA PRIJATELNEHO KURZU NA FAVORITA
            // TODO: tu treba doplnit nejaky pokrocily filtrovac aby odstranil tie zapasy, kde nie je jasny favorit
            // alebo aby zohladnoval aktualnu formu nejakeho timu a ine externe statisticke udaje
            List<CleanBetEvent> redukovanaMnozina = new ArrayList<>();
            // skusime povyhadzovat zapasy s vysokymi kurzami u favorita
            for (CleanBetEvent be : dnesneEventy) {
                if (be.kurz >= minKurz && be.kurz < maxKurz) {
                    //if (Math.min(be.kurz_1, be.kurz_2) <maxKurz) {
                    redukovanaMnozina.add(be);
                }
            }
            //System.out.println(dnesneEventy);
            int pom = dnesneEventy.size();

            dnesneEventy = redukovanaMnozina;
            // nech je velkost mnoziny obmedzena na 7 => 21 pocetTiketov na den ak davam 5kombinacie
            if (dnesneEventy.size() >= velkostMnoziny) {
                Collections.sort(dnesneEventy);
                redukovanaMnozina = new ArrayList<>();
                for (int i = 0; i < velkostMnoziny; i++) {
                    redukovanaMnozina.add(dnesneEventy.get(i));
                }
                dnesneEventy = new ArrayList<>(redukovanaMnozina);
//                System.out.println("utriedene eventy prvy a posledny");
//                System.out.println(dnesneEventy.get(0).kurz+"----"+dnesneEventy.get(dnesneEventy.size()-1).kurz);
            }
            if (dnesneEventy.size() < pocetZapasovNaTikete) {
                // chceme presne 21 prvkove kombinacie
                //if (dnesneEventy.size() < 7) {
                if (aktEvent == utriedeneEventy.size()) {
                    double uspesnost = vyhranych / ((double) vyhranych + prehranych);
                    if (vyhranych + prehranych == 0) {
                        uspesnost = Double.MIN_VALUE;
                    }
                    if (uspesnost == Double.NEGATIVE_INFINITY || uspesnost == Double.POSITIVE_INFINITY || uspesnost == Double.NaN) {
                        uspesnost = Double.MIN_VALUE;
                    }
                    // System.out.println("pocetTiketov: " + pocetTiketov + " penazenka: " + df.format(penazenka) + "(MP " + df.format(pocetTiketov * MP) + ")  uspesnost: " + df.format(uspesnost));
                    vysledok.uspesnost = uspesnost;
                    vysledok.penazenka = penazenka;
                    vysledok.pocetTiketov = pocetTiketov;
                    //System.out.println("mnozin malo zapasov: " + mnozinMaloZapasov);
                    //System.out.println("pocet malo kombinacii: "+pocetMaloKombinacii);
                    return vysledok;
                }
                continue;
            }

            //System.out.println("vybrana mnozina size: "+dnesneEventy.size());
//=================================================================================================
            // VYBERAME VSETKY MOZNE KOMBINACIE ZAPASOV NA TIKET Z REDUKOVANEJ MNOZINY ZAPASOV     
            // v tejto mnozine by uz mali byt iba tie zapasy, ktore "zarucene" vyjdu
//            if (dnesneEventy.size() < pocetZapasovNaTikete) {//TODO: teraz nehladame idealnu mnozinu, overujeme 5 zapasove tikety ci su OK
//                // prilis malo zapasov na podanie tiketu
//                //System.out.println("malo kombinacii");
//                mnozinMaloZapasov++;
//                pocetMaloKombinacii++;
//                if (aktEvent == utriedeneEventy.size()) {
//                    double uspesnost = vyhranych / ((double) vyhranych + prehranych);
//                    System.out.println("pocetTiketov: " + pocetTiketov + " penazenka: " + df.format(penazenka) + "(MP " + df.format(pocetTiketov * MP) + ")  uspesnost: " + df.format(uspesnost));
//                    vysledok.uspesnost = uspesnost;
//                    vysledok.penazenka = penazenka;
//                    vysledok.pocetTiketov = pocetTiketov;
//                    System.out.println("mnozin malo zapasov: " + mnozinMaloZapasov);
//                    //System.out.println("pocet malo kombinacii: "+pocetMaloKombinacii);
//                    return vysledok;
//                }
//                continue;
//            } else {
//               // System.out.println("naslo sa eventov: " + pom + " splna kriteria: " + redukovanaMnozina.size());
//            }
            // ziskame pole kombinacii zapasov na tikete, kde je prave 'pocetZapasovNaTikete' zapasov na jednom tikete
            int[][] kombinacie = getKombinacieZapasov(dnesneEventy, pocetZapasovNaTikete);
            pocetTiketov += kombinacie.length;
            //System.out.println("pocet kombinacii: " + kombinacie.length);
            //System.out.println("dnesnyDatum: " + dnesnyDatum + " pocetDni: " + pocetDni + " pocet zapasov: " + pocetZapasovNaTikete + " pocet kombinacii: " + ArithmeticUtils.binomialCoefficient(dnesneEventy.size(), pocetZapasovNaTikete));
            //vypisKombinacie(kombinacie);
            // vyhodnotime aktualne tikety
            //System.out.println("dnesneEventy size: "+dnesneEventy.size()+" kombinacii: "+kombinacie.length );
            Map<String, Double> vysledokTiketov;
            try {
                vysledokTiketov = getVysledokTiketov(dnesneEventy, kombinacie);
                vyhranych += vysledokTiketov.get("vyhranych");
                prehranych += vysledokTiketov.get("prehranych");
                penazenka += vysledokTiketov.get("penazenka");
            } catch (Exception e) {
            }

//            // ulozime si exemplarny tiket na neskorsie skumanie
//            if (!mamePriklad && pocetDni == 1 && pocetZapasovNaTikete == 5) {
//                for (int i = 0; i < 5; i++) {
//                    prikladTiket.add(dnesneEventy.get(kombinacie[0][i]));
//                    mamePriklad = true;
//                }
//            }
            //System.out.println("datum: " + sdf.format(c.getTime()) + " pocet eventov: " + dnesneEventy.size());
            if (aktEvent == utriedeneEventy.size()) {
                break;
            }
            //System.out.println("dnesne eventy(" + dnesneEventy.size() + "): " + dnesneEventy);
            //System.out.println("========================================================");
        }
        double uspesnost = vyhranych / ((double) vyhranych + prehranych);
        if (vyhranych + prehranych == 0) {
            uspesnost = Double.MIN_VALUE;
        }
        if (uspesnost == Double.NEGATIVE_INFINITY || uspesnost == Double.POSITIVE_INFINITY || uspesnost == Double.NaN) {
            uspesnost = Double.MIN_VALUE;
        }
        //System.out.println("pocetTiketov: " + pocetTiketov + " penazenka: " + df.format(penazenka) + "(MP " + df.format(pocetTiketov * MP) + ")  uspesnost: " + df.format(uspesnost));
        vysledok.uspesnost = uspesnost;
        vysledok.penazenka = penazenka;
        vysledok.pocetTiketov = pocetTiketov;
        //System.out.println("pocet malo kombinacii: "+pocetMaloKombinacii);
        //System.out.println("mnozin malo zapasov: " + mnozinMaloZapasov);
        return vysledok;
    }

    private static Map<String, Double> getVysledokTiketov(List<CleanBetEvent> dnesneEventy, int[][] kombinacie) {
        // zistime stav penazenky, a pocty vyhranych a prehranych
        Map<String, Double> vysledok = new HashMap<>();
        //System.out.println("VYHODNOTENIE " + kombinacie.length + " TIKETOV");
        int vyhranychTiketov = 0;
        int prehranychTiketov = 0;
        double penazenka = 0;

        // prechadzam tikety
        for (int i = 0; i < kombinacie.length; i++) {
            ///System.out.println("TIKET: " + i);
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
            } else {
                prehranychTiketov++;
                penazenka -= VKLAD;
            }
            if (jeVyhranyTiket) {
                for (int j = 0; j < kombinacie[0].length; j++) {
                    int cisloZapasu = kombinacie[i][j];
                    //System.out.println(dnesneEventy.get(cisloZapasu));
                }
                //System.out.println("kurz: " + t.kurz + " penazenka: " + penazenka);
                //System.out.println("");
            }
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

    private static int[][] getKombinacieZapasov(List<CleanBetEvent> dnesneEventy, int maxZapasov) {
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

//// vyberieme vsetky mozne ntice
//        int vybranych = 0;
//        int[][] kombinacie = new int[(int) ArithmeticUtils.binomialCoefficient(dnesneEventy.size(), maxZapasov)][2];
//        int pocetKombinacii = 0;
//        for (int i = 0; i < dnesneEventy.size(); i++) {
//            for (int j = i + 1; j < dnesneEventy.size(); j++) {
//                kombinacie[pocetKombinacii][0] = i;
//                kombinacie[pocetKombinacii][1] = j;
//                pocetKombinacii++;
//            }
//        }
//
////        int[][] vysledok = new int[pocetKombinacii][2];
////        for (int i = 0; i < pocetKombinacii; i++) {
////            vysledok[i][0] = kombinacie[i][0];
////            vysledok[i][1] = kombinacie[i][1];
////        }
        return vysledok;
    }

    private static boolean jeDnesny(CleanBetEvent b, Calendar c) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datum = b.date;
        String dnesny = sdf.format(c.getTime());
//        System.out.print("porovnavam " + datum + " a " + dnesny);
//        System.out.println(datum.split("-")[0]+" ?= "+dnesny.split("-")[0]);
//        System.out.println(datum.split("-")[1]+" ?= "+dnesny.split("-")[1]);
//        System.out.println(datum.split("-")[2].trim()+" ?= "+dnesny.split("-")[2].trim());

        if (datum.split("-")[0].equals(dnesny.split("-")[0])
                && datum.split("-")[1].equals(dnesny.split("-")[1])
                && datum.split("-")[2].trim().equals(dnesny.split("-")[2].trim())) {
            //System.out.println("  DNESNY");
            return true;
        }

        //System.out.println("  NIE");
        return false;
    }

    /**
     * tato metoda nam vrati stav penazenky a uspesnost, ked budeme kombinovat na tiket viac zapasov
     *
     * @param events
     * @param pocetZapasovNaTikete
     * @param minKurz
     * @param maxKurz
     * @param velkostMnoziny
     * @return
     */
    private static Vysledok getVysledkyKombinacii(List<CleanBetEvent> events, int pocetZapasovNaTikete, double minKurz, double maxKurz, int velkostMnoziny) {
        System.out.println("getVysledkyKombinacii: events:" + events.size() + "  naTikete:" + pocetZapasovNaTikete + "  min:" + minKurz + "   max:" + maxKurz + "  velkostMnoziny:" + velkostMnoziny + "=========================================================");

        int pocetTiketov = 0;
        Vysledok vysledok = new Vysledok();
        vysledok.pocetDni = -1;
        vysledok.pocetZapasovNaTikete = pocetZapasovNaTikete;
        vysledok.kurz = maxKurz;
        vysledok.minKurz = minKurz;
        vysledok.velkostMnoziny = velkostMnoziny;
        // zresetujeme si premenne        
        int vyhranych = 0;
        int prehranych = 0;
        double penazenka = 0;
        /*
         taktika: 
         1. vytiahneme si zo zoznamu tie eventy ktore vyhovuju nasim kurzom
         2. kombinujeme tieto zapasy v style ze robime ktice z n prvkov, celkovo n nad k pocetTiketov
         */
        //=========================================================================================
        Queue<CleanBetEvent> vyhovujuce = getVyhovujuceEventy(events, minKurz, maxKurz);
        // TODO: z vyhovujucich povyhadzovat najviac rizikove pomocou nejakeho machine learning, branim do uvahy minule vysledky
        // System.out.println("vyhovujuce size: " + vyhovujuce.size());

        // vytahujeme z vyhovujucich po tolko kolko je urcena velkost mnoziny a robime kombinacie
        List<CleanBetEvent> vybrane;
        while (!vyhovujuce.isEmpty()) {
            // vytiahneme si z vyhovujucich tolko kolko nam treba eventov
            vybrane = new ArrayList<>();
            for (int i = 0; i < velkostMnoziny; i++) {
                if (vyhovujuce.peek() != null) {
                    vybrane.add(vyhovujuce.poll());
                } else {
                    break;
                }
            }
            if (vybrane.size() != velkostMnoziny) {
                break;
            }

            // urobime z nich kombinacie a spocitame uspesnost
            int[][] kombinacie = getKombinacieZapasov(vybrane, pocetZapasovNaTikete);
            pocetTiketov += kombinacie.length;
//            System.out.print("pocet kombinacii: " + kombinacie.length);
//            System.out.println(" pocet zapasov: " + pocetZapasovNaTikete);
//            vypisKombinacie(kombinacie);
            //vyhodnotime aktualne tikety
            //System.out.println("dnesneEventy size: " + vybrane.size() + " kombinacii: " + kombinacie.length);
            Map<String, Double> vysledokTiketov;
            try {
                vysledokTiketov = getVysledokTiketov(vybrane, kombinacie);
                vyhranych += vysledokTiketov.get("vyhranych");
                prehranych += vysledokTiketov.get("prehranych");
                penazenka += vysledokTiketov.get("penazenka");
            } catch (Exception e) {
                System.out.println("VYNIMKA GET VYSLEDOK TIKETOV");
            }
        }

        double uspesnost = vyhranych / ((double) vyhranych + prehranych);
        if (vyhranych + prehranych == 0) {
            uspesnost = Double.MIN_VALUE;
        }
        if (uspesnost == Double.NEGATIVE_INFINITY || uspesnost == Double.POSITIVE_INFINITY || uspesnost == Double.NaN) {
            uspesnost = Double.MIN_VALUE;
        }
        //System.out.println("pocetTiketov: " + pocetTiketov + " penazenka: " + df.format(penazenka) + "(MP " + df.format(pocetTiketov * MP) + ")  uspesnost: " + df.format(uspesnost));
        vysledok.uspesnost = uspesnost;
        vysledok.vyhranych = vyhranych;
        vysledok.penazenka = penazenka;
        vysledok.pocetTiketov = pocetTiketov;
        //System.out.println("pocet malo kombinacii: "+pocetMaloKombinacii);
        //System.out.println("mnozin malo zapasov: " + mnozinMaloZapasov);
        return vysledok;
    }

    private static Queue<CleanBetEvent> getVyhovujuceEventy(List<CleanBetEvent> events, double minKurz, double maxKurz) {
        Queue<CleanBetEvent> vyhovujuce = new LinkedList<>();
        for (CleanBetEvent cbe : events) {
            if (cbe.kurz >= minKurz && cbe.kurz < maxKurz) {
                vyhovujuce.add(cbe);
            }
        }
        return vyhovujuce;
    }

    private static void vypisKombinacie(int[][] kombinacie) {
        System.out.println("pocet kombinacii: " + kombinacie.length);
        for (int i = 0; i < kombinacie.length; i++) {
            for (int j = 0; j < kombinacie[0].length; j++) {
                System.out.print(kombinacie[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
