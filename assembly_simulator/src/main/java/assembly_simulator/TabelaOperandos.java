package assembly_simulator;

public class TabelaOperandos {
    private String name;
    private String value;
    private String type;
    private String tamanho;

    public TabelaOperandos(String name, String value, String type){
        this.name = name;
        this.value = value;
        this.type = type;
        
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        if(type.equals("Const")){
            this.value = value;
        }else{
            System.out.println("Not permited chenges");
        }
       
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}

    
