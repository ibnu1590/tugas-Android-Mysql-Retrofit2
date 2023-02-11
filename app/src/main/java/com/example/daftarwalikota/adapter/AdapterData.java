package com.example.daftarwalikota.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daftarwalikota.R;
import com.example.daftarwalikota.activity.MainActivity;
import com.example.daftarwalikota.activity.TambahActivity;
import com.example.daftarwalikota.activity.UbahActivity;
import com.example.daftarwalikota.api.APIRequestData;
import com.example.daftarwalikota.api.RetroServer;
import com.example.daftarwalikota.model.DataModel;
import com.example.daftarwalikota.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private Context ctx;
    private List<DataModel> listWalikota;
    private List<DataModel> listSpesifikWalikota;
    private int id;

    public AdapterData(Context ctx, List<DataModel> listWalikota) {
        this.ctx = ctx;
        this.listWalikota = listWalikota;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listWalikota.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvNama.setText(dm.getNama());
        holder.tvAlamat.setText(dm.getAlamat());
        holder.tvPartai.setText(dm.getPartai());
    }

    @Override
    public int getItemCount() {
        return listWalikota.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView tvId, tvNama, tvAlamat, tvPartai;


        public HolderData(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvPartai = itemView.findViewById(R.id.tv_partai);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Pilih Action");
                    dialogPesan.setTitle("Perhatian");
                    dialogPesan.setIcon(R.drawable.baseline_question_mark_24);
                    dialogPesan.setCancelable(true);
                    id = Integer.parseInt(tvId.getText().toString());


                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            ((MainActivity) ctx).retrieveData();
                        }
                    });

                    dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                            dialogInterface.dismiss();
                        }
                    });

                    dialogPesan.show();

                    return false;
                }
            });

        }

        private void deleteData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(id);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Terhubung Server : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(id);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listSpesifikWalikota = response.body().getData();

                    int varIdWalkot = listSpesifikWalikota.get(0).getId();
                    String varNamaWalkot = listSpesifikWalikota.get(0).getNama();
                    String varAlamatWalkot = listSpesifikWalikota.get(0).getAlamat();
                    String varPartaiWalkot = listSpesifikWalikota.get(0).getPartai();

                    Intent kirim = new Intent(ctx, UbahActivity.class);
                    kirim.putExtra("xId", varIdWalkot);
                    kirim.putExtra("xNama", varNamaWalkot);
                    kirim.putExtra("xAlamat", varAlamatWalkot);
                    kirim.putExtra("xPartai", varPartaiWalkot);
                    ctx.startActivity(kirim);
//                    Toast.makeText(ctx, "Kode : "+kode+" | Pesan : "+pesan+ " | Data : "+varIdWalkot, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Terhubung Server : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
