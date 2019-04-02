package com.example.memo;

import android.*;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class EditActivity extends AppCompatActivity {
    private EditText etText;
    private Button btnSave;
    private Button btnCancel;
    private Memo memo;
    private DBAccess databaseAccess;
    static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.etText = findViewById(R.id.etText);
        this.btnSave = findViewById(R.id.btnSave);
        this.btnCancel = findViewById(R.id.btnCancel);

        prefs = getSharedPreferences("bgPriority", MODE_PRIVATE);
        String priorityStr = prefs.getString("priority", "low");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            memo = (Memo) bundle.get("MEMO");
            if(memo != null) {
                this.etText.setText(memo.getText());
            }
        }
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });
        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });

//        initPriorityClick();
        initSortBy();
        initSortByClick();

    }

    public void onSaveClicked() {
        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        if(memo == null) {
            // Add new memo
            Memo temp = new Memo();
            temp.setText(etText.getText().toString());
            databaseAccess.save(temp);
        } else {
            // Update the memo
            memo.setText(etText.getText().toString());
            databaseAccess.update(memo);
        }
        databaseAccess.close();
        this.finish();
    }

    public void onCancelClicked() {
        this.finish();
    }

    /*private void initPriorityClick() {
        RadioGroup bgColor = (RadioGroup) findViewById(R.id.radioGroupImp);
        bgColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbLow = (RadioButton) findViewById(R.id.radioLow);
                RadioButton rbMed = (RadioButton) findViewById(R.id.radioMed);
                RadioButton rbHigh = (RadioButton) findViewById(R.id.radioHigh);
                if (rbHigh.isChecked()) {
                    prefs.edit().putString("priority", "high").commit();
//                    memo.setPriority(1);
                }
                else if (rbMed.isChecked()) {
                    prefs.edit().putString("priority", "med").commit();
//                    memo.setPriority(2);
                }
                if (rbLow.isChecked()) {
                    prefs.edit().putString("priority", "low").commit();
//                    memo.setPriority(3);
                }
            }//method for allowing the color radio button group to edit the bgColor sharedprefs
        });
    }*/

    private void initSortBy() {
        String sortBy = getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).getString("sortpriority","low");

        RadioButton rbHigh = findViewById(R.id.radioHigh);
        RadioButton rbMed = findViewById(R.id.radioMed);
        RadioButton rbLow = findViewById(R.id.radioLow);
        if (sortBy.equalsIgnoreCase("low")) {
            rbLow.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("med")) {
            rbMed.setChecked(true);
        }
        else {
            rbHigh.setChecked(true);
        }

    }

    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupImp);
        ContentValues values = new ContentValues();
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbLow = findViewById(R.id.radioLow);
                RadioButton rbMed = findViewById(R.id.radioMed);
                if (rbLow.isChecked()) {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit() .putString("sortpriority", "low").commit();
                    memo.setPriority(3);
                }
                else if (rbMed.isChecked()) {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit().putString("sortpriority", "med").commit();
                    memo.setPriority(2);
                }
                else {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit().putString("sortpriority", "high").commit();
                    memo.setPriority(1);
                }
            }
        });
    }

}