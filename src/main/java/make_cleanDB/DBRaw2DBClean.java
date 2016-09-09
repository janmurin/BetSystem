/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package make_cleanDB;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StreamTokenizer;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Janco1
 */
public class DBRaw2DBClean {

    private ArrayList<RawBetEvent> events;
    private ArrayList<TypEventu> typyEventovPocty;
    DecimalFormat df = new DecimalFormat("##.##");
    private String[] mesiace;
    private HashSet<String> mesiaceSet;

    private void vypisChybneEventy(List<RawBetEvent> chybneEventy) {
        System.out.println("VYPIS CHYBNE EVENTY: " + chybneEventy.size());
        for (RawBetEvent rbe : chybneEventy) {
            if (rbe.competitors.contains("vrátený") || rbe.competitors.contains("pár")) {
                // nebudeme vypisovat taketo chyby
                continue;
            }
            System.out.println(rbe.competitors);
        }
    }

    private double getMieruPodobnosti(String prva, String druha) {
        // hladame maximum 
        int max = 0;
        int pocet = 0;
        druha = druha.replaceAll("'", "");
        for (int i = 0; i < druha.length() - prva.length(); i++) {
            for (int j = 0; j < prva.length(); j++) {
                if (druha.charAt(i + j) == prva.charAt(j)) {
                    pocet++;
                }
            }
            if (pocet > max) {
                max = pocet;
            }
            pocet = 0;
        }
        return ((double) (max) / prva.length());
    }

    private void parse_BetEvent(RawBetEvent rbe, List<RawBetEvent> chybneEventy, List<CleanBetEvent> cleanBetEvents) {
        try {
            // PARSE TYPY VYSLEDKOV Z POZNAMKY
            String poznamka = rbe.poznamka;
            try {
                poznamka = poznamka.substring(rbe.typEventu.length() + 1, poznamka.length() - " Čas".length());
            } catch (Exception e) {
                // chybne sparovane eventy
//                Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
//                System.out.println("POZNAMKA: " + rbe);
                return;
            }
            String[] vysledky = poznamka.split(" ");
            // PARSE KURZY K MOZNYM VYSLEDKOM
            String comp = rbe.competitors;
            String[] pomkurzy = comp.split(" ");
            String[] kurzy = new String[vysledky.length];
            for (int i = 0; i < vysledky.length; i++) {
                kurzy[i] = pomkurzy[pomkurzy.length - vysledky.length + i - 1];
            }

            // CHECK CI NEBOL VRATENY
            String line = rbe.competitors;
            if (line.contains("vrátený")) {
                // nic z tohto eventu
                chybneEventy.add(rbe);
                return;
            }
            // CHECK ZE SU SPRAVNE SPAROVANE
            String prva = rbe.competitors.split("   ")[0];// nezaujima nas text za prvou dvojicou s vysledkami a kurzami
            prva = prva.substring(prva.indexOf(" "));// vynechame cislo ktore je na zaciatku kazdeho riadku
            //prva = prva.substring(0, prva.lastIndexOf(" "));//
            prva = prva.trim(); // teraz mame prvu dvojicu superov
            if (prva.length() < 5) {
//                prva = rbe.competitors.split("   ")[0];
//                System.out.println("PRVA: [" + prva + "]");
//                prva = prva.substring(prva.indexOf(" "));
//                System.out.println("PRVA: [" + prva + "]");
//                //   prva = prva.substring(0, prva.lastIndexOf(" "));
//                //System.out.println("PRVA: ["+prva+"]");
//                prva = prva.trim();
//                System.out.println("PRVA: [" + prva + "]");
                throw new RuntimeException("PRVA LENGTH < 5");
            }
            // vytiahneme si druhu polovicu a v nej potom hladame retazec 'prva', ak tam nie je tak hladame podobnost
            String druha = rbe.competitors.substring(rbe.competitors.indexOf("   "));
            prva = prva.replaceAll(" ", "");
            druha = druha.replaceAll(" ", "");
            if (!druha.contains(prva)) {
                // spocitame mieru podobnosti
                double rank = getMieruPodobnosti(prva, druha);
                //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
                if (rank < 0.5) {
                    // sparovalo dve nesuvisiace eventy
                    chybneEventy.add(rbe);
                    //System.out.println("[" + df.format(rank) + "]CHYBA NEOBSAHUJE: [" + prva + "]====druha[" + druha + "]===" + rbe.competitors);
                    return;
                }
            }

            // NAJDEME MNOZINU SPRAVNYCH VYSLEDKOV
            String result = rbe.competitors.substring(rbe.competitors.indexOf("   ")).trim();
            result = result.substring(0, result.indexOf("_")).replaceAll(",", "").trim();
            result = result.substring(result.indexOf("   ")).trim();
            String[] results = result.split(" ");
            Set<String> resultsSet = new HashSet<>();
            for (int i = 0; i < results.length; i++) {
                resultsSet.add(results[i]);
            }

//            if (rbe.typEventu.equals("Zápas")) {
//                System.out.println(Arrays.toString(vysledky) + Arrays.toString(kurzy) + resultsSet + "====" + rbe.competitors + "==[" + rbe.poznamka + "]");
//            }
            // NAHADZEME DO DATABAZY KAZDY KURZ OSOBITNE
            for (int i = 0; i < vysledky.length; i++) {
                try {
                    CleanBetEvent cbe = new CleanBetEvent();
                    cbe.competitors = rbe.competitors;
                    cbe.date = rbe.date;
                    cbe.liga = rbe.liga;
                    cbe.sport = rbe.sport;
                    cbe.typEventu = rbe.typEventu;
                    cbe.kurz = Double.parseDouble(kurzy[i]);
                    cbe.vyherny = resultsSet.contains(vysledky[i]);
                    cbe.poznamka = vysledky[i];
                    cleanBetEvents.add(cbe);
                } catch (Exception e) {
//                Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
//                System.out.println(rbe.competitors + "===" + rbe.poznamka);
                    // vynechame tento kurz, pravdepodobne je tam --
                    continue;
                }
            }
            // ak mame v resultsete nieco co nie je v ocakavanych vysledkoch tak vypiseme
//            for (String res : resultsSet) {
//                boolean neobsahuje = true;
//                for (String vys : vysledky) {
//                    if (vys.equals(res)) {
//                        neobsahuje = false;
//                        break;
//                    }
//                }
//                if (neobsahuje) {
//                    System.out.println(rbe.typEventu + ":   " + Arrays.toString(vysledky) + Arrays.toString(kurzy) + resultsSet + "==" + rbe.competitors + "==" + rbe.poznamka);
//                }
//            }
            //System.out.println(rbe.typEventu + ":   " + Arrays.toString(vysledky) + Arrays.toString(kurzy) + resultsSet + "==" + rbe.competitors + "==" + rbe.poznamka);
        } catch (Exception e) {
//            Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
//            System.out.println("POZNAMKA: " + rbe);
//            System.out.println("");
            chybneEventy.add(rbe);
        }
    }

//    private String[] getMesiace() {
//        File[] mesiaceFiles = new File("databaza_raw").listFiles();
//        String[] mesiace = new String[mesiaceFiles.length];
//        for (int i = 0; i < mesiaceFiles.length; i++) {
//            mesiace[i] = mesiaceFiles[i].getName().substring(0, 7);
//        }
//        System.out.println(Arrays.toString(mesiace));
//        return mesiace;
//    }

    private class TypEventu implements Comparable<TypEventu> {

        String typ;
        int pocet;

        private TypEventu(String s, Integer get) {
            typ = s;
            pocet = get;
        }

        @Override
        public int compareTo(TypEventu o) {
            return o.pocet - pocet;
        }

    }

    public void loadEvents() {
        //System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        long start = System.currentTimeMillis();
        ObjectMapper mapper = new ObjectMapper();
        events = new ArrayList<RawBetEvent>();
        File[] mesiaceFiles = new File("databaza_raw").listFiles();
        for (File mesiacFile : mesiaceFiles) {
            if (!mesiaceSet.contains(mesiacFile.getName().split("\\.")[0])) {
                // budeme analyzovat iba vybrane subory
                System.out.println("nebudem analyzovat "+mesiacFile.getName());
                continue;
            }
            List<RawBetEvent> aktEvents = null;
            try {
                aktEvents = mapper.readValue(mesiacFile, new TypeReference<List<RawBetEvent>>() {
                });
            } catch (IOException ex) {
                Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, ex);
            }
            events.addAll(aktEvents);
            System.out.println("events from [" + mesiacFile.getName() + "] loaded time (ms): " + (System.currentTimeMillis() - start));
        }

        System.out.println("events.size=" + events.size());
    }

    public void getTypyEventovPocty() {
        Map<String, Integer> typyPocty = new TreeMap<>();
        for (RawBetEvent be : events) {
            if (typyPocty.containsKey(be.getTypEventu())) {
                typyPocty.put(be.getTypEventu(), typyPocty.get(be.getTypEventu()) + 1);
            } else {
                typyPocty.put(be.getTypEventu(), 1);
            }
        }
        System.out.println("POCTY TYPOV EVENTOV");
        typyEventovPocty = new ArrayList<>();
        for (String s : typyPocty.keySet()) {
            typyEventovPocty.add(new TypEventu(s, typyPocty.get(s)));
        }
        Collections.sort(typyEventovPocty);
        int count = 0;
        for (TypEventu v : typyEventovPocty) {
            System.out.println(v.typ + ": " + v.pocet);
            count++;
            if (count > 10) {
                break;
            }
        }
    }

    public void parse2CleanBetEvents() {
        System.out.println("parsing clean bet events from raw database");
        List<CleanBetEvent> cleanBetEvents = new ArrayList<>();
        List<RawBetEvent> chybneEventy = new ArrayList<>();

        // prechadzame podla typov eventov od najcastejsich po najmenej caste
        for (RawBetEvent rbe : events) {
            parse_BetEvent(rbe, chybneEventy, cleanBetEvents);
        }

        // vypiseme statistiky
        System.out.println("cleanBetEvents size: " + cleanBetEvents.size());
        vypisChybneEventy(chybneEventy);

        //====================
        // kazdy mesiac eventy osobitne zapiseme
        //String[] mesiace = getMesiace();
        for (String mesiac : mesiace) {
            List<CleanBetEvent> eventyMesiaca = new ArrayList<>();
            for (CleanBetEvent cbe : cleanBetEvents) {
                if (cbe.date.startsWith(mesiac)) {
                    eventyMesiaca.add(cbe);
                }
            }
            zapisCleanDatabazu(eventyMesiaca, "databaza_clean/" + mesiac + ".json");
        }

    }

    private void zapisCleanDatabazu(List<CleanBetEvent> cleanBetEvents, String nazovSuboru) {
        System.out.println("zapisujem do suboru: " + nazovSuboru);
// zapiseme do suboru
        Writer out = null;
        try {
            //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("fortunaLiga/fortunaLiga.json"), "UTF-8"));
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nazovSuboru), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            int count = 0;
            for (int i = 0; i < cleanBetEvents.size() - 1; i++) {
                count++;
                sb.append(cleanBetEvents.get(i) + ",\n");
                if (count > 10000) {
                    out.write(sb.toString());
                    out.flush();
                    sb = new StringBuilder();
                }
            }
            try {
                sb.append(cleanBetEvents.get(cleanBetEvents.size() - 1) + "\n]");
            } catch (Exception e) {
                Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, e);
            }
            out.write(sb.toString());
            out.flush();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(DBRaw2DBClean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void execute() {
        // nastavime mesiace ktore chceme analyzovat v adresari databaza_raw
        mesiace = new String[]{"2016-07"};
        mesiaceSet = new HashSet<>();
        for (int i = 0; i < mesiace.length; i++) {
            mesiaceSet.add(mesiace[i]);
        }
        loadEvents();
        System.out.println("");
        getTypyEventovPocty();
        System.out.println("");
        parse2CleanBetEvents();
    }

    // nacitame raw databazu a ziskame z nej cistu databazu s eventami, kurzami a ci kurz vysiel alebo nie
    public static void main(String[] args) throws IOException {
        DBRaw2DBClean db2 = new DBRaw2DBClean();
        db2.execute();

    }
}
