package com.example.vs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
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
                Editable id = idText.getText();
                String idText = id.toString();
                try {
                    //sql添加数据语句
                    String sql = "INSERT IGNORE INTO `patient` VALUES ("+id+", 'name', 'male', '20', 'suzhou')";
                    if (!id.equals("")) {//判断输入框是否有数据
                        //4.获取用于向数据库发送sql语句的ps
                        connection = DBOpenHelper.getConn();
                        PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                        ps.executeUpdate();
                        //传递id到下一页
                        Intent intent1 = new Intent(MainActivity.this, ShowData.class);
                        intent1.putExtra("idText", idText);
                        startActivity(intent1);
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