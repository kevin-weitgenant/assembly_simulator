/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assembly_simulator;

import static assembly_simulator.TelaPrincipal.listRegisterModel;
import static assembly_simulator.TelaPrincipal.listMemoryModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class Emulador2 {
    static public List<String> instrucoes = new ArrayList<String>();
    
    public static int linha_atual;
    public static int IP = 0;
    static String[] aux_reg = {"AX: ","DX: ","SP: ","SI: ","IP: ","SR: ","CS: ","DS: "}; 
    

        public static void  updateRegistrador(int posicao_reg, int valor){
        
        String new_content = String.format("0x%02X",valor);
        listRegisterModel.setElementAt(aux_reg[posicao_reg] +new_content, posicao_reg);
        
    } 
    
    public static int getRegistrador(int posicao_reg){   
        String linha_reg = listRegisterModel.getElementAt(posicao_reg);
        linha_reg = linha_reg.split("0x")[1];
        linha_reg = linha_reg.replaceAll("\\s+","");   
        return Integer.parseInt(linha_reg,16);
        
        //RETORNA EM DECIMAL
    }
    
    public static int getMemoria(int posicao_palavra){
        String linha_mem = listMemoryModel.getElementAt(posicao_palavra);
        linha_mem = linha_mem.split("0x")[1];
        linha_mem = linha_mem.replaceAll("\\s+","");   
        return Integer.parseInt(linha_mem,16);
        
        //RETORNA EM DECIMAL
        
    }
    
    public static void updateMemoria(int posicao_palavra, int valor){
        String new_content = String.format("0x%02X",valor);
        new_content = String.format("%04d", posicao_palavra) + ": " + new_content ;
        listMemoryModel.setElementAt(""+new_content, posicao_palavra);        
        
    }
    


}
