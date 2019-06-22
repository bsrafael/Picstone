package com.example.picstone.network;

import com.example.picstone.models.input.ChangePasswordInputModel;
import com.example.picstone.models.input.PostInputModel;
import com.example.picstone.models.input.TokenInputModel;
import com.example.picstone.models.input.UserInputModel;
import com.example.picstone.models.output.ImageViewModel;
import com.example.picstone.models.output.PostViewModel;
import com.example.picstone.models.output.TokenViewModel;
import com.example.picstone.models.output.UserViewModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

// TODO tirar parametro do header e ver como criar um interceptor
// TODO provavelmente ficaria mais organizado separar em 3 classes
public interface SoapstoneClient {
    // Tokens
    @POST("api/tokens")
    Call<TokenViewModel> getToken(@Body TokenInputModel inputModel);

    @GET("api/tokens")
    Call authenticate(@Header("Authorization") String token);

    // Users
    @GET("api/users")
    Call<List<UserViewModel>> getUsers(@Header("Authorization") String token);

    @GET("api/users")
    Call<List<UserViewModel>> getUsers(@Header("Authorization") String token, @Query("skip") int skip, @Query("take") int take);

    @GET("api/users/{id}")
    Call<UserViewModel> getUser(@Header("Authorization") String token, @Path(value = "id") String id);

    @GET("api/users/{id}/posts")
    Call<List<PostViewModel>> getUserPosts(@Header("Authorization") String token, @Path(value = "id") String id);

    @GET("api/users/{id}/posts")
    Call<List<PostViewModel>> getUserPosts(@Header("Authorization") String token, @Path(value = "id") String id, @Query("skip") int skip, @Query("take") int take);

    @GET("api/users/{id}/saved")
    Call<List<PostViewModel>> getUserSavedPosts(@Header("Authorization") String token, @Path(value = "id") String id);

    @GET("api/users/{id}/saved")
    Call<List<PostViewModel>> getUserSavedPosts(@Header("Authorization") String token, @Path(value = "id") String id, @Query("skip") int skip, @Query("take") int take);

    @POST("api/users")
    Call<UserViewModel> createUser(@Body UserInputModel inputModel);

    @PUT("api/users/{id}")
    Call changePassword(@Header("Authorization") String token, @Path(value = "id") String id, @Body ChangePasswordInputModel inputModel);

    @DELETE("api/users/{id}")
    Call deleteUser(@Header("Authorization") String token, @Path(value = "id") String id);

    // Posts
    @GET("api/posts")
    Call<List<PostViewModel>> getPostsNearUser(@Header("Authorization") String token, @Query("latitude") double latitude, @Query("longitude") double longitude);

    @GET("api/posts")
    Call<List<PostViewModel>> getPostsNearUser(@Header("Authorization") String token, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("skip") int skip, @Query("take") int take);

    @GET("api/posts/top")
    Call<List<PostViewModel>> getTopPosts(@Header("Authorization") String token);

    @GET("api/posts/top")
    Call<List<PostViewModel>> getTopPosts(@Header("Authorization") String token, @Query("skip") int skip, @Query("take") int take);

    @POST("api/posts/image") // TODO essa rota provavelmente vai mudar
    Call<ImageViewModel> uploadImage(@Header("Authorization") String token, @Part MultipartBody.Part image, @Part("name") RequestBody requestBody);
    // TODO finalizar: https://stackoverflow.com/questions/39953457/how-to-upload-image-file-in-retrofit-2

    @POST("api/posts")
    Call<PostViewModel> createPost(@Header("Authorization") String token, @Body PostInputModel inputModel);

    @DELETE("api/posts/{id}")
    Call deletePost(@Header("Authorization") String token, @Path(value = "id") String id);

    @PUT("api/posts/{id}/upvote")
    Call upvotePost(@Header("Authorization") String token, @Path(value = "id") String id);

    @PUT("api/posts/{id}/downvote")
    Call downvotePost(@Header("Authorization") String token, @Path(value = "id") String id);

    @PUT("api/posts/{id}/save")
    Call savePost(@Header("Authorization") String token, @Path(value = "id") String id);

    @PUT("api/posts/{id}/report")
    Call reportPost(@Header("Authorization") String token, @Path(value = "id") String id);
}
