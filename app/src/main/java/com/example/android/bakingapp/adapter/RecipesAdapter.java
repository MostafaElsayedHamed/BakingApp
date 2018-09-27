package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetails;
import com.example.android.bakingapp.model.Example;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mostafa on 8/4/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> implements View.OnClickListener {

    private final List<Example> examples ;
    Context context;



    public RecipesAdapter(Context context, List<Example> examples) {
        this.examples = examples;
        this.context = context;

    }


    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_view, parent, false);


        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipesViewHolder holder, final int position) {
        if (!(examples.get(holder.getAdapterPosition()).getImage().isEmpty()) &&
                !(examples.get(holder.getAdapterPosition()).getImage().equals(""))){
            Picasso.with(context).load(examples.get(holder.getAdapterPosition()).getImage())
                    .error(R.drawable.vp_placeholder).into(holder.recipeImage);
        }else {
            Picasso.with(context).load(R.drawable.vp_placeholder).error(R.drawable.vp_placeholder).into(holder.recipeImage);
        }

        holder.mTextView.setText(examples.get(position).getName());
        holder.recipesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context,RecipeDetails.class);
                intent.putExtra("position",holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return examples.size();
    }

    @Override
    public void onClick(View view) {

    }


    class RecipesViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public CardView recipesView;
        public ImageView recipeImage;


        public RecipesViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_recipes_view);
            recipesView = itemView.findViewById(R.id.recipes_view);
            recipeImage = itemView.findViewById(R.id.recipeImage);

        }
    }


}
