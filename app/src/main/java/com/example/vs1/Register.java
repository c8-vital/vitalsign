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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Register extends AppCompatActivity {
    Connection connection = null;
    Button register;
    EditText nameText;
    TextView id;
    EditText genderText;
    EditText ageText;
    ResultSet rs;
    ResultSet rs1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.register);
        id = findViewById(R.id.id_show);
        nameText = findViewById(R.id.name_new);
        genderText = findViewById(R.id.gender_new);
        ageText = findViewById(R.id.age_new);
        register.setOnClickListener(v -> new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String name = nameText.getText().toString();
                String gender = genderText.getText().toString();
                String age = ageText.getText().toString();
                try {
                    connection = DBOpenHelper.getConn();
                    if (DBOpenHelper.getExit(connection,"patient_name", name)==1){
                        Toast.makeText(Register.this, "该姓名已存在", Toast.LENGTH_LONG).show();
                    } else {
                        String sql1 = "INSERT INTO patient ( patient_id, patient_name, patient_gender, patient_age)\n" +
                                "VALUES\n" +
                                "( null, ?, ?, ?)";
                        PreparedStatement ps1 = (PreparedStatement) connection.prepareStatement(sql1);
                        ps1.setString(1, name);
                        ps1.setString(2, gender);
                        ps1.setString(3, age);
                        ps1.executeUpdate();
                        //sql添加数据语句
                        if (DBOpenHelper.getExit(connection, "patient_name", name)==1) {
                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_LONG).show();
                            String sql = "SELECT patient_id FROM patient WHERE patient_name='" + name + "'";
                            rs1 = DBOpenHelper.getQuery(connection, sql);
                            id.setText(rs1.getString("patient_id"));
                        } else {
                            Toast.makeText(Register.this, "注册失败", Toast.LENGTH_LONG).show();
                        }
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