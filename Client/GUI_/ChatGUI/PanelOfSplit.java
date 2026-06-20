package GUI_.ChatGUI;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;


public class PanelOfSplit extends JPanel implements MouseListener{//分割容器的容器类
    JSplitPane split_pane;//分割容器

    JPanel current_panel_for_recv;

    JPanel panel_for_send;
    JTabbedPane tabbed_pane;//选项卡容器
    JScrollPane scroll_pane_for_send;//用来显示将要发送的信息的滚动容器

    JTextArea text_area_for_send;

    JPanel card_panel;
    CardLayout card_layout;
    
    Window window;
    
    
    PanelOfSplit(Window window){
        this.window=window;
        setLayout(new BorderLayout());
        //initAreaForRecv();
        initAreaForSend();
        initCardPanel();
        initSplitPane();

        text_area_for_send.addKeyListener(new SendListener(this.window));
        text_area_for_send.addMouseListener(this);
    }



    
    private void initSplitPane(){//初始化分割容器
        this.split_pane=new JSplitPane(JSplitPane.VERTICAL_SPLIT,this.card_panel,this.scroll_pane_for_send);//竖直分割容器，上为接收文本区，下为选项卡
        add(this.split_pane);
        //this.split_pane.setDividerLocation(0.7);
    }




    private void initCardPanel(){
        this.card_panel=new JPanel();
        this.card_layout=new CardLayout();
        this.card_panel.setLayout(this.card_layout);
        //this.card_panel.add(this.current_scroll_pane_for_recv,"qwq");
        this.card_layout.show(this.card_panel,"qwq");

    }



    public JPanel addNewCard(String name){
        JPanel panel_for_recv=new JPanel();
        panel_for_recv.setLayout(new BoxLayout(panel_for_recv,BoxLayout.Y_AXIS));//接收区的容器，垂直布局

        JScrollPane scroll_pane_for_recv=new JScrollPane(panel_for_recv);//接收区的滚动容器    
        panel_for_recv.setMinimumSize(new Dimension(0,300));//设置最小尺寸
        scroll_pane_for_recv.setMinimumSize(new Dimension(0,300));
        scroll_pane_for_recv.setPreferredSize(new Dimension(0,300));
        
        this.card_panel.add(scroll_pane_for_recv,name);
        return panel_for_recv;
    }



    public void setCurrentCard(JPanel current_panel_for_recv,String name){
        this.current_panel_for_recv=current_panel_for_recv;
        this.card_layout.show(this.card_panel,name);
        this.split_pane.revalidate();
        this.split_pane.repaint();
    }





    private void initAreaForSend(){//初始化发送区域
        
        this.text_area_for_send=new JTextArea();//发送文本区
        this.text_area_for_send.append("\n");
        text_area_for_send.setLineWrap(true);//设置自动换行，防止横向滚动
        
        this.panel_for_send=new JPanel();//发送区的容器
        this.panel_for_send.setLayout(new BorderLayout());
        this.scroll_pane_for_send=new JScrollPane(this.panel_for_send);//发送区的滚动容器

        this.tabbed_pane=new JTabbedPane();//选项卡容器
        this.tabbed_pane.add("发送文本",this.text_area_for_send);//将发送区容器加入选项卡

        this.panel_for_send.add(this.tabbed_pane);
        
    }




    public String getSendingMes(){//获取发送文本区的文本
        return this.text_area_for_send.getText();
    }



    public void appendSendingMes(String mes){//主要用于向文本区添加换行
        this.text_area_for_send.append(mes);
    }
    



    public void appendSendedMes(String mes){//将发出的信息也显示到接收文本区
        Box h_box=Box.createHorizontalBox();//创建水平box
        h_box.add(Box.createHorizontalGlue());//先添加水平弹性box，使信息右对齐
        
        JLabel mes_label=new JLabel(mes);
        mes_label.setBackground(new Color(200,100,100));
        h_box.add(mes_label);//将信息以标签的形式添加到接收区容器
        this.current_panel_for_recv.add(h_box);
        //this.current_panel_for_recv.add(Box.createHorizontalBox());//设置空行分隔
        this.current_panel_for_recv.revalidate();
        this.current_panel_for_recv.repaint();

    }

     


    public void appendRecvedMes(String mes,String name){//将接收到的文本显示到接收文本区
        Box h_box=Box.createHorizontalBox();//创建水平box
        
        h_box.add(new JLabel(mes));
        h_box.add(Box.createHorizontalGlue());//后添加水平弹性box，使消息左对齐
        this.window.getDict().get(name).add(h_box);
        //this.current_panel_for_recv.add(Box.createHorizontalBox());
        this.window.getDict().get(name).revalidate();
        this.window.getDict().get(name).repaint();
    }




    public void clearSendingMes(){//发送信息后清空发送文本区
        this.text_area_for_send.setText(null);
        this.text_area_for_send.setCaretPosition(0);
    }

    public void setWarningMes(){
        this.text_area_for_send.setText("发送的信息含有敏感词");
    }




    public void mousePressed(MouseEvent event){
        
    }

    public void mouseReleased(MouseEvent event){

    }

    public void mouseClicked(MouseEvent event){
        if(event.getSource()==this.text_area_for_send && this.text_area_for_send.getText().contains("发送的信息含有敏感词")){
            this.text_area_for_send.setText("");
        }
    }

    public void mouseEntered(MouseEvent event){

    }

    public void mouseExited(MouseEvent event){

    }

}