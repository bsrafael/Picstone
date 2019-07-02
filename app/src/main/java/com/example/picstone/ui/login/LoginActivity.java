package com.example.picstone.ui.login;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.example.picstone.data.LoginDataSource;
import com.example.picstone.data.LoginRepository;
import com.example.picstone.data.Result;
import com.example.picstone.data.model.LoggedInUser;
import com.example.picstone.main.MainActivity;
import com.example.picstone.main.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.loginBtn);
        final Button registerButton = findViewById(R.id.create_account);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final boolean DEBUG_BYPASS_LOGIN = true;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
//                System.out.println("\n---deu click (LoginActivity:49)");
//
//                LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
//                Result<LoggedInUser> result = loginRepository.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
//                System.out.println("\n---vai pro if (LoginActivity:53)");
//
//                if (result instanceof Result.Success) {
//                    LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//                    updateUiWithUser();
//                    System.out.println("\n----deu certo (LoginActivity:58)");
//
//                } else {
//                    // TODO a message or something
//                    System.out.println("\n----deu pau (LoginActivity:62)");
//                }
                if (DEBUG_BYPASS_LOGIN) updateUiWithUser(); //TODO: remove this after login is fixed.
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
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
