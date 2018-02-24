package com.sourcey.materiallogindemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.List;

/**
 * Created by ASUS on 5/22/2017.
 */

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    private Context context;
    private List<MyData> my_data;

    public CustomAdapter(Context context, List<MyData> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(my_data.get(position).getTitle());
        holder.description.setText(my_data.get(position).getDescription());
        holder.longitude.setText(my_data.get(position).getLongitude());
        holder.latitude.setText(my_data.get(position).getLatitude());
        holder.isvalidated.setText(my_data.get(position).getIsvalidated());
//        Glide.with(context).load(my_data.get(position).getImage_link()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView longitude;
        public TextView latitude;
        public TextView isvalidated;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            longitude = (TextView) itemView.findViewById(R.id.longitude);
            latitude = (TextView) itemView.findViewById(R.id.latitude);
            isvalidated = (TextView) itemView.findViewById(R.id.isvalidated);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
