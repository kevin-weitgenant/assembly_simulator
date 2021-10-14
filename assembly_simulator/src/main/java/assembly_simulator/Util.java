/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author renafs
 */
package Emulador;
public class Util {
    public static String convertIntegerToBinary(short n) {
        if (n == 0) {
            return "00000000 00000000";
        }
        StringBuilder binaryNumber = new StringBuilder();
        while (n > 0) {
            int remainder = n % 2;
            binaryNumber.append(remainder);
            n /= 2;
        }
        while(binaryNumber.length()<15){
            binaryNumber.append("0");
        }
        binaryNumber.append(n<0?1:0);
        binaryNumber = binaryNumber.reverse();
        String word =  binaryNumber.toString();
        return word.substring(0, 8) + " " + word.substring(8,16);
    }
    static boolean getBit( int k,int n) {
        return ((k >> n) & 1)==1;
    }
    public static int setBit(int target,int bit,  boolean value) {
        int mask = 1 << bit;
        return target | mask;
    }
}
