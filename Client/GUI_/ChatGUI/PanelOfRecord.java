package GUI_.ChatGUI;
import Message.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


public class PanelOfRecord extends JPanel implements ActionListener,FocusListener{
    JButton text_record_button;
    JTabbedPane tabbed_for_record;

    JPanel panel_for_text_record;
    JTextField record_text_text;
    JTextField record_text_to_name;
    JPanel show_text_record;
    JScrollPane scroll_show_text_record;
    JTextField record_id_for_delete;
    JButton button_for_delete;

    Window window;
    

    public PanelOfRecord(Window window){//构造方法
        this.window=window;

        setLayout(new BorderLayout());
        this.tabbed_for_record=new JTabbedPane();
        add(this.tabbed_for_record);

        initPanelForTextRecord();
        
    }


    private void initPanelForTextRecord(){
        this.panel_for_text_record=new JPanel();//文本查询的容器
        this.panel_for_text_record.setLayout(new BorderLayout());

        this.tabbed_for_record.add("文本查询",this.panel_for_text_record);//文本查询容器加入选项卡容器

        this.record_text_text=new JTextField("输入查询文本");//查询文本输入区
        this.record_text_to_name=new JTextField("输入查询对象");//查询对象输入区
        this.record_id_for_delete=new JTextField("输入要删除消息的id");//删除记录输入区

        this.text_record_button=new JButton("查询");//查询按钮
        this.button_for_delete=new JButton("删除");//删除按钮

        this.show_text_record=new JPanel();//显示查询结果面板
        this.show_text_record.setLayout(new BoxLayout(this.show_text_record,BoxLayout.Y_AXIS));//显示结果的面板为垂直布局
        this.scroll_show_text_record=new JScrollPane(this.show_text_record);//查询结果面板的滚动容器

        Box box=Box.createHorizontalBox();//容纳文本输入区和查询按钮的box
        box.add(this.record_text_to_name);
        box.add(this.record_text_text);
        box.add(this.text_record_button);


        box.add(this.record_id_for_delete);
        box.add(this.button_for_delete);


        this.panel_for_text_record.add(box,BorderLayout.NORTH);//将box添加至文本查询容器的北面
        this.panel_for_text_record.add(this.scroll_show_text_record,BorderLayout.CENTER);//将查询结果的滚动面板添加至文本查询容器中心

        //添加监听器
        this.text_record_button.addActionListener(this);
        this.record_text_text.addFocusListener(this);
        this.record_text_to_name.addFocusListener(this);
        this.button_for_delete.addActionListener(this);
        this.record_id_for_delete.addFocusListener(this);
    }




    public void showQueryRes(Message mes){//向查询结果容器中添加查询结果
        Box box=Box.createHorizontalBox();
        box.add(new JLabel(mes.getMes()));
        box.add(Box.createHorizontalGlue());
        this.show_text_record.add(box);
        this.show_text_record.revalidate();
        this.show_text_record.repaint();
    }




    public void actionPerformed(ActionEvent event){
        try{
            if(event.getSource()==this.text_record_button){
                this.show_text_record.removeAll();
                this.window.getCon().getObjectOutputStream().writeObject(new Message(this.record_text_text.getText(),this.window.getCon().getName(),this.record_text_to_name.getText(),"text_query"));
            }else if(event.getSource()==this.button_for_delete){
                if(this.record_id_for_delete.getText().matches("\\d+?")){
                    this.window.getCon().getObjectOutputStream().writeObject(new Message(this.record_id_for_delete.getText(),this.window.getCon().getName(),"","text_delete"));
                    this.record_id_for_delete.setText("删除成功");
                }else{
                    this.record_id_for_delete.setText("请输入整形id");
                }
                
            }
        }catch(IOException e){
            System.out.println(e);
        }
        
    }

    public void focusGained(FocusEvent event){
        if(event.getSource()==this.record_text_text && this.record_text_text.getText().equals("输入查询文本")){
            this.record_text_text.setText("");
        }else if(event.getSource()==this.record_text_to_name && this.record_text_to_name.getText().equals("输入查询对象")){
            this.record_text_to_name.setText("");
        }else if(event.getSource()==this.record_id_for_delete &&this.record_id_for_delete.getText().equals("输入要删除消息的id") || this.record_id_for_delete.getText().equals("请输入整形id") || this.record_id_for_delete.getText().equals("删除成功")){
            this.record_id_for_delete.setText("");
        }
    }

    public void focusLost(FocusEvent event){

    }

}
