package assembly_simulator;

public class TabelaOperandos {
    private String name;
    private String PosicaoNaMemoria;
    private String type;
    

    public TabelaOperandos(String name, String value, String type){
        this.name = name;
        this.PosicaoNaMemoria = value;
        this.type = type;
        
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPosicaoNaMemoria(String value) {
        if(type.equals("Const")){
            this.PosicaoNaMemoria = value;
        }else{
            System.out.println("Not permited changes");
        }
       
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPosicaoNaMemoria() {
        return PosicaoNaMemoria;
    }
}

    
