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
public class VysledokPodlaUspesnostiKomparator implements Comparator<Vysledok> {

    @Override
    public int compare(Vysledok o1, Vysledok o2) {

        return Double.compare(o2.uspesnost, o1.uspesnost);
    }

}
