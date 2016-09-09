/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated;

import data.CleanBetEvent;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Janco1
 */
public class TypEventu implements Comparable<TypEventu>{

    String nazov;
    ArrayList<CleanBetEvent> eventy=new ArrayList<>();

    TypEventu(String typEventu) {
        nazov=typEventu;
    }

    @Override
    public int compareTo(TypEventu o) {
        return o.eventy.size()-eventy.size();
    }
}
