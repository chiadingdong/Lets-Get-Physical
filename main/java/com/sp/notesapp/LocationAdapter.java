package com.sp.notesapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    private ArrayList<LocationModel> locationList;
    private MyRecyclerViewItemClickListener mItemClickListener;

    public LocationAdapter(ArrayList<LocationModel> locationList, MyRecyclerViewItemClickListener itemClickListener) {
        this.locationList = locationList;
        this.mItemClickListener = itemClickListener;
    }

    //RecyclerView View Holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, mrt, description;
        ImageView image;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleET);
            mrt = itemView.findViewById(R.id.mrtEt);
            description = itemView.findViewById(R.id.descripEt);
            image = itemView.findViewById(R.id.locationImage);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_row, parent, false);
        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(locationList.get(myViewHolder.getLayoutPosition()));
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final LocationModel location = locationList.get(position);
        holder.title.setText(location.getTitle());
        holder.mrt.setText("How to get there: "+location.getNearestMrt());
        holder.description.setText(location.getDescription());
        holder.image.setImageBitmap(stringToBitMap(location.getImage()));
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(LocationModel location);
    }

    public Bitmap stringToBitMap(String encodedString){
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
