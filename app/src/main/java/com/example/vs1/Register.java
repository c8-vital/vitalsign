package com.example.vs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Register extends AppCompatActivity {
    Connection connection = null;
    Button register;
    EditText nameText;
    EditText id;
    EditText genderText;
    EditText ageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.register);
        id = findViewById(R.id.id_new);
        nameText = findViewById(R.id.name_new);
        genderText = findViewById(R.id.gender_new);
        ageText = findViewById(R.id.age_new);
        register.setOnClickListener(v -> new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Editable idText = id.getText();
                String name = nameText.getText().toString();
                String gender = genderText.getText().toString();
                Editable age = ageText.getText();
                try {
                    //sql添加数据语句
                    String sql = "INSERT IGNORE INTO `patient` VALUES ("+idText+", '"+name+"', '"+gender+"', '"+age+"', 'suzhou')";
                    if (!idText.toString().equals("")) {//判断输入框是否有数据
                        //获取用于向数据库发送sql语句的ps
                        connection = DBOpenHelper.getConn();
                        PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                        ps.executeUpdate();
                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_LONG).show();
                        //传递id到下一页
//                        Intent intent = new Intent(Register.this, ShowData.class);
//                        intent.putExtra("idText", idText);
//                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(Register.this, "id不能为空", Toast.LENGTH_SHORT).show();
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