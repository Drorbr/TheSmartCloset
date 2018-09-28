package com.morandror.scclient.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.morandror.scclient.R;
import com.morandror.scclient.models.Item;

import java.text.SimpleDateFormat;

public class ItemInfoPopUp extends Activity {

    Item item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.item_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

            getWindow().setLayout((int)(width*.85), (int)(height*.75));

        item = (Item)getIntent().getSerializableExtra(getString(R.string.item));

        setTexts();

    }

    private void setTexts() {

        TextView usage = findViewById(R.id.item_usage_key);
        usage.setText(R.string.item_usage_key);

        TextView ru = findViewById(R.id.item_recentlyUsed_key);
        ru.setText(R.string.item_recentlyUsed_key);

        TextView brand = findViewById(R.id.item_brand_key);
        brand.setText(R.string.item_brand_key);

        TextView usageVal = findViewById(R.id.item_usage_value);
        usageVal.setText(String.valueOf(item.getUsage()));

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:MM");
        TextView ruVal = findViewById(R.id.item_recentlyUsed_value);
        ruVal.setText(String.valueOf(DATE_FORMAT.format(item.getRecentlyUsed())));//format date

        TextView brandVal = findViewById(R.id.item_brand_value);
        brandVal.setText(String.valueOf(item.getBrand()));

        TextView location = findViewById(R.id.item_location);
        location.setText(item.getFoundAt());
    }
}