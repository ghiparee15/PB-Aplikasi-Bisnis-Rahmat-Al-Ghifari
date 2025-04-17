package com.example.bussinesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNoteFragment extends DialogFragment {

    private EditText edtCatatan;
    private String username;
    private DatabaseReference catatanReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Mengatur layout untuk fragment
        View view = inflater.inflate(R.layout.activity_add_note_fragment, container, false);

        // Mengambil username dari Bundle yang diteruskan dari HomeActivity
        if (getArguments() != null) {
            username = getArguments().getString("USERNAME");
        }

        edtCatatan = view.findViewById(R.id.edtCatatan);

        // Menangani tombol simpan catatan
        view.findViewById(R.id.btnSimpanCatatan).setOnClickListener(v -> {
            String catatan = edtCatatan.getText().toString().trim();

            if (!catatan.isEmpty()) {
                // Simpan catatan ke Firebase
                catatanReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("catatan");
                catatanReference.setValue(catatan)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getActivity(), "Catatan berhasil disimpan", Toast.LENGTH_SHORT).show();
                            dismiss(); // Menutup fragment
                        })
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Gagal menyimpan catatan", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(getActivity(), "Catatan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
