package com.example.picstone.main;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.picstone.R;
import com.example.picstone.User;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
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
//                    String imageUri = bundle.getString(PHOTO_KEY);
//                    if (imageUri != null)
//                        pinPhoto.setImageURI(Uri.parse(imageUri));
                    String imageUrl = bundle.getString(PHOTO_KEY);
                    if (imageUrl != null) {
                        if ( imageUrl.contains("http")) {
//                            new DownloadImageFromInternet(pinPhoto).execute(imageUrl);
                            Picasso.get().load(imageUrl).into(pinPhoto);
                        }else {
                            new DownloadImageFromInternet(pinPhoto).execute("http://" + imageUrl);
                            Picasso.get().load("http://" + imageUrl).into(pinPhoto);
                        }
//                        Picasso.get().load(imageUrl).centerCrop().into(pinPhoto);
//                        System.out.println("\n\n" + imageUrl + "\n\n");

                    }



                    String text = bundle.getString(TEXT_KEY);
                    if (text != null)
                        pinText.setText(text);

                    String author = bundle.getString(AUTHOR_KEY);
                    if (author != null) {
                        pinAuthor.setText(author);
//                        pinAuthor.setText(user.getUsername());
                    }

                    String data = bundle.getString((DATA_KEY));
                    if (data != null)
                        pinData.setText(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Não foi possível carregar a imagem pelo URI: excessão inesperada", Toast.LENGTH_SHORT).show();
            }

        }
    }



    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageFromInternet(PhotoView view) {
            pinPhoto = view;
            Toast.makeText(getApplicationContext(), "Baixando a foto, aguarde...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            pinPhoto.setImageBitmap(result);
        }
    }

}
