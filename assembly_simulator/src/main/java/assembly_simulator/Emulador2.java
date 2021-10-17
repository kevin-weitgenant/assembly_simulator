/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assembly_simulator;

import static assembly_simulator.TelaPrincipal.listRegisterModel;
import static assembly_simulator.TelaPrincipal.listMemoryModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 *
 * @author kevin
 */

// POSICOES DA MEMORIA

// PILHA  0= CHECK OVERFLOW PILHA, 1= TAMANHO DA PILHA, 2-11= POSICOES PARA EMPILHAR

// SEGMENTO DE DADOS = 11-2011

//SEGMENTO DE INSTRUCOES = 2012-4095




public class Emulador2 {
    static public List<String> instrucoes = new ArrayList<String>();
 
   
    
    
    public static int linha_atual;
    public static int IP = 0;
    static String[] aux_reg = {"AX: ","DX: ","SP: ","SI: ","IP: ","SR: ","CS: ","DS: "}; 
    
    
    protected static final int DS = 11;
    protected static final int CS = 2012;
    protected static final int SP = 2;
    
    protected static int apontador_memLixo;
    
    public static Map<String, List<Integer>> tabela_opds = new HashMap<String, List<Integer>>();
    
    
    
    
    
    public static void  updateRegistrador(int valor,int posicao_reg){
        
        String new_content = String.format("0x%04X",valor);
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
        
        
        
        String new_content = String.format("0x%04X",valor);
        listRegisterModel.setElementAt(aux_reg[posicao_reg] +new_content, posicao_reg);
        
    } 
    
    
    
    public static int getRegistrador(int posicao_reg){   
        String linha_reg = listRegisterModel.getElementAt(posicao_reg);
        linha_reg = linha_reg.split("0x")[1];
        linha_reg = linha_reg.trim();   
        return Integer.parseInt(linha_reg,16);
        
        
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
        linha_reg = linha_reg.trim();   
        return Integer.parseInt(linha_reg,16);
        
        
    }
    
    public static void InitRegistradores(){
        // POSICOES DA MEMORIA

// PILHA  0= CHECK OVERFLOW PILHA, 1= TAMANHO DA PILHA, 2-11= POSICOES PARA EMPILHAR

// SEGMENTO DE DADOS = 11-2011

//SEGMENTO DE INSTRUCOES = 2012-4095
        updateRegistrador(DS,"DS");
        updateRegistrador(CS,"CS");
        updateRegistrador(SP,"SP");
        updateRegistrador(CS,"IP");
    }
    

    public static int getMemoria(int posicao_palavra){
        String linha_mem = listMemoryModel.getElementAt(posicao_palavra);
        linha_mem = linha_mem.split("0x")[1];
        linha_mem = linha_mem.trim();   
        return Integer.parseInt(linha_mem,16);
        
        //RETORNA EM DECIMAL
        
    }
    
    public static void updateMemoria( int valor, int posicao_palavra){
        String new_content = String.format("0x%04X",valor);
        new_content = String.format("0x%04X", posicao_palavra) + ": " + new_content ;
        listMemoryModel.setElementAt(""+new_content, posicao_palavra);        
        
    }
    

    public static void load_instrucoes(){
        int controle_mem = CS;
        
        for (int i = 0; i< instrucoes.size(); i++){
            
            
            String instrucao = instrucoes.get(i);
            if(instrucao.contains("add AX,AX")){
               
                updateMemoria(0x03C0,controle_mem++ );      
            }
            
            
            else if(instrucao.contains("add AX,DX")){
                updateMemoria(0x03C2, controle_mem++);
 
                
            }else if(instrucao.matches("add AX,.*")){
                updateMemoria(0x05, controle_mem++);
                String opd = instrucao.split("AX,")[1];
                opd = opd.trim();

                updateMemoria(calculateOpd(opd),controle_mem++);
                
                    
            }else if(instrucao.contains("div SI")){
                updateMemoria(0xf7f6, controle_mem++);
                
            }else if(instrucao.contains("div AX")){
                updateMemoria(0xf7c0, controle_mem++);
                
            }else if(instrucao.contains("sub AX AX")){
                updateMemoria(0x2bc0, controle_mem++);
                
            }else if(instrucao.contains("sub AX DX")){
                updateMemoria(0x2bc2, controle_mem++);
                
                
            
            }else if(instrucao.contains("sub AX,")){
                updateMemoria(0x25, controle_mem++);
                String opd = instrucao.split("AX,")[1];
                opd = opd.trim();

                
                updateMemoria(calculateOpd(opd),controle_mem++);
                
            
            }else if(instrucao.contains("mul SI")){
                updateMemoria(0xf7f6, controle_mem++);
                
            }else if(instrucao.contains("mul AX")){
                updateMemoria(0xf7f0, controle_mem++);
                
            }else if(instrucao.contains("cmp AX DX")){
                updateMemoria(0x3BC2, controle_mem++);
                
            
            }else if(instrucao.contains("cmp AX ")){
                updateMemoria(0x3d, controle_mem++);
                String opd = instrucao.split("AX")[1];
                opd = opd.trim();
                updateMemoria(calculateOpd(opd),controle_mem++);

            }else if(instrucao.contains("and AX DX")){
                updateMemoria(0xf7C2, controle_mem++);
  
            }else if(instrucao.contains("and AX ")){
                updateMemoria(0x25, controle_mem++);
                String opd = instrucao.split("AX,")[1];
                opd = opd.trim();

                
                updateMemoria(calculateOpd(opd),controle_mem++);
                
            
            }else if(instrucao.contains("not AX")){
                updateMemoria(0xF8C0, controle_mem++);
                
            }else if(instrucao.contains("or AX,AX")){
                updateMemoria(0x0BC0, controle_mem++);
                
            }else if(instrucao.contains("or AX,DX")){
                updateMemoria(0x0BC0, controle_mem++);
                
 
            }else if(instrucao.contains("or AX,")){
                updateMemoria(0x0D, controle_mem++);
                 String opd = instrucao.split("AX,")[1];
                opd = opd.trim();

                updateMemoria(calculateOpd(opd),controle_mem++);

            
            }else if(instrucao.contains("xor AX,AX")){
                updateMemoria(0x33C0, controle_mem++);

            }else if(instrucao.contains("xor AX,DX")){
                updateMemoria(0x33C2, controle_mem++);
 
            
            }else if(instrucao.contains("xor AX ")){
                updateMemoria(0x35, controle_mem++);
                String opd = instrucao.split("AX,")[1];
                opd = opd.trim();

                
                updateMemoria(calculateOpd(opd),controle_mem++);

            
            }else if(instrucao.contains("jmp ")){
                updateMemoria(0xEB, controle_mem++);
                String opd = instrucao.split("jmp")[1];
                opd = opd.trim();

                
                updateMemoria(calculateOpd(opd),controle_mem++);
                
            
            }else if(instrucao.contains("jz ")){
                updateMemoria(0x74, controle_mem++);
                String opd = instrucao.split("jz")[1];
                opd = opd.trim();
                updateMemoria(calculateOpd(opd),controle_mem++);
                
            
            }else if(instrucao.contains("jnz ")){
                updateMemoria(0x75, controle_mem++);
                String opd = instrucao.split("jnz")[1];
                opd = opd.trim();

                
                updateMemoria(calculateOpd(opd),controle_mem++);
 
            
            }else if(instrucao.contains("jp ")){
                updateMemoria(0x7A, controle_mem++);
                String opd = instrucao.split("jp")[1];
                opd = opd.trim();

                
                updateMemoria(calculateOpd(opd),controle_mem++);
                
            
            }else if(instrucao.contains("call ")){
                updateMemoria(0xE8, controle_mem++);
                String opd = instrucao.split("call")[1];
                opd = opd.trim();

                
                updateMemoria(calculateOpd(opd),controle_mem++);
                
            
            
            }else if(instrucao.contains("ret")){
                updateMemoria(0xEF, controle_mem++);
                
            
            }else if(instrucao.contains("hlt")){
                updateMemoria(0xEE, controle_mem++);
                
            }else if(instrucao.contains("pop AX")){
                updateMemoria(0x58C0, controle_mem++);
                
            }else if(instrucao.contains("pop DX")){
                updateMemoria(0x58C2, controle_mem++);
                
            
            }else if(instrucao.contains("pop ")){
                updateMemoria(0x58, controle_mem++);
                String opd = instrucao.split("pop")[1];
                opd = opd.trim();

                
                updateMemoria(calculateOpd(opd),controle_mem++);
                
            }else if(instrucao.contains("popf")){
                updateMemoria(0x9C, controle_mem++);
                
            }else if(instrucao.contains("push AX")){
                updateMemoria(0x50C0, controle_mem++);
                
            }else if(instrucao.contains("push DX")){
                updateMemoria(0x50C2, controle_mem++);
                
            }else if(instrucao.contains("pushf")){
                updateMemoria(0x9C, controle_mem++);
                
            }else if(instrucao.contains("store AX")){
                updateMemoria(0x07C0, controle_mem++);
                
            }else if(instrucao.contains("store DX")){
                updateMemoria(0x07C2, controle_mem++);
                
            
            }else if(instrucao.contains("read ")){
                updateMemoria(0x12, controle_mem++);
                String opd = instrucao.split("read")[1];
                opd = opd.trim();

                
                updateMemoria(calculateOpd(opd),controle_mem++);
                
            
            }else if(instrucao.contains("write ")){
                updateMemoria(0x08, controle_mem++);
                String opd = instrucao.split("AX,")[1];
                opd = opd.trim();

                
                updateMemoria(calculateOpd(opd),controle_mem++);
                controle_mem++;
            }else if(instrucao ==""||instrucao.contains("EQU") || instrucao.contains("DW")){

            }


            else{
                System.out.println("Instrução nao reconhecida = "+ instrucao);
            }      
 
            
        //updateRegistrador(instrucoes.size(),"DS");    CASO SETAR DINAMICAMENTE OS SEGMENTOS   PILHA-INSTRUCOES-DADOS
            
            
            
            
            
            
        
            
            
            
            
        }
    }
      
    
    public static int calculateOpd(String opd){

        
        if(opd.matches("[0-1]+b")){
            int valor = Integer.parseInt(converter.binToHex(opd),16);
            updateMemoria(valor,++apontador_memLixo);
            return apontador_memLixo;
            
        }else if(opd.matches("0x[0-9a-fA-F]+")){
            int valor = Integer.parseInt(opd);
            updateMemoria(valor,++apontador_memLixo);
            return apontador_memLixo;
        }else if(opd.matches("[0-9]+")){
            int valor = Integer.parseInt(converter.decToHex(opd),16);
            updateMemoria(valor,++apontador_memLixo);
            return apontador_memLixo;
        }
        
        
        else if(opd.matches("[A-Za-z][A-Za-z0-9]*")){

            if(tabela_opds.keySet().contains(opd)){
                
                return tabela_opds.get(opd).get(0);
                
            }
            
            else{
                System.out.println("OPD NÃO ENCONTRADO NA TABELA!");
                
            }
          
        }
        return -1;
                
    }
    
    /*
    public static int getOpdValue(String opd){
        for 
        
        
        
    }
    */
    
    
    
    
    public static void run_instrucao(String instrucao){
        
            
        
        for (int i = 0; i< instrucoes.size(); i++){
            
            
        
        // acrescentar
        

        }
    }
    

    
    
    
    
    
    public static void tabela_operandos(){

        int controle = 0;
        for (int i = 0; i< instrucoes.size(); i++){    
            String instrucao = instrucoes.get(i);
            
            if (instrucao.contains("EQU")){
                
                String op_nome = instrucao.split("EQU")[0].trim();
                
                String op_valor_str = instrucao.split("EQU")[1];
                op_valor_str = op_valor_str.trim();
                int op_valor = Integer.parseInt(op_valor_str);
                
                int posicao = controle+getRegistrador("DS");
                
                updateMemoria(op_valor,posicao);
                tabela_opds.put(op_nome, Arrays.asList(posicao,0));
                
                controle++;
                     
            } 
            
            else if (instrucao.contains("DW")){
                String op_nome = instrucao.split("DW")[0];
                
                String op_valor_str = instrucao.split("DW")[1];
                op_valor_str = op_valor_str.trim();
                int op_valor = Integer.parseInt(op_valor_str);
                
                int posicao = controle+getRegistrador("DS");
                
                updateMemoria(op_valor,posicao);
                
                tabela_opds.put(op_nome, Arrays.asList(posicao,1));
                
                controle++;
                
                
            }
        
        } 
        apontador_memLixo = controle + DS;
        updateMemoria(0xFFFF,apontador_memLixo);
    }
    
    
    public static void print_tabela(){    
        
    for (String key : tabela_opds.keySet()){
        System.out.println("OPERADOR " +key+ tabela_opds.get(key) );

        
    }
    
    }
    
    
    public static int getbitSR(int flag){
        
    String str_SR = converter.decToBin(getRegistrador("DS")+"");
    int valor_bin = Integer.parseInt(str_SR);
    
    
    str_SR = String.format("%016d", valor_bin);
    
    System.out.println(Integer.parseInt(str_SR.charAt(flag) + ""));
    
    
    return(Integer.parseInt(str_SR.charAt(flag) + ""));
        
        
    }
    
    
    public static int setbitSR(int valor,int flag){
        
    String str_SR = converter.decToBin(getRegistrador("DS")+"");
    int valor_bin = Integer.parseInt(str_SR);
    
    
    str_SR = String.format("%016d", valor_bin);
    
    System.out.println(Integer.parseInt(str_SR.charAt(flag) + ""));
    
    
    return(Integer.parseInt(str_SR.charAt(flag) + ""));
        
        
    }
       
}



