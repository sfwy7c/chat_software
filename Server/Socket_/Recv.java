package Socket_;
import Message.*;
import java.io.IOException;
import java.util.List;

public class Recv implements Runnable{//接收类
    TelOfSocket tel;

    Recv(TelOfSocket tel){//构造方法
        this.tel=tel;
    }

    private void recv(){//接收方法
        while(true){
            try{
                Message mes=(Message)this.tel.getObjectInputStream().readObject();
                if(mes.getType().equals("text")){//文本信息
                    this.tel.getServer().getDbCon().saveRecord(mes);

                    if(!this.tel.getServer().getDict().containsKey(mes.getToName())){//如果消息字典里没有发送目标的键则将其放入字典
                        this.tel.getServer().getDict().put(mes.getToName(),mes);//将接收到的文本信息放入字典
                    }else{//否则放入缓冲队列
                        this.tel.server.getDictForQueue().get(mes.getToName()).add(mes);
                    }
                    //System.out.println(mes.getMes());


                }else if(mes.getType().equals("text_query")){//查询信息
                    List<String> list=this.tel.getServer().getDbCon().queryRecord(mes);//调用queryRecord进行聊天记录查询，返回结果列表
                    for(String i:list){
                        if(!this.tel.getServer().getDict().containsKey(mes.getName())){//如果消息字典里没有发送目标的键则将其放入字典
                            this.tel.getServer().getDict().put(mes.getName(),new Message(i,mes.getName(),mes.getName(),"query_res"));//将列表中的元素放入发送字典
                        }else{//否则放入缓冲队列
                            this.tel.server.getDictForQueue().get(mes.getToName()).add(new Message(i,mes.getName(),mes.getName(),"query_res"));
                        }
                        
                        try{
                            Thread.sleep(100);
                        }catch(InterruptedException e){
                            System.out.println(e);
                        }
                    }


                }else if(mes.getType().equals("text_delete")){//删除记录的信息
                    this.tel.getServer().getDbCon().deleteRecord(mes.getMes());
                    System.out.println("id:"+mes.getMes()+"删除成功");


                }else if(mes.getType().equals("group_names")){//创建群聊信息
                    System.out.println(mes.getMes());
                    String mes_con=mes.getMes().replaceAll("的群聊","");
                    String []group_names=mes_con.split("与");
                    for(String i:group_names){
                        this.tel.getServer().getDict().put(i,new Message(mes.getMes(),"",i,"group_names"));
                    }


                }else if(mes.getType().equals("group_text")){//群聊文本信息
                    String names=mes.getToName().replaceAll("的群聊","");
                    String []names_arr=names.split("与");
                    for(String i:names_arr){
                        if(i.equals(mes.getName())){
                            continue;
                        }
                        this.tel.getServer().getDbCon().saveRecord(mes);
                        //System.out.println("给"+i+"发送群聊信息");
                        if(!this.tel.getServer().getDict().containsKey(i)){//如果消息字典里没有发送目标的键则将其放入字典
                            this.tel.getServer().getDict().put(i,mes);//将接收到的文本信息放入字典
                        }else{//否则放入缓冲队列
                            this.tel.server.getDictForQueue().get(i).add(mes);
                        }
                    }
                }
                
            }catch(ClassNotFoundException e){
                System.out.println(e+"3");
                break;
            }catch(IOException e){
                System.out.println(tel.getClientName()+"已下线");
                this.tel.getServer().getClientList().remove(this.tel.getClientName());//客户端断开连接后删除列表中该用户的用户名
                tel.getServer().sendAllClient();//调用sendAllClient向所有在线用户发送当前所有在线用户的用户名，更新客户端的在线客户列表
                try{
                    this.tel.getSocket().close();//关闭套接字
                }catch(IOException e_){
                    System.out.println(e_+"5");
                }
                
                break;
            }
        }
    }

    public void run(){
        recv();
    }
}