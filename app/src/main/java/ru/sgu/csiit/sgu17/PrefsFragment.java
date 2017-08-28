package ru.sgu.csiit.sgu17;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;

public class PrefsFragment extends Fragment {

    private Switch wifiOnlySwitch;
    private Switch notificationSwitch;
    private Switch useLocationSwitch;
    private TextView updateFrequency;
    private TextView periodText;

    private boolean flagFavourite;


    public PrefsFragment(boolean flagFavourite) {
        this.flagFavourite = flagFavourite;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       //if(menu.size() == 2) {
       //    inflater.inflate(R.menu.menu_main, menu);
           super.onCreateOptionsMenu(menu, inflater);
      // }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_favorite){
            onFavouriteListClicked();
        }
        else if(id == R.id.action_newsBlog){
            onNewsBlogClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onNewsBlogClicked() {
        Intent intent = new Intent(this.getActivity(), NewsListActivity.class);
        startActivity(intent);
    }

    private void onFavouriteListClicked() {
        Intent intent = new Intent(this.getActivity(), FavouriteActivity.class);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.prefs_fragment, container, false);

        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(R.string.action_prefs);
        }

        this.wifiOnlySwitch = (Switch) v.findViewById(R.id.wi_fi_only_sw);
        this.notificationSwitch = (Switch) v.findViewById(R.id.notifications_sw);
        this.useLocationSwitch = (Switch) v.findViewById(R.id.use_location_sw);
        this.updateFrequency = (TextView) v.findViewById(R.id.periodic_updates_text);
        this.periodText = (TextView) v.findViewById(R.id.period);


        init();
        return v;
    }

    private void init() {
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        int period = prefs.getInt("periodUpdate",15);

        switch (period){
            case 15:
                this.periodText.setText("Every 15 minutes");
                break;
            case 30:
                this.periodText.setText("Every 30 minutes");
                break;
            case 1:
                this.periodText.setText("Every hour");
                break;
            case 0:
                this.periodText.setText("Manually");
                break;
            default:
                break;
        }

        wifiOnlySwitch.setChecked(prefs.getBoolean("wifiOnly", false));
        wifiOnlySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onWiFiSwitched(isChecked);
            }
        });

        notificationSwitch.setChecked(prefs.getBoolean("notifications", true));
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onNotificationsSwitched(isChecked);
            }
        });

        useLocationSwitch.setChecked(prefs.getBoolean("location", false));
        useLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onUseLocationSwitched(isChecked);
            }


        });
        updateFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateFragment(); //переходим на др. фрагмент
            }
        });

    }

    private void onUseLocationSwitched(boolean isChecked) {
        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putBoolean("location", isChecked)
                .apply();
    }

    private void onUpdateFragment() {
        UpdateFragment fragment = new UpdateFragment();

        if(flagFavourite) {
            getFragmentManager().beginTransaction()
                    .add(R.id.containerFavourite, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        else {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void onWiFiSwitched(boolean checked) {
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("wifiOnly", checked);
        editor.apply();
    }

    private void onNotificationsSwitched(boolean checked) {
        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putBoolean("notifications", checked)
                .apply();
    }

}
