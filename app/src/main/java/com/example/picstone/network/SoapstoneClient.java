package com.example.picstone.network;

import com.example.picstone.models.input.ChangePasswordInputModel;
import com.example.picstone.models.input.PostInputModel;
import com.example.picstone.models.output.ImageViewModel;
import com.example.picstone.models.output.PostViewModel;
import com.example.picstone.models.output.UserViewModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SoapstoneClient {
    // Tokens
    @GET("api/tokens")
    Call authenticate();

    // Users
    @GET("api/users")
    Call<List<UserViewModel>> getUsers();

    @GET("api/users")
    Call<List<UserViewModel>> getUsers(@Query("skip") int skip, @Query("take") int take);

    @GET("api/users/{id}")
    Call<UserViewModel> getUser(@Path(value = "id") String id);

    @GET("api/users/{id}/posts")
    Call<List<PostViewModel>> getUserPosts(@Path(value = "id") String id);

    @GET("api/users/{id}/posts")
    Call<List<PostViewModel>> getUserPosts(@Path(value = "id") String id, @Query("skip") int skip, @Query("take") int take);

    @GET("api/users/{id}/saved")
    Call<List<PostViewModel>> getUserSavedPosts(@Path(value = "id") String id);

    @GET("api/users/{id}/saved")
    Call<List<PostViewModel>> getUserSavedPosts(@Path(value = "id") String id, @Query("skip") int skip, @Query("take") int take);

    @PUT("api/users/{id}")
    Call changePassword(@Path(value = "id") String id, @Body ChangePasswordInputModel inputModel);

    @DELETE("api/users/{id}")
    Call deleteUser(@Path(value = "id") String id);

    // Posts
    @GET("api/posts")
    Call<List<PostViewModel>> getPostsNearUser(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @GET("api/posts")
    Call<List<PostViewModel>> getPostsNearUser(@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("skip") int skip, @Query("take") int take);

    @GET("api/posts/top")
    Call<List<PostViewModel>> getTopPosts();

    @GET("api/posts/top")
    Call<List<PostViewModel>> getTopPosts(@Query("skip") int skip, @Query("take") int take);

    @POST("api/posts/image") // TODO essa rota provavelmente vai mudar
    Call<ImageViewModel> uploadImage(@Part MultipartBody.Part image, @Part("name") RequestBody requestBody);
    // TODO finalizar: https://stackoverflow.com/questions/39953457/how-to-upload-image-file-in-retrofit-2

    @POST("api/posts")
    Call<PostViewModel> createPost(@Body PostInputModel inputModel);

    @DELETE("api/posts/{id}")
    Call deletePost(@Path(value = "id") String id);

    @PUT("api/posts/{id}/upvote")
    Call upvotePost(@Path(value = "id") String id);

    @PUT("api/posts/{id}/downvote")
    Call downvotePost(@Path(value = "id") String id);

    @PUT("api/posts/{id}/save")
    Call savePost(@Path(value = "id") String id);

    @PUT("api/posts/{id}/report")
    Call reportPost(@Path(value = "id") String id);
}
