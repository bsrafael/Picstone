package com.example.picstone.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.picstone.R;
import com.example.picstone.models.input.UserInputModel;
import com.example.picstone.models.output.UserViewModel;
import com.example.picstone.network.ClientFactory;
import com.example.picstone.network.SoapstoneClientPublic;
import com.example.picstone.ui.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Button backButton;
    private Button registerButton;
    private EditText usernameEdit;
    private EditText emailEdit;
    private EditText confirmEmailEdit;
    private EditText passwordEdit;
    private EditText confirmPasswordEdit;

    private SoapstoneClientPublic clientPublic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backButton = findViewById(R.id.btn_back);
        registerButton = findViewById(R.id.btn_create_account);

        usernameEdit = findViewById(R.id.username);
        emailEdit = findViewById(R.id.email);
        confirmEmailEdit = findViewById(R.id.confirm_email);
        passwordEdit = findViewById(R.id.password);
        confirmPasswordEdit = findViewById(R.id.confirm_password);

        clientPublic = ClientFactory.GetSoapstoneClient();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO um loading aqui seria massa
                UserInputModel inputModel = new UserInputModel();
                inputModel.setEmail(emailEdit.getText().toString());
                inputModel.setPassword(passwordEdit.getText().toString());
                inputModel.setUsername(usernameEdit.getText().toString());

                clientPublic.createUser(inputModel).enqueue(
                        new Callback<UserViewModel>() {
                            @Override
                            public void onResponse(Call<UserViewModel> call, Response<UserViewModel> response) {
                                String username = response.body().getUsername();
                                Toast.makeText(getApplicationContext(), "Usuário " + username + " registrado com sucesso", Toast.LENGTH_LONG).show();
                                goBack();
                            }

                            @Override
                            public void onFailure(Call<UserViewModel> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Falha ao cadastrar o usuário, " + t.getMessage(), Toast.LENGTH_LONG).show();
                                goBack();
                            }
                        }
                );
            }
        });
    }

    private void goBack() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
