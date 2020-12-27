package com.example.vs1;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBOpenHelper {
    //要连接的数据库url,服务器上的MySQl的地址
    private static String url = "jdbc:mysql://sh-cynosdbmysql-grp-clwrjfde.sql.tencentcdb.com:26019/patient_oxygen";
    //连接数据库使用的用户名
    private static String userName = "root";
    //连接的数据库时使用的密码
    private static String password = "159875321Cai";

    public static Connection getConn(){
        Connection connection = null;
        try{
            //1、加载驱动
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("驱动加载成功！");
            //2、获取与数据库的连接
            connection = (Connection) DriverManager.getConnection(url, userName, password);
            System.out.println("连接数据库成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeAll(Connection connection){
        if (connection != null){
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
