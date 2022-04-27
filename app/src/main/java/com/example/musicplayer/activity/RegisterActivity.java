package com.example.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.API.APIService;
import com.example.musicplayer.R;
import com.example.musicplayer.model.User;
import com.example.musicplayer.model.UserAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.registerEditTextUsername);
        editTextEmail = findViewById(R.id.registerEditTextEmail);
        editTextPassword = findViewById(R.id.registerEditTextPassword);
        buttonSignUp = findViewById(R.id.registerButtonRegister);
        textViewLogin = findViewById(R.id.registerTextViewLogin);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                APIService.userAPIService.userRegister(username, email, password).enqueue(new Callback<UserAPIResponse>() {
                    @Override
                    public void onResponse(Call<UserAPIResponse> call, Response<UserAPIResponse> response) {
                        if (response.code() == 201) {
                            String userID = response.body().getData().getUserID();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user_id", userID);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Email or username already exists!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserAPIResponse> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }


}