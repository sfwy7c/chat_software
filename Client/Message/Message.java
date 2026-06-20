package Message;
import java.io.Serializable;


public class Message implements Serializable{
    private static final long serialVersionUID=1234567890;
    public Message(String mes,String name,String to_name,String type){
        this.name=name;
        this.to_name=to_name;
        this.type=type;
        this.mes=mes;
    }
    public Message(){
        
    }
    String name;
    String to_name;
    String type;
    String mes;

    public String getToName(){
        return this.to_name;
    }
    public String getType(){
        return this.type;
    }
    public String getName(){
        return this.name;
    }
    public String getMes(){
        return this.mes;
    }
}
