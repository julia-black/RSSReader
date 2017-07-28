package ru.sgu.csiit.sgu17;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class CustomPrimaryDrawerItem extends PrimaryDrawerItem {

    private String name;

    @Override
    public CustomPrimaryDrawerItem withName(String name) {
        this.name = "";
        return this;
    }

     @Override
     public int getLayoutRes() {
         return R.layout.logo_item;
     }

    @Override
    public View convertView(Activity activity, LayoutInflater inflater, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(getLayoutRes(), parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

     private static class ViewHolder {
         private View view;

         private ViewHolder(View view) {
             this.view = view;
         }
     }

}