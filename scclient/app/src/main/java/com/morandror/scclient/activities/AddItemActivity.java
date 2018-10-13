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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.morandror.scclient.R;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.models.Item;
import com.morandror.scclient.models.QrCodeData;
import com.morandror.scclient.utils.JsonHandler;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedHashMap;

import static com.morandror.scclient.utils.SharedStrings.ADD_ITEM_URL;
import static com.morandror.scclient.utils.SharedStrings.REQUEST_TIMEOUT;

public class AddItemActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ViewGroup parent;
    int index;
    private TextView errorText;
    LinkedHashMap<EditText, String> etAndStringDict;
    Item newItem;
    int closetId;
    QrCodeData qrCodeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar = findViewById(R.id.progressBar_cyclic2);
                errorText = findViewById(R.id.add_item_error_text);
                parent = (ViewGroup) errorText.getParent();
                index = parent.indexOfChild(errorText);
                closetId = getIntent().getIntExtra(getString(R.string.closet_id), 0);
                qrCodeData = (QrCodeData) getIntent().getSerializableExtra(getString(R.string.qrData));
                etAndStringDict = initEtAndStringDict();

                if(qrCodeData != null) {
                    EditText editText = findViewById(R.id.add_item_type_val);
                    editText.setText(qrCodeData.getType());
                    editText = findViewById(R.id.add_item_brand_val);
                    editText.setText(qrCodeData.getBrand());
                    editText = findViewById(R.id.add_item_color_val);
                    editText.setText(qrCodeData.getColor());
                    editText = findViewById(R.id.add_item_size_val);
                    editText.setText(qrCodeData.getSize());
                }
            }
        });
    }

    protected void submitItem(View button) {
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
            injectItemToServer();
        else
            removeProgressBar();
    }

    private void injectItemToServer() {
        newItem = getItemFromDict(etAndStringDict);

        try {
            JSONObject newItemObj = new JSONObject(newItem.toJson());
            JsonObjectRequest request = new JsonObjectRequest(String.format(ADD_ITEM_URL, closetId), newItemObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Item added successfully");
                            returnToClosetInfo((Closet) JsonHandler.getInstance().fromString(response.toString(), Closet.class));
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Failed to add new item - " + error.toString());
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

    private void returnToClosetInfo(Closet closet) {
        Intent startNewActivity = new Intent(getBaseContext(), ClosetInfoActivity.class);
        startNewActivity.putExtra(getString(R.string.closet), closet);
        startNewActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startNewActivity);
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

        etAndStringDict.put((EditText) findViewById(R.id.add_item_type_val), null);
        etAndStringDict.put((EditText) findViewById(R.id.add_item_brand_val), null);
        etAndStringDict.put((EditText) findViewById(R.id.add_item_color_val), null);
        etAndStringDict.put((EditText) findViewById(R.id.add_item_foundAt_val), null);
        etAndStringDict.put((EditText) findViewById(R.id.add_item_size_val), null);

        return etAndStringDict;
    }

    private Item getItemFromDict(LinkedHashMap<EditText, String> etAndStringDict) {
        Item item = new Item();

        item.setBrand(etAndStringDict.get((EditText) findViewById(R.id.add_item_brand_val)));
        item.setType(etAndStringDict.get((EditText) findViewById(R.id.add_item_type_val)));
        item.setColor(etAndStringDict.get((EditText) findViewById(R.id.add_item_color_val)));
        item.setSize(etAndStringDict.get((EditText) findViewById(R.id.add_item_size_val)));
        item.setFoundAt(etAndStringDict.get((EditText) findViewById(R.id.add_item_foundAt_val)));
        item.setUsage(0);
        item.setInsert_date(new Date());
        item.setImage(qrCodeData != null ? qrCodeData.getImage() : null);

        return item;
    }

    public void scanQR(View view) {
        Intent scanQR = new Intent(getBaseContext(), ScanQRActivity.class);
        scanQR.putExtra(getString(R.string.closet_id), closetId);
        scanQR.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(scanQR);
    }
}
