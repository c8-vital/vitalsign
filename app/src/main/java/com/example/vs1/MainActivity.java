package com.example.vs1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    //要连接的数据库url,注意：此处连接的应该是服务器上的MySQl的地址
    String url = "jdbc:mysql://sh-cynosdbmysql-grp-clwrjfde.sql.tencentcdb.com:26019/database";
    //连接数据库使用的用户名
    String userName = "root";
    //连接的数据库时使用的密码
    String password = "159875321Cai";
    Connection connection = null;
    TextView tem;
    TextView oxi;
    Button query;
    EditText idText;
    private ResultSet rs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        query = findViewById(R.id.query);
        tem = findViewById(R.id.tem);
        oxi = findViewById(R.id.oxi);
        idText = findViewById(R.id.id);
        query.setOnClickListener(v -> new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String id = idText.getText().toString();
                try {
                    //1、加载驱动
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    System.out.println("驱动加载成功！");
                    //2、获取与数据库的连接
                    connection = (Connection) DriverManager.getConnection(url, userName, password);
                    System.out.println("连接数据库成功！");
                    //3.sql添加数据语句
                    String sql = "SELECT * FROM vital WHERE id=?";
                    if (!id.equals("")) {//判断输入框是否有数据
                        //4.获取用于向数据库发送sql语句的ps
                        PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                        ps.setString(1, id);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            tem.setText(rs.getString("tem"));
                            oxi.setText(rs.getString("oxi"));
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this,"id不能为空",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (connection!=null){
                        try {
                            connection.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                Looper.loop();
            }
        }).start());
    }



}