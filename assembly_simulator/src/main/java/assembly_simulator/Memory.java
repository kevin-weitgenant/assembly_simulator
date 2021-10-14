/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Emulador;

/**
 *
 * @author renafs
 */
public class Memory{
    private short[] palavras = new short[8192];

    public Memory(){
        for(int i = 0; i < 8192; i++){
            this.palavras[i] = (short)0;
        }  
    }
    public void setPalavra(short data,int position){
        this.palavras[position] = data;
    }

    public short getPalavra(int position){
        return this.palavras[position];
    }
}