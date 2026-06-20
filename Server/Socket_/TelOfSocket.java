package Socket_;
import Message.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;


class TelOfSocket{//通信类
    ObjectOutputStream ser_obj_out;
    ObjectInputStream ser_obj_in;
    Socket socket;
    String client_name;
    AcceptConnection server;
    ConcurrentLinkedQueue<Message> mes_queue;

    TelOfSocket(Socket socket,AcceptConnection server){//构造方法
        this.server=server;
        this.socket=socket;
        initSerializedObjStream();
    }

    private void initSerializedObjStream(){//初始化数据流
        try{
            this.ser_obj_out=new ObjectOutputStream(socket.getOutputStream());
            this.ser_obj_in=new ObjectInputStream(socket.getInputStream());
        }catch(IOException e){
            System.out.println(e);
        }
    }

    private void startMes(){//接收初始信息并验证

        try{
            while(true){
                Message mes=(Message)ser_obj_in.readObject();//接收用户的初始信息
                if(mes!=null&&mes.getType().equals("login_mes")){//判断消息类型是否为登录类型
                    Message login_res=this.server.getDbCon().logIn(mes.getName(),mes.getMes());
                    if(login_res.getMes().equals("OK")){//调用数据库连接类里的logIn方法进行验证
                        this.ser_obj_out.writeObject(login_res);//发送登录成功的信息
                        this.client_name=mes.getName();//为用户赋予用户名
                        this.server.getClientList().add(this.client_name);//将用户名添加至列表
                        this.mes_queue=new ConcurrentLinkedQueue<>();
                        this.server.getDictForQueue().put(this.client_name,this.mes_queue);
                        break;//退出循环
                    }else{
                        this.ser_obj_out.writeObject(login_res);
                    }
                    
                }else if(mes!=null&&mes.getType().equals("signup_mes")){
                    this.server.getDbCon().signUp(mes.getName(),mes.getMes());
                    this.client_name=mes.getName();
                    this.server.getClientList().add(this.client_name);//将用户名添加至列表
                    this.mes_queue=new ConcurrentLinkedQueue<>();
                    this.server.getDictForQueue().put(this.client_name,this.mes_queue);
                    break;
                }
            }
            
        }catch(IOException e){
            try{
                this.socket.close();
            }catch(IOException e_){
                System.out.println(e_+"6");
            }
            System.out.println(e);
        }catch(ClassNotFoundException e){
            System.out.println(e);
        }
    }

    public ObjectInputStream getObjectInputStream(){
        return this.ser_obj_in;
    }

    public ObjectOutputStream getObjectOutputStream(){
        return this.ser_obj_out;
    }

    public String getClientName(){
        return this.client_name;
    }

    public Socket getSocket(){
        return this.socket;
    }

    public AcceptConnection getServer(){
        return this.server;
    }

    public ConcurrentLinkedQueue<Message> getMesQueue(){
        return this.mes_queue;
    }

    public void createDialog(){//创建接收与转发线程并调用startMes
        startMes();
        Thread send_thread=new Thread(new Send(this));
        Thread recv_thread=new Thread(new Recv(this));
        recv_thread.start();
        send_thread.start();
        
        
    }

}
