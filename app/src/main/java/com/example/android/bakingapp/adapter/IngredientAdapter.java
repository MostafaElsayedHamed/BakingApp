package com.example.android.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;

import java.util.List;

/**
 * Created by mostafa on 8/13/2018.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private final List<Ingredient> ingredients;



    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_view, parent, false);


        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.name.setText(ingredients.get(holder.getAdapterPosition()).getIngredient());
        holder.measure.setText(ingredients.get(holder.getAdapterPosition()).getMeasure());
        holder.quantity.setText(String.valueOf(ingredients.get(holder.getAdapterPosition()).getQuantity()));

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView measure;
        TextView quantity;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ingredient_name);
            measure = itemView.findViewById(R.id.ingredient_measure);
            quantity = itemView.findViewById(R.id.ingredient_quantity);
        }
    }
}
