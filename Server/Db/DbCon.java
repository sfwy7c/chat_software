package Db;
import Message.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class DbCon {//数据库模块的类
    Connection con;
    PreparedStatement signup_statement;//注册预处理语句
    PreparedStatement login_statement;//登录预处理语句
    PreparedStatement save_record_statement;//存储聊天记录的预处理语句
    PreparedStatement query_record_statement;//查询聊天记录的预处理语句
    PreparedStatement delete_record_statement;//删除聊天记录的语句

    public DbCon(String ip,int port,String uname,String pwd){
        initCon(ip,port,uname,pwd);
        initStatement();
        //initTimeConfig();
    }

    private void initCon(String ip,int port,String uname,String pwd){//初始化数据库连接
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e);
        }

        String url="jdbc:mysql://"+ip+":"+port+"/database_qwq?useSSL=true&serverTimezone=Asia/Shanghai";
        try{
            this.con=DriverManager.getConnection(url,uname,pwd);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    /* 
    private void initTimeConfig(){
            SHOW VARIABLES LIKE 'wait_timeout';
            SHOW VARIABLES LIKE 'interactive_timeout';

            -- 修改超时时间（需要重启或全局设置）
            SET GLOBAL wait_timeout = 28800; -- 8小时
            SET GLOBAL interactive_timeout = 28800; 
            
        
        try{
            PreparedStatement time_config_statement=this.con.prepareStatement("SET GLOBAL wait_timeout = 28800;SET GLOBAL interactive_timeout = 28800;");
            time_config_statement.executeUpdate();

        }catch(SQLException e){
            System.out.println(e);
        }
    }
    */


    private void initStatement(){//初始化预处理语句
        try{
            this.signup_statement=this.con.prepareStatement("insert into clients values(null,?,?)");
            this.login_statement=this.con.prepareStatement("select * from clients where uname=?");
            this.save_record_statement=this.con.prepareStatement("INSERT INTO `messages` (`id`,`name`,`to_name`,`mes`,`type`,`datetime`) VALUES (NULL,?,?,?,?,CURRENT_TIMESTAMP);");
            this.query_record_statement=this.con.prepareStatement("select * from `messages` where `name`=? and `to_name`=? and `type`='text' or `name`=? and `to_name`=? and `type`='text'");
            this.delete_record_statement=this.con.prepareStatement("delete from `messages` where `id`=?");

        }catch(SQLException e){
            System.out.println(e);
        }
    }

    public void signUp(String name,String pwd){//注册语句执行方法
        try{
            this.signup_statement.setString(1,name);
            this.signup_statement.setString(2,pwd);
            int status=this.signup_statement.executeUpdate();
            if(status!=1){
                System.out.println("signup error");
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }


    public Message logIn(String name,String pwd){//登录语句执行方法
        ResultSet res=null;
        try{
            this.login_statement.setString(1,name);
            res=this.login_statement.executeQuery();
        
            if(res.next()){//如果用该用户名有查询结果，则比对密码，否则返回mes项为NO的登录结果信息
                String pwd_=res.getString("pwd");
                //System.out.println(pwd+"+++"+pwd_);
                if(pwd.equals(pwd_)){
                    
                    //密码正确返回mes项为OK的登录结果信息
                    return new Message("OK","",name,"login_res");
                }else{
                    return new Message("NO","",name,"login_res");
                }
            }else{
                return new Message("NO","",name,"login_res");
            } 
            
        }catch(SQLException e){
            System.out.println(e);
            return new Message("NO","",name,"login_res");
        }finally{
            try{
                if(res!=null){
                    res.close();
                }
            }catch(SQLException e){
                System.out.println(e);
            }
        }
    }


    public void saveRecord(Message mes){//存储聊天记录的方法
        try{
            this.save_record_statement.setString(1,mes.getName());
            this.save_record_statement.setString(2,mes.getToName());
            this.save_record_statement.setString(3,mes.getMes());
            this.save_record_statement.setString(4,mes.getType());
            int status=this.save_record_statement.executeUpdate();
            if(status!=1){
                System.out.println("save_record_error");
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }



    public List<String> queryRecord(Message mes){//查询聊天记录的方法，返回结果列表
        ResultSet res=null;
        
        String content;
        List<String> list=new ArrayList<>(200);
        //System.out.println("开始查询");
        try{
            this.query_record_statement.setString(1,mes.getName());
            this.query_record_statement.setString(2,mes.getToName());
            this.query_record_statement.setString(3,mes.getToName());
            this.query_record_statement.setString(4,mes.getName());
            res=this.query_record_statement.executeQuery();

            while(res.next()){
                content=res.getString("mes");
                //System.out.println(res.getString("id")+":"+content);
                if(content.contains(mes.getMes())){
                    //System.out.println(content);
                    list.add("id:"+res.getString("id")+"   "+res.getString("datetime")+"   from   "+mes.getName()+"   to   "+mes.getToName()+":   "+content);
                    
                }
            }
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            
            try{
                if(res!=null){
                    res.close();
                }
            }catch(SQLException e){
                System.out.println(e);
            }
        }
        return list;
    }




    public void deleteRecord(String id){//删除聊天记录的方法
        try{
            this.delete_record_statement.setInt(1,Integer.parseInt(id));
            this.delete_record_statement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e);
        }

    }
    
}
