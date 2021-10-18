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
        linha_mem = linha_mem.split(":")[1];
        linha_mem = linha_mem.split("0x")[1];
        linha_mem = linha_mem.trim(); 
        
        
        return Integer.parseInt(linha_mem,16);
        
        
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
    

      
    
    public static void run_instrucao(){
            
                
                int instrucao = getMemoria(getRegistrador("IP"));
                System.out.println("getRegistradorIP = "+getRegistrador("IP"));
                System.out.println("getMemoria do Registrador IP = "+getMemoria(getRegistrador("IP")));
                
                switch (instrucao){
            case 0x03c0:// add ax
                System.out.println("cheguei = ");
                updateRegistrador(2*getRegistrador("AX"),"AX");
            break;
            case 0x03c2:// add dx
                updateRegistrador(getRegistrador("AX")+getRegistrador("DX") ,"AX");
            break;
            /*
            case 0x05: // add opd
                opd = memory.getPalavra(CS+IP++);
                AX += opd;
            break;
            case 0xf7f6:// div si
                div = AX / SI;
                AX = (short)(div & 256);
                DX = (short)(div >>> 8);
            break;
            case 0xf7c0:// div ax
                div = AX / AX;
                AX = (short)(div & 256);
                DX = (short)(div >>> 8);
            break;
            case 0x2bc0:// sub ax
                AX -= AX;
            break;
            case 0x2bc2:// sub dx
                AX -= DX;
            break;
            case 0x25:// sub opd
                memory.getPalavra(CS+IP++);
                AX -= opd;
            break;
            // case 0xf7f6:// mul si
                //todo
            // break;
            case 0xf7f0:// mul AX
                mul = AX * AX;
                AX = (short)(mul & 256);
                DX = (short)(mul >>> 8);
            break;
            case 0x3d:// cmp opd
                opd = memory.getPalavra(CS+IP++);
                setFlag("zf", AX == opd);
            break;
            case 0x3bc2://cmp DX
                setFlag("zf", AX == DX);
            break;
            case 0x23c0:// and AX
                setFlag("zf", AX == AX);
            break;
            case 0x23c2:// and DX
                AX &= AX;
            break;
            // case 0x25:// and opd
                //todo
                // CS+IP++;
            // break;
            case 0xf8c0:// not ax
                AX = (short)~AX;
            break;
            case 0x0bc0:// or ax
                AX|=AX;
            break;
            case 0x0bc2:// or dx
                AX|=DX;
            break;
            case 0x0d:// or opd
                opd = memory.getPalavra(CS+IP++);
                AX|=opd;
            break;
            case 0x33c0:// xor ax
                AX|=AX;
            break;
            case 0x33c2:// xor dx
                AX|=DX;
            break;
            case 0x35:// xor opd
                opd = memory.getPalavra(CS+IP++);
                AX^=opd;
            break;
            case 0xeb:// jmp
                opd = memory.getPalavra(CS+IP++);
                IP = opd;
            break;
            case 0x74:// jz
                opd = memory.getPalavra(CS+IP++);
                if(getFlag("zf")) IP = opd;
            break;
            case 0x75:// jnz
                opd = memory.getPalavra(CS+IP++);
                if(!getFlag("zf")) IP = opd;
            break;
            case 0x7a:// jp
                opd = memory.getPalavra(CS+IP++);
                if(!getFlag("SF")) IP = opd;
            break;
            case 0xe8:// call
                opd = memory.getPalavra(CS+IP++);
                memory.setPalavra(IP, SI++);
                IP = opd;
            break;
            case 0xef:// ret
                IP = memory.getPalavra(--SI);
            break;
            case 0x58c0:// pop ax
                AX = memory.getPalavra(--SI);
            break;
            case 0x58c2:// pop dx
                DX = memory.getPalavra(--SI);
            break;
            case 0x59:// pop opd
                opd = memory.getPalavra(IP++);
                memory.setPalavra(memory.getPalavra(--SI), DS+opd);
            break;
            case 0x9d:// popf
                SR = memory.getPalavra(--SI);
            break;
            case 0x50c0:// push ax
                memory.setPalavra(AX, DS+opd);
            break;
            case 0x50c2:// push dx
                memory.setPalavra(DX, DS+opd);
            break;
            case 0x9c://pushf
                memory.setPalavra(SR, SI++);
            break;
            case 0x07c0:// store ax
                //todo
            break;
            case 0x07c2:// store dx
                //todo
            break;
            case 0x12:// read opd
                opd = memory.getPalavra(IP++);
                outputStream = Util.convertIntegerToBinary(opd);
                if(inputStream.size()>inputStreamIndex){
                    memory.setPalavra(inputStream.get(inputStreamIndex++).shortValue(), opd);
                }else{
                    IP-=2;
                }
            break;
            case 0x08:// write opd
                opd = memory.getPalavra(CS+IP++);
                outputStream = Util.convertIntegerToBinary(opd);
            break;
            case 0xEE: // hlt
                
            break;
        }         */
                 
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
    
    public static int getbyte(String reg, String MSouLS){
    
        if (reg.equals("AX")){
            int aux =getRegistrador("AX");
            String aux_str = Integer.toBinaryString(aux);
            aux_str = String.format("%016d", Integer.parseInt(aux_str),2);
            
            
            
            String MS = aux_str.subSequence(0, 8).toString();
            String LS = aux_str.subSequence(8, 16).toString();
            
            
            if (MSouLS.equals("MS"))return Integer.parseInt(MS, 2);
            else if (MSouLS.equals("LS")) return Integer.parseInt(LS, 2);
           
        }
        
        else if (reg.equals("DX")){
            int aux =getRegistrador("DX");
            String aux_str = Integer.toBinaryString(aux);
            String MS = aux_str.subSequence(0, 8).toString();
            String LS = aux_str.subSequence(8, 16).toString();
            
            if (MSouLS.equals("MS"))return Integer.parseInt(MS, 2);
            else if (MSouLS.equals("LS")) return Integer.parseInt(LS, 2);            
        }
        
        
        return -1;
    }

    public static void setbyte(String reg, String MSouLS, int valor){
    
        String new_content;
        if (reg.equals("AX")){
            int aux =getRegistrador("AX");
            String aux_str = Integer.toBinaryString(aux);
            aux_str = String.format("%016d", Integer.parseInt(aux_str),2);
            
            String valor_str = Integer.toBinaryString(valor);
            valor_str = String.format("%08d", Integer.parseInt(valor_str),2);
            
            String MS = aux_str.subSequence(0, 8).toString();
            String LS = aux_str.subSequence(8, 16).toString();
            
            
            if (MSouLS.equals("MS")){
                new_content = valor_str+LS  ;
                updateRegistrador(Integer.parseInt(converter.binToDec(new_content)),"AX");
            }
            else if (MSouLS.equals("LS")){
                new_content = MS + valor_str;
                updateRegistrador(Integer.parseInt(converter.binToDec(new_content)),"AX");
            }
        
        }
        
        else if (reg.equals("DX")){
            int aux =getRegistrador("DX");
            String aux_str = Integer.toBinaryString(aux);
            aux_str = String.format("%016d", Integer.parseInt(aux_str),2);
            
            String valor_str = Integer.toBinaryString(valor);
            valor_str = String.format("%08d", Integer.parseInt(valor_str),2);
            
            String MS = aux_str.subSequence(0, 8).toString();
            String LS = aux_str.subSequence(8, 16).toString();
            
            
            if (MSouLS.equals("MS")){
                new_content = valor_str+LS  ;
                updateRegistrador(Integer.parseInt(converter.binToDec(new_content)),"DX");
            }
            else if (MSouLS.equals("LS")){
                new_content = MS+valor_str ;
                updateRegistrador(Integer.parseInt(converter.binToDec(new_content)),"DX");
            }          
        }
        
        
       
    }
    
    
    
    public static int getbitSR(int flag){
        
    String str_SR = converter.decToBin(getRegistrador("DS")+"");
    int valor_bin = Integer.parseInt(str_SR);
    
    
    str_SR = String.format("%016d", valor_bin);
    
    //System.out.println(Integer.parseInt(str_SR.charAt(flag) + ""));
    
    
    return(Integer.parseInt(str_SR.charAt(flag) + ""));
        
        
    }
    
    
    public static void setbitSR(char valor,int flag){
        
    String str_SR = converter.decToBin(getRegistrador("DS")+"");
    
    int valor_bin = Integer.parseInt(str_SR);
    
    
    str_SR = String.format("%016d", valor_bin);
 

    StringBuilder new_str = new StringBuilder(str_SR);
    new_str.setCharAt(flag, valor);
    

    String new_str_dec = converter.binToDec(new_str.toString());
    
    System.out.println("new_str_dec " + new_str_dec);
    int new_content = Integer.parseInt(new_str_dec);
  
    
    updateRegistrador(new_content,"SR");
    
        
    }
       
}



