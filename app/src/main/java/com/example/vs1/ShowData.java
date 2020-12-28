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
    TextView pul;
    Button query;
    TextView showid;
    TextView time;
    TextView name;
    private ResultSet rs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        query = findViewById(R.id.query);
        tem = findViewById(R.id.tem);
        oxi = findViewById(R.id.oxi);
        pul = findViewById(R.id.pul);
        showid = findViewById(R.id.showid);
        time = findViewById(R.id.time);
        name = findViewById(R.id.name);
        //接收MainActivity的idText
        Intent intent = getIntent();
        String idText = intent.getStringExtra("idText");
        showid.setText(idText);
        query.setOnClickListener(v -> new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    //sql添加数据语句
                    String sql = "SELECT * FROM `patient_"+idText+"` a, patient b WHERE a.p_id = b.patient_id";
                    if (!idText.equals("")) {//判断输入框是否有数据
                        //4.获取用于向数据库发送sql语句的ps
                        connection = DBOpenHelper.getConn();
                        PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            //显示数据
                            tem.setText(rs.getString("temperature"));
                            oxi.setText(rs.getString("oxygen_content"));
                            pul.setText(rs.getString("pulse"));
                            time.setText(rs.getString("time"));
                            name.setText(rs.getString("patient_name"));
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

    //设置右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //菜单点击内容
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