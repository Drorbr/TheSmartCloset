package com.morandror.scclient.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.morandror.scclient.ClosetAdapter;
import com.morandror.scclient.R;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.models.User;

import java.util.ArrayList;
import java.util.Date;

public class home_page_activity2 extends AppCompatActivity {
    private User user;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Closet> data;
    static View.OnClickListener myOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_activity2);

        final TextView mTextView = findViewById(R.id.welcomeSign);

        user = (User)getIntent().getSerializableExtra(getString(R.string.user));
        mTextView.setText(String.format(getString(R.string.welcomeActivity2), user.getFirstName(), user.getLastName()));

        user.getClosets().add(new Closet(123, "very nice closet", "Winter clothes", "Parent's house", new Date()));
        user.getClosets().add(new Closet(1233, "even nicer closet", "All daily clothes", "My house", new Date()));
        user.getClosets().add(new Closet(12334, "best closet everrr", "The kid's closet", "My house", new Date()));

        if (user.getClosets() == null || user.getClosets().isEmpty()){
            showNoClosets();
        } else {
            showUsersClosets();
        }
    }

    private void showUsersClosets() {
        myOnClickListener = new MyOnClickListener(this);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();

        data.addAll(user.getClosets());

        adapter = new ClosetAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    private void showNoClosets() {

    }

    public static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        public MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            //enter closet
        }

//        private void removeItem(View v) {
//            int selectedItemPosition = recyclerView.getChildPosition(v);
//            RecyclerView.ViewHolder viewHolder
//                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
//            TextView textViewName
//                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
//            String selectedName = (String) textViewName.getText();
//            int selectedItemId = -1;
//            for (int i = 0; i < MyData.nameArray.length; i++) {
//                if (selectedName.equals(MyData.nameArray[i])) {
//                    selectedItemId = MyData.id_[i];
//                }
//            }
//            removedItems.add(selectedItemId);
//            data.remove(selectedItemPosition);
//            adapter.notifyItemRemoved(selectedItemPosition);
//        }
//    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.closet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
/*        if (item.getItemId() == R.id.add_item) {
            //check if any items to add
            if (removedItems.size() != 0) {
                addRemovedItemToList();
            } else {
                Toast.makeText(this, "Nothing to add", Toast.LENGTH_SHORT).show();
            }
        }*/
        return true;
    }

    private void addRemovedItemToList(){
        int addItemAtListPosition = 3;
//        data.add(addItemAtListPosition, new DataModel(
//                MyData.nameArray[removedItems.get(0)],
//                MyData.versionArray[removedItems.get(0)],
//                MyData.id_[removedItems.get(0)],
//                MyData.drawableArray[removedItems.get(0)]
//        ));
//        adapter.notifyItemInserted(addItemAtListPosition);
//        removedItems.remove(0);
    }
}
