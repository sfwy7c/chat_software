package Socket_;
import Message.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RequestForConnection{//客户端创建socket并连接以及后续通信的类
    Socket socket;
    ObjectInputStream ser_obj_in;
    ObjectOutputStream ser_obj_out;
    String name,to_name,pwd;
    

    public RequestForConnection(String ip,int port){//构造方法
        initSocket(ip,port);
        initSerObjStream();
    }

    private void initSocket(String ip,int port){//初始化套接字
        try{
            this.socket=new Socket(ip,port);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    private void initSerObjStream(){//初始化数据流
        try{
            this.ser_obj_in=new ObjectInputStream(socket.getInputStream());
            this.ser_obj_out=new ObjectOutputStream(socket.getOutputStream());
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void initAccount(String uname,String pwd){//初始化啊用户名
        this.name=uname;
        this.pwd=pwd;
    }

    public void setToName(String to_name){
        this.to_name=to_name;
    }

    public String getName(){
        return this.name;
    }
    public String getToName(){
        return this.to_name;
    }

    public ObjectOutputStream getObjectOutputStream(){
        return this.ser_obj_out;
    }

    public ObjectInputStream getObjectInputStream(){
        return this.ser_obj_in;
    }

    public int loginMes(){//发送登录信息
        try{
            ser_obj_out.writeObject(new Message(this.pwd,this.name,"","login_mes"));//发送登录类型的信息，服务器会将此信息与数据库的内容进行比对，并返回登录结果的信息
            Message mes=(Message)ser_obj_in.readObject();//接收登录结果信息（此时接收线程还未启动，调用对象输入流读取单条信息）
            if(mes.getMes().equals("OK")){//登录结果信息的mes项为OK则登录成功，返回1
                return 1;
            }else{
                return 0;
            }
        }catch(IOException e){
            System.out.println(e);
            return 0;
        }catch(ClassNotFoundException e){
            System.out.println(e);
            return 0;
        }
    }

    public void signupMes(){//发送注册信息
        try{
            ser_obj_out.writeObject(new Message(this.pwd,this.name,"","signup_mes"));
        }catch(IOException e){
            System.out.println(e);
        }
    }
 
    
}






