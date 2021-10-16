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

//import org.graalvm.compiler.code.DataSection.Data;

/**
 *
 * @author kevin
 */

// POSICOES DA MEMORIA
// PILHA  0= CHECK OVERFLOW PILHA, 1= TAMANHO DA PILHA, 2-11= POSICOES PARA EMPILHAR
// SEGMENTO DE DADOS = 11-3000
//SEGMENTO DE INSTRUCOES = 3000-4095


public class Emulador2 {
    static public List<String> instrucoes = new ArrayList<String>();
    static public int dataSegmentControl;

    public static int IP = 0;
    static String[] aux_reg = {"AX: ","DX: ","SP: ","SI: ","IP: ","SR: ","CS: ","DS: ","SS: "}; 
    static List<TabelaOperandos> tabela = new ArrayList<TabelaOperandos>();

    public static void  updateRegistrador(String valor, int posicao_reg){
        
        String new_content = new String(valor);
        listRegisterModel.setElementAt(aux_reg[posicao_reg] +new_content, posicao_reg);
        
    } 
    
    public static void  updateRegistrador(String valor, String nome_reg){
        int posicao_reg = 0;
        for (int i = 0; i<aux_reg.length; i++){
            String aux = aux_reg[i].split(":")[0];
            
            if (aux.equals(nome_reg)){
                posicao_reg = i;
                break;
            }
        } 
        String new_content = String.format(valor);
        listRegisterModel.setElementAt(aux_reg[posicao_reg] + new_content, posicao_reg);
    } 
    
    public static String getRegistrador(int posicao_reg){   
        String linha_reg = listRegisterModel.getElementAt(posicao_reg);
        linha_reg = linha_reg.split(":")[1];
        linha_reg = linha_reg.replaceAll("\\s+","");   
        return linha_reg;
   
    }
    
    public static String getRegistrador(String nome_reg){   
        
        int posicao_reg = 0;
        for (int i = 0; i<aux_reg.length; i++){
            String aux = aux_reg[i].split(":")[0];
            
            if (aux.equals(nome_reg)){
                posicao_reg = i;
                break;
            }
        }
 
        String linha_reg = listRegisterModel.getElementAt(posicao_reg);
        linha_reg = linha_reg.split(":")[1];
        linha_reg = linha_reg.replaceAll("\\s+","");
        return linha_reg;
   
    }
    
    public static void InitRegistradores(){
    // POSICOES DA MEMORIA
    // PILHA  0= CHECK OVERFLOW PILHA, 1= TAMANHO DA PILHA, 2-11= POSICOES PARA EMPILHAR
    // SEGMENTO DE DADOS = 11-2011
    //SEGMENTO DE INSTRUCOES = 2012-4095
        updateRegistrador("3000","DS");
        updateRegistrador("1000","CS");
        updateRegistrador("2","SP");
        String IPValueInitial = getRegistrador("CS");
        updateRegistrador(IPValueInitial,"IP");
        dataSegmentControl = Integer.parseInt(getRegistrador("DS"));

    }
    
    public static int getMemoria(int posicao_palavra){
        String linha_mem = listMemoryModel.getElementAt(posicao_palavra);
        linha_mem = linha_mem.split("0x")[1];
        linha_mem = linha_mem.replaceAll("\\s+","");   
        return Integer.parseInt(linha_mem,16);
        
        //RETORNA EM DECIMAL
        
    }
    
    public static void updateMemoria(String valor, int posicao_palavra){
        String new_content = valor;
        new_content = String.format("%04d", posicao_palavra) + ": " + new_content ;
        listMemoryModel.setElementAt(""+new_content, posicao_palavra);        
        
    }
    
    public static void load_instrucoes(){

        String opdRegex=".*";
        Integer CSPosition = Integer.parseInt(getRegistrador("CS"));
        Integer DSPosition = Integer.parseInt(getRegistrador("DS"));
        int instrucoesPosition = 0; // Necessario pois EQU e DW não carrega valores no Segmento de Instrucao

        for (int i = 0; i< instrucoes.size(); i++){
            
            Integer posicao = CSPosition + instrucoesPosition;
            String instrucao = instrucoes.get(i);

            if(instrucao.matches(".*EQU.*")){ // var EQU value
                String opdName = instrucao.split("EQU")[0];
                String opdValue = instrucao.split("EQU")[1];
                opdName = opdName.replaceAll("\\s+","");
                opdValue = opdValue.replaceAll("\\s+","");
                String positionInMemory = verifyIfOperandoExist(opdName, "EQU");

                updateMemoria(opdValue, Integer.parseInt(positionInMemory));
                instrucoesPosition--;
            }
            else if(instrucao.matches("DW")){ // var DW value
                String opdName = instrucao.split("EQU")[0];
                String opdValue = instrucao.split("EQU")[1];
                opdName = opdName.replaceAll("\\s+","");
                opdValue = opdValue.replaceAll("\\s+","");
                String positionInMemory = verifyIfOperandoExist(opdName, "DW");
                updateMemoria(opdValue, Integer.parseInt(positionInMemory));
                instrucoesPosition--;
            }
            else if(instrucao.matches("add AX AX")){
                updateMemoria("03 C0",posicao );    
            }
            else if(instrucao.matches("add AX DX")){
                updateMemoria("03 C2", posicao);
                
            }else if(instrucao.matches("add AX.*")){
                
                String opd = instrucao.split("AX")[1];
                opd = opd.replaceAll("\\s+","");
                String adressPosition = verifyIfOperandoExist(opd); // Retorna endereço da variavel
                if(!(adressPosition.equals("false"))){
                    updateMemoria("05 "+adressPosition, posicao);
                }else{
                    // Mostrar mensagem de erro
                }
            }
  
            /*    
            }else if(instrucao.matches("div SI")){
                updateMemoria("f7 f6", i);
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
                System.out.println("Instru��o nao reconhecida!");
            }      
            
            }*/
            
        //updateRegistrador(instrucoes.size(),"DS");    CASO SETAR DINAMICAMENTE OS SEGMENTOS   PILHA-INSTRUCOES-DADOS    
        instrucoesPosition++;
         }
    }
        
    public static void runNextStep(String instruction){
        
    // acrescentar
        Integer AX = Integer.parseInt(getRegistrador("AX"));
        Integer DX = Integer.parseInt(getRegistrador("DX"));
        Integer IP = Integer.parseInt(getRegistrador("IP"));
        int div;
        int mul;
        Integer opd = 0;

        instruction = instruction.split(":")[1];
        instruction = instruction.trim();
        //System.out.println("Aqui: " + IP + ": " + instruction);
        
        switch(instruction){
            case "03 C0":// add ax ax
                AX += AX;
                IP++;
            break;
            case "03 C2":// add ax dx
                AX += DX;
                IP++;
            break;
            
            case "05.*": // add opd
            //System.out.println("Vai entrar? " + IP + ": " + instruction);
            String position = instruction.split("\\S")[1];
                String valor = listMemoryModel.getElementAt(Integer.parseInt(position));
                valor = valor.split(":")[1];
                valor = valor.split("\\S")[0];
                opd = Integer.parseInt(valor);
                AX += opd;
                IP++;
            break;
            /*
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
                this.finished = true;
            break;*/
        }
        updateRegistrador(Integer.toString(IP), "IP");
        updateRegistrador(Integer.toString(AX), "AX");
        updateRegistrador(Integer.toString(DX), "DX");
    }
    
    public static void tabela_operandos(List<TabelaOperandos> tabela){
        //NOME_OPERANDO DW VALOR
        
        
        for (int i = 0; i< instrucoes.size(); i++){    
            String instrucao = instrucoes.get(i);
            
            if (instrucao.contains("EQU")){
                
                String op_nome = instrucao.split("EQU")[0];
                
                String op_valor_str = instrucao.split("EQU")[1];
                op_valor_str = op_valor_str.replaceAll("\\s+","");
                
                //System.out.println("getRegistrador(\"DS\")" + getRegistrador("DS") +"   i = "+ i  );
                updateMemoria(op_valor_str, Integer.parseInt(getRegistrador("DS")) + i+1);
                
                tabela.add(new TabelaOperandos(op_nome,""+i,"VAR") );  
            } 
            
            else if (instrucao.contains("DW")){
                String op_nome = instrucao.split("DW")[0];
                
                String op_valor_str = instrucao.split("DW")[1];
                op_valor_str = op_valor_str.replaceAll("\\s+","");
                updateMemoria(op_valor_str, Integer.parseInt(getRegistrador("DS")) +i+1);
                tabela.add(new TabelaOperandos(op_nome,""+i,"VAR") ); 
            }
        } 
    }
    
    public static void tabela_get_operando(String opdRegex){
        for (int i =0; i< TelaPrincipal.tabela.size(); i++){
            TelaPrincipal.tabela.get(i).getName();
              
        } 
    }
    
    public static void print_tabela(List<TabelaOperandos> tabela){    
        
        for (int i = 0; i<tabela.size();i++){
            System.out.println(tabela.get(i).getName()+"-" + tabela.get(i).getPosicaoNaMemoria()+ "-" + tabela.get(i).getType() +" -" +i);
                
        }
    
    }

    public static int linha_atual(){
        Integer IP = Integer.parseInt(getRegistrador("IP")); 
        return IP;
    };

    public static String verifyIfOperandoExist(String opd){ // Se o operando existe, retorna endereço dele na memoria
        if(tabela == null){
            return "false";
        }
        for(int i = 0; i < tabela.size(); i++){
            if(opd.equals(tabela.get(i).getName())){
                return tabela.get(i).getPosicaoNaMemoria();
            }
        }
        return "false";
    }

    public static String verifyIfOperandoExist(String opd, String type){ // Se operando não existe, adiciona na tabela, se existe, retorna endereço
        if(tabela == null){
            TabelaOperandos newRow = new TabelaOperandos(opd, Integer.toString(dataSegmentControl), type);
            tabela.add(newRow);
            dataSegmentControl++;
            return Integer.toString(dataSegmentControl-1);
        }
        for(int i = 0; i < tabela.size(); i++){
            if(opd.equals(tabela.get(i).getName())){
                return tabela.get(i).getPosicaoNaMemoria();
            }
        }
        TabelaOperandos newRow = new TabelaOperandos(opd, Integer.toString(dataSegmentControl), type);
        tabela.add(newRow);
        dataSegmentControl++;
        return Integer.toString(dataSegmentControl-1);
    }
    
}
