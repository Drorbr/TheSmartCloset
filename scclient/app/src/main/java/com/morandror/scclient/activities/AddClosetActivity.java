package com.morandror.scclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.morandror.scclient.R;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.models.User;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;

import static com.morandror.scclient.utils.SharedStrings.ADD_CLOSET_URL;
import static com.morandror.scclient.utils.SharedStrings.ASSIGN_CLOSET_TO_USER_URL;

public class AddClosetActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ViewGroup parent;
    int index;
    private TextView errorText;
    private Gson gson = new Gson();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_closet_activity);
        progressBar = findViewById(R.id.progressBar_cyclic2);
        errorText = findViewById(R.id.add_closet_error_text);
        parent = (ViewGroup) errorText.getParent();
        index = parent.indexOfChild(errorText);
        user = (User)getIntent().getSerializableExtra(getString(R.string.user));
    }

    protected void submitInfo(View button){
        showProgressBar();
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
    }

    private void showProgressBar() {
        setProgressBarVisibility1(View.VISIBLE);
    }


    private void removeProgressBar() {
        parent.removeView(progressBar);
    }

    private void setProgressBarVisibility1(int visibility) {
        ProgressBar newPB = new ProgressBar(this);
        newPB.setVisibility(visibility);
        newPB.setPadding(0, (int) getResources().getDimension(R.dimen.progressbar_top_padding), 0, 0);
        newPB = (ProgressBar)getLayoutInflater().inflate(R.layout.mypb, parent, false);
        parent.addView(newPB, index);
        progressBar = newPB;
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
                    System.out.println(getString(R.string.add_closet_success));
                    Closet newCloset = gson.fromJson(response.toString(), Closet.class);
                    assignClosetToUser(newCloset);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Failed to add new closet - " + error.toString());
                    removeProgressBar();
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.error_insert_closet);
                    
                }
            });


            // Add the request to the RequestQueue.
            RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);
        } catch (JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    private void assignClosetToUser(Closet newCloset) {
        if (user.getClosets() == null)
            user.setClosets(new HashSet<Closet>());
        user.getClosets().add(newCloset);
        StringRequest request = new StringRequest(String.format(ASSIGN_CLOSET_TO_USER_URL, user.getId(), newCloset.getId()),
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        System.out.println("Assign closet to user succeeded");
                        Intent startNewActivity = new Intent(getBaseContext(), home_page_activity2.class);
                        startNewActivity.putExtra(getString(R.string.user), user);
                        startActivity(startNewActivity);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Failed to assign closet to user");
                    }
        });

        RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);
    }

    private Closet getClosetFromDict(LinkedHashMap<EditText, String> etAndStringDict) {
        Closet closet = new Closet();

        closet.setDescription(etAndStringDict.get(findViewById(R.id.closet_description_et)));
        closet.setName(etAndStringDict.get(findViewById(R.id.closet_name_et)));
        closet.setRfidScanner(etAndStringDict.get(findViewById(R.id.closet_rfid_et)));
        closet.setLocation(etAndStringDict.get(findViewById(R.id.closet_location_et)));
        closet.setInsertDate(new Date());

        return closet;
    }
}
