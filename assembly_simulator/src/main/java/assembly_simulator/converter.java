package assembly_simulator;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kevin
 */
public class converter {
 
 public static String decToHex(String binario){
    String hex =  Integer.toHexString(Integer.parseInt(binario));
    return hex;
}
  
 public static String decToBin(String dec){  //ok  
     
     String bin = Integer.toBinaryString(Integer.parseInt(dec));
     return bin;

        
}
   
 public static String binToDec(String bin){    //ok
     int dec =  Integer.parseInt(bin, 2 );
     return ""+dec;

}

 public static String binToHex(String bin){   //ok
     int decimal = Integer.parseInt(binToDec(bin));

     return " "+ Integer.toHexString(decimal);
} 
 
 
 
 public static String hexToBin(String hex){
     String dec = hexToDec(hex);
     return decToBin(dec);

}
  
 public static String hexToDec(String hex){
     int dec =  Integer.parseInt(hex, 16 );
     return ""+dec;

} 
   



 
}