package com.example.vs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Connection connection = null;
    Button login;
    Button reg;
    EditText nameText;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        reg = findViewById(R.id.reg);
        nameText = findViewById(R.id.name_login);
        login.setOnClickListener(this);
        reg.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String name = nameText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                            if (!nameText.equals("")) {//判断输入框中是否有数据
                                connection = DBOpenHelper.getConn();
                                if (DBOpenHelper.getExit(connection, "patient_name", name)==1) {
                                    connection = DBOpenHelper.getConn();
                                    //获取id
                                    String sql = "SELECT patient_id FROM patient WHERE patient_name='" + name + "'";
                                    rs = DBOpenHelper.getQuery(connection, sql);
                                    String id = rs.getString("patient_id");
                                    //传递id到下一页
                                    Intent intent = new Intent(MainActivity.this, ShowData.class);
                                    intent.putExtra("idText", id);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "请先注册", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Name不能为空", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        DBOpenHelper.closeAll(connection);
                        Looper.loop();
                    }
                }).start();
                break;
            case R.id.reg:
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}