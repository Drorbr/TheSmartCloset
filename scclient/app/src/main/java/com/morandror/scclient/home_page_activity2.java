package com.morandror.scclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.morandror.scclient.http.RequestQueueSingleton;

import org.json.JSONArray;

public class home_page_activity2 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_activity2);

        String url = "http://192.168.1.106:8080/scServer/checkdb";
        final TextView mTextView = findViewById(R.id.textView3);

        JsonArrayRequest jonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        mTextView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });


        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(this).getRequestQueue().add(jonArrayRequest);
    }
}
