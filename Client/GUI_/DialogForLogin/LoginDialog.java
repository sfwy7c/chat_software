package GUI_.DialogForLogin;
import GUI_.ChatGUI.*;
import Socket_.*;
import javax.swing.JOptionPane;

public class LoginDialog extends DialogForLogin {
    Window window;
    

    public LoginDialog(Choose choose,RequestForConnection con){
        super(choose,con);
        
    }
    
    public void setTitle(){
        this.title="登录";
    }


    public void setAccount(int option){
        if(option==JOptionPane.OK_OPTION){
            
            this.con.initAccount(this.uname.getText(),this.pwd.getText());//初始化账号的uname和pwd，但仅当验证通过后才真正有效
            
            if(this.con.loginMes()==1){//如果loginMes返回的验证结果是1，则登录成功，显示聊天界面，并关闭登录选项页面
                showWindow(this.uname.getText());
                startRecv();//启动接收线程
                this.choose.dispose();

            }else{
                JOptionPane.showConfirmDialog(choose,"密码或账号错误","登录失败",JOptionPane.OK_CANCEL_OPTION);
            }

        }else{
           
        }
    }

    public void showWindow(String title){
        this.window=new Window(title,this.con);
    }

    private void startRecv(){
        Thread recv_thread=new Thread(new Recv(this.window));
        recv_thread.start();
    }

}

