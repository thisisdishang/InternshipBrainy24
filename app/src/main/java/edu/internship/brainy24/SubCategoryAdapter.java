package edu.internship.brainy24;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyHolder> {
    Context context;
    ArrayList<SubCategoryList> arrayList;
    SharedPreferences sp;
//    String[] nameArray;
//    int[] imageArray;

//    public CategoryAdapter(Context context, String[] nameArray, int[] imageArray) {
//        this.context = context;
//        this.nameArray = nameArray;
//        this.imageArray = imageArray;
//    }

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSp.PREF, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public SubCategoryAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category, parent, false);
        return new SubCategoryAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_category_image);
            name = itemView.findViewById(R.id.custom_category_name);
        }
    }

//    @Override
//    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
//        holder.imageView.setImageResource(imageArray[position]);
//        holder.name.setText(nameArray[position]);
//    }


    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.name.setText(arrayList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.SUB_CATEGORY_ID, arrayList.get(position).getId()).commit();
                sp.edit().putString(ConstantSp.SUB_CATEGORY_NAME, arrayList.get(position).getName()).commit();
                new CommonMethod(context, ProductActivity.class);
            }
        });
    }

//    @Override
//    public int getItemCount() {
//        return nameArray.size;
//    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}