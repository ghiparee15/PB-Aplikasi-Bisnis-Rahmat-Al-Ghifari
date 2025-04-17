package com.example.bussinesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private TextView tvUsername, tvPerusahaan, tvCatatanIsi;
    private ImageButton btnHapusCatatan;
    private FloatingActionButton fabAddCatatan;
    private String username;
    private DatabaseReference database;
    private DatabaseReference catatanReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Mendapatkan username dari LoginActivity
        username = getIntent().getStringExtra("USERNAME");

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inisialisasi View
        tvUsername = findViewById(R.id.tvUsername);
        tvPerusahaan = findViewById(R.id.tvPerusahaan);
        tvCatatanIsi = findViewById(R.id.tvCatatanIsi);
        btnHapusCatatan = findViewById(R.id.btnHapusCatatan);
        fabAddCatatan = findViewById(R.id.fabAddCatatan);

        // Firebase Refs
        database = FirebaseDatabase.getInstance().getReference("users").child(username);
        catatanReference = database.child("catatan");

        // Ambil data dari Firebase
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String usernameFromDb = dataSnapshot.child("username").getValue(String.class);
                    String perusahaanFromDb = dataSnapshot.child("perusahaan").getValue(String.class);
                    String catatanFromDb = dataSnapshot.child("catatan").getValue(String.class);

                    tvUsername.setText(usernameFromDb);
                    tvPerusahaan.setText(perusahaanFromDb);

                    if (catatanFromDb != null && !catatanFromDb.isEmpty()) {
                        tvCatatanIsi.setText(catatanFromDb);
                        btnHapusCatatan.setVisibility(View.VISIBLE);
                    } else {
                        tvCatatanIsi.setText("Klik ikon plus di bawah untuk menambahkan catatan tentang perusahaan");
                        btnHapusCatatan.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
            }
        });

        // FAB Add Catatan
        fabAddCatatan.setOnClickListener(v -> showAddNoteFragment());

        // Tombol Hapus Catatan
        btnHapusCatatan.setOnClickListener(v -> {
            catatanReference.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        tvCatatanIsi.setText("Klik ikon plus di bawah untuk menambahkan catatan tentang perusahaan");
                        btnHapusCatatan.setVisibility(View.GONE);
                        Toast.makeText(HomeActivity.this, "Catatan dihapus", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Gagal menghapus catatan", Toast.LENGTH_SHORT).show());
        });

        // Bottom Navigation Logic
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_Home); // Set Home as selected

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_penjualan) {
                Intent intent = new Intent(HomeActivity.this, Penjualan.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_settings) {
                Intent intent = new Intent(HomeActivity.this, Setting.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void showAddNoteFragment() {
        AddNoteFragment addNoteFragment = new AddNoteFragment();
        Bundle args = new Bundle();
        args.putString("USERNAME", username);
        addNoteFragment.setArguments(args);
        addNoteFragment.show(getSupportFragmentManager(), "AddNoteFragment");
    }
}
