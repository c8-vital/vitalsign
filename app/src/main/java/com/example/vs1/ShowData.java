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

public class ShowData extends AppCompatActivity {

    Connection connection = null;
    TextView tem;
    TextView oxi;
    Button query;
    TextView showid;
    private ResultSet rs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        query = findViewById(R.id.query);
        tem = findViewById(R.id.tem);
        oxi = findViewById(R.id.oxi);
        showid = findViewById(R.id.showid);
        //接收MainActivity的id
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        showid.setText(id);
        query.setOnClickListener(v -> new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    //sql添加数据语句
                    String sql = "SELECT * FROM `"+id+"`";
                    if (!id.equals("")) {//判断输入框是否有数据
                        //4.获取用于向数据库发送sql语句的ps
                        connection = DBOpenHelper.getConn();
                        PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
//                        ps.setString(1, id);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            tem.setText(rs.getString("tem"));
                            oxi.setText(rs.getString("oxi"));
                        }
                    } else {
                        Toast.makeText(ShowData.this, "id不能为空", Toast.LENGTH_SHORT).show();
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