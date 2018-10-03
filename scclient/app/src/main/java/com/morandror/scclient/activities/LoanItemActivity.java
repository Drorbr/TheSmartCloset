package com.morandror.scclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.morandror.scclient.R;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.models.Friend;
import com.morandror.scclient.models.Item;
import com.morandror.scclient.utils.JsonHandler;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedHashMap;

import static com.morandror.scclient.utils.SharedStrings.ADD_ITEM_URL;
import static com.morandror.scclient.utils.SharedStrings.LOAN_ITEM_URL;
import static com.morandror.scclient.utils.SharedStrings.REQUEST_TIMEOUT;

public class LoanItemActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ViewGroup parent;
    int index;
    private TextView errorText;
    LinkedHashMap<EditText, String> etAndStringDict;
    //int closetId;
    int itemId;
    Friend newFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_item);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar = findViewById(R.id.progressBar_cyclic2);
                errorText = findViewById(R.id.loan_item_error_text);
                parent = (ViewGroup) errorText.getParent();
                index = parent.indexOfChild(errorText);
                //closetId = getIntent().getIntExtra(getString(R.string.closet_id), 0);
                itemId = getIntent().getIntExtra(getString(R.string.item_id), 0);
                etAndStringDict = initEtAndStringDict();
            }
        });
    }

    public void submitLoanAction(View view) {
        boolean foundError = false;
        String emptyErr = "Value can't be empty";

        showProgressBar();
        errorText.setVisibility(View.INVISIBLE);

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
            updateItemDetailsOnServer();
        else
            removeProgressBar();
    }

    private void updateItemDetailsOnServer() {
        newFriend = getFriendFromDict(etAndStringDict);

        try {
            JSONObject newItemObj = new JSONObject(newFriend.toJson());
            JsonObjectRequest request = new JsonObjectRequest(String.format(LOAN_ITEM_URL, itemId), newItemObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Loan item successfully");
                            returnToItemPage((Item) JsonHandler.getInstance().fromString(response.toString(), Item.class));
                            //returnToUserPage();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Failed to loan item - " + error.toString());
                    removeProgressBar();
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.error_insert_item);
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void returnToItemPage(Item item) {
        Intent startNewActivity = new Intent(getBaseContext(), ItemInfoPopUp.class);
        startNewActivity.putExtra(getString(R.string.item), item);
        startActivity(startNewActivity);
    }

/*    private void returnToUserPage() {//TODO:change to get closet
        Intent startNewActivity = new Intent(getBaseContext(), ClosetInfoActivity.class);
        startNewActivity.putExtra(getString(R.string.closet), closet);
        startActivity(startNewActivity);
    }*/

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

        etAndStringDict.put((EditText) findViewById(R.id.loan_item_email_val), null);
        etAndStringDict.put((EditText) findViewById(R.id.loan_item_first_name_val), null);
        etAndStringDict.put((EditText) findViewById(R.id.loan_item_last_name_val), null);

        return etAndStringDict;
    }

    private Friend getFriendFromDict(LinkedHashMap<EditText, String> etAndStringDict) {
       Friend friend = new Friend();

       friend.setFirstName(etAndStringDict.get((EditText) findViewById(R.id.loan_item_first_name_val)));
       friend.setLastName(etAndStringDict.get((EditText) findViewById(R.id.loan_item_last_name_val)));
       friend.setEmail(etAndStringDict.get((EditText) findViewById(R.id.loan_item_email_val)));

        return friend;
    }
}
