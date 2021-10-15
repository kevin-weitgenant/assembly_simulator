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
    
    public static void updateMemoria( int valor, int posicao_palavra){
        String new_content = String.format("0x%02X",valor);
        new_content = String.format("%04d", posicao_palavra) + ": " + new_content ;
        listMemoryModel.setElementAt(""+new_content, posicao_palavra);        
        
    }
    

    public static void load_instrucoes(){
        for (int i = 0; i< instrucoes.size(); i++){
            String instrucao = instrucoes.get(i);
            if(instrucao.matches("add AX AX")){
                updateMemoria(i, 0x03C0);
            }
            
            /*
            else if(instrucao.matches("add AX DX")){
                updateMemoria((short)0x03C2, i);
            }else if(instrucao.matches("add AX "+opdRegex)){
                updateMemoria((short)0x05, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("div SI")){
                updateMemoria((short)0xf7f6, i);
            }else if(instrucao.matches("div AX")){
                updateMemoria((short)0xf7c0, i);
            }else if(instrucao.matches("sub AX AX")){
                updateMemoria((short)0x2bc0, i);
            }else if(instrucao.matches("sub AX DX")){
                updateMemoria((short)0x2bc2, i);
            }else if(instrucao.matches("sub AX "+opdRegex)){
                updateMemoria((short)0x25, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("mul SI")){
                updateMemoria((short)0xf7f6, i);
            }else if(instrucao.matches("mul AX")){
                updateMemoria((short)0xf7f0, i);
            }else if(instrucao.matches("cmp AX DX")){
                updateMemoria((short)0x3BC2, i);
            }else if(instrucao.matches("cmp AX "+opdRegex)){
                updateMemoria((short)0x3d, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("and AX DX")){
                updateMemoria((short)0xf7C2, i);
            }else if(instrucao.matches("and AX "+opdRegex)){
                updateMemoria((short)0x25, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("not AX")){
                updateMemoria((short)0xF8C0, i);
            }else if(instrucao.matches("or AX AX")){
                updateMemoria((short)0x0BC0, i);
            }else if(instrucao.matches("or AX DX")){
                updateMemoria((short)0x0BC0, i);
            }else if(instrucao.matches("or AX "+opdRegex)){
                updateMemoria((short)0x0D, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("xor AX AX")){
                updateMemoria((short)0x33C0, i);
            }else if(instrucao.matches("xor AX DX")){
                updateMemoria((short)0x33C2, i);
            }else if(instrucao.matches("xor AX "+opdRegex)){
                updateMemoria((short)0x35, i++);
                updateMemoria(calculateOpd(params[1]), i);
            }else if(instrucao.matches("jmp "+opdRegex)){
                updateMemoria((short)0xEB, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("jz "+opdRegex)){
                updateMemoria((short)0x74, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("jnz "+opdRegex)){
                updateMemoria((short)0x75, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("jp "+opdRegex)){
                updateMemoria((short)0x7A, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("call "+opdRegex)){
                updateMemoria((short)0xE8, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("ret")){
                updateMemoria((short)0xEF, i);
            }else if(instrucao.matches("hlt")){
                updateMemoria((short)0xEE, i);
            }else if(instrucao.matches("pop AX")){
                updateMemoria((short)0x58C0, i);
            }else if(instrucao.matches("pop DX")){
                updateMemoria((short)0x58C2, i);
            }else if(instrucao.matches("pop "+opdRegex)){
                updateMemoria((short)0x58, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("popf")){
                updateMemoria((short)0x9C, i);
            }else if(instrucao.matches("push AX")){
                updateMemoria((short)0x50C0, i);
            }else if(instrucao.matches("push DX")){
                updateMemoria((short)0x50C2, i);
            }else if(instrucao.matches("pushf")){
                updateMemoria((short)0x9C, i);
            }else if(instrucao.matches("store AX")){
                updateMemoria((short)0x07C0, i);
            }else if(instrucao.matches("store DX")){
                updateMemoria((short)0x07C2, i);
            }else if(instrucao.matches("read "+opdRegex)){
                updateMemoria((short)0x12, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao.matches("write "+opdRegex)){
                updateMemoria((short)0x08, i++);
                updateMemoria(calculateOpd(params[0]), i);
            }else if(instrucao ==""){

            }else{
                System.out.println("Instrução nao reconhecida!");
            }      
            */
            
            
        updateRegistrador(instrucoes.size(),6);  
            
            
            
            
            
            
        
            
            
            
            
        }
        
        
        
    }
    
    
    
}
