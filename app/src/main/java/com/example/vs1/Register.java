package com.example.vs1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends AppCompatActivity {
    Connection connection = null;
    Button register;
    EditText nameText;
    TextView id;
    EditText genderText;
    EditText ageText;
    EditText numberText;
    ResultSet rs;
    ResultSet rs1;
    ProgressBar progressBar;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        id.setText(rs1.getString("patient_id"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 11:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case 12:
                    progressBar.setVisibility(View.GONE);
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.register);
        id = findViewById(R.id.id_show);
        nameText = findViewById(R.id.name_new);
        genderText = findViewById(R.id.gender_new);
        ageText = findViewById(R.id.age_new);
        numberText = findViewById(R.id.number_new);
        progressBar = findViewById(R.id.progressBar_register);
        progressBar.setVisibility(View.GONE);
        register.setOnClickListener(v -> new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String name = nameText.getText().toString();
                String gender = genderText.getText().toString();
                String age = ageText.getText().toString();
                String number = numberText.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(Register.this, "Please enter your name", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        handler.sendEmptyMessage(11);
                        connection = DBOpenHelper.getConn();
                        if (DBOpenHelper.getExit(connection, "patient_name", name) == 1) {
                            Toast.makeText(Register.this, "The name already exists", Toast.LENGTH_LONG).show();
                        } else {
                            String sql1 = "INSERT INTO patient ( patient_id, patient_name, patient_gender, patient_age, contact_number)\n" +
                                    "VALUES\n" +
                                    "( null, ?, ?, ?, ?)";
                            PreparedStatement ps1 = (PreparedStatement) connection.prepareStatement(sql1);
                            ps1.setString(1, name);
                            ps1.setString(2, gender);
                            ps1.setString(3, age);
                            ps1.setString(4, number);
                            ps1.executeUpdate();
                            //sql添加数据语句
                            if (DBOpenHelper.getExit(connection, "patient_name", name) == 1) {
                                Toast.makeText(Register.this, "Registration success", Toast.LENGTH_LONG).show();
                                String sql = "SELECT patient_id FROM patient WHERE patient_name='" + name + "'";
                                rs1 = DBOpenHelper.getQuery(connection, sql);
                                handler.sendEmptyMessage(1);

                            } else {
                                Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_LONG).show();
                            }
                        }
                        Intent intent = new Intent();
                        intent.putExtra("name", name);
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(12);
                Looper.loop();
            }
        }).start());
        DBOpenHelper.closeAll(connection);
    }
}