package com.stubborn.firestorecrude;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ShowActivity activity;
    private List<Modul> list;
    FirebaseFirestore fb = FirebaseFirestore.getInstance();


    public MyAdapter(ShowActivity activity, List<Modul> list) {
        this.activity = activity;
        this.list = list;
    }


    //method to update data
    public void updateData(int position) {

        Modul item = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("uid", item.getId());
        bundle.putString("uname", item.getName());
        bundle.putString("udesc", item.getDescription());
        //this will pass data to main activity
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public void deleteDATA(final int position) {
        final Modul modul = list.get(position);
        fb.collection("Documents").document(modul.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    notifyremoved(position);
                    Toast.makeText(activity, "Data deletede successfully" + modul.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "something went wrong" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void notifyremoved(int position){
        list.remove(position);
        notifyItemRemoved(position);
        activity.showdata();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.desc.setText(list.get(position).getDescription());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            desc = itemView.findViewById(R.id.item_desc);

        }
    }
}
