/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package deprecated;

import data.CleanBetEvent;
import java.util.Comparator;

/**
 *
 * @author Janco1
 */
public class PodlaDatumuKomparator implements Comparator<CleanBetEvent>{

    public int compare(CleanBetEvent o1, CleanBetEvent o2) {
        return o1.date.compareTo(o2.date);
    }
    
}
