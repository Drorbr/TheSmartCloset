package com.morandror.scclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.morandror.scclient.R;
import com.morandror.scclient.models.User;
import com.morandror.scclient.utils.JsonHandler;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.morandror.scclient.utils.SharedStrings.ADD_USER_URL;
import static com.morandror.scclient.utils.SharedStrings.GET_USER_BY_EMAIL_URL;
import static com.morandror.scclient.utils.SharedStrings.GET_USER_KEY;
import static com.morandror.scclient.utils.SharedStrings.REQUEST_TIMEOUT;
import static com.morandror.scclient.utils.SharedStrings.RESPONSE_USER_NO_EXIST;

public class MainActivity extends AppCompatActivity {
    static GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    ProgressBar progressBar;
    ViewGroup pbParent;

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        progressBar = findViewById(R.id.progressBarDummyMain);
        pbParent = (ViewGroup) progressBar.getParent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            getUserFromServer(account);
        } else {
            removeProgressBar();
            showSignInButton();
        }
    }

    private void showProgressBar() {
        setProgressBarVisibility1(View.VISIBLE);
    }


    private void removeProgressBar() {
        setProgressBarVisibility1(View.GONE);
    }

    private void setProgressBarVisibility1(int visibility) {
        progressBar.setVisibility(visibility);
    }

    private void showSignInButton() {
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setVisibility(View.VISIBLE);
    }

    private void signIn() {
        showProgressBar();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
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
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Failed to get user's account");
            removeProgressBar();
        }
    }

    private void openUserPage(User user) {
        Intent startNewActivity = new Intent(this, home_page_activity2.class);
        startNewActivity.putExtra(getString(R.string.user), user);
        startActivity(startNewActivity);
    }

    private void getUserFromServer(final GoogleSignInAccount account) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(new HashMap<String, String>() {{
                        put(GET_USER_KEY, account.getEmail());
                    }});
                    JsonObjectRequest request = new JsonObjectRequest
                            (GET_USER_BY_EMAIL_URL, jsonObject, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    openUserPage((User) JsonHandler.getInstance().fromString(response.toString(), User.class));
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error.networkResponse != null && error.networkResponse.statusCode == RESPONSE_USER_NO_EXIST) {
                                        User newUser = new User(account);
                                        addNewUser(newUser);
                                    } else {
                                        Toast.makeText(getBaseContext(), "Failed to get user data from server", Toast.LENGTH_LONG).show();
                                        showSignInButton();
                                        removeProgressBar();
                                        mGoogleSignInClient.signOut();
                                    }
                                }
                            });

                    request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueueSingleton.getInstance(getBaseContext()).getRequestQueue().add(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void addNewUser(final User newUser) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject newUserObj = new JSONObject(newUser.toJson());
                    JsonObjectRequest request = new JsonObjectRequest(
                            ADD_USER_URL, newUserObj, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            openUserPage((User) JsonHandler.getInstance().fromString(response.toString(), User.class));
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String message = "Failed to add new user";
                            System.out.println(message + " error, " + error.getMessage());
                            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                            showSignInButton();
                            removeProgressBar();
                            mGoogleSignInClient.signOut();
                        }
                    });

                    request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueueSingleton.getInstance(getBaseContext()).getRequestQueue().add(request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
