package com.example.picstone.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.picstone.R;

import com.example.picstone.User;
import com.example.picstone.models.input.PostInputModel;
import com.example.picstone.models.output.ImageViewModel;
import com.example.picstone.models.output.PostViewModel;
import com.example.picstone.network.ClientFactory;
import com.example.picstone.network.SoapstoneClient;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreatePostActivity extends AppCompatActivity {


    private PhotoView photoTaken;
    private Button btnPublish;
    private Button btnCancel;
    private EditText edtMessage;

    private SoapstoneClient client;

    private Uri imageUri;
    private String uploadedUrl;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        photoTaken = (PhotoView) findViewById(R.id.photoTaken);
        btnPublish = (Button) findViewById(R.id.btn_publish_post);
        btnCancel = (Button) findViewById(R.id.btn_cancel_post);
        edtMessage = findViewById(R.id.editText); // TODO probably a good idea to change

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Uri uriId;

            try {
                uriId = Uri.parse(bundle.getString("resUri"));
                if (uriId != null) {
                    imageUri = uriId;
                    photoTaken.setImageURI(uriId);
                }
            } catch (Exception e) {
                Toast.makeText(this, "Não foi possível carregar a imagem pelo URI: excessão inesperada", Toast.LENGTH_SHORT).show();
            }

        }

        try
        {
            String token = User.getInstance().getToken();
            client = ClientFactory.GetSoapstoneClient(token);
        }
        catch (Exception e)
        {
            // TODO on unauthorized
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate())
                    return;

                uploadedUrl = "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/bc8f83c5-2e9c-472f-9146-37d230ff5d99/d5wdr3p-3931de90-4cfb-4d83-bbd1-0132cea3f296.jpg/v1/fill/w_774,h_1033,q_70,strp/orange_guidance_soapstone_by_hellduck_d5wdr3p-pre.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9MTM2NiIsInBhdGgiOiJcL2ZcL2JjOGY4M2M1LTJlOWMtNDcyZi05MTQ2LTM3ZDIzMGZmNWQ5OVwvZDV3ZHIzcC0zOTMxZGU5MC00Y2ZiLTRkODMtYmJkMS0wMTMyY2VhM2YyOTYuanBnIiwid2lkdGgiOiI8PTEwMjQifV1dLCJhdWQiOlsidXJuOnNlcnZpY2U6aW1hZ2Uub3BlcmF0aW9ucyJdfQ.yiXdHr2s87R_GR40JElaJKqIII0lmmWF-npppsQg4Gw";

                if (uploadedUrl == null)
                    UploadImage();
                else
                    CreatePost();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

    }

    private void UploadImage() {
        File file = new File(imageUri.getPath());
         RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        client.uploadImage(body).enqueue(new Callback<ImageViewModel>() {
            @Override
            public void onResponse(Call<ImageViewModel> call, Response<ImageViewModel> response) {
                if (response.isSuccessful()) {
                    uploadedUrl = response.body().getImageUrl();
                    CreatePost();
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG);
                    System.out.println("\n\n\n" + response.message());
                }
            }

            @Override
            public void onFailure(Call<ImageViewModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falha ao fazer o upload da imagem " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void CreatePost() {
        PostInputModel inputModel = new PostInputModel();
        inputModel.setImageUrl(uploadedUrl);
        inputModel.setLatitude(User.getInstance().getLocation().latitude);
        inputModel.setLongitude(User.getInstance().getLocation().longitude);
        inputModel.setMessage(edtMessage.getText().toString());

        client.createPost(inputModel).enqueue(new Callback<PostViewModel>() {
            @Override
            public void onResponse(Call<PostViewModel> call, Response<PostViewModel> response) {
                if (response.isSuccessful()) {
                    setResult(Activity.RESULT_OK);
                }

                System.out.println(response.message());
                finish();
            }

            @Override
            public void onFailure(Call<PostViewModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falha ao criar o post " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate() {
        if (imageUri == null)
            return false;

        String message = edtMessage.getText().toString();

        if (message.length() == 0) {
            Toast.makeText(getApplicationContext(), "Preencha a mensagem", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (message.length() > 80) {
            Toast.makeText(getApplicationContext(), "Mensagem longa demais", Toast.LENGTH_SHORT).show();;
            return false;
        }

        return true;
    }
}
