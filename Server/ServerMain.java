import Db.*;
import Socket_.*;


public class ServerMain {
    public static void main(String[]args){
        DbCon db_con=new DbCon("127.0.0.1",3306,"root","root");

        AcceptConnection socket_of_server=new AcceptConnection(db_con);
        Thread server=new Thread(socket_of_server);
        server.start();
    }    
}


