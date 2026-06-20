import GUI_.DialogForLogin.*;
import Socket_.RequestForConnection;


public class ClientMain{
    public static void main(String[]args){
        
        RequestForConnection con=new RequestForConnection("127.0.0.1",2025);

        Choose choose=new Choose(con);
        
    }
}


