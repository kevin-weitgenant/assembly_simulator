package assembly_simulator;

public class  VarAndConst.{
    private String name;
    private String value;
    private String type;

    public VarAndConst(String name, String value, String type){
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
