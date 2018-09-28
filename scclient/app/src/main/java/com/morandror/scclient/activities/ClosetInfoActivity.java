package com.morandror.scclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.morandror.scclient.R;
import com.morandror.scclient.adapters.ExpandableListAdapter;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.models.Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ClosetInfoActivity extends AppCompatActivity {
    TextView name;
    TextView description;
    TextView location;

    Closet closet;
    List<Item> closetItems;

    ExpandableListView expListView;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<Item>> listDataChild;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet_info);

        closet = (Closet) getIntent().getSerializableExtra(getString(R.string.closet));

        name = findViewById(R.id.closet_info_name);
        description = findViewById(R.id.closet_info_description);
        location = findViewById(R.id.closet_info_location);

        name.setText(closet.getName());
        description.setText(closet.getDescription());
        location.setText(closet.getLocation());

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

//        closetItems = getClosetItemsDEBUG();
        closetItems = closet.getItems().stream().collect(Collectors.<Item>toList());

        if (closetItems == null || closetItems.isEmpty())
            showNoItems();
        else {
            prepareListData();
            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
            initOnClicks();
        }
    }

    private void showNoItems() {
        View emptyView = findViewById(R.id.no_item_text);
        expListView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void initOnClicks() {
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int groupPosition, int childPosition, long l) {
                Item item = (Item) listAdapter.getChild(groupPosition,childPosition);
                Intent newIntent = new Intent(ClosetInfoActivity.this, ItemInfoPopUp.class);
                newIntent.putExtra(getString(R.string.item), item);
                startActivity(newIntent);
                return false;
            }
        });
    }

    private List<Item> getClosetItemsDEBUG() {
        ArrayList<Item> res = new ArrayList<>();

        res.add(new Item("closet", null, new Date(), 5, "Zara", "blue", 32, "T-shirt"));
        res.add(new Item("rotem", null, new Date(), 6, "Pull n' Bear", "red", 32, "dress"));
        res.add(new Item("dror", null, new Date(), 7, "Bershka", "green", 32, "Pants"));

        return res;
    }

    private void prepareListData() {
        listDataHeader = setCategories();
        listDataChild = new HashMap<>();

        for (Item item : closetItems){
            if (!listDataChild.containsKey(item.getType()))
                listDataChild.put(item.getType(), new ArrayList<Item>());
            listDataChild.get(item.getType()).add(item);
        }
    }

    private List<String> setCategories() {
        ArrayList<String> res = new ArrayList<>();

        for (Item item : closetItems){
            if (!res.contains(item.getType()))
                res.add(item.getType());
        }

        return res;
    }
}
