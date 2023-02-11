package com.example.daftarwalikota.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class UbahActivity extends AppCompatActivity {
    private int xId;
    private String xNama, xAlamat, xPartai;
    private EditText etNama, etAlamat, etPartai;
    private Button btnUbah;
    private String yNama, yAlamat, yPartai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xNama = terima.getStringExtra("xNama");
        xAlamat = terima.getStringExtra("xAlamat");
        xPartai = terima.getStringExtra("xPartai");

        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        etPartai = findViewById(R.id.et_partai);

        btnUbah = findViewById(R.id.btn_ubah);

        etNama.setText(xNama);
        etAlamat.setText(xAlamat);
        etPartai.setText(xPartai);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yNama = etNama.getText().toString();
                yAlamat = etAlamat.getText().toString();
                yPartai = etPartai.getText().toString();

                updateData();
            }
        });
    }

    private void updateData() {
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> ubahData = ardData.ardUpdateData(xId, yNama, yAlamat, yPartai);

        ubahData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(UbahActivity.this, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal Tersambung Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}