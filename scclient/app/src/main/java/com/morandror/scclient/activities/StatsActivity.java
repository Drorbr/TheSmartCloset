package com.morandror.scclient.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.morandror.scclient.R;
import com.morandror.scclient.adapters.ExpandableListAdapter;
import com.morandror.scclient.models.Item;
import com.morandror.scclient.models.Statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatsActivity extends AppCompatActivity {
    Statistics stats;
    ExpandableListView expListView;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<Item>> listDataChild;

    private static final String LAST_DAYS = "Last days";
    private static final String NEWEST = "Newest items";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        stats = (Statistics) getIntent().getSerializableExtra(getString(R.string.stats));

        setValues();
    }

    private void setValues() {
        TextView mostUsed = findViewById(R.id.most_used_value);
        if (!TextUtils.isEmpty(stats.getMostUsed().getBrand()))
            mostUsed.setText(stats.getMostUsed().getBrand());
        mostUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getBaseContext(), ItemInfoPopUp.class);
                newIntent.putExtra(getString(R.string.item), stats.getMostUsed());
                startActivity(newIntent);
            }
        });

        TextView lovedColor = findViewById(R.id.loved_color_value);
        if (!TextUtils.isEmpty(stats.getFavoriteColor())) {
            lovedColor.setText(stats.getFavoriteColor());
            try {
                int colorInt = Color.parseColor(stats.getFavoriteColor());
                lovedColor.setTextColor(colorInt);
            } catch (IllegalArgumentException e) {
                System.out.println("Didn't find color, use default");
            }
        }

        expListView = findViewById(R.id.exp_list_stats);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, R.layout.list_item);

        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int groupPosition, int childPosition, long l) {
                Item item = (Item) listAdapter.getChild(groupPosition,childPosition);
                Intent newIntent = new Intent(getBaseContext()  , ItemInfoPopUp.class);
                newIntent.putExtra(getString(R.string.item), item);
                startActivity(newIntent);
                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        if (stats.getLastDays() != null && !stats.getLastDays().isEmpty())
            listDataHeader.add(LAST_DAYS);
            listDataChild.put(LAST_DAYS, stats.getLastDays());

        if (stats.getNewestItems() != null && !stats.getNewestItems().isEmpty())
            listDataHeader.add(NEWEST);
            listDataChild.put(NEWEST, stats.getNewestItems());
    }
}
