package com.morandror.scclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.morandror.scclient.R;
import com.morandror.scclient.adapters.ClosetAdapter;
import com.morandror.scclient.adapters.deleteClosetListener;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.models.Statistics;
import com.morandror.scclient.models.User;
import com.morandror.scclient.utils.JsonHandler;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.morandror.scclient.activities.MainActivity.mGoogleSignInClient;
import static com.morandror.scclient.utils.SharedStrings.GET_USER_STATS;
import static com.morandror.scclient.utils.SharedStrings.REQUEST_TIMEOUT;

public class home_page_activity2 extends AppCompatActivity implements deleteClosetListener {
    private User user;
    private static ClosetAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private static ArrayList<Closet> data;
    static View.OnClickListener myOnClickListener;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_activity2);

        user = (User) getIntent().getSerializableExtra(getString(R.string.user));

        final TextView mTextView = findViewById(R.id.welcomeSign);
        mTextView.setText(String.format(getString(R.string.welcomeActivity2), user.getFirstName(), user.getLastName()));

        recyclerView = findViewById(R.id.my_recycler_view);

        handleClosets();
        setBottomNavigationMenu();
    }

    private void setBottomNavigationMenu() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bottomNavigationView = findViewById(R.id.bottom_navigation);
                bottomNavigationView.setOnNavigationItemSelectedListener(
                        new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.add_closet:
                                        Intent startNewActivity = new Intent(getBaseContext(), AddClosetActivity.class);
                                        startNewActivity.putExtra(getString(R.string.user), user);
                                        startActivity(startNewActivity);
                                        return true;
                                    case R.id.user_stats:
                                        startStatsActivity();
                                        return true;
                                    case R.id.log_out:
                                        mGoogleSignInClient.signOut()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Intent goBackHome = new Intent(getApplicationContext(), MainActivity.class);
                                                        goBackHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(goBackHome);

                                                    }
                                                });
                                        return true;
                                }
                                return false;
                            }
                        });
            }
        });
    }

    private void startStatsActivity() {
        JsonObjectRequest request = new JsonObjectRequest
                (String.format(GET_USER_STATS, user.getId()), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Intent statsIntent = new Intent(getBaseContext(), StatsActivity.class);
                        statsIntent.putExtra(getString(R.string.stats), (Statistics) JsonHandler.getInstance().fromString(response.toString(), Statistics.class));
                        startActivity(statsIntent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Failed to get statistics, error: " + error.toString());
                        Toast.makeText(getBaseContext(), "Failed to get user statistics", Toast.LENGTH_LONG).show();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);
    }

    private void handleClosets() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (user.getClosets() == null || user.getClosets().isEmpty()) {
                    showNoClosets();
                } else {
                    showUsersClosets();
                }
            }
        });
    }

    private void showUsersClosets() {
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();

        data.addAll(user.getClosets());

        adapter = new ClosetAdapter(data);
        adapter.setDeleteClosetListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void showNoClosets() {
        View emptyView = findViewById(R.id.no_closet_text);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClosetDeleted(Closet closet) {
        user.getClosets().remove(closet);
        handleClosets();
    }
}
