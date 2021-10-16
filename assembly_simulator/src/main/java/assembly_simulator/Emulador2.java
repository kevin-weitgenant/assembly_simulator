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
    
    protected static int apontador_memLixo = DS;
    
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
        
        
        
        String new_content = String.format("0x%02X",valor);
        listRegisterModel.setElementAt(aux_reg[posicao_reg] +new_content, posicao_reg);
        
    } 
    
    
    
    public static int getRegistrador(int posicao_reg){   
        String linha_reg = listRegisterModel.getElementAt(posicao_reg);
        linha_reg = linha_reg.split("0x")[1];
        linha_reg = linha_reg.replaceAll("\\s+","");   
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
        linha_reg = linha_reg.replaceAll("\\s+","");   
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
            int posicao = getRegistrador("CS")+i;
            
            String instrucao = instrucoes.get(i);
            if(instrucao.matches("add AX,AX")){
               
                updateMemoria(0x03C0,posicao );
                   
            }
            
            
            else if(instrucao.matches("add AX,DX")){
                updateMemoria(0x03C2, posicao);
                
            }else if(instrucao.matches("add AX,.*")){
                updateMemoria(0x05, i++);
                String opd = instrucao.split("AX,")[1];
                opd = opd.replaceAll("\\s+","");
                System.out.println("opd = "+ opd);
                calculateOpd(opd);
                
                //int valor_opd = tabela_get_operando(opdRegex);
                //updateMemoria(valor_opd,i);
            /*    
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
            }// DELETAR
            
        //updateRegistrador(instrucoes.size(),"DS");    CASO SETAR DINAMICAMENTE OS SEGMENTOS   PILHA-INSTRUCOES-DADOS
            
            
            
            
            
            
        
            
            
            
            
        }
    }
      
    
    public static int calculateOpd(String opd){

        
        if(opd.matches("[0-1]+b")){
            int valor = Integer.parseInt(converter.binToHex(opd));
            updateMemoria(valor,++apontador_memLixo);
            return apontador_memLixo;
            
        }else if(opd.matches("0x[0-9a-fA-F]+")){
            int valor = Integer.parseInt(opd);
            updateMemoria(valor,++apontador_memLixo);
            return apontador_memLixo;
        }else if(opd.matches("[0-9]+")){
            int valor = Integer.parseInt(converter.decToHex(opd));
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
            
            if(instrucao.matches("add AX,AX")){    //ta errado tem quer ser pelos os códigos das instruções, burrei
                
                int resultado = getRegistrador("AX") *2;
                updateRegistrador(resultado,"AX");
                break;
            }
        
        // acrescentar
        
        
        
        
      
        }
    }
    

    
    
    
    
    
    public static void tabela_operandos(){

        int controle = 0;
        for (int i = 0; i< instrucoes.size(); i++){    
            String instrucao = instrucoes.get(i);
            
            if (instrucao.contains("EQU")){
                
                String op_nome = instrucao.split("EQU")[0];
                
                String op_valor_str = instrucao.split("EQU")[1];
                op_valor_str = op_valor_str.replaceAll("\\s+","");
                int op_valor = Integer.parseInt(op_valor_str);
                
                int posicao = controle+getRegistrador("DS");
                
                updateMemoria(op_valor,posicao);
                tabela_opds.put(op_nome, Arrays.asList(posicao,0));
                apontador_memLixo++;
                controle++;
                     
            } 
            
            else if (instrucao.contains("DW")){
                String op_nome = instrucao.split("DW")[0];
                
                String op_valor_str = instrucao.split("DW")[1];
                op_valor_str = op_valor_str.replaceAll("\\s+","");
                int op_valor = Integer.parseInt(op_valor_str);
                
                int posicao = controle+getRegistrador("DS");
                
                updateMemoria(op_valor,posicao);
                
                tabela_opds.put(op_nome, Arrays.asList(posicao,1));
                apontador_memLixo++;
                controle++;
                
                
            }
        
        } 
    
    }
    
    
    public static void print_tabela(){    
        
    for (String key : tabela_opds.keySet()){
        System.out.println("OPERADOR " +key+ tabela_opds.get(key) );

        
    }
    
    
    }
    
}
