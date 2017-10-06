package com.app.shoptree.shoptree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shoptree.shoptree.CategoryActivity;
import com.app.shoptree.shoptree.R;
import com.app.shoptree.shoptree.model.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lovishbajaj on 30/05/17.
 */

public class Category_Adapter extends BaseAdapter{

    String[] imgt;
    private Context mcontext;
    int[] imgId;
    private ArrayList<CategoryModel> categories;
    private static LayoutInflater inflater = null;
    public Category_Adapter(Context context, ArrayList<CategoryModel> categories){
        this.categories = categories;
        this.mcontext = context;
        //this.imgId = ImageId;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public  class Holder
    {
        TextView catText,catdesc;
        ImageView catImg;
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder=new Holder();
        View rowView;
        final CategoryModel categoryModel = categories.get(position);
        rowView = inflater.inflate(R.layout.vertical_category_item, null);
        holder.catText=(TextView) rowView.findViewById(R.id.vert_category_title);
        holder.catImg=(ImageView) rowView.findViewById(R.id.vert_category_title_img);
        holder.catdesc=(TextView) rowView.findViewById(R.id.vert_category_desciption);

        holder.catText.setText(categoryModel.getCatName());
        holder.catdesc.setText(categoryModel.getCatDescription());
        String a = categoryModel.getCatImage();

        Picasso.with(mcontext)
                .load("https://shopptree.com"+ a.substring(1))
                .centerInside()
                .fit()
                //.placeholder(R.drawable.fruit)
                //.error(R.drawable.fruit)
                .into(holder.catImg);
        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CategoryActivity.class)
                .putExtra("categoryId",categoryModel.getCategoryId())
                        .putExtra("categoryname",categoryModel.getCatName());

                v.getContext().startActivity(intent);
                // TODO Auto-generated method stub
            }
        });

        return rowView;
    }
}
