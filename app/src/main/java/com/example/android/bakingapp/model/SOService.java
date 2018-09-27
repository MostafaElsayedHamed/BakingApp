package com.example.android.bakingapp.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by mostafa on 8/4/2018.
 */

public interface  SOService {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Example>> getAnswers();
}
