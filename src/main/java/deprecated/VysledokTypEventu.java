/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated;

/**
 *
 * @author Janco1
 */
public class VysledokTypEventu implements Comparable<VysledokTypEventu>{
    
    String typEventu="";
    Vysledok vysledok;

    @Override
    public int compareTo(VysledokTypEventu o) {
        return Double.compare(o.vysledok.ziskovost, vysledok.ziskovost);
    }
}
