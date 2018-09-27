package com.example.android.bakingapp.adapter;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.IngredientActivity;
import com.example.android.bakingapp.IngredientFragment;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeActivity;
import com.example.android.bakingapp.RecipeFragment;

import java.util.List;

/**
 * Created by mostafa on 8/10/2018.
 */

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipesDetailsViewHolder> implements View.OnClickListener  {
    private final List<String> strings ;
    Context context;
    int rePosition;


    public RecipeDetailsAdapter(Context context, List<String> strings, int rePosition) {
        this.strings = strings;
        this.context = context;
        this.rePosition = rePosition;
    }


    @Override
    public RecipesDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_details_view, parent, false);


        return new RecipesDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipesDetailsViewHolder holder, final int position) {
        holder.textRecipesDetails.setText(strings.get(holder.getAdapterPosition()));
        holder.recipesDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getAdapterPosition() == 0 && !(context.getResources().getBoolean(R.bool.isTablet))) {
                    Intent intent = new Intent(context, IngredientActivity.class);
                    intent.putExtra("position", rePosition);
                    context.startActivity(intent);

                } else if (holder.getAdapterPosition() != 0 && !(context.getResources().getBoolean(R.bool.isTablet))) {
                    Intent intent = new Intent(context, RecipeActivity.class);
                    intent.putExtra("position", holder.getAdapterPosition() - 1);
                    intent.putExtra("rePosition", rePosition);
                    context.startActivity(intent);

                } else if (holder.getAdapterPosition() == 0 && (context.getResources().getBoolean(R.bool.isTablet))) {
                    IngredientFragment ingredientFragment = new IngredientFragment();
                    ((Activity)context).getFragmentManager().beginTransaction()
                            .replace(R.id.frg_container, ingredientFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

                } else if (holder.getAdapterPosition() != 0 && (context.getResources().getBoolean(R.bool.isTablet))) {
                    RecipeFragment recipeFragment = new RecipeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("step_position", holder.getAdapterPosition() - 1);
                    recipeFragment.setArguments(bundle);
                    ((Activity)context).getFragmentManager().beginTransaction()
                            .replace(R.id.frg_container, recipeFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    @Override
    public void onClick(View view) {

    }


    class RecipesDetailsViewHolder extends RecyclerView.ViewHolder {
    CardView recipesDetails;
    TextView textRecipesDetails;


        public RecipesDetailsViewHolder(View itemView) {
            super(itemView);
            recipesDetails = itemView.findViewById(R.id.recipes_details_view);
            textRecipesDetails = itemView.findViewById(R.id.text_recipes_details_view);

        }
    }
}
