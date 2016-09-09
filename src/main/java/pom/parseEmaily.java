/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author jmurin
 */
public class parseEmaily {

    public void execute() {
        Scanner scanner = null;
        PrintWriter pw = null;
        try {
            scanner = new Scanner(new File("emailyDB.json"));

            int c = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.startsWith("\"message\"")) {
                    c++;
                    System.out.println(c);
                    pw = new PrintWriter(new File("emaily" + c + ".json"));
                    line = scanner.nextLine();
                    while (!line.contains("}")) {
                        pw.println(line);
                        line = scanner.nextLine();
                    }
                    pw.close();
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(parseEmaily.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }

//        ObjectMapper mapper = new ObjectMapper();
//        ArrayList<Email> emaily = new ArrayList<>();
//        File json = new File("emaily.json");
//        try {
//            emaily = mapper.readValue(json, new TypeReference<List<Email>>() {
//            });
//        } catch (IOException ex) {
//            Logger.getLogger(Spustac2.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("emaily size: " + emaily.size());
//        int count = 0;
//        for (Email e : emaily) {
//            count++;
//            PrintWriter pw = null;
//            try {
//                pw = new PrintWriter(new File("emaily" + count + ".json"));
//                pw.println(e.message);
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(parseEmaily.class.getName()).log(Level.SEVERE, null, ex);
//            } finally {
//                pw.close();
//            }
//        }
    }

    public static void main(String[] args) {
        parseEmaily pe = new parseEmaily();
        pe.execute();
    }
}
