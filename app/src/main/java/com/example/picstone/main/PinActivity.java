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

    public static final String ID_KEY = "idMarker";
    public static final String PHOTO_KEY = "photoMarker";
    public static final String TEXT_KEY = "textMarker";
    public static final String AUTHOR_KEY = "authorMarker";
    public static final String DATA_KEY = "dataMarker";

    private PhotoView pinPhoto;
    private TextView pinText;
    private TextView pinAuthor;
    private TextView pinData;
    private User user = null;

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
                idMarker = bundle.getString(ID_KEY);
                if (idMarker != null) {
                    String imageUri = bundle.getString(PHOTO_KEY);
                    if (imageUri != null)
                        pinPhoto.setImageURI(Uri.parse(imageUri));

                    String text = bundle.getString(TEXT_KEY);
                    if (text != null)
                        pinText.setText(text);

                    String author = bundle.getString(AUTHOR_KEY);
                    if (author != null)
                        pinAuthor.setText(author);

                    String data = bundle.getString((DATA_KEY));
                    if (data != null)
                        pinData.setText(data);
                }
            } catch (Exception e) {
                Toast.makeText(this, "Não foi possível carregar a imagem pelo URI: excessão inesperada", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
