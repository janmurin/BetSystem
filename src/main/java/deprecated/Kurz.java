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
public class Kurz {

    double min;
    double max;
    int pocetVyskytov;
    int pocetUspechov;
    double zarobok;
    double sucetKurzov=0;

    public double getOcakavanaUspesnost() {
        double priemer = sucetKurzov / pocetVyskytov;
        return 1 / priemer;
    }
    
    public double getRealnaUspesnost() {
        return pocetUspechov / (double) pocetVyskytov;
    }
}
