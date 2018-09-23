package com.morandror.scclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.morandror.scclient.R;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedHashMap;

import static com.morandror.scclient.utils.SharedStrings.ADD_CLOSET_URL;

public class AddClosetActivity extends AppCompatActivity {
    ProgressBar progressBar;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_closet_activity);
        progressBar = findViewById(R.id.progressBar_cyclic);
        errorText = findViewById(R.id.add_closet_error_text);
    }

    protected void submitInfo(View button){
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.INVISIBLE);

        boolean foundError = false;
        LinkedHashMap<EditText, String> etAndStringDict = initEtAndStringDict();

        for (EditText et : etAndStringDict.keySet()){
            String value = et.getText().toString();
            if (TextUtils.isEmpty(value)){
                foundError = true;
                et.setError("Value can't be empty");
            } else {
                etAndStringDict.put(et, value);
            }
        }

        if (!foundError)
            injectClosetToServer(etAndStringDict);
        progressBar.setVisibility(View.GONE);
    }

    private LinkedHashMap<EditText, String> initEtAndStringDict() {
        LinkedHashMap<EditText, String> etAndStringDict = new LinkedHashMap<>();

        etAndStringDict.put((EditText)findViewById(R.id.closet_name_et), null);
        etAndStringDict.put((EditText)findViewById(R.id.closet_location_et), null);
        etAndStringDict.put((EditText)findViewById(R.id.closet_rfid_et), null);
        etAndStringDict.put((EditText)findViewById(R.id.closet_description_et), null);

        return etAndStringDict;
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
                    String errorStr = "Failed to add new closet - " + error.toString();
                    System.out.println(errorStr);
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(errorStr);
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
