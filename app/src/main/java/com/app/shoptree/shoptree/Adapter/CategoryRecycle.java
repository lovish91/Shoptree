package com.app.shoptree.shoptree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shoptree.shoptree.CategoryActivity;
import com.app.shoptree.shoptree.R;
import com.app.shoptree.shoptree.model.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lovishbajaj on 07/09/17.
 */

public class CategoryRecycle extends RecyclerView.Adapter<CategoryRecycle.ViewHolder> {
    private List<CategoryModel> categoryModels;
    private Context context;
    private String TAG = "testing";

    public CategoryRecycle(Context context, List<CategoryModel> categoryModels){
        this.categoryModels = categoryModels;
        this.context =context;
    }

    @Override
    public CategoryRecycle.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vertical_category_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryRecycle.ViewHolder holder, final int position) {
        holder.title.setText(categoryModels.get(position).getCatName());
        holder.description.setText(categoryModels.get(position).getCatDescription());
        String a = categoryModels.get(position).getCatImage();
        //Log.i(TAG, categoryModels.get(position).getCatDescription().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryActivity.class)
                        .putExtra("categoryId",categoryModels.get(position).getCategoryId())
                        .putExtra("categoryname",categoryModels.get(position).getCatName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
        Picasso.with(context)
                .load("https://shopptree.com"+a.substring(1))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,description;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.vert_category_title);
            description = (TextView)itemView.findViewById(R.id.vert_category_desciption);
            imageView = (ImageView) itemView.findViewById(R.id.vert_category_title_img);
        }
    }
}
