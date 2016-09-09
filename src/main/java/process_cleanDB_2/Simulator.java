/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_cleanDB_2;

import data.CleanBetEvent;
import data.Databaza;
import data.TipovaciDen;
import java.util.Iterator;

/**
 *
 * @author janmu
 */
public class Simulator {

    public static void main(String[] args) {
        Simulator s = new Simulator();
        s.execute();
    }

    private void execute() {
        Databaza db = new Databaza();
        Simulacia_81_Kombinacii simulacie = new Simulacia_81_Kombinacii();

        Iterator<TipovaciDen> iterator = db.iterator();
        TipovaciDen den;
        int eventov = 0;
        double suma = 0;
        while (iterator.hasNext()) {
            den = iterator.next();
            System.out.println(den.den + ": pocet eventov: " + den.eventy.size());
            suma += simulacie.simulacia_81moznosti(den.eventy);
            System.out.println("SUMA: " + suma);

            eventov += den.eventy.size();
            //break;
            System.out.println("");
        }

        System.out.println("Vsetkych eventov: " + eventov);
    }

}
