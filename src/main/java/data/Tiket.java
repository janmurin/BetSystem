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
public class Tiket {

    public List<CleanBetEvent> zapasy=new ArrayList<>();
    public double kurz;
    public boolean jeVyherny;

    @Override
    public String toString() {
        return "Tiket{" + "kurz=" + kurz + ",zapasy=" + zapasy + ",  jeVyherny=" + jeVyherny + '}';
    }
    
    
}
