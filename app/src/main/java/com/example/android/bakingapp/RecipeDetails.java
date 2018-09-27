package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.bakingapp.adapter.RecipeDetailsAdapter;
import com.example.android.bakingapp.data.Contract;
import com.example.android.bakingapp.model.Example;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.s;
import com.example.android.bakingapp.widget.WidgetProvider;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetails extends AppCompatActivity {
    int position;
    private SharedPreferences sharedPreferences;
    Example example;
    RecipeDetailsAdapter recipeDetailsAdapter;
    RecyclerView recyclerDetails;
    LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        recyclerDetails = findViewById(R.id.recycle_details);
        sharedPreferences = getSharedPreferences("com.example.android.baking", Context.MODE_PRIVATE);
        if (getIntent().hasExtra("position")) {
            position = getIntent().getIntExtra("position", 0);
        }
        example = MainActivity.examplesList.get(position);
        setTitle(example.getName());
        List<String> strings = new ArrayList<>();
        strings.add(String.format("%s Ingredients", example.getName()));
        List<s> steps = example.getSteps();
        for (int i = 0; i < steps.size(); i++) {

            strings.add(steps.get(i).getShortDescription());

        }
        recyclerDetails.setHasFixedSize(true);
        recyclerDetails.setLayoutManager(linearLayoutManager );
        recipeDetailsAdapter = new RecipeDetailsAdapter(this, strings, position);
        recyclerDetails.setAdapter(recipeDetailsAdapter);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.selected_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_addToWIdget) {
            item.setVisible(false);
            showIngredientsInWidget(position);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return true;
    }

    private void showIngredientsInWidget(int adapterPosition) {
        List<Ingredient> ingredients = MainActivity.examplesList.get(adapterPosition).getIngredients();
        String recipeName = MainActivity.examplesList.get(adapterPosition).getName();
        sharedPreferences.edit().putString(getString(R.string.pref_recipe_name), recipeName).apply();
        saveIngredients(ingredients);
        Uri uri = Contract.Entry.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            //Delete the existing data first
            while (cursor.moveToNext()) {
                getContentResolver().delete(uri,
                        Contract.Entry._ID + "=?",
                        new String[]{
                                cursor.getString(cursor.getColumnIndex(Contract.Entry._ID))}
                );
            }
            cursor.close();
            ContentValues[] values = new ContentValues[ingredients.size()];

            for (int i = 0; i < ingredients.size(); i++) {
                values[i] = new ContentValues();
                values[i].put(Contract.Entry.COLUMN_NAME_QUANTITY, ingredients.get(i).getQuantity());
                values[i].put(Contract.Entry.COLUMN_NAME_MEASURE, ingredients.get(i).getMeasure());
                values[i].put(Contract.Entry.COLUMN_NAME_INGREDIENTS, ingredients.get(i).getIngredient());
            }
            getContentResolver().bulkInsert(uri, values);
        }

        //Update recipe's name and ingredients
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(),
                        WidgetProvider.class));
        WidgetProvider ingredientWidget = new WidgetProvider();
        ingredientWidget.onUpdate(this, AppWidgetManager.getInstance(this), ids);

    }

    private void saveIngredients(List<Ingredient> ingredients) {

        String noOfIngredient = ingredients.size() + " Ingredients";
        StringBuilder sb = new StringBuilder();
        sb.append(noOfIngredient);
        for (Ingredient ingredient : ingredients) {
            String name = ingredient.getIngredient();
            double quantity = ingredient.getQuantity();
            String measure = ingredient.getMeasure();
            sb.append("\n");
            sb.append(CommonUtils.formatIngredient(this, name, quantity, measure));
        }

        String formattedIngredients = String.valueOf(CommonUtils.setTextWithSpan(sb.toString(), noOfIngredient, new StyleSpan(Typeface.BOLD)));
        sharedPreferences.edit().putString(getString(R.string.pref_recipe_ingredients), formattedIngredients).apply();
    }

}
