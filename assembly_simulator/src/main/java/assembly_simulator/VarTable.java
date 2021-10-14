/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Emulador;
import java.util.ArrayList;

/**
 *
 * @author renafs
 */

class LinhaTable {
    String name;
    ArrayList<Integer> reference;
    Boolean status;

    public LinhaTable(String nome, Boolean status, int reference) {
        this.name = nome;
        this.reference = new ArrayList<Integer>();
        this.status = status;
    }

    public void addReference(int newReference) {
        this.reference.add(newReference); 
    }
}

class VarTable {
    private ArrayList<LinhaTable> tabela;
    private int lineCounter;

    public VarTable() {
        this.tabela = new ArrayList<LinhaTable>();
        this.lineCounter = 0;
    }

    public boolean hasItemName(String varName) {
        if(this.tabela.size() == 0) {
            return false;
        }

        for(int i = 0; i < this.tabela.size(); i++) {
            if(this.tabela.get(i).name == varName)
                return true;
        }
        return false;
    }

    public void addVar(String nome, Boolean status, int reference) {
        this.tabela.add(new LinhaTable(nome, status, reference));
        this.lineCounter++;
    }

    public void checkVariable(String varName, int Step_Counter_memory) {

        if(this.tabela.size() == 0)
            this.addVar(varName, false, Step_Counter_memory);

        for(int i = 0; i < this.tabela.size(); i++) {
            if(this.hasItemName(varName)){
                if(this.tabela.get(0).status == false) {
                    this.tabela.get(i).addReference(Step_Counter_memory);
                } else if (this.tabela.get(i).status == true) {

                }

            } else {
                this.addVar(varName, false, Step_Counter_memory);
            }
        }
    }
}