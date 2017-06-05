package com.example.felix.appalimentacio.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.felix.appalimentacio.Model.ItemListCompraModel;
import com.example.felix.appalimentacio.R;

import java.util.List;



public class ListCompraAdapter extends BaseAdapter {
    private Activity activity;
    private List<ItemListCompraModel> data;
    private LayoutInflater inflater;
    private int item_layout;
    // private Fragment fragment;

    public ListCompraAdapter(Activity activity, List<ItemListCompraModel> data, int item_layout) {
        this.activity = activity;
        this.data = data;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.item_layout = item_layout;
    }
    /*
        public MyListAdapter(FragmentList fragmentList, List<ItemModel> data, int list_item) {
            this.fragment = fragmentList;
            this.data = data;
            this.inflater = (LayoutInflater) fragment.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.item_layout = item_layout;

        }
    */
    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = this.inflater.inflate(item_layout,null);

        TextView tnom= (TextView) convertView.findViewById(R.id.nomIngredient);
        TextView tcantitat = (TextView) convertView.findViewById(R.id.cantitatIngredient);
        CheckBox tchecked = (CheckBox) convertView.findViewById(R.id.checkboxIngredient);

        tnom.setText(data.get(position).getNomRecepta());
        tcantitat.setText(data.get(position).getCantitat());
       tchecked.setChecked(data.get(position).isItemCheck());

        return convertView;

    }
}
