package Socket_;
import Db.*;
import Message.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;


public class AcceptConnection implements Runnable{ //服务端用来接收socket连接的类
    ServerSocket server_socket;
    Map<String,Message> dict;//存放即将发送的消息的字典，toname-mes
    Map<String,ConcurrentLinkedQueue<Message>> dict_for_queue;//存放用户名与其缓冲队列的字典，name-queue
    List<String> client_list;//存放在线用户的用户名的列表
    DbCon db_con;//数据库连接
    
    public AcceptConnection(DbCon db_con){//构造方法
        this.db_con=db_con;
        initSocket();
        initDictAndList();
    }

    private void initDictAndList(){//初始化字典和列表
        this.dict=new ConcurrentHashMap<>(200);
        this.dict_for_queue=new ConcurrentHashMap<>(200);
        this.client_list=new CopyOnWriteArrayList<>();
    }
    
    private void initSocket(){//初始化服务端套接字
        try{
            this.server_socket=new ServerSocket(2025,50,InetAddress.getByName("0.0.0.0"));
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    private void acceptConnection(){//接收客户端连接的方法
        try{
            TelOfSocket tel=new TelOfSocket(server_socket.accept(),this);//接收到连接后启动TelOfSocket对应的线程，进行通信
            
            tel.createDialog();//调用tel的createDialog，启动服务端与对应客户端的发送线程，并在该方法里调用tel的startMes方法，接收用户的与登录相关的初始信息并验证，只有通过验证后，服务端才会在createDialog中启动接收线程并继续向下执行

            //将当前连接的客户端信息发送给所有客户端
            sendAllClient();
        }catch(IOException e){
            System.out.println(e);
        }
    }



    public void sendAllClient(){//向所有用户发送当前在线客户的用户名的方法
        if(!this.client_list.isEmpty()){
            String name_arr[]=client_list.toArray(new String[client_list.size()]);
            System.out.println("当前在线的客户："+Arrays.toString(name_arr).replaceAll("\\[|\\]",""));
            for(String i:name_arr){
                this.dict.put(i,new Message(Arrays.toString(name_arr),"",i,"all_client"));
            }
        }
    }



    public List<String> getClientList(){
        return this.client_list;
    }

    public Map<String,Message> getDict(){
        return this.dict;
    }

    public DbCon getDbCon(){
        return this.db_con;
    }
    
    public Map<String,ConcurrentLinkedQueue<Message>> getDictForQueue(){
        return this.dict_for_queue;
    }


    public void run(){//线程，不断调用acceptConnection，等待客户端连接，达到连接多个客户端的目的
        while(true){
            acceptConnection();
        }
    }
}









