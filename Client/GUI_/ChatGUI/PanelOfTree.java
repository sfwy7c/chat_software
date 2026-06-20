package GUI_.ChatGUI;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


public class PanelOfTree extends JPanel implements TreeSelectionListener{//树组件的容器类
    DefaultMutableTreeNode chat_target;
    DefaultMutableTreeNode single_target;
    DefaultMutableTreeNode group_target;
    JTree tree;
    DefaultTreeModel tree_model;
    Window window;

    PanelOfTree(Window window){
        this.window=window;
        setLayout(new BorderLayout());
        initTree();
        this.tree_model=(DefaultTreeModel)this.tree.getModel();
        
        this.tree.addTreeSelectionListener(this);
    }

    private void initTree(){
        this.chat_target=new DefaultMutableTreeNode("chat_target");
        this.single_target=new DefaultMutableTreeNode("single_target");
        this.group_target=new DefaultMutableTreeNode("group_target");
        this.chat_target.add(this.single_target);
        this.chat_target.add(this.group_target);
        this.tree=new JTree(this.chat_target);
        add(this.tree);
    }




    public void addSingleTarget(String target_names){//向树中添加在线用户名
        target_names=target_names.replaceAll("\\[|\\]","");
        String[]name_arr=target_names.split(", ");
        
        this.single_target.removeAllChildren();
        
        for(String i:name_arr){
            this.single_target.add(new DefaultMutableTreeNode(i));
            
            if(this.window.getDict().get(i)==null){
                System.out.println("新增对话容器"+i);
                JPanel new_card=this.window.getPanelOfSplit().addNewCard(i);
                this.window.getDict().put(i,new_card);
                if(i.equals(this.window.getCon().getName())){//设置默认聊天对象
                    this.window.getPanelOfSplit().setCurrentCard(this.window.getDict().get(i),i);
                    this.window.getCon().setToName(i);
                }
                
            }
        }
        this.tree.revalidate();
        this.tree.repaint();
        this.tree_model.reload(this.chat_target);
    }


    public void addGroupTarget(String group_names){//向树中添加群聊
        this.group_target.add(new DefaultMutableTreeNode(group_names));
        JPanel new_card=this.window.getPanelOfSplit().addNewCard(group_names);
        this.window.getDict().put(group_names,new_card);//向在线用户字典中添加群聊名与分页容器的对象实例组成的键值对，用于切换容器和接收信息

        this.tree.revalidate();
        this.tree.repaint();
        this.tree_model.reload(this.chat_target);
    }


    public void valueChanged(TreeSelectionEvent event){
        
        DefaultMutableTreeNode node=(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        //System.out.println("****"+node+"****"+event.getSource()+"****");
        if(node!=null && node.isLeaf()&&node!=this.group_target&&node!=this.single_target){
            this.window.getCon().setToName(node+"");
            //System.out.println("选中了"+node);
            
            System.out.println("切换到了"+node);
            this.window.getPanelOfSplit().setCurrentCard(this.window.getDict().get(node+""),node+"");
            
        }
    }
    
}