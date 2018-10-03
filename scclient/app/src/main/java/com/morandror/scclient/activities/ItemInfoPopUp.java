package com.morandror.scclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        getWindow().setLayout((int) (width * .85), (int) (height * .75));

        item = (Item) getIntent().getSerializableExtra(getString(R.string.item));

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

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        TextView ruVal = findViewById(R.id.item_recentlyUsed_value);
        if (item.getRecently_used() != null)
            ruVal.setText(String.valueOf(DATE_FORMAT.format(item.getRecently_used())));//format date

        TextView brandVal = findViewById(R.id.item_brand_value);
        brandVal.setText(String.valueOf(item.getBrand()));

        TextView location = findViewById(R.id.item_location);
        location.setText(item.getFoundAt());

        if (item.getImage() != null && !item.getImage().isEmpty()) {
            ImageView imageView = findViewById(R.id.imageView3);
            byte[] itemImageBytes = Base64.decode(item.getImage(), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(itemImageBytes, 0, itemImageBytes.length);
            if (bmp != null) {
                imageView.setImageBitmap(Bitmap.createBitmap(bmp));
            }
        }

        Button remainder = findViewById(R.id.button_reminder);
        if (location.getText().toString().equalsIgnoreCase("me")) {
            remainder.setEnabled(false);
        } else {
            remainder.setEnabled(true);
        }
    }

    public void openLoanForm(View view) {
        Intent loanItem = new Intent(getBaseContext(), LoanItemActivity.class);
        loanItem.putExtra(getString(R.string.item_id), item.getId());
        loanItem.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(loanItem);
    }

    public void sendRemainder(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{item.getFriendEmail()});
        i.putExtra(Intent.EXTRA_SUBJECT, "Reminder from 'Ths Smart Closet' App");
        String msg = String.format("Hi %s,\nDon't forget to bring my back my %s.\nThanks in advance", item.getFoundAt(), item);
        i.putExtra(Intent.EXTRA_TEXT, msg);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ItemInfoPopUp.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}