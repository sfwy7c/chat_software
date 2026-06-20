package GUI_.DialogForLogin;
import GUI_.ChatGUI.Window;
import Socket_.*;
import javax.swing.JOptionPane;

public class SignupDialog extends DialogForLogin{//注册对话框
    Window window;

    public SignupDialog(Choose choose,RequestForConnection con){
        super(choose,con);
    }
    
    public void setTitle(){
        this.title="注册";
    }

    public void setAccount(int option){
        if(option==JOptionPane.OK_OPTION){
            this.con.initAccount(this.uname.getText(),this.pwd.getText());
            this.con.signupMes();//调用signupMes发送注册类型的信息
            showWindow();
            startRecv();
            this.choose.dispose();
        }else{
            
        }
    }

    public void showWindow(){
        this.window=new Window(this.uname.getText(),this.con);
    }

    private void startRecv(){
        Thread recv_thread=new Thread(new Recv(this.window));
        recv_thread.start();
    }

}
