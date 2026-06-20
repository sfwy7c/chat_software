package Socket_;
import GUI_.ChatGUI.*;
import Message.*;
import java.io.IOException;

public class Recv implements Runnable{//接收线程
    Window window;

    public Recv(Window window){//构造方法，传入数据输入流和分割容器的容器
        this.window=window;
    }

    private void recv(){//接收方法
        while(true){
            try{
                Message mes=(Message)this.window.getCon().getObjectInputStream().readObject();
                if(mes.getType().equals("text")){
                    window.getPanelOfSplit().appendRecvedMes(mes.getName()+">>>"+mes.getMes(),mes.getName());//将接收到的文本信息显示到接收文本区
                }else if(mes.getType().equals("all_client")){//接收服务器发送的有关当前在线用户的消息，并更新树组件
                    this.window.getPanelOfTree().addSingleTarget(mes.getMes());
                }else if(mes.getType().equals("query_res")){//接收服务器发送的查询结果消息并显示到对应容器里
                    this.window.getPanelOfRecord().showQueryRes(mes);
                }else if(mes.getType().equals("group_names")){//接收服务器发送的建群消息并更新树组件
                    this.window.getPanelOfTree().addGroupTarget(mes.getMes());
                }else if(mes.getType().equals("group_text")){//接收服务器发送的群聊消息并显示到对应容器
                    window.getPanelOfSplit().appendRecvedMes(mes.getName()+">>>"+mes.getMes(),mes.getToName());
                }
                //System.out.println(mes.getMes());
            }catch(IOException e){
                System.out.println(e);
            }catch(ClassNotFoundException e){
                System.out.println(e);
            }
        }
    }

    public void run(){
        recv();
    }
}
