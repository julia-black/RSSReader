package ru.sgu.csiit.sgu17;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RadioButton;

/**
 * Created by Juli on 28.07.2017.
 */

public class UpdateFragment extends Fragment {

    private RadioButton radioButton15min;
    private RadioButton radioButton30min;
    private RadioButton radioButton1hour;
    private RadioButton radioButtonManual;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.size() == 2) {
            inflater.inflate(R.menu.menu_favourite, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
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
        View v = inflater.inflate(R.layout.update_frequency, container, false);

        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(R.string.update_frequency);
        }
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        this.radioButton15min = (RadioButton) v.findViewById(R.id.fifteen_min);
        this.radioButton30min = (RadioButton) v.findViewById(R.id.thirdty_min);
        this.radioButton1hour = (RadioButton) v.findViewById(R.id.hour);
        this.radioButtonManual = (RadioButton) v.findViewById(R.id.manually);

        int period = prefs.getInt("periodUpdate",0);
        Log.i("Update", period + "");

        switch (period){
            case 15:
                this.radioButton15min.setChecked(true);
                break;
            case 30:
                this.radioButton30min.setChecked(true);
                break;
            case 1:
                this.radioButton1hour.setChecked(true);
                break;
            case 0:
                this.radioButtonManual.setChecked(true);
                break;
            default:
                break;
        }

        View.OnClickListener radioButtonClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RadioButton rB = (RadioButton) v;
                switch (rB.getId()){
                    case R.id.fifteen_min:
                        onPeriodUpdate(15);
                        radioButton15min.setChecked(true);
                        break;
                    case R.id.thirdty_min:
                        onPeriodUpdate(30);
                        radioButton30min.setChecked(true);
                        break;
                    case R.id.hour:
                        onPeriodUpdate(1);
                        radioButton1hour.setChecked(true);
                        break;
                    case R.id.manually:
                        onPeriodUpdate(0);
                        radioButtonManual.setChecked(true);
                        break;
                    default:
                        break;
                }
            }
        };

        this.radioButton15min.setOnClickListener(radioButtonClickListener);
        this.radioButton30min.setOnClickListener(radioButtonClickListener);
        this.radioButton1hour.setOnClickListener(radioButtonClickListener);
        this.radioButtonManual.setOnClickListener(radioButtonClickListener);

        return v;
    }

    private void onPeriodUpdate(int period) { //15/30 - 15/30 min, 1 - hour, 0 - manual
        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putInt("periodUpdate", period)
                .apply();
    }

}
