package com.example.vs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Connection connection = null;
    Button register;
    EditText idText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register = findViewById(R.id.register);
        idText = findViewById(R.id.id);
        register.setOnClickListener(v -> new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String id = idText.getText().toString();
                try {
                    //sql添加数据语句
                    String sql = "CREATE TABLE if not exists `"+id+"` (\n" +
                            "  `time` int (10) NOT NULL,\n" +
                            "  `tem` double (3, 1) NULL,\n" +
                            "  `oxi` double (3, 1) NULL\n" +
                            ") ENGINE = innodb";
                    if (!id.equals("")) {//判断输入框是否有数据
                        //4.获取用于向数据库发送sql语句的ps
                        connection = DBOpenHelper.getConn();
                        PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                        ps.executeUpdate();
                        //传递id到下一页
                        Intent intent = new Intent(MainActivity.this, ShowData.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "id不能为空", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DBOpenHelper.closeAll(connection);
                Looper.loop();
            }
        }).start());
    }



}