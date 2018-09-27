package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingapp.adapter.RecipesAdapter;
import com.example.android.bakingapp.model.Example;
import com.example.android.bakingapp.model.RetrofitClient;
import com.example.android.bakingapp.model.SOService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static List<Example> examplesList = new ArrayList<>();
    RecyclerView recyclerView;
    RecipesAdapter adapter;
    LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycle_main);
        recyclerView.setHasFixedSize(true);
        if (getResources().getBoolean(R.bool.isTablet)) {
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        SOService apiService =
                RetrofitClient.getClient().create(SOService.class);

        Call<List<Example>> call = apiService.getAnswers();
        call.enqueue(new Callback<List<Example>>() {
            @Override
            public void onResponse(Call<List<Example>> call, Response<List<Example>> response) {
                if(examplesList.size()!=0)
                    examplesList.clear();
                examplesList.addAll(response.body());
                adapter = new RecipesAdapter(MainActivity.this, examplesList);
                recyclerView.setAdapter(adapter);


                Log.d(TAG, "Number of r received: ");
            }

            @Override
            public void onFailure(Call<List<Example>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });


    }
}
