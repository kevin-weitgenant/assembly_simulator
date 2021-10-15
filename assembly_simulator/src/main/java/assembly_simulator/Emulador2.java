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
    

    public static void  updateRegistrador(int valor,int posicao_reg){
        
        String new_content = String.format("0x%02X",valor);
        listRegisterModel.setElementAt(aux_reg[posicao_reg] +new_content, posicao_reg);
        
    } 
    
    public static void  updateRegistrador(int valor,String nome_reg){
        int posicao_reg = 0;
        for (int i = 0; i<aux_reg.length; i++){
            String aux = aux_reg[i].split(":")[0];
            
            if (aux.equals(nome_reg)){
                posicao_reg = i;
                break;
            }
        } 
        
        
        
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
    
    public static int getRegistrador(String nome_reg){   
        
        int posicao_reg = 0;
        for (int i = 0; i<aux_reg.length; i++){
            String aux = aux_reg[i].split(":")[0];
            
            if (aux.equals(nome_reg)){
                posicao_reg = i;
                break;
            }
        } 
        
        
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
    
    public static void updateMemoria( int valor, int posicao_palavra){
        String new_content = String.format("0x%02X",valor);
        new_content = String.format("%04d", posicao_palavra) + ": " + new_content ;
        listMemoryModel.setElementAt(""+new_content, posicao_palavra);        
        
    }
    

    
    
    

    public static void load_instrucoes(){
        String opdRegex=".*";
        for (int i = 0; i< instrucoes.size(); i++){
            String instrucao = instrucoes.get(i);
            if(instrucao.matches("add AX AX")){
                updateMemoria(0x03C0,i );
                int resultado = getRegistrador("AX") *2;
                
            }
            
            /*
            else if(instrucao.matches("add AX DX")){
                updateMemoria(0x03C2, i);
                updateRegistrador()
            }else if(instrucao.matches("add AX "+opdRegex)){
                updateMemoria(0x05, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("div SI")){
                updateMemoria(0xf7f6, i);
            }else if(instrucao.matches("div AX")){
                updateMemoria(0xf7c0, i);
            }else if(instrucao.matches("sub AX AX")){
                updateMemoria(0x2bc0, i);
            }else if(instrucao.matches("sub AX DX")){
                updateMemoria(0x2bc2, i);
            }else if(instrucao.matches("sub AX "+opdRegex)){
                updateMemoria(0x25, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("mul SI")){
                updateMemoria(0xf7f6, i);
            }else if(instrucao.matches("mul AX")){
                updateMemoria(0xf7f0, i);
            }else if(instrucao.matches("cmp AX DX")){
                updateMemoria(0x3BC2, i);
            }else if(instrucao.matches("cmp AX "+opdRegex)){
                updateMemoria(0x3d, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("and AX DX")){
                updateMemoria(0xf7C2, i);
            }else if(instrucao.matches("and AX "+opdRegex)){
                updateMemoria(0x25, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("not AX")){
                updateMemoria(0xF8C0, i);
            }else if(instrucao.matches("or AX AX")){
                updateMemoria(0x0BC0, i);
            }else if(instrucao.matches("or AX DX")){
                updateMemoria(0x0BC0, i);
            }else if(instrucao.matches("or AX "+opdRegex)){
                updateMemoria(0x0D, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("xor AX AX")){
                updateMemoria(0x33C0, i);
            }else if(instrucao.matches("xor AX DX")){
                updateMemoria(0x33C2, i);
            }else if(instrucao.matches("xor AX "+opdRegex)){
                updateMemoria(0x35, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("jmp "+opdRegex)){
                updateMemoria(0xEB, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("jz "+opdRegex)){
                updateMemoria(0x74, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("jnz "+opdRegex)){
                updateMemoria(0x75, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("jp "+opdRegex)){
                updateMemoria(0x7A, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("call "+opdRegex)){
                updateMemoria(0xE8, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("ret")){
                updateMemoria(0xEF, i);
            }else if(instrucao.matches("hlt")){
                updateMemoria(0xEE, i);
            }else if(instrucao.matches("pop AX")){
                updateMemoria(0x58C0, i);
            }else if(instrucao.matches("pop DX")){
                updateMemoria(0x58C2, i);
            }else if(instrucao.matches("pop "+opdRegex)){
                updateMemoria(0x58, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("popf")){
                updateMemoria(0x9C, i);
            }else if(instrucao.matches("push AX")){
                updateMemoria(0x50C0, i);
            }else if(instrucao.matches("push DX")){
                updateMemoria(0x50C2, i);
            }else if(instrucao.matches("pushf")){
                updateMemoria(0x9C, i);
            }else if(instrucao.matches("store AX")){
                updateMemoria(0x07C0, i);
            }else if(instrucao.matches("store DX")){
                updateMemoria(0x07C2, i);
            }else if(instrucao.matches("read "+opdRegex)){
                updateMemoria(0x12, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("write "+opdRegex)){
                updateMemoria(0x08, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao ==""){

            }else{
                System.out.println("Instrução nao reconhecida!");
            }      
            */
            }   //APAGAR!!
            
        updateRegistrador(instrucoes.size(),6);  
            
            
            
            
            
            
        
            
            
            
            
        }
     
        
    public static void run_instrucao(String instrucao){
        
            
        
        for (int i = 0; i< instrucoes.size(); i++){
            
            if(instrucao.matches("add AX AX")){
                
                int resultado = getRegistrador("AX") *2;
                updateRegistrador(resultado,"AX");
                break;
            }
        
        
        
        
        
        
        
        }
    }
    
    
    
}
