package com.example.musicplayer.API;

import com.example.musicplayer.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    Gson gson = new GsonBuilder().setDateFormat("DD-MM-YYYY HH:MM:SS").create();

    APIService apiService = new Retrofit.Builder()
            .baseUrl("http://localhost/api/users/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    @GET("detail/{id}/")
    Call<User> getUserInformationByUserID(@Path("id") int userID);

    @POST("authentication/login/")
    Call<User> userLogin(@Body User user);

}
