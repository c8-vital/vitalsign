package com.example.vs1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ShowData extends AppCompatActivity {

    Connection connection = null;
    TextView tem;
    TextView oxi;
    Button query;
    TextView showid;
    private ResultSet rs = null;
//    private String idText = getIntent().getStringExtra("id");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        query = findViewById(R.id.query);
        tem = findViewById(R.id.tem);
        oxi = findViewById(R.id.oxi);
        showid = findViewById(R.id.showid);
        //接收MainActivity的id
        Intent intent1 = getIntent();
        String idText = intent1.getStringExtra("idText");
        showid.setText(idText);
        query.setOnClickListener(v -> new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    //sql添加数据语句
                    String sql = "SELECT * FROM `patient_"+idText+"`";
                    if (!idText.equals("")) {//判断输入框是否有数据
                        //4.获取用于向数据库发送sql语句的ps
                        connection = DBOpenHelper.getConn();
                        PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            tem.setText(rs.getString("temperature"));
                            oxi.setText(rs.getString("oxygen_content"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.history:
                Intent intent1 = getIntent();
                String idText = intent1.getStringExtra("idText");
                Intent intent2 = new Intent(ShowData.this, HistoryData.class);
                intent2.putExtra("idText", idText);
                startActivity(intent2);
            break;
            default:
        }
        return true;
    }
}