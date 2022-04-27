package com.example.musicplayer.API;

import com.example.musicplayer.model.User;
import com.example.musicplayer.model.UserAPIResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    String USER_BASE_URL = "http://10.0.2.2/api/users/";

    Gson gson = new GsonBuilder().setDateFormat("dd-mm-yyyy").create();

    APIService userAPIService = new Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    @GET("detail/{id}/")
    Call<User> getUserInformationByUserID(@Path("id") int userID);

    @FormUrlEncoded
    @POST("authentication/login/")
    Call<UserAPIResponse> userLogin(@Field("username_or_email") String username_or_email, @Field("password") String password);

    @FormUrlEncoded
    @POST("authentication/register/")
    Call<UserAPIResponse> userRegister(@Field("username") String username, @Field("email") String email, @Field("password") String password);

}
