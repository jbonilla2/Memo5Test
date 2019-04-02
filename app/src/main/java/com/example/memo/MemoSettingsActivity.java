package com.example.memo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MemoSettingsActivity extends AppCompatActivity {

    static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_settings);

        initSettings();
        initSortByClick();
        initSortOrderClick();
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).getString("sortfield","date");
        String sortOrder = getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).getString("sortorder","ASC");

        RadioButton rbPriority = findViewById(R.id.radioPriority);
        RadioButton rbDate = (RadioButton) findViewById(R.id.radioDate);

        if (sortBy.equalsIgnoreCase("priority")) {
            rbPriority.setChecked(true);
        }
        else {
            rbDate.setChecked(true);
        }

        RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
        RadioButton rbDescending = (RadioButton) findViewById(R.id.radioDescending);
        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        }
        else {
            rbDescending.setChecked(true);
        }
    }

    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbPriority = findViewById(R.id.radioPriority);
                RadioButton rbDate = findViewById(R.id.radioDate);
                if (rbPriority.isChecked()) { /* This triggers the list to be sorted by alphabetical order */
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit() .putString("sortfield", "priority").commit();
                }
                else {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "date").commit();
                }
            }
        });
    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()) {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "ASC").commit();
                }
                else {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "DESC").commit();
                }
            }
        });
    }


}
