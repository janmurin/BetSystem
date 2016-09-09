/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manual_preprocess;

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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Janco1
 */
public class VysledkyRaw2utf8 {

    private static File[] listOfFiles;

    public static void main(String[] args) throws IOException {
        // toto sa spusta pre prvotne spracovanie textakov s vysledkami
        // konkretne sa len zmeni encoding na utf8
        String inputDir = "INPUT/vysledky_raw";
        String outputDir = "INPUT/vysledky_utf8";

        //File folder = new File("fortunaLiga");
        File folder = new File(inputDir);
        listOfFiles = folder.listFiles();
        String line;
        for (int i = 0; i < listOfFiles.length; i++) {
            StringBuilder sb = new StringBuilder();
            File file = listOfFiles[i];
            if (file.getName().split("\\.")[1].equalsIgnoreCase("TXT")) {
                System.out.println("nacitavam subor: " + file.getName() + " ");
                BufferedReader f = new BufferedReader(new InputStreamReader(new FileInputStream(file), "8859_2"));
                while (true) {
                    line = f.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line+"\n");
                }
                Writer out = null;
                try {
                    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDir + "/" + file.getName()), "UTF-8"));
                    out.write(sb.toString());
                    out.flush();
                    System.out.println(file.getName()+" ---> "+outputDir+"/"+file.getName());
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
            }
        }

    }
}
