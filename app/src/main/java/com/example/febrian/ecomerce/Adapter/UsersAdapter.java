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

import com.example.febrian.ecomerce.ChattingActivity;
import com.example.febrian.ecomerce.R;
import com.example.febrian.ecomerce.Response.UserModel;

import java.util.List;

/**
 * Created by febrian on 06/04/18.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    private List<UserModel> arrData;
    private Context mContext;

    public UsersAdapter(Context mContext, List<UserModel> arrData){
        this.arrData = arrData;
        this.mContext = mContext;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_chat,parent,false);
        UsersAdapter.ViewHolder vh = new UsersAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, int position) {
        final UserModel singleItem = arrData.get(position);
        holder.tvEmail.setText(singleItem.getEmail());
        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, ChattingActivity.class);
                i.putExtra("guid_friend",singleItem.getGuid());
                i.putExtra("email_friend",singleItem.getEmail());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrData.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        private TextView tvEmail;
        private CardView itemCard;
        public ViewHolder(View itemView) {
            super(itemView);
            tvEmail      = (TextView) itemView.findViewById(R.id.tvEmail);
            itemCard    = (CardView) itemView.findViewById(R.id.itemCardView);
        }
    }


}
