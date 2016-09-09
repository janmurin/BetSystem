/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated;

import java.util.Comparator;

/**
 *
 * @author Janco1
 */
public class VysledokPodlaPenazenkyKomparator implements Comparator<VysledokTypEventu>{

    @Override
    public int compare(VysledokTypEventu o1, VysledokTypEventu o2) {
        return Double.compare(o2.vysledok.penazenka, o1.vysledok.penazenka);
    }
    
}
