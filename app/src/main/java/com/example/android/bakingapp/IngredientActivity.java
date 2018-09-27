package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingapp.adapter.IngredientAdapter;
import com.example.android.bakingapp.model.Example;
import com.example.android.bakingapp.model.Ingredient;

import java.util.List;

public class IngredientActivity extends AppCompatActivity {
    int position;
    RecyclerView recyclerIngredient;
    IngredientAdapter ingredientAdapter;
    Example example;
    LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        recyclerIngredient = findViewById(R.id.recycle_ingredient);
        if (getIntent().hasExtra("position")) {
            position = getIntent().getIntExtra("position", 0);
        }
        example = MainActivity.examplesList.get(position);
        List<Ingredient> ingredients = example.getIngredients();
        recyclerIngredient.setHasFixedSize(true);
        recyclerIngredient.setLayoutManager(linearLayoutManager );
        ingredientAdapter = new IngredientAdapter(ingredients);
        recyclerIngredient.setAdapter(ingredientAdapter);
    }
}
