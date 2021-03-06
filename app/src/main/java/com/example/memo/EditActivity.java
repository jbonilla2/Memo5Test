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
import android.widget.Toast;


public class EditActivity extends AppCompatActivity {
    private EditText etText;
    private Button btnSave;
    private Button btnCancel;
    private Memo memo;
    private Memo newMemo;
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

//Issue is here

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            memo = (Memo) bundle.get("MEMO");
            if(memo != null) {
                this.etText.setText(memo.getText());
            }
        }
        else {
            newMemo = new Memo();
        }

 //Look at ContactActivity

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

        initSortBy();
        initSortByClick();

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        if(memo == null) {
            // Add new memo
            //final Memo temp = new Memo();
            //temp.setText(etText.getText().toString());
            //databaseAccess.save(temp);

            newMemo.setText(etText.getText().toString());
            databaseAccess.save(newMemo);

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
        //ContentValues values = new ContentValues();
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbLow = findViewById(R.id.radioLow);
                RadioButton rbMed = findViewById(R.id.radioMed);


                if(memo == null){


                }

                if (rbLow.isChecked()) {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit().putString("sortpriority", "low").commit();

                    if(memo == null){
                        newMemo.setPriority(3);
                    }
                    else{
                        memo.setPriority(3);
                    }



                }
                else if (rbMed.isChecked()) {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit().putString("sortpriority", "med").commit();
                    //memo.setPriority(2);

                    if(memo == null){
                        newMemo.setPriority(2);
                    }
                    else{
                        memo.setPriority(2);
                    }
                }
                else {
                    getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).edit().putString("sortpriority", "high").commit();
                   // memo.setPriority(1);
                    if(memo == null){
                        newMemo.setPriority(1);
                    }
                    else{
                        memo.setPriority(1);
                    }
                }
            }
        });
    }

}