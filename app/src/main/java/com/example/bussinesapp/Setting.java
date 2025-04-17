package com.example.bussinesapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        String username = getIntent().getStringExtra("USERNAME");
        TextView tv = new TextView(this);
        tv.setText("Halaman Pengaturan - Pengguna: " + username);
        setContentView(tv);
    }
}
