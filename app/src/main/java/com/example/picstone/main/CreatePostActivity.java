package com.example.picstone.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.picstone.R;

import com.github.chrisbanes.photoview.PhotoView;


public class CreatePostActivity extends AppCompatActivity {


    PhotoView photoTaken;
    Button btnPublish;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        photoTaken = (PhotoView) findViewById(R.id.photoTaken);
        btnPublish = (Button) findViewById(R.id.btn_publish_post);
        btnCancel = (Button) findViewById(R.id.btn_cancel_post);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Uri uriId;

            try {
                uriId = Uri.parse(bundle.getString("resUri"));
                if (uriId != null) {
                    photoTaken.setImageURI(uriId);
                }
            } catch (Exception e) {
                Toast.makeText(this, "Não foi possível carregar a imagem pelo URI: excessão inesperada", Toast.LENGTH_SHORT).show();
            }

        }

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                handleFloatingActionButton(view);
                // TODO: submit post and enhance this if.
                if (true) {
                    setResult(Activity.RESULT_OK);
                }

                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                handleFloatingActionButton(view);
                // TODO: submit post and enhance this if.
                if (true) {
                    setResult(Activity.RESULT_CANCELED);
                }
                finish();
            }
        });

    }
}
