package GUI_.ChatGUI;
import Message.*;
import Socket_.RequestForConnection;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane; 

public class Window extends JFrame implements ActionListener{//窗口类
    JPanel base_panel;
    JPanel chat_panel;
    PanelOfRecord record_panel;

    PanelOfTree panel_of_tree;//树组件的容器
    PanelOfSplit panel_of_split;
    Map<String,JPanel> dict;
    CardLayout card_layout;
    JMenu panel_menu;
    JMenuBar menu_bar;
    JMenuItem chat_item;
    JMenuItem record_item;

    JMenu chat_groups;
    JMenuItem add_chat_groups;

    RequestForConnection con;


    public Window(String title,RequestForConnection con){//构造方法
        this.con=con;

        this.dict=new HashMap<>(100);
        setTitle(title);

        this.base_panel=new JPanel();
        this.card_layout=new CardLayout();
        this.base_panel.setLayout(this.card_layout);
        add(this.base_panel);

        setBounds(100,100,800,600);
        initChatPanel();
        initRecordPanel();
        initMenu();
        initMenuListener();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initChatPanel(){//初始化聊天容器
        this.chat_panel=new JPanel();
        this.chat_panel.setLayout(new BorderLayout());
        this.panel_of_tree=new PanelOfTree(this);
        this.panel_of_split=new PanelOfSplit(this);
        this.chat_panel.add(this.panel_of_tree,BorderLayout.WEST);
        this.chat_panel.add(this.panel_of_split,BorderLayout.CENTER);
        this.panel_of_tree.setPreferredSize(new Dimension(200,600));
        this.base_panel.add(this.chat_panel,"chat_panel");
    }



    private void initRecordPanel(){//初始化查询容器
        this.record_panel=new PanelOfRecord(this);
        this.base_panel.add(this.record_panel,"record_panel");
    }


    private void initMenu(){
        this.panel_menu=new JMenu("切换界面");
        this.chat_item=new JMenuItem("切换到聊天界面");
        this.record_item=new JMenuItem("切换到查询聊天记录界面");

        this.chat_groups=new JMenu("群聊");
        this.add_chat_groups=new JMenuItem("添加群聊");

        this.menu_bar=new JMenuBar();
        this.menu_bar.add(this.panel_menu);
        this.menu_bar.add(this.chat_groups);

        this.panel_menu.add(this.chat_item);
        this.panel_menu.add(this.record_item);
        this.chat_groups.add(this.add_chat_groups);
        setJMenuBar(this.menu_bar);
    }


    private void initMenuListener(){
        this.chat_item.addActionListener(this);
        this.record_item.addActionListener(this);
        this.add_chat_groups.addActionListener(this);
    }


    public PanelOfSplit getPanelOfSplit(){//获取分割容器的容器
            return this.panel_of_split;
    }
    
    public PanelOfTree getPanelOfTree(){
        return this.panel_of_tree;
    }

    public PanelOfRecord getPanelOfRecord(){
        return this.record_panel;
    }

    public Map<String,JPanel> getDict(){
        return this.dict;
    }

    public RequestForConnection getCon(){
        return this.con;
    }




    public void actionPerformed(ActionEvent event){//为菜单写的ActionListener
        if(event.getSource()==this.chat_item){//点击切换到聊天界面
            this.card_layout.show(this.base_panel,"chat_panel");
        }else if(event.getSource()==this.record_item){//点击切换到聊天记录查询页面
            this.card_layout.show(this.base_panel,"record_panel");
        }else if(event.getSource()==this.add_chat_groups){//点击选择成员并创建群聊
            JPanel add_group_pane=new JPanel();
            add_group_pane.setLayout(new BoxLayout(add_group_pane,BoxLayout.Y_AXIS));//对话框里的容器为垂直布局
            JScrollPane add_group_scroll_pane=new JScrollPane(add_group_pane);//可滚动
            add_group_scroll_pane.setPreferredSize(new Dimension(300,400));//设置滚动容器大小
            List<JCheckBox> check_box_list=new ArrayList<>(); //checkbox对象实例的列表
            for(String name:dict.keySet()){//遍历聊天对象字典的键，也就是所有聊天对象的name
                Box box=Box.createHorizontalBox();
                if(name.equals(this.con.getName())){//checkbox选项不包括自己
                    continue;
                }
                JCheckBox check_box=new JCheckBox(name);
                box.add(check_box);
                box.add(Box.createHorizontalGlue());//checkbox左对齐
                add_group_pane.add(box);
                check_box_list.add(check_box);//将checkbox实例加入列表
            }
            int option=JOptionPane.showConfirmDialog(this,add_group_scroll_pane,"选择成员",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);//弹出对话框
            if(option==JOptionPane.OK_OPTION){
                String names=this.con.getName();//names的格式为xxx与xxx与xxx的群聊，第一个名字默认为自己的名字
                int num=0;
                for(JCheckBox i:check_box_list){//遍历checkbox列表，逐个判断是否被勾选
                    if(i.isSelected()){
                        System.out.println(i.getText()+"被选中");
                        names=names+"与"+i.getText();
                        num+=1;       
                    }
                }
                names=names+"的群聊";
                try{
                    if(num<2){
                        JOptionPane.showConfirmDialog(this,"至少选择两个人","创建群聊失败",JOptionPane.OK_CANCEL_OPTION);
                    }else{
                        con.getObjectOutputStream().writeObject(new Message(names,con.getName(),"","group_names"));//创建成功则向服务器发送建群消息，服务器会向所有names里包含的用户发送建群消息
                    }
                    
                }catch(IOException e){
                    System.out.println(e);
                }
            }
        }
    }


    


}








