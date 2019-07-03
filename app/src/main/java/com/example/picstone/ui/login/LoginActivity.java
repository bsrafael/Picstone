package com.example.picstone.ui.login;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picstone.R;
import com.example.picstone.User;
import com.example.picstone.data.LoginDataSource;
import com.example.picstone.data.LoginRepository;
import com.example.picstone.data.Result;
import com.example.picstone.data.model.LoggedInUser;
import com.example.picstone.main.MainActivity;
import com.example.picstone.main.RegisterActivity;
import com.example.picstone.models.input.TokenInputModel;
import com.example.picstone.models.output.TokenViewModel;
import com.example.picstone.network.ClientFactory;
import com.example.picstone.network.SoapstoneClient;
import com.example.picstone.network.SoapstoneClientPublic;
import com.google.android.gms.common.api.Api;

import java.net.UnknownServiceException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private SoapstoneClientPublic clientPublic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = findViewById(R.id.loginBtn);
        final Button registerButton = findViewById(R.id.create_account);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        clientPublic = ClientFactory.GetSoapstoneClient();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                final String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                clientPublic.getToken(new TokenInputModel(username, password)).enqueue(new Callback<TokenViewModel>() {
                    @Override
                    public void onResponse(Call<TokenViewModel> call, Response<TokenViewModel> response) {
                        User user = User.getInstance();
                        user.setUsername(username);
                        user.setToken(response.body().getToken());

                        updateUiWithUser();
                    }

                    @Override
                    public void onFailure(Call<TokenViewModel> call, Throwable t) {
                        // TODO
                    }
                });
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUiWithUser() {
        String welcome = getString(R.string.welcome);
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
