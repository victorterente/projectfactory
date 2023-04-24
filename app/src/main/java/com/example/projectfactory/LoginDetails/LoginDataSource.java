package com.example.projectfactory.LoginDetails;



import android.util.Log;

import com.example.projectfactory.LoginDetails.Model.LoggedInUser;
import com.example.projectfactory.Downloaders.JSONObjDownloader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoginDataSource {

    private String id;
    private JSONObject objLogin;
    public static int ID = 1;

    public LoginDataSource(){
    }


    public Result<LoggedInUser> login(String username, String password) {

        try {
            getJson(username, password);
            if (objLogin != null){
                LoggedInUser user =
                        new LoggedInUser(
                                id,
                                ""+username);
                return new Result.Success<>(user);
            } else {
                return new Result.Error(new IOException("Error logging in"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }


    public void getJson(String username, String password){
        JSONObjDownloader task = new JSONObjDownloader();
        String url = "https://wimuuv.herokuapp.com/api/student/login/"+username+"/"+password;
        try {
            objLogin = task.execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            objLogin = null;
        }

        Log.e("Login", ""+objLogin);

        try {
            id = objLogin.getString("id");
            Log.e("id", id);

            ID = Integer.parseInt(id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
