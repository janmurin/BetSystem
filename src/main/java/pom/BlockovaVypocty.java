/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pom;

import org.apache.commons.math3.util.ArithmeticUtils;

/**
 *
 * @author Janco1
 */
public class BlockovaVypocty {

    public void execute() {
        // spocitame kolko je sanca na vyhru ked zaregistrujem 1000 blockov
        for (int celkovo = 500000; celkovo < 1000000; celkovo += 50000) {
            System.out.println("celkovo blockov: "+celkovo);
            for (int blockov = 5000; blockov <= 5000; blockov += 1000) {
                double pocetBlockov = blockov;
                double blockovCelkovo = celkovo;
                // sanca na vyhru pri jednom zrebovani
                double sancaVyhra = pocetBlockov / blockovCelkovo;
                double sancaPrehra = 1 - sancaVyhra;
                double sancaPrehraCelkovo = Math.pow(sancaPrehra, 100);
                double sancaVyhraCelkovo = (1 - sancaPrehraCelkovo);
                System.out.println("pocet blockov: " + pocetBlockov + "/" + blockovCelkovo);
                System.out.println("sanca nevyhrajem - vyhrajem celkovo: " + sancaPrehraCelkovo + " - " + sancaVyhraCelkovo);
                double sum = 0;
                for (int i = 1; i <= 15; i++) {
                    //System.out.println(i + "/" + 100);
                    double sanca = ArithmeticUtils.binomialCoefficient(100, i) * Math.pow(sancaVyhra, i) * Math.pow(sancaPrehra, 100 - i);
                    //System.out.println("sanca " + i + "/100: " + sanca);
                    sum += sanca * i * 100;
                }
                System.out.println("celkovy zarobok: " + sum);
                double jackpot=sancaVyhra*celkovo/100;
                System.out.println("hodnota 1000 blockov: " + (sum / (blockov / 1000))+" plus jackpot: +"+(jackpot/(blockov/1000)));
                System.out.println("");
            }
        }
    }

    public static void main(String[] args) {
        BlockovaVypocty bv = new BlockovaVypocty();
        bv.execute();
    }
}
