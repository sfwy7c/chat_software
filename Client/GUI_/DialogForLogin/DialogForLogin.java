package GUI_.DialogForLogin;
import Socket_.*;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class DialogForLogin extends JPanel{
    JTextField pwd;
    JTextField uname;
    RequestForConnection con;
    String title;
    Choose choose;

    public DialogForLogin(Choose choose,RequestForConnection con){
        this.con=con;
        this.choose=choose;
        setLayout(new GridLayout(2,2));
        initComponents();
        setTitle();
        int option=JOptionPane.showConfirmDialog(choose,this,this.title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        setAccount(option);
    }

    public void initComponents(){
        this.pwd=new JTextField(12);
        this.uname=new JTextField(12);
        add(new JLabel("用户名:"));
        add(this.uname);
        add(new JLabel("密码:"));
        add(this.pwd);
    }

    abstract public void setAccount(int option);

    public abstract void setTitle();
    
}
