/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Janco1
 */
public class TipovaciDen implements Comparable<TipovaciDen> {

    public String den;
    public List<CleanBetEvent> eventy = new ArrayList<>();
    public List<ComplexBetEvent> complexEventy=new ArrayList<>();

    public TipovaciDen(String den) {
        this.den = den;
    }

    @Override
    public int compareTo(TipovaciDen o) {
        return den.compareTo(o.den);
    }

}
