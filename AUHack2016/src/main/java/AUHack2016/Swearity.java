
/**
 * Philip Alring Rützou Aakast
 */

// import lib

import java.util.Vector;
import java.util.ArrayList;
import java.lang.Math;

public class Swearity {
  private Vector vektor;
  
  public static void main(String args[]) {
        Vector vektor = new Vector();
  }
  
  public Vector createVector(ArrayList<Double> list ) {
      for (Double i : list) {
          if ( vektor.size() >= vektor.capacity() ) {
              vektor.ensureCapacity( vektor.size() + 1 );
          }
          vektor.addElement(i);
      }
      return vektor;
  }
  
  public double findMean(Vector vektor) {
      double temp = 0;
      for (int k = 0;k <= vektor.size();k++) {
          temp += (double) vektor.get(k);
      }
      return temp / vektor.size();
  }
  
  public Vector adjustVectorValues(Vector vektor) {
      for (int n = 0; n <= vektor.size();n++) {
          vektor.set( n, Math.sqrt( ( (double) vektor.get(n) - findMean(vektor) )* (double) vektor.get(n) - findMean(vektor) ) );
      }
      return vektor;
  }
  
  public double magicNumber(Vector vektor) {
      double temp = 0;
      for (int m = 0; m <= vektor.size();m++) {
          temp += (float) vektor.get(m);
        }
      return temp / vektor.size();
    }
}
