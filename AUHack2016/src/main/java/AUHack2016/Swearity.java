package AUHack2016;

/**
 * Philip Alring Rützou Aakast
 */

// import lib

import java.util.ArrayList;
import java.util.Vector;

public class Swearity {
    private Vector vektor;

    public Swearity() {
        vektor = new Vector();
    }

    public Vector createVector(ArrayList<Double> list) {
        for (Double i : list) {
            if (vektor.size() >= vektor.capacity()) {
                vektor.ensureCapacity(vektor.size() + 1);
            }
            vektor.addElement(i);
        }
        return vektor;
    }

    public double findMean(Vector vektor) {
        double temp = 0;
        for (int k = 0; k <= vektor.size() - 1; k++) {
            temp += (double) vektor.get(k);
        }
        return temp / vektor.size();
    }

    public Vector adjustVectorValues(Vector vektor) {
        for (int n = 0; n <= vektor.size() - 1; n++) {
            if (((double) vektor.get(n) - findMean(vektor)) < 0) {
                vektor.set(n, -1 * ((double) vektor.get(n) - findMean(vektor)));
            } else {
                vektor.set(n, ((double) vektor.get(n) - findMean(vektor)));
            }
        }
        return vektor;
    }

    public double magicNumber(Vector vektor) {
        adjustVectorValues(vektor);
        double temp = 0;
        for (int m = 0; m <= vektor.size() - 1; m++) {
            temp += (double) vektor.get(m);
        }
        return temp / vektor.size();
    }
}
