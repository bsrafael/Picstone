package com.example.picstone.main;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picstone.R;
import com.example.picstone.User;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PinActivity extends AppCompatActivity {

    PhotoView pinPhoto;
    TextView pinText;
    TextView pinAuthor;
    TextView pinData;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        pinPhoto = findViewById(R.id.pinPhoto);
        pinText = findViewById(R.id.pinText);
        pinAuthor = findViewById(R.id.pinAuthor);
        pinData = findViewById(R.id.pinData);

        user = user.getInstance();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String idMarker;

            try {
                idMarker = bundle.getString("idMarker");
                if (idMarker != null) {
                    pinPhoto.setImageURI(user.getDEBUG_SAMPLE_MARKER_PICTURE());
                    pinText.setText("texto exemplo do pin bonitão cadastrado pelo usuário para a foto acima, que por sinal também é exemplo (última tirada)");
                    pinAuthor.setText(user.getUsername());
                    pinData.setText(new Date(System.currentTimeMillis()).toString());
                }
            } catch (Exception e) {
                Toast.makeText(this, "Não foi possível carregar a imagem pelo URI: excessão inesperada", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
