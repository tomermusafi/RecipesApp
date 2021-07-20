package com.musafi.recipesapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.musafi.recipesapp.DB.modole.Recipe;
import com.musafi.recipesapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context context;
    private List<Recipe> recipes;
    private List<Recipe> AllRecipes;
    private OnItemClickListener mItemClickListener;
    private int type;

    public RecipeAdapter(Context context, List<Recipe> recipes, int type) {
        this.context = context;
        this.recipes = recipes;
        this.AllRecipes = new ArrayList<>(recipes);
        this.type = type;
    }

    public void updateList(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        this.AllRecipes =new ArrayList<>(recipes);
        notifyDataSetChanged();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if(this.type == 0 || this.type == 1) {
             view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_list_item, viewGroup, false);
        }else{
             view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.link_list_item, viewGroup, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final Recipe recipe = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;


            if(this.type == 0) {
                genericViewHolder.name.setText(recipe.getName());
                String tempCategories = "";
                if(recipe.getCategories() != null) {
                    for (String s : recipe.getCategories()) {
                        tempCategories += s + "   ";
                    }
                    genericViewHolder.categories.setText(tempCategories);
                }
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(recipe.getDish_url()));
                    genericViewHolder.photo.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                genericViewHolder.name1.setText(recipe.getName());
                genericViewHolder.url.setText(recipe.getWeb_url());
            }
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    private Recipe getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Recipe> filteredList = new ArrayList<>();
            if(charSequence.toString().isEmpty()){
                filteredList.addAll(AllRecipes);
            }else{
                for(Recipe recipe : AllRecipes){
                    if(recipe.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(recipe);

                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            recipes.clear();
            recipes.addAll((Collection<?extends Recipe>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView name1;
        private ImageView photo;
        private TextView categories;
        private TextView url;


        public ViewHolder(final View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.name1 = itemView.findViewById(R.id.name1);
            this.photo = itemView.findViewById(R.id.photo);
            this.categories = itemView.findViewById(R.id.categories);
            this.url = itemView.findViewById(R.id.url);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
        }
    }


    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Recipe recipe);
    }
}
