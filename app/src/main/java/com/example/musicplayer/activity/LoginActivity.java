package com.example.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.API.APIService;
import com.example.musicplayer.R;
import com.example.musicplayer.model.UserAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsernameOrEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        editTextUsernameOrEmail = findViewById(R.id.loginEditTextUsernameOrEmail);
        editTextPassword = findViewById(R.id.loginEditTextPassword);
        buttonLogin = findViewById(R.id.loginButtonLogin);
        buttonRegister = findViewById(R.id.loginButtonRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameOrEmail = editTextUsernameOrEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                APIService.userAPIService.userLogin(usernameOrEmail, password).enqueue(new Callback<UserAPIResponse>() {
                    @Override
                    public void onResponse(Call<UserAPIResponse> call, Response<UserAPIResponse> response) {
                        if (response.code() == 200) {
                            String userID = response.body().getData().getUserID();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user_id", userID);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserAPIResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}