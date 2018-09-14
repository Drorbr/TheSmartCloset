package com.morandror.scclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.morandror.scclient.R;
import com.morandror.scclient.models.User;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import org.json.JSONArray;

public class home_page_activity2 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_activity2);

        String url = "http://localhost:8080/scServer/checkdb";
        final TextView mTextView = findViewById(R.id.textView3);

        User user = (User)getIntent().getSerializableExtra(getString(R.string.user));

        try {
            mTextView.setText(user.toJson());
        } catch (JsonProcessingException e) {
            System.out.println("Failed to show user details");
        }
//        JsonArrayRequest jonArrayRequest = new JsonArrayRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        mTextView.setText("Response: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO: Handle error
//
//                    }
//                });
//
//
//        // Add the request to the RequestQueue.
//        RequestQueueSingleton.getInstance(this).getRequestQueue().add(jonArrayRequest);

    }

}
