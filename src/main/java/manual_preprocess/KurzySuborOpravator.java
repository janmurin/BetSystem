/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manual_preprocess;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import make_cleanDB.EventKurzy;
import make_cleanDB.Text2jsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Janco1
 */
public class KurzySuborOpravator {

    private static File[] listOfFiles;
    static Logger logger = Logger.getLogger("KurzySuborOpravator");
    static FileHandler fh;

    static void opravuj(boolean opravujeme, String inputDir) throws IOException{
        File folder = new File(inputDir);
        List<String> riadky;
        listOfFiles = folder.listFiles();

        //for (int i = offset; i < offset+range; i++) {
        for (int i = 0; i < listOfFiles.length; i++) {
            riadky = new ArrayList<>();

            File file = listOfFiles[i];
            if (!file.getName().contains(".txt")) {
                // System.out.println("PRESKOCENIE SUBORU: "+file.getName());
                continue;
            }
            String datum = file.getName().replaceAll("today_bet_offer", "").replaceAll("\\.txt", "");
            String nazov = inputDir + "/today_bet_offer" + datum + ".txt";

            logger.info("opravujem kurzy subor: " + nazov);
            List<EventKurzy> eventKurzy = new ArrayList<>();

            BufferedReader f = null;
            StringBuilder sb = new StringBuilder();
            int cislo = 0;
            try {
                f = new BufferedReader(new InputStreamReader(new FileInputStream(new File(nazov)), "UTF8"));
                String line;
                while (true) {
                    line = f.readLine();
                    if (line == null) {
                        break;
                    }
                    try {
                        if (line.startsWith("live") || line.startsWith("Hrajte")) {
                            line = f.readLine();
                            // vyhodime posledny riadok a pridame mu dalsi
                            String pom = riadky.get(riadky.size() - 1);
                            riadky.remove(riadky.size() - 1);
                            riadky.add(pom + " " + line);
                            logger.warning("OPRAVA: " + pom + " --> " + pom + " " + line);
                        } else {
                            // ak zacina cislom a nekonci casom

                            if (zacinaCislom(line) && !kontrolaOK(line)) {
                                //logger.warning("ZACINA CISLOM A NEKONCI CISLOM:\n " + line);
                                StringBuilder message = new StringBuilder();
                                if (!opravujeme) {
                                    // ked neopravujeme tak mergujeme 3 riadky do jedneho
                                    message.append("ZACINA CISLOM A NEKONCI CISLOM:\n " + line);
                                    String pom = f.readLine();
                                    String pom2 = f.readLine();
                                //logger.warning(pom + "\n" + pom2);
                                    //logger.warning("-->" + line + " " + pom + " " + pom2 + "<--                                    " + datum);
                                    //logger.warning("");
                                    message.append(pom + "\n" + pom2+"\n");
                                    message.append("-->" + line + " " + pom + " " + pom2 + "<--                                    " + datum+"\n\n");
                                    logger.warning(message.toString());
                                    // opravime to tak ze spojime riadky a na prvom udaji o case to zlomime
                                    riadky.add(line + " " + pom + " " + pom2);
                                } else {
                                    // opravujeme, takze v jednom riadku je prilis vela veci
                                    int lineBreak = getLineBreak(line);
                                    if (lineBreak != -1) {
                                        riadky.add(line.substring(0, lineBreak));
                                        riadky.add(line.substring(lineBreak));
                                        message.append("OPRAVA RIADKU: " + line+"<--                                    " + datum+"\n");
                                        message.append("NOVY RIADOK 1: " + line.substring(0, lineBreak)+"\n");
                                        message.append("NOVY RIADOK 2: " + line.substring(lineBreak)+"\n");
                                        logger.warning(message.toString());
                                    } else {
                                        // to znamena ze treba pripocitat nasledujuci riadok
                                        String pom = f.readLine();
                                        logger.warning("OPRAVUJEM RIADOK DO TVARU: " + line+" "+pom+"<--                                    " + datum+"\n\n");
                                        riadky.add(line+" "+pom);
                                    }
                                }
                            } else {
                                // idealny pripad, ked zacina aj konci cislom, nemusi byt 100% korektne
                                riadky.add(line);
                            }
                        }
                        //sb.append(line+"\n");
                        // nevyhodilo vynimku takze je to udalost
                    } catch (Exception e) {

                    }
                }
                for (String s : riadky) {
                    sb.append(s + "\n");
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
            }

            Writer out = null;
            try {
                //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("fortunaLiga/fortunaLiga.json"), "UTF-8"));
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nazov), "UTF-8"));
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
                    out.close();
                }
            }
            //parseSubor(listOfFiles[0]);
            //pw=new PrintWriter(new File("processedCaptchas\\data.txt"));
        }
    }
    
    // opravi textaky s kurzami tak, aby kazdy event bol iba na jednom riadku
    public static void main(String[] args) throws IOException {
        // najprv treba nejako z pdfka prekopirovat text do txt suborov (zatial to robim manualne)
        // potom nacitame vsetky txt subory a na kazdom robime kontrolu ci na kazdom riadku je jeden cely event (aby nebol na viacerych riadkoch)
        String inputDir = "INPUT/kurzy_raw";
        fh = new FileHandler("C:\\Users\\janmu\\NetBeans Projects\\BetSystem2\\log.txt", true);
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        // the following statement is used to log any messages  
        logger.info("starting KurzySuborOpravator");
        // opravuj najprv mergovanim
        opravuj(false, inputDir);
        // teraz opravuj linebreakovanim
        opravuj(true, inputDir);
//        String riadok="fdsfsdfsd 00:04 fefwfef5we1few62f3s";
//        System.out.println(riadok);
//        int br=getLineBreak(riadok);
//        System.out.println(br);
//        System.out.println(riadok.substring(0,br));
//        System.out.println(riadok.substring(br));
        
    }

    private static boolean kontrolaOK(String line) {
        String[] zlozky = line.split(" ");
        try {
            int cislo = Integer.parseInt(zlozky[zlozky.length - 1].replaceAll(":", ""));
//            if (cislo>10){
//                return true;
//            }else{
//                return false;
//            }
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
        return true;
    }

    private static boolean zacinaCislom(String line) {
        try {
            int cislo = Integer.parseInt(line.split(" ")[0]);
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
        return true;
    }

    private static int getLineBreak(String line) {
        // najde prvy vyskyt v tvare DD:DD, co znamena cas udalosti a vrati jeho koncovu poziciu
        int result = -1;
        for (int i = 0; i < line.length() - 5; i++) {
            if (line.substring(i, i + 5).matches("^(?:[01][0-9]|2[0-3]):[0-5][0-9]$")) {
                result = i + 5;
                return result;
            }
        }
        return result;
    }
}
