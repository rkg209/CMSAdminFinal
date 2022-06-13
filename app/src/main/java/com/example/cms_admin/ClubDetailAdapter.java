package com.example.cms_admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClubDetailAdapter extends RecyclerView.Adapter<ClubDetailAdapter.Holder> {

    ArrayList<ClubDetail> arrayList;
    Context context;

    public ClubDetailAdapter(ArrayList<ClubDetail> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.club_row,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ClubDetail current=arrayList.get(position);
        Glide.with(holder.logo.getContext()).load(current.getLogo()).into(holder.logo);
        holder.textName.setText(arrayList.get(position).getUserName());
        holder.textEmail.setText(arrayList.get(position).getEmail());
        //holder.bind(arrayList.get(position),listner);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder
    {
        CircleImageView logo;
//        ImageView logo;
        TextView textName;
        TextView textEmail;


        public Holder(@NonNull View itemView) {
            super(itemView);
            logo=itemView.findViewById(R.id.img);
            textName = itemView.findViewById(R.id.txt_club_Uname);
            textEmail = itemView.findViewById(R.id.txt_club_type);
        }
    }
    public void filterList(ArrayList<ClubDetail> filterllist) {
        arrayList = filterllist;
        notifyDataSetChanged();
    }
}
