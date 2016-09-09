/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

public class sieteUloha {

    int[][] vektory = {
        {1, 1, 1, -1, -1, 1, -1},
        {1, -1, -1, -1, 1, 1, -1},
        {1, -1, 1, 1, 1, -1, -1},
        {1, -1, 1, -1, -1, -1, 1},
        {1, 1, -1, 1, -1, -1, -1},
        {-1, 1, 1, -1, 1, -1, -1},
        {1, 1, -1, -1, 1, -1, 1}};
    int vybranyVektor = 0;

    public void execute(int vekt) {
        vybranyVektor=vekt;
        int[] vektor = vektory[vybranyVektor]; // vyberieme si prvy vektor

        int min = 50;
        // iterujem vsetky katice
        for (int k = 1; k <= 7; k++) {
            System.out.println("========== k=" + k + "===================");
            int[][] kombinacieZapasov = getKombinacieZapasov(k);
            for (int i = 0; i < kombinacieZapasov.length; i++) {
                int cislo = dekoduj(spocitaj(kombinacieZapasov[i]), vektor);
                //System.out.println(Arrays.toString(kombinacieZapasov[i]) + " = " + cislo);
                if (obsahujeNasVektor(kombinacieZapasov[i])) {
                    // nas vektor sa tam nachadza
                    min = Math.min(min, cislo);
                }
            }
        }
        System.out.println("minimalna hodnota ked sa nas vektor nachadzal je: " + min);
    }

    private boolean obsahujeNasVektor(int[] kombinacia) {
        for (int i = 0; i < kombinacia.length; i++) {
            if (kombinacia[i] == vybranyVektor) {
                return true;
            }
        }
        return false;
    }

    private int[] spocitaj(int[] komb) {
        int[] sum = new int[7];
        for (int i = 0; i < komb.length; i++) {
            for (int j = 0; j < 7; j++) {
                sum[j] += vektory[komb[i]][j];
            }
        }
        return sum;
    }

    private int dekoduj(int[] komb, int[] vektor) {
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += vektor[i] * komb[i];
        }
        return sum;
    }

    private int[][] getKombinacieZapasov(int maxZapasov) {
        List<Integer> cisla = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            cisla.add(i);
        }
        // Create the initial vector
        ICombinatoricsVector<Integer> initialVector = Factory.createVector(cisla);

        // Create a simple combination generator to generate maxZapasov-combinations of the initial vector
        Generator<Integer> gen = Factory.createSimpleCombinationGenerator(initialVector, maxZapasov);

        int[][] vysledok = new int[(int) ArithmeticUtils.binomialCoefficient(7, maxZapasov)][maxZapasov];
        // Print all possible combinations
        int i = 0;
        for (ICombinatoricsVector<Integer> combination : gen) {
            for (int j = 0; j < maxZapasov; j++) {
                vysledok[i][j] = combination.getValue(j);
            }
            i++;
        }

        return vysledok;
    }

    public static void main(String[] args) {
        sieteUloha u = new sieteUloha();
        u.execute(0);
        u.execute(1);
        u.execute(2);
        u.execute(3);
        u.execute(4);
        u.execute(5);
        u.execute(6);
    }

}
