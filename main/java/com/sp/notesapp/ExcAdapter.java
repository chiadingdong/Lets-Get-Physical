package com.sp.notesapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExcAdapter extends RecyclerView.Adapter<ExcAdapter.MyViewHolder> {

    private ArrayList<ExcModel> excList;
    private MyRecyclerViewItemClickListener mItemClickListener;

    public ExcAdapter(ArrayList<ExcModel> excList, MyRecyclerViewItemClickListener itemClickListener) {
        this.excList = excList;
        this.mItemClickListener = itemClickListener;
    }

    //RecyclerView View Holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, timeNCal;
        ImageView image;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.excTitleTV);
            timeNCal = itemView.findViewById(R.id.excTimeTV);
            image = itemView.findViewById(R.id.excImage);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exc_row, parent, false);
        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(excList.get(myViewHolder.getLayoutPosition()));
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ExcModel exercise = excList.get(position);
        holder.title.setText(exercise.getTitle());
        holder.timeNCal.setText(exercise.getTimeAndCalories());
        holder.image.setImageBitmap(stringToBitMap(exercise.getImage()));
    }

    @Override
    public int getItemCount() {
        return excList.size();
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(ExcModel exercise);
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
