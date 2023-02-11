package com.example.daftarwalikota.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daftarwalikota.R;
import com.example.daftarwalikota.api.APIRequestData;
import com.example.daftarwalikota.api.RetroServer;
import com.example.daftarwalikota.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {
    private EditText etNama, etAlamat, etPartai;
    private Button btnSimpan;
    private String nama, alamat, partai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        etPartai = findViewById(R.id.et_partai);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = etNama.getText().toString();
                alamat = etAlamat.getText().toString();
                partai = etPartai.getText().toString();

                if (nama.trim().equals("")){
                    etNama.setError("Nama Harus di isi");
                } else if (alamat.trim().equals("")){
                    etAlamat.setError("Alamat Harus di isi");

                } else if (partai.trim().equals("")){
                    etPartai.setError("Partai Harus di isi");

                } else {
                    createData();
                }
            }
        });

    }
    private void createData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> simpanData = ardData.ardCreateData(nama, alamat, partai);

        simpanData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(TambahActivity.this, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Gagal Tersambung Server : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}