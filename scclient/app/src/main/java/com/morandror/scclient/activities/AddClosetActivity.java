package com.morandror.scclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.morandror.scclient.R;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.models.User;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import static com.morandror.scclient.utils.SharedStrings.ADD_CLOSET_URL;
import static com.morandror.scclient.utils.SharedStrings.ADD_USER_URL;

public class AddClosetActivity extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_closet_activity);
    }

    protected void submitInfo(View button){
        progressBar = findViewById(R.id.progressBar_cyclic);
        progressBar.setVisibility(View.VISIBLE);
        LinkedHashMap<EditText, String> etAndStringDict = new LinkedHashMap<>();
        etAndStringDict.put((EditText)findViewById(R.id.closet_name_et), null);
        etAndStringDict.put((EditText)findViewById(R.id.closet_location_et), null);
        etAndStringDict.put((EditText)findViewById(R.id.closet_rfid_et), null);
        etAndStringDict.put((EditText)findViewById(R.id.closet_description_et), null);

        for (EditText et : etAndStringDict.keySet()){
            String value = et.getText().toString();
            if (TextUtils.isEmpty(value)){
                et.setError("Value can't be empty");
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                etAndStringDict.put(et, value);
            }
        }

        injectClosetToServer(etAndStringDict);
    }

    private void injectClosetToServer(LinkedHashMap<EditText, String> etAndStringDict) {
        Closet closet = getClosetFromDict(etAndStringDict);

        try {
            JSONObject newUserObj = new JSONObject(closet.toJson());
            JsonObjectRequest request = new JsonObjectRequest(
                ADD_CLOSET_URL, newUserObj, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //goBack
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Failed to add new closet");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });


            // Add the request to the RequestQueue.
            RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);
        } catch (JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private Closet getClosetFromDict(LinkedHashMap<EditText, String> etAndStringDict) {
        Closet closet = new Closet();

        closet.setDescription(etAndStringDict.get((EditText)findViewById(R.id.closet_description_et)));
        closet.setName(etAndStringDict.get((EditText)findViewById(R.id.closet_name_et)));
        closet.setRfidScanner(etAndStringDict.get((EditText)findViewById(R.id.closet_rfid_et)));
        closet.setLocation(etAndStringDict.get((EditText)findViewById(R.id.closet_location_et)));
        closet.setInsertDate(new Date());

        return closet;
    }
}
