/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author janmu
 */
public class Databaza implements Iterable<TipovaciDen> {

    private static final String DATABAZA_CLEAN_FOLDER = "C:\\Users\\jan.murin\\Google Drive\\databaza_clean";
    private Iterator<TipovaciDen> denIterator;
    private final Iterator<File> mesiaceIterator;

    // simulator alebo thread manager od databazy dostanu len iterator na tipovaci den a iteruju tieto dni
    public Databaza() {
        mesiaceIterator = Arrays.asList(new File(DATABAZA_CLEAN_FOLDER).listFiles()).iterator();
        denIterator = getTipovacieDniFromFile(mesiaceIterator.next()).iterator();
    }

    public Iterator<TipovaciDen> iterator() {
        Iterator<TipovaciDen> it = new Iterator<TipovaciDen>() {

            private int currentIndex = 0;


            public boolean hasNext() {
                return denIterator.hasNext() || mesiaceIterator.hasNext();
            }


            public TipovaciDen next() {
                if (denIterator.hasNext()) {
                    return denIterator.next();
                } else if (mesiaceIterator.hasNext()) {
                    denIterator = getTipovacieDniFromFile(mesiaceIterator.next()).iterator();
                    if (denIterator.hasNext()) {
                        return denIterator.next();
                    } else {
                        throw new RuntimeException("chyba databazy: iterator " + denIterator.toString() + " po nacitani mesiac file nenasiel tipovacie dni.");
                    }
                }
                // tu by sme sa nikdy nemali dostat
                throw new RuntimeException("chyba databazy: iteratory maju nexty ale nemaju hodnoty");
                //return new TipovaciDen("0");
            }


            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    private List<TipovaciDen> getTipovacieDniFromFile(File mesiacFile) {
        //System.out.println("nacitavam subor: " + mesiacFile.getName());
        long start2 = System.currentTimeMillis();
        // nacitame z jsona vsetky eventy
        List<TipovaciDen> tipovacieDni = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<CleanBetEvent> eventyMesiaca = new ArrayList<>();
        try {
            eventyMesiaca = mapper.readValue(mesiacFile, new TypeReference<List<CleanBetEvent>>() {
            });
        } catch (IOException ex) {
            Logger.getLogger(Databaza.class.getName()).log(Level.SEVERE, null, ex);
        }
        // roztriedime eventy do jednotlivych dni, zgrupime eventy tej istej udalosti
        // eventy musia byt usporiadane podla idcka v liste
        int idx = 0;
        while (idx < eventyMesiaca.size()) {
            CleanBetEvent cbe = eventyMesiaca.get(idx);
            ComplexBetEvent novy = new ComplexBetEvent();
            novy.events.add(cbe);
            // zistime idcko
            novy.id = getIDFromCompetitors(cbe.competitors);
            // najdeme mu kamaratov
            idx++;
            while (idx < eventyMesiaca.size()) {
                if (novy.id == getIDFromCompetitors(eventyMesiaca.get(idx).competitors)) {
                    novy.events.add(eventyMesiaca.get(idx));
                    idx++;
                } else {
                    // teraz je v idx novy event, ktoremu treba najst kamaratov
                    insertIntoTipovaciDen(novy, tipovacieDni);
                    break;
                }
            }
            if (idx == eventyMesiaca.size()) {
                // este musime vlozit posledny event lebo v cykle sa ukladaju iba ked najde iny novy a teraz nenaslo novy ale skoncil zoznam
                insertIntoTipovaciDen(novy, tipovacieDni);
            }
        }
        Collections.sort(tipovacieDni);
        // System.out.println("subor " + mesiacFile.getName() + " nacitany za " + (System.currentTimeMillis() - start2) + " ms");
        return tipovacieDni;
    }

    private void insertIntoTipovaciDen(ComplexBetEvent cbe, List<TipovaciDen> tipovacieDni) {
        // najdeme aktualnemu eventu tipovaci den kam patri a priradime ho k nemu
        for (TipovaciDen td : tipovacieDni) {
            if (cbe.events.get(0).getDate().equals(td.den)) {
                td.eventy.addAll(cbe.events);
                td.complexEventy.add(cbe);
                return;
            }
        }
        // vytvorime novy tipovaci den
        TipovaciDen td = new TipovaciDen(cbe.events.get(0).date);
        td.eventy.addAll(cbe.events);
        td.complexEventy.add(cbe);
        tipovacieDni.add(td);
    }

    private int getIDFromCompetitors(String competitors) {
        return Integer.parseInt(competitors.substring(0, competitors.indexOf(" ")));
    }
}
