package com.morandror.scclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.morandror.scclient.R;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.models.User;
import com.morandror.scclient.utils.JsonHandler;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedHashMap;

import static com.morandror.scclient.utils.SharedStrings.ADD_CLOSET_AND_ASSIGN_URL;
import static com.morandror.scclient.utils.SharedStrings.ASSIGN_CLOSET_TO_USER_URL;
import static com.morandror.scclient.utils.SharedStrings.GET_CLOSET_URL;
import static com.morandror.scclient.utils.SharedStrings.GET_USER_BY_ID_URL;
import static com.morandror.scclient.utils.SharedStrings.REQUEST_TIMEOUT;

public class AddClosetActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ViewGroup parent;
    int index;
    private TextView errorText;
    User user;
    RadioButton newClosetRdBtn;
    RadioButton existingClosetRdBtn;
    LinkedHashMap<EditText, String> etAndStringDict;
    EditText closetIdEt;
    Closet newCloset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_closet_activity);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar = findViewById(R.id.progressBar_cyclic2);
                errorText = findViewById(R.id.add_closet_error_text);
                parent = (ViewGroup) errorText.getParent();
                index = parent.indexOfChild(errorText);
                user = (User) getIntent().getSerializableExtra(getString(R.string.user));
                newClosetRdBtn = findViewById(R.id.rdBtnNew);
                existingClosetRdBtn = findViewById(R.id.rdBtnExisting);
                etAndStringDict = initEtAndStringDict();
                closetIdEt = findViewById(R.id.closet_id_et);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newClosetRdBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            manageNewClosetTextLines(true);
                            closetIdEt.setEnabled(false);
                        }
                    }
                });

                existingClosetRdBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            manageNewClosetTextLines(false);
                            closetIdEt.setEnabled(true);
                        }
                    }
                });
            }
        });
    }

    private void manageNewClosetTextLines(boolean enabled) {
        for (EditText et : etAndStringDict.keySet()) {
            et.setEnabled(enabled);
        }
    }

    protected void submitInfo(View button) {
        boolean foundError = false;
        String emptyErr = "Value can't be empty";

        showProgressBar();
        errorText.setVisibility(View.INVISIBLE);
        if (existingClosetRdBtn.isChecked()) {
            String value = closetIdEt.getText().toString();
            if (TextUtils.isEmpty(value)) {
                foundError = true;
                closetIdEt.setError(emptyErr);
            } else {
                getClosetAndAssign(value);
            }
        } else {

            for (EditText et : etAndStringDict.keySet()) {
                String value = et.getText().toString();
                if (TextUtils.isEmpty(value)) {
                    foundError = true;
                    et.setError(emptyErr);
                } else {
                    etAndStringDict.put(et, value);
                }
            }

            if (!foundError)
                injectClosetToServer();
        }
        if (foundError)
            removeProgressBar();
    }

    private void getClosetAndAssign(final String closetId) {
        JsonObjectRequest request = new JsonObjectRequest
                (String.format(GET_CLOSET_URL, closetId), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        newCloset = (Closet) JsonHandler.getInstance().fromString(response.toString(), Closet.class);
                        assignClosetToUser();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Failed to get closet with id: " + closetId + " from server");
                        removeProgressBar();
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText(R.string.error_assign_closet);
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);
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
        newPB = (ProgressBar) getLayoutInflater().inflate(R.layout.mypb, parent, false);
        parent.addView(newPB, index);
        progressBar = newPB;
    }

    private LinkedHashMap<EditText, String> initEtAndStringDict() {
        LinkedHashMap<EditText, String> etAndStringDict = new LinkedHashMap<>();

        etAndStringDict.put((EditText) findViewById(R.id.closet_name_et), null);
        etAndStringDict.put((EditText) findViewById(R.id.closet_location_et), null);
        etAndStringDict.put((EditText) findViewById(R.id.closet_rfid_et), null);
        etAndStringDict.put((EditText) findViewById(R.id.closet_description_et), null);

        return etAndStringDict;
    }

    private void injectClosetToServer() {
        Closet closet = getClosetFromDict(etAndStringDict);

        try {
            JSONObject newClosetObj = new JSONObject(closet.toJson());
            JsonObjectRequest request = new JsonObjectRequest(String.format(ADD_CLOSET_AND_ASSIGN_URL, user.getId()), newClosetObj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(getString(R.string.add_closet_success));
                            newCloset = (Closet) JsonHandler.getInstance().fromString(response.toString(), Closet.class);
                            returnToUserPage();
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

            request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void returnToUserPage() {
        JsonObjectRequest request = new JsonObjectRequest(
                String.format(GET_USER_BY_ID_URL, user.getId()), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Got user from server");
                        Intent startNewActivity = new Intent(getBaseContext(), home_page_activity2.class);
                        startNewActivity.putExtra(getString(R.string.user), (User) JsonHandler.getInstance().fromString(response.toString(), User.class));
                        startNewActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(startNewActivity);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Failed to get user from server");
                removeProgressBar();
                errorText.setVisibility(View.VISIBLE);
                errorText.setText(R.string.error_get_user);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);

    }


    private void assignClosetToUser() {
        StringRequest request = new StringRequest(String.format(ASSIGN_CLOSET_TO_USER_URL, user.getId(), newCloset.getId()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Assign closet to user succeeded");
                        returnToUserPage();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Failed to assign closet to user");
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
