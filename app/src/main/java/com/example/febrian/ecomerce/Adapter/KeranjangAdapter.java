package com.example.febrian.ecomerce.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.febrian.ecomerce.DetailProdukActivity;
import com.example.febrian.ecomerce.R;
import com.example.febrian.ecomerce.Response.Produk;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * Created by febrian on 16/03/18.
 */

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.ViewHolder> {
    private List<Produk> arrData;
    private Context mContext;

    public KeranjangAdapter(Context mContext, List<Produk> arrData){
        this.arrData = arrData;
        this.mContext = mContext;
    }

    @Override
    public KeranjangAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk_chart,parent,false);
        KeranjangAdapter.ViewHolder vh = new KeranjangAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(KeranjangAdapter.ViewHolder holder, int position) {
        final Produk singleItem = arrData.get(position);
        holder.tvNama.setText(singleItem.getNamaBarang());
        holder.tvHarga.setText(KatergoriAdapter.formatRupiah(Double.parseDouble(singleItem.getHargaJual())));

        Picasso.with(mContext).load(singleItem.getGambar()).fit()
                .error(R.drawable.user_bg)
                .placeholder(R.drawable.user_bg)
                .centerCrop().into(holder.ivGambar);
        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return arrData.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        private TextView tvNama, tvHarga;
        private ImageView ivGambar;
        private CardView itemCard;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama      = (TextView) itemView.findViewById(R.id.tvNama);
            tvHarga     = (TextView) itemView.findViewById(R.id.tvHarga);
            itemCard    = (CardView) itemView.findViewById(R.id.itemCardView);
            ivGambar    = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public static String formatRupiah(Double harga){
        DecimalFormat kursIndonesia     = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp   = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(harga).replace(",00","");
    }
}
