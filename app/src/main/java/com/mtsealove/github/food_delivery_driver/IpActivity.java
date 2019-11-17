package com.mtsealove.github.food_delivery_driver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class IpActivity extends AppCompatActivity {
    EditText ipEt;
    Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        ipEt = findViewById(R.id.ipEt);
        confirmBtn = findViewById(R.id.confirmBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIp();
            }
        });

        GetIP();
    }

    private void setIp() {
        String IP = ipEt.getText().toString();
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ip", IP);
        editor.commit();

        Toast.makeText(this, "IP가 저장됨", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void GetIP() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        ipEt.setText(pref.getString("ip", ""));
    }
}
