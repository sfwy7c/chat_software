package GUI_.ChatGUI;
import Message.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class SendListener implements KeyListener{//为发送文本区写的键盘监听器
    int warning_time;
    File dir;
    File [] file_arr;
    Window window;

    public SendListener(Window window){//构造方法，传入数据输出流和分割容器的容器
        this.window=window;
        initFile();
    }

    public void keyPressed(KeyEvent event){
        if(event.isControlDown()&&event.getKeyCode()==KeyEvent.VK_ENTER){//监听ctrl和enter的热键，换行
            //System.out.println("换行");
            this.window.getPanelOfSplit().appendSendingMes("\n");
        }else if(event.getKeyCode()==KeyEvent.VK_ENTER&&!event.isControlDown()){//监听enter键，发送消息
            if(!this.window.getCon().getToName().contains("的群聊")){
                sendText("text");//发送文本信息
            }else{
                sendText("group_text");
            }
            
            
        }
    }

    public void keyTyped(KeyEvent event){

    }

    public void keyReleased(KeyEvent event){
       
    }

    private void sendText(String type){
        try{
            String mes=this.window.getPanelOfSplit().getSendingMes();//获取发送文本区的内容
            if(miganciMatch(mes)!=1){
                this.window.getCon().getObjectOutputStream().writeObject(new Message(mes,this.window.getCon().getName(),this.window.getCon().getToName(),type));
                this.window.getPanelOfSplit().appendSendedMes(mes+"<<<"+this.window.getCon().getName());//将发送的信息显示到接收文本区
                this.window.getPanelOfSplit().clearSendingMes();//清空发送文本区
            }else{
                this.window.getPanelOfSplit().setWarningMes();
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }


    private void initFile(){
        this.dir=new File(".\\statics\\敏感词库\\Vocabulary");
        this.file_arr=dir.listFiles();
    }

    private int miganciMatch(String sen){
        for(File i:file_arr){
            //System.out.println(i.getName());
            try{
                BufferedReader buffer_in=new BufferedReader(new FileReader(i));
                while(true){
                    String st=buffer_in.readLine();
                    //System.out.println("+++"+st+"+++");
                    if(st==null){
                        break;
                    }
                    if(sen.contains(st)){
                        //System.out.println(sen+"==="+st);
                        //System.out.println("含有敏感词1");
                        return 1;
                    }
                    //System.out.println(st);
                }

            }catch(FileNotFoundException e){
                System.out.println(e);
            }catch(IOException e){
                System.out.println(e);
            }
        }
        return 0;
    }

}
