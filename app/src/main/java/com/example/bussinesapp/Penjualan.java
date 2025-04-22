package com.example.bussinesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Penjualan extends AppCompatActivity {

    private LinearLayout cardContainer;
    private String username;
    private DatabaseReference database; // Referensi ke Firebase Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);

        username = getIntent().getStringExtra("USERNAME");
        cardContainer = findViewById(R.id.card_container);

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("users").child(username);

        // Dummy data, bisa diganti dengan data dari Firebase atau database

        // 🔘 Floating Action Button untuk menambah barang
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> showInputDialog());
       

        // Bottom Navigation untuk berpindah ke halaman lain
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_penjualan);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_Home) {
                Intent intent = new Intent(Penjualan.this, HomeActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_settings) {
                Intent intent = new Intent(Penjualan.this, Setting.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void showInputDialog() {
        final EditText input = new EditText(this);
        input.setHint("Masukkan nama barang");

        new AlertDialog.Builder(this)
                .setTitle("Tambah Barang")
                .setMessage("Silakan masukkan nama barang:")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String namaBarang = input.getText().toString().trim();
                    if (!namaBarang.isEmpty()) {
                        Barang barangBaru = new Barang(namaBarang, 0);
                        addCard(barangBaru);
                        saveToDatabase(barangBaru);
                    } else {
                        Toast.makeText(Penjualan.this, "Nama barang tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    // Fungsi untuk menambah CardView ke dalam LinearLayout
    private void addCard(Barang barang) {
        // Inflate layout item_penjualan
        View cardView = getLayoutInflater().inflate(R.layout.item_penjualan, null);

        // Menentukan elemen di dalam cardView
        TextView txtNama = cardView.findViewById(R.id.txt_nama_barang);
        TextView txtJumlah = cardView.findViewById(R.id.txt_jumlah);
        Button btnPlus = cardView.findViewById(R.id.btn_plus);
        Button btnMin = cardView.findViewById(R.id.btn_min);
        Button btnSimpan = cardView.findViewById(R.id.btn_simpan);
        ImageView btnDelete = cardView.findViewById(R.id.btn_delete); // Tombol delete (tong sampah)

        // Set data untuk nama barang dan jumlah
        txtNama.setText(barang.getNamaBarang());
        txtJumlah.setText(String.valueOf(barang.getJumlah()));

        // Tombol untuk menambah jumlah
        btnPlus.setOnClickListener(v -> {
            int jumlah = Integer.parseInt(txtJumlah.getText().toString());
            txtJumlah.setText(String.valueOf(jumlah + 1));
            barang.setJumlah(jumlah + 1); // Update jumlah pada objek barang
            updateDatabase(barang); // Update jumlah barang di database
        });

        // Tombol untuk mengurangi jumlah
        btnMin.setOnClickListener(v -> {
            int jumlah = Integer.parseInt(txtJumlah.getText().toString());
            if (jumlah > 0) {
                txtJumlah.setText(String.valueOf(jumlah - 1));
                barang.setJumlah(jumlah - 1); // Update jumlah pada objek barang
                updateDatabase(barang); // Update jumlah barang di database
            }
        });

        // Tombol untuk menyimpan perubahan jumlah barang
        btnSimpan.setOnClickListener(v -> {
            int jumlahBaru = Integer.parseInt(txtJumlah.getText().toString());
            barang.setJumlah(jumlahBaru); // Update jumlah pada objek barang
            Toast.makeText(Penjualan.this, "Disimpan: " + barang.getNamaBarang() + " x " + jumlahBaru, Toast.LENGTH_SHORT).show();
            updateDatabase(barang); // Simpan ke database Firebase
        });

        // Tombol delete untuk menghapus card
        btnDelete.setOnClickListener(v -> {
            // Menghapus cardView dari container
            cardContainer.removeView(cardView);
            deleteFromDatabase(barang); // Menghapus data dari Firebase
            Toast.makeText(Penjualan.this, "Card Dihapus", Toast.LENGTH_SHORT).show();
        });

        // Menambahkan cardView ke dalam LinearLayout
        cardContainer.addView(cardView);
    }

    // Fungsi untuk menyimpan barang ke Firebase
    private void saveToDatabase(Barang barang) {
        String idBarang = database.push().getKey(); // Generate key untuk barang baru
        if (idBarang != null) {
            database.child(idBarang).setValue(barang); // Simpan barang ke Firebase dengan key
        }
    }

    // Fungsi untuk update data barang di Firebase
    private void updateDatabase(Barang barang) {
        // Asumsi kita sudah memiliki ID barang dari Firebase
        String idBarang = barang.getId(); // ID dari barang yang sudah ada
        if (idBarang != null) {
            database.child(idBarang).setValue(barang); // Update barang ke Firebase
        }
    }

    // Fungsi untuk menghapus barang dari Firebase
    private void deleteFromDatabase(Barang barang) {
        String idBarang = barang.getId();
        if (idBarang != null) {
            database.child(idBarang).removeValue(); // Hapus barang dari Firebase
        }
    }
}