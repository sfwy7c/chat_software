package Socket_;
import Message.*;
import java.io.IOException;


public class Send implements Runnable{//服务端的转发类
    TelOfSocket tel;

    Send(TelOfSocket tel){//构造方法
        this.tel=tel;
    }

    private void send(){//转发发法
        while(true){
            try{
                Message mes=this.tel.getServer().getDict().get(this.tel.getClientName());//从消息字典中取出对应的消息对象实例
                if(mes!=null){
                    //System.out.println("已发送"+mes.getMes()+" from "+mes.getName()+" to "+mes.getToName());
                    this.tel.getObjectOutputStream().writeObject(mes);
                    boolean status=this.tel.getServer().getDict().remove(this.tel.getClientName(),mes);//将已发送的信息从字典中删除
                    //System.out.println("从字典中删除？"+status);
                    if(!this.tel.getMesQueue().isEmpty()){//若发送完毕后缓冲队列不为空，则从缓冲队列中取出消息并放入字典
                        if(!this.tel.getServer().getDict().containsKey(this.tel.getClientName())){
                            Message mes_=this.tel.getMesQueue().poll();
                            this.tel.getServer().getDict().put(this.tel.getClientName(),mes_);
                        }
                    }
                }
            }catch(IOException e){
                System.out.println(e+"1");
                try{
                    this.tel.getSocket().close();
                }catch(IOException e_){
                    System.out.println(e_+"4");
                }
                break;
            }catch(NullPointerException e){
                System.out.println(e+"8");
                break;
            }
        }
    }

    public void run(){
        send();
    }
}