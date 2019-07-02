package com.example.picstone.data;

import com.example.picstone.data.model.LoggedInUser;
import com.example.picstone.models.input.TokenInputModel;
import com.example.picstone.models.output.TokenViewModel;
import com.example.picstone.network.ClientFactory;
import com.example.picstone.network.SoapstoneClientPublic;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        SoapstoneClientPublic client = ClientFactory.GetSoapstoneClient();

        try {
            Response<TokenViewModel> auth = client.getToken(new TokenInputModel(username, password)).execute();
            System.out.println("\n-----Try (LoginDataSource:24)");
            if (!auth.isSuccessful()) {
                System.out.println("\n------Not successful (LoginDataSource:26)");

                return new Result.Unauthorized(auth.message());
            }

            LoggedInUser user =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "TODO put username on response body", // TODO put username on response body
                            auth.body().getToken());
            System.out.println("\n------Successful (LoginDataSource:36)");

            return new Result.Success<>(user);
        } catch (Exception e) {
            System.out.println("\n-----Catch (LoginDataSource:40)");
            e.printStackTrace();

            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
    }
}
