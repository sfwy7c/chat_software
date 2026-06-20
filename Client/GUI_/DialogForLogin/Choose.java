package GUI_.DialogForLogin;
import Socket_.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Choose extends JFrame implements ActionListener{
    RequestForConnection con;
    JButton signup;
    JButton login;
    public Choose(RequestForConnection con){
        this.con=con;
        setLayout(new FlowLayout());
        setBounds(300,300,200,200);
        initComponents();
        initActionListener();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        
    }

    private void initComponents(){
        this.signup=new JButton("注册");
        this.login=new JButton("登录");
        add(this.login);
        add(this.signup);
    }

    private void initActionListener(){
        this.signup.addActionListener(this);
        this.login.addActionListener(this);
    }

    
    public void actionPerformed(ActionEvent event){
        if(event.getSource()==this.login){
            LoginDialog login_dialog=new LoginDialog(this,this.con);
        }else if(event.getSource()==this.signup){
            SignupDialog signup_dialog=new SignupDialog(this,this.con);
        }
    }

}
