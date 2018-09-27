package com.example.android.bakingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.adapter.IngredientAdapter;
import com.example.android.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mostafa on 8/14/2018.
 */

public class IngredientFragment extends Fragment {
    RecyclerView recyclerIngredient;
    int position;
    List<Ingredient> ingredients = new ArrayList<>();
    IngredientAdapter ingredientAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredient_fragment,container,false);
        populateUI(view);
        return view;
    }
    public void populateUI( View view){
        recyclerIngredient = view.findViewById(R.id.recycle_ingredient);
        position = getActivity().getIntent().getIntExtra("position", 0);
        ingredients = MainActivity.examplesList.get(position).getIngredients();
        getActivity().setTitle(MainActivity.examplesList.get(position).getName());
        ingredientAdapter = new IngredientAdapter(ingredients);
        recyclerIngredient.setAdapter(ingredientAdapter);
        recyclerIngredient.setHasFixedSize(true);
        recyclerIngredient.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

    }
}
