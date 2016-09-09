/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package make_cleanDB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Janco1
 */
public class Text2jsonParser {

    private static File[] listOfFiles;
    private static Set<String> typyEventov = new TreeSet<>();

    private static void nacitajTypyEventov() {
        File file = new File("typyEventov.txt");
        typyEventov = new TreeSet<>();

        BufferedReader f = null;
        try {
            f = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            while (true) {
                String line = f.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("?")) {
                    System.out.println("line????????? " + line);
                    line = line.substring(1);
                }
                typyEventov.add(line);
            }
            System.out.println("nacitane typy eventov: " + typyEventov);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            StringTokenizer st = null;
            try {
                String line = f.readLine();
                if (line == null) {
                    break;
                }

            } catch (Exception ex) {
                Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }

    /**
     * vykonava sa iba raz pri parsovani do JSONa
     *
     * @param file
     * @return
     */
    private static String parseSubor(File file, List<EventKurzy> eventKurzy) {
        //System.out.println("parsing file: " + file.getName() + " ");
        StringBuilder sb = new StringBuilder();

        try {
            // nacitame vysledkovy textak
            BufferedReader f = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            String aktDatum = "";
            String line = "";
            String sport = "";
            String liga = "";
            String typEventu = "";
            String pom = "";
            String poznamka = "";
            String datum = file.getName().replaceAll("vysledky", "").replaceAll("\\.txt", "");
            RawBetEvent be = new RawBetEvent();
            int sparovanych = 0;
            int cislo = 0;

            while (true) {
                // System.out.println("true");
                try {
                    line = f.readLine();
                    //System.out.println("line: "+line);
                    if (line == null) {
                        //  System.out.println("line is null");
                        break;
                    }
                    // 1. SPORT
                    if (line.startsWith("=")) {
                        // mame sport
                        //System.out.println(line);
                        sport = line.replaceAll("=", "").trim();
                        continue;
                    }
                    // 2. LIGA
                    if (line.startsWith("-")) {
                        // mame ligu
                        //System.out.println(line);
                        line = line.trim();
                        // vymazeme pomlcky
                        for (int i = 0; i < line.length(); i++) {
                            if (line.charAt(i) != line.charAt(i + 1)) {
                                line = line.substring(i + 1);
                                break;
                            }
                        }
                        for (int i = line.length() - 1; i > 0; i--) {
                            if (line.charAt(i) != line.charAt(i - 1)) {
                                line = line.substring(0, i);
                                break;
                            }
                        }
                        liga = line.trim();
                        continue;
                    }
                    line = line.trim();
                    // ak nie je cislo na zaciatku riadka tak to je typ beteventu
                    if (line.length() > 0) {
                        // nie je to prazdny riadok
                        try {
                            cislo = Integer.parseInt(line.split(" ")[0]);
                            // nevyhodilo vynimku takze je to udalost
                            System.err.println("CHYBA: cislo:[" + cislo + "] sport:[" + sport + "]  liga:[" + liga + "] " + line);

                        } catch (Exception e) {
                            // nie je to cislo, takze to bude typ beteventu
                            // 3. TYP BETEVENTU
                            if (!line.startsWith("(")) {// nezaujimaju na zatvorkove komentarove riadky
                                //System.out.println(line);
                                //typyEventov.add(line);
                                // niektore typy specialne osetrit
                                if (line.startsWith("Zápas")) {
                                    if (line.startsWith("Zápas bez remízy")) {
                                        line = "Zápas bez remízy";
                                    } else {
                                        line = "Zápas";
                                    }
                                }
                                if (line.startsWith("zápas")) {
                                    if (line.startsWith("zápas/oba tímy dajú gól")) {
                                        line = "zápas/oba tímy dajú gól";
                                    } else if (line.startsWith("zápas/počet gamov")) {
                                        line = "zápas/počet gamov";
                                    } else if (line.startsWith("zápas/počet gólov")) {
                                        line = "zápas/počet gólov";
                                    } else {
                                        line = "zápas";
                                    }
                                }
                                if (line.startsWith("1X2")) {
                                    line = "1X2";
                                }
                                if (line.startsWith("Kto lepší")) {
                                    line = "Kto lepší";
                                }
                                if (line.startsWith("Víťaz")) {
                                    if (line.startsWith("Víťaz vrátane predľženia")) {
                                        line = "Víťaz vrátane predľženia";
                                    } else {
                                        line = "Víťaz";
                                    }
                                }
                                if (line.startsWith("Ano - Nie")) {
                                    line = "Ano - Nie";
                                }
                                if (line.startsWith("Celkové umiestnenie")) {
                                    line = "Celkové umiestnenie";
                                }
                                if (line.startsWith("Postup")) {
                                    line = "Postup";
                                }
                                if (line.startsWith("Menej/viac")) {
                                    line = "Menej/viac";
                                }
                                if (liga.startsWith("Špeciál")
                                        || liga.startsWith("Turné 4 mostíkov celkom-náskok víťaza")
                                        || liga.startsWith("Speciál")
                                        || liga.startsWith("Fotbal 1.Česko 2014/2015")) {
                                    // citame riadky dokym nemame prve cislo
                                    while (true) {
                                        try {
                                            // sme v speciali, iba sa dostaneme k prvemu eventu a pokracujeme dalej
                                            line = f.readLine();
                                            cislo = Integer.parseInt(line.split(" ")[0]);
                                            // nacitame prvy zapas v speciali a ostatne pojdu cez while cyklus dalej
//                                            be.sport = sport;
//                                            be.liga = liga;
//                                            be.typEventu = "Špeciál";
//                                            be.competitors = line;
//                                            be.date = datum;

                                            try {
                                                //
                                                //pocetEventov++;
                                                cislo = Integer.parseInt(line.split(" ")[0]);
//                                                be.id = cislo;
//                                                be.competitors = be.competitors + getKurzy(cislo, eventKurzy);
                                            } catch (NumberFormatException numberFormatException) {
                                                System.out.println("toto by sa nemalo stat:  NumberFormatExceptionNumberFormatExceptionNumberFormatExceptionNumberFormatException");
                                            }
                                            //System.out.println(line);
                                            //System.out.println(be);
                                            //sb.append(be + "\n");
// VYNECHAVAM SPECIALY                                            
                                            //sb.append(datum + " _ " + be.competitors + "\n");
                                            break;
                                        } catch (Exception e2) {
                                        }
                                    }
                                    // takyto event v zozname neexistuje
                                    // narokom ho takto davam aby sme si ich nedavali do databazy
                                    line = "Špeciál2";
                                }
                                line = line.trim();
                                //typyEventov.add(line);
                                // mame betevent, nacitavat zapasy, kym nie je prazdny riadok

                                if (typyEventov.contains(line)) {
                                    typEventu = line;
                                    // sme na znamom type eventu, nacitavame zapasy do neho
                                    line = f.readLine().trim();
                                    // citame kym nenajdeme prazdny riadok
                                    while (line.length() > 0) {
                                        be.sport = sport;
                                        be.liga = liga;
                                        be.typEventu = typEventu;
                                        // pripad ked sa viacej vysledkov nevojde na riadok
                                        if (line.charAt(line.length() - 1) == ',') {
                                            line = line + f.readLine().trim();
                                        }
                                        be.competitors = line;
                                        be.date = datum;

                                        try {
                                            cislo = Integer.parseInt(line.split(" ")[0]);
                                            //be.id = cislo;
                                            // najdeme kurzy k danemu eventu
                                            pom = getKurzy(cislo, eventKurzy);
                                            if (!pom.equals("NENASLO KURZY")) {
                                                poznamka = pom.split("_")[1];
                                                pom = pom.split("_")[0];
                                                // sparovalo kurzy s eventom
                                                sparovanych++;
                                                be.competitors = be.competitors + " _" + pom;
                                                // znormalizujeme competitors aby medzi prvym cislom a druhym tokenom bola iba jedna medzera
                                                String cislo2 = be.competitors.substring(0, be.competitors.indexOf(" "));
                                                String zvysok = be.competitors.substring(be.competitors.indexOf(" ")).trim();
                                                be.competitors = cislo2 + " " + zvysok;
                                                be.poznamka = poznamka;
                                                // sb.append(datum + " _ " + be.competitors + "\n");
                                                sb.append(be + ",\n");
                                            } else {
                                                // nepouzijeme tento event
                                                // 1. event sa nakoniec mozno neodohral
                                                // 2. event bol stiahnuty z ponuky
                                            }
                                        } catch (NumberFormatException numberFormatException) {
                                            System.out.println("toto by sa nemalo stat:  NumberFormatExceptionNumberFormatExceptionNumberFormatExceptionNumberFormatException");
                                        }
                                        //System.out.println(line);
                                        //System.out.println(be);
                                        //sb.append(be + "\n");
                                        line = f.readLine().trim();
                                        if (line.startsWith("(")) {
                                            line = f.readLine().trim();
                                        }
                                    }
                                } else // nacitali sme event ktory nas nezaujima
                                //System.out.println("");
                                //if (sport.equals("Hádzaná")) {
                                    System.out.println("NEZAUJIMA NAS TENTO EVENT: [" + line + "]");
                                //} //System.out.println("");
                            }
                        }
                    }

                } catch (Exception ex) {
                    System.out.println("vynimka subor: " + file.getName());
                    System.out.println("line: " + line);
                    Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
            }
            System.out.println("sparovanych : " + sparovanych);
//            System.out.println("NESPAROVANE EVENT KURZY:");
//            for (EventKurzy ek:eventKurzy){
//                if (!ek.jeSparovany){
//                    System.out.println(ek.id+""+ek.line);
//                }
//            }

        } catch (Exception ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("TYPY EVENTOV");
//        for (String s : typyEventov) {
//            System.out.println(s);
//        }
        return sb.toString();
    }

    private static void spocitajPocetEventov(File file) {
        //File file = new File("typyEventov.txt");
        BufferedReader f = null;
        int cislo = 0;
        int pocet = 0;
        try {
            f = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            //System.out.println("VSETKY EVENTY ZOZNAM");
            while (true) {
                String line = f.readLine();
                if (line == null) {
                    break;
                }
                try {
                    cislo = Integer.parseInt(line.split(" ")[0]);
                    pocet++;
                    //System.out.println(pocet + ": " + line);
                    // nevyhodilo vynimku takze je to udalost
                } catch (Exception e) {

                }
            }
            System.out.println("pocet vysledky eventov: " + pocet);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static List<EventKurzy> getEventKurzy(File file) {
        //File file = new File("typyEventov.txt");
        List<EventKurzy> eventKurzy = new ArrayList<>();
        // do poznamky budeme hadzat vzdy posledny riadok, na ktorom neni prvy token cislo
        // teda riadok ktory definuje kurzy
        String poznamka = "";

        BufferedReader f = null;
        int cislo = 0;
        try {
            f = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            while (true) {
                String line = f.readLine();
                if (line == null) {
                    break;
                }
                try {
                    cislo = Integer.parseInt(line.split(" ")[0]);
                    // nevyhodilo vynimku takze je to udalost
                    EventKurzy ek = new EventKurzy();
                    ek.id = cislo;
                    ek.line = line.substring(line.indexOf(" "));
                    ek.poznamka = poznamka;
                    eventKurzy.add(ek);

                    if (!kontrolaOK(line)) {
                        // nekonci sa casom, nastala chyba vo vstupe
                        // nemalo by nastat, lebo sme uz opravovali surove event kurzy
                        System.out.println(eventKurzy.size() + ": " + line);
                    } else {
                        //System.out.println(eventKurzy.size() + ": " + line);
                    }

                } catch (Exception e) {
                    poznamka = line;
                    if (poznamka.startsWith("Dá gól (60 min.)")) {
                        poznamka = poznamka + " " + f.readLine();// dodatocne nacitanie, lebo je to rozdelene na 2 riadky
                    }
                    if (poznamka.startsWith("Dá gól (hráˇc musí")) {
                        poznamka = poznamka + " " + f.readLine() + " " + f.readLine() + " " + f.readLine();// dodatocne nacitanie, lebo je to rozdelene na 2 riadky
                    }
                }
            }
            System.out.println("nacitane event kurzy size: " + eventKurzy.size());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return eventKurzy;
    }

    /**
     * vykonava sa iba raz pri parsovani dat do JSONa
     */
    public static void nacitajData() {
        // sparujeme vysledky v adresari vysledky_utf8 s kurzami v adresari kurzy_clean
        String outputDir = "databaza_raw";
        // nacitame zname typy eventov, kazdy takyto typ eventu ma vlastnu strukturu zobrazenia vysledkov a kurzov
        nacitajTypyEventov();

        // nacitame nazvy vsetkych suborov, vytvorime skupiny podla mesiacov a kazdu skupinu ulozime
        List<RawDBSkupina> skupiny = getSkupiny();
        for (RawDBSkupina rdbs : skupiny) {
            System.out.println("");
            System.out.println("PARSING SKUPINA");
            System.out.println("Skupina: " + rdbs.nazovSkupiny);
            System.out.println("zoznam: " + rdbs.files);

            StringBuilder sb = new StringBuilder("[");
            try {
//                int offset = 0;
//                int range = 1;
                //for (int i = offset; i < offset + range; i++) {
                for (File f : rdbs.files) {
                    //File f = listOfFiles[i];
                    if (f.getName().split("\\.")[1].equalsIgnoreCase("TXT")) {
                        System.out.println("");
                        System.out.println("nacitavam subor: " + f.getName() + " ");
                        // spocitame kolko je eventov v subore s vysledkami pre neskorsiu kontrolu
                        spocitajPocetEventov(f);
                        String datum = f.getName().replaceAll("vysledky", "").replaceAll("\\.txt", "");
                        // zo suboru s kurzami nacitame vsetky riadky zacinajuce cislom, teda vsetky eventy
                        List<EventKurzy> eventKurzy = getEventKurzy(new File("INPUT/kurzy_clean/today_bet_offer" + datum + ".txt"));
                        // sparujeme vysledok eventu a kurzy vypisane na event podla cisla eventu, ktore je rovnake vo vysledkoch aj kurzoch
                        sb.append(parseSubor(f, eventKurzy) + "\n");
                    }
                }
                // zbavime sa ciarky na konci
                sb = new StringBuilder(sb.toString().substring(0, sb.length() - 3));
                sb.append("]");

                // aplikujeme korektury vdaka zlemu encodingu z pdfka
                String databaza = sb.toString();
                databaza = databaza.replaceAll("Cˇ ", "Č");
                databaza = databaza.replaceAll("ˇC", "Č");
                databaza = databaza.replaceAll("ˇ C", "Č");
                databaza = databaza.replaceAll("cˇ", "č");
                databaza = databaza.replaceAll("ˇc", "č");
                databaza = databaza.replaceAll(" ˇ c", "č");
                databaza = databaza.replaceAll("ˇ c", "č");
                databaza = databaza.replaceAll("Rˇ ", "Ř");
                databaza = databaza.replaceAll("rˇ", "ř");
                databaza = databaza.replaceAll("ˇr", "ř");
                databaza = databaza.replaceAll("nˇ", "ň");
                databaza = databaza.replaceAll(" ˇ n", "ň");
                databaza = databaza.replaceAll("eˇ ", "ě");
                databaza = databaza.replaceAll("eˇ", "ě");
                databaza = databaza.replaceAll(" ˇ e", "ě");
                databaza = databaza.replaceAll("ˇ e", "ě");
                databaza = databaza.replaceAll("t'", "ť");
                databaza = databaza.replaceAll("t’", "ť");
                databaza = databaza.replaceAll("l'", "ľ");
                databaza = databaza.replaceAll("l’", "ľ");
                databaza = databaza.replaceAll("Dˇ ", "Ď");
                databaza = databaza.replaceAll("ˇT", "Ť");
                databaza = databaza.replaceAll("č istým", "čistým");
                databaza = databaza.replaceAll("predl’ženia", "predľženia");
                databaza = databaza.replaceAll("aspoňjeden", "aspoň jeden");

                //System.out.println(databaza);
                Writer out = null;
                try {
                    //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("fortunaLiga/fortunaLiga.json"), "UTF-8"));
                    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDir + "/" + rdbs.nazovSkupiny + ".json"), "UTF-8"));
                    out.write(databaza);
                    out.flush();
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
//            parseSubor(listOfFiles[0]);
//            pw=new PrintWriter(new File("processedCaptchas\\data.txt"));
            } catch (Exception ex) {
                Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
//        String[] data={"fortunaLiga","premierLeague", "bundesliga","primeraDivision", "francuzskaLiga"};
        nacitajData();
//        System.out.println("TYPY EVENTOV");
//        for (String s : typyEventov) {
//            System.out.println(s);
//        }
    }

    private static String getKurzy(int cislo, List<EventKurzy> eventKurzy) {
        for (EventKurzy ek : eventKurzy) {
            if (ek.id == cislo) {
                ek.jeSparovany = true;
                return ek.line + "_" + ek.poznamka;
            }
        }
        return "NENASLO KURZY";
    }

    // kontrola ci sa riadok konci cislom, lebo kazdy event ma na konci cas
    private static boolean kontrolaOK(String line) {
        String[] zlozky = line.split(" ");
        try {
            int cislo = Integer.parseInt(zlozky[zlozky.length - 1].replaceAll(":", ""));
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
        return true;
    }

    private static List<RawDBSkupina> getSkupiny() {
        File folder = new File("INPUT/vysledky_utf8");
        File[] files = folder.listFiles();
        Set<String> existuje = new HashSet<>();
        Map<String, List<File>> skupiny = new HashMap<>();
        for (int i = 0; i < files.length; i++) {
            File aktFile = files[i];
            String skupina = aktFile.getName().replaceAll("vysledky", "").replaceAll("\\.txt", "").substring(0, 7);
            if (!existuje.add(skupina)) {
                // nepridali sme novu skupinu
                skupiny.get(skupina).add(aktFile);
            } else {
                // pridali sme novu skupinu
                skupiny.put(skupina, new ArrayList<File>());
                skupiny.get(skupina).add(aktFile);
            }
        }

        List<RawDBSkupina> zoznam = new ArrayList<>();
        for (String skupina : skupiny.keySet()) {
            zoznam.add(new RawDBSkupina(skupina, skupiny.get(skupina)));
        }
        return zoznam;
    }

}
