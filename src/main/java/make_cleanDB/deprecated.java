/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package make_cleanDB;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Janco1
 */
public class deprecated {
//    
//        private void parse_PocetGolov(RawBetEvent rbe, List<RawBetEvent> chybneEventy, List<CleanBetEvent> cleanBetEvents) {
//        // naparsujeme pocet golov typ eventu
//        String[] zlozky = rbe.competitors.split("\\|");
//        try {
//            // moze sa stat ze sparovalo dva rozne eventy, napriklad ked bol na nieco vypisany kurz ale potom sa zrusil a idcko ostalo
//            if (zlozky.length > 0 && zlozky.length != 3) {
//                // mame chybny event
//                chybneEventy.add(rbe);
//                return;
//            }
//            String line = zlozky[1];
//            StringTokenizer st = new StringTokenizer(line);
//            String golov = st.nextToken();
//            // kontrola ci nie je vrateny
//            String vrateny = st.nextToken();
//            if (vrateny.equals("vrátený")) {
//                chybneEventy.add(rbe);
//                return;
//            }
//            // spravime kontrolu ze su sparovane spravne eventy!!!!!!!!!!!!!!!!!!!!!!!!!!!
//            String prva = rbe.competitors.split("\\|")[0];
//            prva = prva.substring(prva.indexOf(" "));
//            prva = prva.substring(0, prva.lastIndexOf(" "));
//            if (prva.length() == 0) {
//                throw new RuntimeException("PRVA LENGTH JE NULA");
//            }
//            prva = prva.trim(); // teraz mame prvu dvojicu superov
//            // vytiahneme si druhu polovicu a v nej potom hladame retazec 'prva', ak tam nie je tak hladame podobnost
//            String druha = rbe.competitors.substring(rbe.competitors.indexOf("|"));
//            prva = prva.replaceAll(" ", "");
//            druha = druha.replaceAll(" ", "");
//            if (!druha.contains(prva)) {
//                // spocitame mieru podobnosti
//                double rank = getMieruPodobnosti(prva, druha);
//                //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//                if (rank < 0.5) {
//                    // sparovalo dve nesuvisiace eventy
//                    chybneEventy.add(rbe);
//                    //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//                    return;
//                }
//            }
//
//            // preslo kontrolami
//            // zistime vyhercu
//            String vyhral = st.nextToken();
//            boolean vyherny1 = false;
//            boolean vyherny2 = false;
//            if (vyhral.equalsIgnoreCase("+")) {
//                vyherny2 = true;
//            } else {
//                if (vyhral.equals("-")) {
//                    vyherny1 = true;
//                } else {
//                    // nejaka necakana chyba
//                    chybneEventy.add(rbe);
//                    return;
//                }
//            }
//            // nacitanie kurzov, druhy a treti token su nase kurzy
//            st = new StringTokenizer(zlozky[2]);
//            st.nextToken();
//            double kurz1 = Double.parseDouble(st.nextToken());
//            double kurz2 = Double.parseDouble(st.nextToken());
////                    System.out.println("PARSE");
////                    System.out.println(rbe.competitors);
////                    System.out.println("golov: "+golov);
////                    System.out.println("vyhral: "+vyhral);
////                    System.out.println("kurz1: "+kurz1);
////                    System.out.println("kurz2: "+kurz2);
//
//            CleanBetEvent cbe = new CleanBetEvent();
//            cbe.competitors = rbe.competitors;
//            cbe.date = rbe.date;
//            cbe.liga = rbe.liga;
//            cbe.sport = rbe.sport;
//            cbe.typEventu = rbe.typEventu;
//            cbe.kurz = kurz1;
//            cbe.vyherny = vyherny1;
//            cbe.poznamka = golov + "-";
//            cleanBetEvents.add(cbe);
//            //System.out.println(cbe);
//
//            CleanBetEvent cbe2 = new CleanBetEvent();
//            cbe2.competitors = rbe.competitors;
//            cbe2.date = rbe.date;
//            cbe2.liga = rbe.liga;
//            cbe2.sport = rbe.sport;
//            cbe2.typEventu = rbe.typEventu;
//            cbe2.kurz = kurz2;
//            cbe2.vyherny = vyherny2;
//            cbe2.poznamka = golov + "+";
//            cleanBetEvents.add(cbe2);
//            //System.out.println(cbe2);
//        } catch (Exception e) {
//            Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
//            System.out.println(Arrays.toString(zlozky));
//            System.out.println(Arrays.toString(zlozky[1].split(" ")));
//            System.out.println(rbe);
//            return;
//        }
//    }
//
//    private void parse_PresnyVysledok(RawBetEvent rbe, List<RawBetEvent> chybneEventy, List<CleanBetEvent> cleanBetEvents) {
//        // naparsujeme presny vysledok typ eventu
//
//        // JSON PRIKLAD
//        // {"sport":"Futbal","liga":"1.Austrália","typEventu":"Presný výsledok","date":"2015-03-20",
//        //"competitors":"12941 FC Sydney-H.Melbourne                                0:1                  - FC Sydney-H.Melbourne 11.5 5.7 11 42 120 65 60 65 80 09:40",
//        //"poznamka":"Presný výsledok 0:0 1:1 2:2 3:3 4:4 5:0 5:1 4:3 3:4 Čas"},
//        // najprv naparsujeme typy vysledkov
//        String poznamka = rbe.poznamka;
//        poznamka = poznamka.substring("Presný výsledok ".length(), poznamka.length() - " Čas".length());
//        String[] vysledky = poznamka.split(" ");
//        // naparsujeme prisluchajuce kurzy
//        String comp = rbe.competitors;
//        String[] pomkurzy = comp.split(" ");
//        String[] kurzy = new String[vysledky.length];
//        for (int i = 0; i < vysledky.length; i++) {
//            kurzy[i] = pomkurzy[pomkurzy.length - vysledky.length + i - 1];
//        }
//        // zistime spravny vysledok, ak je iba '-' tak vsetky su nespravne
//        // spravny vysledok je vtedy ak 2x po sebe ide rovnaky token ktory ma cisla a dvojbodku
//        String pred = "";
//        String result = "";
//        String line = rbe.competitors;
//        if (line.contains("vrátený")) {
//            // nic z tohto eventu
//            chybneEventy.add(rbe);
//            return;
//        }
//        // spravime kontrolu ze su sparovane spravne eventy!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        String prva = rbe.competitors.split(":")[0];
//        prva = prva.substring(prva.indexOf(" "));
//        prva = prva.substring(0, prva.lastIndexOf(" "));
//        prva = prva.trim(); // teraz mame prvu dvojicu superov
//        // vytiahneme si druhu polovicu a v nej potom hladame retazec 'prva', ak tam nie je tak hladame podobnost
//        String druha = rbe.competitors.substring(rbe.competitors.indexOf(":"));
//        prva = prva.replaceAll(" ", "");
//        druha = druha.replaceAll(" ", "");
//        if (!druha.contains(prva)) {
//            // spocitame mieru podobnosti
//            double rank = getMieruPodobnosti(prva, druha);
//            //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//            if (rank < 0.5) {
//                // sparovalo dve nesuvisiace eventy
//                chybneEventy.add(rbe);
//                //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//                return;
//            }
//        }
//
//        // najdeme prvy token ktory ma v sebe dvojbodku a dalsi bud je rovnaky alebo je minus
//        StringTokenizer st = new StringTokenizer(line);
//        pred = st.nextToken();
//        while (st.hasMoreTokens() && !pred.contains(":")) {
//            pred = st.nextToken();
//        }
//        result = st.nextToken();
//        // mame co potrebujeme, ideme hadzat do zoznamu
//        for (int i = 0; i < vysledky.length; i++) {
//            try {
//                CleanBetEvent cbe = new CleanBetEvent();
//                cbe.competitors = rbe.competitors;
//                cbe.date = rbe.date;
//                cbe.liga = rbe.liga;
//                cbe.sport = rbe.sport;
//                cbe.typEventu = rbe.typEventu;
//                cbe.kurz = Double.parseDouble(kurzy[i]);
//                cbe.vyherny = vysledky[i].equals(result);
//                cbe.poznamka = vysledky[i];
//                cleanBetEvents.add(cbe);
//            } catch (Exception e) {
////                Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
////                System.out.println(rbe.competitors + "===" + rbe.poznamka);
//                // vynechame tento kurz, pravdepodobne je tam --
//                continue;
//            }
//        }
//        //System.out.println(Arrays.toString(vysledky)+Arrays.toString(kurzy)+"["+result+"]===================================="+rbe.competitors);
//    }
//     private void parse_TimDaGol(RawBetEvent rbe, List<RawBetEvent> chybneEventy, List<CleanBetEvent> cleanBetEvents) {
//        // PARSE TYPY VYSLEDKOV Z POZNAMKY
//        String poznamka = rbe.poznamka;
//        try {
//            if (poznamka.startsWith("1.tím dá gól ")) {
//                poznamka = poznamka.substring("1.tím dá gól ".length(), poznamka.length() - " Čas".length());
//            }
//            if (poznamka.startsWith("2.tím dá gól ")) {
//                poznamka = poznamka.substring("2.tím dá gól ".length(), poznamka.length() - " Čas".length());
//            }
//            if (poznamka.startsWith("Oba tímy dajú gól ")) {
//                poznamka = poznamka.substring("Oba tímy dajú gól ".length(), poznamka.length() - " Čas".length());
//            }
//            if (poznamka.startsWith("zápas/oba tímy dajú gól ")) {
//                poznamka = poznamka.substring("zápas/oba tímy dajú gól ".length(), poznamka.length() - " Čas".length());
//            }
//        } catch (Exception e) {
//            Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
//            System.out.println("POZNAMKA: " + rbe);
//        }
//        String[] vysledky = poznamka.split(" ");
//        // PARSE KURZY K MOZNYM VYSLEDKOM
//        String comp = rbe.competitors;
//        String[] pomkurzy = comp.split(" ");
//        String[] kurzy = new String[vysledky.length];
//        for (int i = 0; i < vysledky.length; i++) {
//            kurzy[i] = pomkurzy[pomkurzy.length - vysledky.length + i - 1];
//        }
//
//        // CHECK CI NEBOL VRATENY
//        String line = rbe.competitors;
//        if (line.contains("vrátený")) {
//            // nic z tohto eventu
//            chybneEventy.add(rbe);
//            return;
//        }
//        // CHECK ZE SU SPRAVNE SPAROVANE
//        String prva = rbe.competitors.split(":")[0];
//        prva = prva.substring(prva.indexOf(" "));
//        prva = prva.substring(0, prva.lastIndexOf(" "));
//        prva = prva.trim(); // teraz mame prvu dvojicu superov
//        // vytiahneme si druhu polovicu a v nej potom hladame retazec 'prva', ak tam nie je tak hladame podobnost
//        String druha = rbe.competitors.substring(rbe.competitors.indexOf(":"));
//        prva = prva.replaceAll(" ", "");
//        druha = druha.replaceAll(" ", "");
//        if (!druha.contains(prva)) {
//            // spocitame mieru podobnosti
//            double rank = getMieruPodobnosti(prva, druha);
//            //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//            if (rank < 0.5) {
//                // sparovalo dve nesuvisiace eventy
//                chybneEventy.add(rbe);
//                //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//                return;
//            }
//        }
//
//        // NAJDEME MNOZINU SPRAVNYCH VYSLEDKOV
//        String result = line.split(":")[1]; // vysledky su enclosnute medzi dvoma casmi
//        result = result.substring(result.indexOf(" "), result.indexOf("_")).trim().replaceAll(" ", "");
//        // voci tejto mnozine budeme porovnavat ci nejaky kurz je vyherny
//        Set<String> resultsSet = new HashSet<>();
//        resultsSet.add(result);
//
//        insertEventy(vysledky, rbe, kurzy, resultsSet, cleanBetEvents);
//        //System.out.println(Arrays.toString(vysledky)+Arrays.toString(kurzy)+resultsSet+"===================================="+rbe.competitors);
//    }
////            if (rbe.typEventu.equals("Počet gólov")) {
////                parse_PocetGolov(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("Presný výsledok")) {
////                parse_PresnyVysledok(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("Zápas")) {
////                parse_Zapas(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("1.pol. počet gólov")||rbe.typEventu.equals("2.pol. počet gólov")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_PocetGolov(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("Dá gól (60 min.)")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_DaGolMinuta(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("1.tím dosiahne gólov")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_PocetGolov(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("2.tím dosiahne gólov")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_PocetGolov(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("Zápas bez remízy")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_ZapasBezRemizy(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("1.polčas")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_Polcas(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("zápas/počet gólov")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_ZapasPocetGolov(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("Dvojitá šanca")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_DvojitaSanca(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("1.tretina počet gólov")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_PocetGolov(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("1.tím dá gól")
////                    || rbe.typEventu.equals("2.tím dá gól")
////                    || rbe.typEventu.equals("Oba tímy dajú gól") || rbe.typEventu.equals("Oba tímy dajú gól")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_TimDaGol(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("Víťaz")||rbe.typEventu.equals("1.gól")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                parse_ZapasBezRemizy(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("1.tretina") || rbe.typEventu.equals("2.tretina") || rbe.typEventu.equals("3.tretina") || rbe.typEventu.equals("Rozmedzie gólov")
////                    || rbe.typEventu.equals("1.tím rozmedzie gólov") || rbe.typEventu.equals("2.tím rozmedzie gólov")|| rbe.typEventu.equals("Dá gól (hráč musí nastúpiť v zákl.zostave)")
////                    || rbe.typEventu.equals("Handicap")|| rbe.typEventu.equals("Handicap 0:1")||rbe.typEventu.equals("1.gól - minuta")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////                //parse_DvojitaSanca(rbe, chybneEventy, cleanBetEvents);
////            }
////            if (rbe.typEventu.equals("gól v čase 20:01-26:59")) {
////                //System.out.println(rbe.competitors + "===" + rbe.poznamka);
////               parse_DvojitaSanca(rbe, chybneEventy, cleanBetEvents);
////            }
//    
//    private void parse_Zapas(RawBetEvent rbe, List<RawBetEvent> chybneEventy, List<CleanBetEvent> cleanBetEvents) {
//        //System.out.println(rbe);
//        // naparsujeme presny vysledok typ eventu
//
//        // JSON PRIKLAD
//        // {"sport":"Hokej","liga":"1.Česko-juniori","typEventu":"Zápas","date":"2014-11-27",
//        //"competitors":"5339  Plzeň-Jihlava                                4:2              1, 10, 12              17:00 Plzeň -Jihlava 1.23 6 7.5 1.03 3.35 1.07 17:00",
//        //"poznamka":"Zápas 1 0 2 10 02 12 Čas"}
//        //
//        // PARSE TYPY VYSLEDKOV Z POZNAMKY
//        String poznamka = rbe.poznamka;
//        try {
//            if (poznamka.startsWith("Zápas ")) {
//                poznamka = poznamka.substring("Zápas ".length(), poznamka.length() - " Čas".length());
//            }
//        } catch (Exception e) {
//            Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
//            System.out.println("POZNAMKA: " + rbe);
//        }
//        String[] vysledky = poznamka.split(" ");
//        // PARSE KURZY K MOZNYM VYSLEDKOM
//        String comp = rbe.competitors;
//        String[] pomkurzy = comp.split(" ");
//        String[] kurzy = new String[vysledky.length];
//        for (int i = 0; i < vysledky.length; i++) {
//            kurzy[i] = pomkurzy[pomkurzy.length - vysledky.length + i - 1];
//        }
//
//        // CHECK CI NEBOL VRATENY
//        String line = rbe.competitors;
//        if (line.contains("vrátený")) {
//            // nic z tohto eventu
//            chybneEventy.add(rbe);
//            return;
//        }
//        // CHECK ZE SU SPRAVNE SPAROVANE
//        String prva = rbe.competitors.split(":")[0];
//        prva = prva.substring(prva.indexOf(" "));
//        prva = prva.substring(0, prva.lastIndexOf(" "));
//        prva = prva.trim(); // teraz mame prvu dvojicu superov
//        // vytiahneme si druhu polovicu a v nej potom hladame retazec 'prva', ak tam nie je tak hladame podobnost
//        String druha = rbe.competitors.substring(rbe.competitors.indexOf(":"));
//        prva = prva.replaceAll(" ", "");
//        druha = druha.replaceAll(" ", "");
//        if (!druha.contains(prva)) {
//            // spocitame mieru podobnosti
//            double rank = getMieruPodobnosti(prva, druha);
//            //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//            if (rank < 0.5) {
//                // sparovalo dve nesuvisiace eventy
//                chybneEventy.add(rbe);
//                //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//                return;
//            }
//        }
//
//        // NAJDEME MNOZINU SPRAVNYCH VYSLEDKOV
//        String result = line.split(":")[1]; // vysledky su enclosnute medzi dvoma casmi
//        result = result.substring(result.indexOf(" "), result.lastIndexOf(" ")).trim().replaceAll(" ", "");
//        String[] resulty = result.split(",");
//        // voci tejto mnozine budeme porovnavat ci nejaky kurz je vyherny
//        Set<String> resultsSet = new HashSet<>();
//        for (int i = 0; i < resulty.length; i++) {
//            resultsSet.add(resulty[i]);
//        }
//
//        insertEventy(vysledky, rbe, kurzy, resultsSet, cleanBetEvents);
//    }
//
//    private void parse_DaGolMinuta(RawBetEvent rbe, List<RawBetEvent> chybneEventy, List<CleanBetEvent> cleanBetEvents) {
//        //System.out.println(rbe);
//        // naparsujeme presny vysledok typ eventu
//
//        // JSON PRIKLAD
//        // {"sport":"Hokej","liga":"NHL","typEventu":"Dá gól (60 min.)","date":"2014-11-25",
//        // "competitors":"50066 Col-Win:Johansen Ryan                                 1                  Ano Col-Win:Johansen Ryan 3.1 1.27 8.5 8.5 8.5 24 01:05",
//        //"poznamka":"Dá gól (60 min.) Ano Nie prvý posled. 2+ 3+ Čas"},
//        //
//        // PARSE TYPY VYSLEDKOV Z POZNAMKY
//        String poznamka = rbe.poznamka;
//        try {
//            poznamka = poznamka.substring("Dá gól (60 min.) ".length(), poznamka.length() - " Čas".length());
//        } catch (Exception e) {
//            Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
//            System.out.println("POZNAMKA: " + rbe);
//        }
//        String[] vysledky = poznamka.split(" ");
//        // PARSE KURZY K MOZNYM VYSLEDKOM
//        String comp = rbe.competitors;
//        String[] pomkurzy = comp.split(" ");
//        String[] kurzy = new String[vysledky.length];
//        for (int i = 0; i < vysledky.length; i++) {
//            kurzy[i] = pomkurzy[pomkurzy.length - vysledky.length + i - 1];
//        }
//
//        // CHECK CI NEBOL VRATENY
//        String line = rbe.competitors;
//        if (line.contains("vrátený")) {
//            // nic z tohto eventu
//            chybneEventy.add(rbe);
//            return;
//        }
//        // CHECK ZE SU SPRAVNE SPAROVANE
//        String prva = rbe.competitors.split("   ")[0];
//        prva = prva.substring(prva.indexOf(" "));
//        //prva = prva.substring(0, prva.lastIndexOf(" "));
//        prva = prva.trim(); // teraz mame prvu dvojicu superov
//        // vytiahneme si druhu polovicu a v nej potom hladame retazec 'prva', ak tam nie je tak hladame podobnost
//        String druha = rbe.competitors.substring(rbe.competitors.indexOf("   "));
//        prva = prva.replaceAll(" ", "");
//        druha = druha.replaceAll(" ", "");
//        if (!druha.contains(prva)) {
//            // spocitame mieru podobnosti
//            double rank = getMieruPodobnosti(prva, druha);
//            //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//            if (rank < 0.5) {
//                // sparovalo dve nesuvisiace eventy
//                chybneEventy.add(rbe);
//                //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//                return;
//            }
//        }
//
//        // NAJDEME MNOZINU SPRAVNYCH VYSLEDKOV
//        line = line.substring(line.indexOf("   "));
//        String result = line.substring(0, line.indexOf(":")); // vysledky su medzi prvou velkou medzerou a dvojbodkou
//        // voci tejto mnozine budeme porovnavat ci nejaky kurz je vyherny
//        Set<String> resultsSet = new HashSet<>();
//        for (int i = 0; i < vysledky.length; i++) {
//            if (result.contains(vysledky[i])) {
//                resultsSet.add(vysledky[i]);
//            }
//        }
//
//        insertEventy(vysledky, rbe, kurzy, resultsSet, cleanBetEvents);
//    }
//
//    private void parse_ZapasBezRemizy(RawBetEvent rbe, List<RawBetEvent> chybneEventy, List<CleanBetEvent> cleanBetEvents) {
//               //System.out.println(rbe);
//        // naparsujeme presny vysledok typ eventu
//
//        // JSON PRIKLAD
//        // 3524  BATE Borisov-FC Porto                                 2                   2 BATE Borisov-FC Porto 4.7 1.18 18:00===Zápas bez remízy 1 2 Čas
//        //
//        // PARSE TYPY VYSLEDKOV Z POZNAMKY
//        String poznamka = rbe.poznamka;
//        try {
//            if (poznamka.startsWith("Zápas bez remízy ")) {
//                poznamka = poznamka.substring("Zápas bez remízy ".length(), poznamka.length() - " Čas".length());
//            }
//            if (poznamka.startsWith("Víťaz ")) {
//                poznamka = poznamka.substring("Víťaz ".length(), poznamka.length() - " Čas".length());
//            }
//            if (poznamka.startsWith("1.gól ")) {
//                poznamka = poznamka.substring("1.gól ".length(), poznamka.length() - " Čas".length());
//            }
//        } catch (Exception e) {
//            // zle sparovany event
////            Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
////            System.out.println("POZNAMKA: " + rbe);
//            chybneEventy.add(rbe);
//            return;
//        }
//        String[] vysledky = poznamka.split(" ");
//        // PARSE KURZY K MOZNYM VYSLEDKOM
//        String comp = rbe.competitors;
//        String[] pomkurzy = comp.split(" ");
//        String[] kurzy = new String[vysledky.length];
//        for (int i = 0; i < vysledky.length; i++) {
//            kurzy[i] = pomkurzy[pomkurzy.length - vysledky.length + i - 1];
//        }
//
//        // CHECK CI NEBOL VRATENY
//        String line = rbe.competitors;
//        if (line.contains("vrátený")) {
//            // nic z tohto eventu
//            chybneEventy.add(rbe);
//            return;
//        }
//        // CHECK ZE SU SPRAVNE SPAROVANE
//        String prva = rbe.competitors.split("   ")[0];
//        prva = prva.substring(prva.indexOf(" "));
//        //prva = prva.substring(0, prva.lastIndexOf(" "));
//        prva = prva.trim(); // teraz mame prvu dvojicu superov
//        // vytiahneme si druhu polovicu a v nej potom hladame retazec 'prva', ak tam nie je tak hladame podobnost
//        String druha = rbe.competitors.substring(rbe.competitors.indexOf("   "));
//        prva = prva.replaceAll(" ", "");
//        druha = druha.replaceAll(" ", "");
//        if (!druha.contains(prva)) {
//            // spocitame mieru podobnosti
//            double rank = getMieruPodobnosti(prva, druha);
//            //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//            if (rank < 0.5) {
//                // sparovalo dve nesuvisiace eventy
//                chybneEventy.add(rbe);
//                //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//                return;
//            }
//        }
//
//        // NAJDEME MNOZINU SPRAVNYCH VYSLEDKOV
//        String result = line.substring(line.indexOf("   ")).trim(); // vysledky su enclosnute medzi dvoma casmi
//        StringTokenizer st = new StringTokenizer(result);
//        st.nextToken();
//        result = st.nextToken();
//        //result = result.substring(result.indexOf(" "), result.lastIndexOf(" ")).trim().replaceAll(" ", "");
//        //String[] resulty = result.split(",");
//        // voci tejto mnozine budeme porovnavat ci nejaky kurz je vyherny
//        Set<String> resultsSet = new HashSet<>();
//        resultsSet.add(result);
////        for (int i = 0; i < resulty.length; i++) {
////            resultsSet.add(resulty[i]);
////        }
//        insertEventy(vysledky, rbe, kurzy, resultsSet, cleanBetEvents);
//        //System.out.println(Arrays.toString(vysledky) + Arrays.toString(kurzy) + "[" + result + "]====================================" + rbe.competitors);
//    }
//
//    private void parse_Polcas(RawBetEvent rbe, List<RawBetEvent> chybneEventy, List<CleanBetEvent> cleanBetEvents) {
//        // PARSE TYPY VYSLEDKOV Z POZNAMKY
//        String poznamka = rbe.poznamka;
//        try {
//            if (poznamka.startsWith("1.polčas ")) {
//                poznamka = poznamka.substring("1.polčas ".length(), poznamka.length() - " Čas".length());
//            }
//        } catch (Exception e) {
//            Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
//            System.out.println("POZNAMKA: " + rbe);
//        }
//        String[] vysledky = poznamka.split(" ");
//        // PARSE KURZY K MOZNYM VYSLEDKOM
//        String comp = rbe.competitors;
//        String[] pomkurzy = comp.split(" ");
//        String[] kurzy = new String[vysledky.length];
//        for (int i = 0; i < vysledky.length; i++) {
//            kurzy[i] = pomkurzy[pomkurzy.length - vysledky.length + i - 1];
//        }
//
//        // CHECK CI NEBOL VRATENY
//        String line = rbe.competitors;
//        if (line.contains("vrátený")) {
//            // nic z tohto eventu
//            chybneEventy.add(rbe);
//            return;
//        }
//        // CHECK ZE SU SPRAVNE SPAROVANE
//        String prva = rbe.competitors.split(":")[0];
//        prva = prva.substring(prva.indexOf(" "));
//        prva = prva.substring(0, prva.lastIndexOf(" "));
//        prva = prva.trim(); // teraz mame prvu dvojicu superov
//        // vytiahneme si druhu polovicu a v nej potom hladame retazec 'prva', ak tam nie je tak hladame podobnost
//        String druha = rbe.competitors.substring(rbe.competitors.indexOf(":"));
//        prva = prva.replaceAll(" ", "");
//        druha = druha.replaceAll(" ", "");
//        if (!druha.contains(prva)) {
//            // spocitame mieru podobnosti
//            double rank = getMieruPodobnosti(prva, druha);
//            //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//            if (rank < 0.5) {
//                // sparovalo dve nesuvisiace eventy
//                chybneEventy.add(rbe);
//                //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//                return;
//            }
//        }
//
//        // NAJDEME MNOZINU SPRAVNYCH VYSLEDKOV
//        String result = line.split(":")[1]; // vysledky su enclosnute medzi dvoma casmi
//        result = result.substring(result.indexOf(" "), result.indexOf("_")).replaceAll(",", "").trim();
//        String[] results = result.split(" ");
//        Set<String> resultsSet = new HashSet<>();
//        for (int i = 0; i < results.length; i++) {
//            resultsSet.add(results[i]);
//        }
//
//        insertEventy(vysledky, rbe, kurzy, resultsSet, cleanBetEvents);
//    }
//    
//    private void parse_ZapasPocetGolov(RawBetEvent rbe, List<RawBetEvent> chybneEventy, List<CleanBetEvent> cleanBetEvents) {
//      // naparsujeme presny vysledok typ eventu
//
//        // JSON PRIKLAD
//        // {"sport":"Futbal","liga":"1.Austrália","typEventu":"Presný výsledok","date":"2015-03-20",
//        //"competitors":"12941 FC Sydney-H.Melbourne                                0:1                  - FC Sydney-H.Melbourne 11.5 5.7 11 42 120 65 60 65 80 09:40",
//        //"poznamka":"Presný výsledok 0:0 1:1 2:2 3:3 4:4 5:0 5:1 4:3 3:4 Čas"},
//        // najprv naparsujeme typy vysledkov
//        String poznamka = rbe.poznamka;
//        try {
//            poznamka = poznamka.substring("zápas/počet gólov ".length(), poznamka.length() - " Čas".length());
//        } catch (Exception e) {
////              Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
////            System.out.println("POZNAMKA: " + rbe);
//        }
//        String[] vysledky = poznamka.split(" ");
//        // naparsujeme prisluchajuce kurzy
//        String comp = rbe.competitors;
//        String[] pomkurzy = comp.split(" ");
//        String[] kurzy = new String[vysledky.length];
//        for (int i = 0; i < vysledky.length; i++) {
//            kurzy[i] = pomkurzy[pomkurzy.length - vysledky.length + i - 1];
//        }
//        // zistime spravny vysledok, ak je iba '-' tak vsetky su nespravne
//        // spravny vysledok je vtedy ak 2x po sebe ide rovnaky token ktory ma cisla a dvojbodku
//        String pred = "";
//        String line = rbe.competitors;
//        if (line.contains("vrátený")) {
//            // nic z tohto eventu
//            chybneEventy.add(rbe);
//            return;
//        }
//        // spravime kontrolu ze su sparovane spravne eventy!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        String prva = rbe.competitors.split(":")[0];
//        prva = prva.substring(prva.indexOf(" "));
//        prva = prva.substring(0, prva.lastIndexOf(" "));
//        prva = prva.trim(); // teraz mame prvu dvojicu superov
//        // vytiahneme si druhu polovicu a v nej potom hladame retazec 'prva', ak tam nie je tak hladame podobnost
//        String druha = rbe.competitors.substring(rbe.competitors.indexOf(":"));
//        prva = prva.replaceAll(" ", "");
//        druha = druha.replaceAll(" ", "");
//        if (!druha.contains(prva)) {
//            // spocitame mieru podobnosti
//            double rank = getMieruPodobnosti(prva, druha);
//            //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//            if (rank < 0.5) {
//                // sparovalo dve nesuvisiace eventy
//                chybneEventy.add(rbe);
//                //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
//                return;
//            }
//        }
//
//        String result = rbe.competitors.split(":")[1];
//        result = result.substring(result.indexOf(" "), result.indexOf("_")).trim();
//        // mame co potrebujeme, ideme hadzat do zoznamu
//        for (int i = 0; i < vysledky.length; i++) {
//            try {
//                CleanBetEvent cbe = new CleanBetEvent();
//                cbe.competitors = rbe.competitors;
//                cbe.date = rbe.date;
//                cbe.liga = rbe.liga;
//                cbe.sport = rbe.sport;
//                cbe.typEventu = rbe.typEventu;
//                cbe.kurz = Double.parseDouble(kurzy[i]);
//                cbe.vyherny = vysledky[i].equals(result);
//                cbe.poznamka = vysledky[i];
//                cleanBetEvents.add(cbe);
//            } catch (Exception e) {
//                Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
//                System.out.println(rbe.competitors + "===" + rbe.poznamka);
//                // vynechame tento kurz, pravdepodobne je tam --
//                continue;
//            }
//        }
//        //System.out.println(Arrays.toString(vysledky) + Arrays.toString(kurzy) + result + "====================================" + rbe.competitors);
//    }
}
