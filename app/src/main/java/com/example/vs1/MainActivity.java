package com.example.vs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Connection connection = null;
    Button login;
    Button reg;
    EditText idText;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        reg = findViewById(R.id.reg);
        idText = findViewById(R.id.id);
        login.setOnClickListener(this);
        reg.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Editable id = idText.getText();
                String idText = id.toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                            String sql = "SELECT patient_id FROM patient WHERE patient_id=?";
                            if (!idText.equals("")) {
                                connection = DBOpenHelper.getConn();
                                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                                ps.setString(1, idText);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    //传递id到下一页
                                    Intent intent = new Intent(MainActivity.this, ShowData.class);
                                    intent.putExtra("idText", idText);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "请先注册", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "id不能为空", Toast.LENGTH_SHORT).show();
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
                Intent intent1 = new Intent(MainActivity.this, Register.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}