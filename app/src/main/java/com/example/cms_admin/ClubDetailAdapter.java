package com.example.cms_admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClubDetailAdapter extends RecyclerView.Adapter<ClubDetailAdapter.Holder> {

    ArrayList<ClubDetail> arrayList;
    Context context;

    DatabaseReference reference;

    public ClubDetailAdapter(ArrayList<ClubDetail> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        reference= FirebaseDatabase.getInstance().getReference();
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

        holder.deleteClubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.deleteClubMenu);
                popupMenu.inflate(R.menu.remove);
                popupMenu.setOnMenuItemClickListener(item ->{
                    String club= current.getUserName();
                    reference.child("Club").child(club).removeValue();
                    Toast.makeText(context, club+" Removed Successfully", Toast.LENGTH_SHORT).show();

                    return false;
                });
                popupMenu.show();
            }
        });
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
        TextView deleteClubMenu;


        public Holder(@NonNull View itemView) {
            super(itemView);
            logo=itemView.findViewById(R.id.img);
            textName = itemView.findViewById(R.id.txt_club_Uname);
            textEmail = itemView.findViewById(R.id.txt_club_type);
            deleteClubMenu = itemView.findViewById(R.id.deleteClub);
        }
    }
    public void filterList(ArrayList<ClubDetail> filterllist) {
        arrayList = filterllist;
        notifyDataSetChanged();
    }
}
