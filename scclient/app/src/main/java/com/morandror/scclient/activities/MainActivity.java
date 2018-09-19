package com.morandror.scclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.CompactStringObjectMap;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.morandror.scclient.R;
import com.morandror.scclient.utils.http.RequestQueueSingleton;
import com.morandror.scclient.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.morandror.scclient.utils.SharedStrings.ADD_USER_URL;
import static com.morandror.scclient.utils.SharedStrings.GET_USER_URL;

public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // Configure sign-in to request the User's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the User is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            getUserFromServer(account);
        } else {
            //show sign in button
            SignInButton signInButton = findViewById(R.id.sign_in_button);
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            System.out.println(result.getStatus().getStatusMessage());
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult();
            System.out.println(account.getDisplayName());
            getUserFromServer(account);
        } catch (Exception e){}
    }

    private void openUserPage(User user){
        Intent startNewActivity = new Intent(this, home_page_activity2.class);
        startNewActivity.putExtra(getString(R.string.user), user);
        startActivity(startNewActivity);
    }

    private void getUserFromServer(final GoogleSignInAccount account){
        try {
            JSONObject jsonObject = new JSONObject(new HashMap<String, String>() {{
                put("token",account.getIdToken());
            }});
            JsonObjectRequest request = new JsonObjectRequest
                    (GET_USER_URL, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            openUserPage(gson.fromJson(response.toString(), User.class));
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            User newUser = new User(account);
                            addNewUser(newUser);
                        }
                    });


            // Add the request to the RequestQueue.
            RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewUser(User newUser) {
        try {
            JSONObject newUserObj = new JSONObject(newUser.toJson());
            JsonObjectRequest request = new JsonObjectRequest(
                    ADD_USER_URL, newUserObj, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            openUserPage(gson.fromJson(response.toString(), User.class));
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Failed to add new user");
                        }
                    });


            // Add the request to the RequestQueue.
            RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}