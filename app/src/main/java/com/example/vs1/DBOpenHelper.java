package com.example.vs1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
//判断数据库中是否已经存在对应值
    public static int getExit(Connection connection ,String patientType, String patientInfo){
        ResultSet rs;
        try {
            String sql = "SELECT "+patientType+" FROM patient WHERE "+patientType+"=?";
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
            ps.setString(1,patientInfo);
            rs = ps.executeQuery();
            if (rs.next()) {
                return 1;
            } else {
                return 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ResultSet getQuery(Connection connection, String sql) {
        ResultSet rs;
        try {
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                return rs;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
