package com.morandror.scclient.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.morandror.scclient.R;
import com.morandror.scclient.models.Item;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import java.util.HashMap;
import java.util.List;

import static com.morandror.scclient.utils.SharedStrings.DELETE_CLOSET_URL;
import static com.morandror.scclient.utils.SharedStrings.DELETE_ITEM_URL;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<String, List<Item>> _listDataChild;
    private int listItemStyle;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Item>> listChildData, int listItemStyle) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.listItemStyle = listItemStyle;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Item item = (Item)getChild(groupPosition, childPosition);
        final String childText = item.toString();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(listItemStyle, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.lblListItem);

        txtListChild.setText(childText);

        if (listItemStyle == R.layout.list_item_with_delete) {

            ImageButton delBtn = convertView.findViewById(R.id.del_item_btn);
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View view1 = view;
                    StringRequest request = new StringRequest(String.format(DELETE_ITEM_URL, item.getId()),
                            new Response.Listener<String>(){
                                @Override
                                public void onResponse(String response){
                                    System.out.println("Delete item succeeded");
                                    Toast.makeText(view1.getContext(), "Item deleted successfully", Toast.LENGTH_LONG).show();
                                    _listDataChild.get(_listDataHeader.get(groupPosition)).remove(item);
                                    notifyDataSetChanged();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    String msg = "Failed to delete item";
                                    System.out.println(msg);
                                    Toast.makeText(view1.getContext(), msg, Toast.LENGTH_LONG).show();
                                }
                    });

                    RequestQueueSingleton.getInstance(view.getContext()).getRequestQueue().add(request);
                }
            });

        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
