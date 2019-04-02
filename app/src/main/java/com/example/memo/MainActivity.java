package com.example.memo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Button btnAdd;
    private DBAccess databaseAccess;
    private List<Memo> memos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.databaseAccess = DBAccess.getInstance(this);

        this.listView = (ListView) findViewById(R.id.listView);
        this.btnAdd = (Button) findViewById(R.id.btnAdd);

        this.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClicked();
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = memos.get(position);
                TextView txtMemo = (TextView) view.findViewById(R.id.txtMemo);
                if (memo.isFullDisplayed()) {
                    txtMemo.setText(memo.getShortText());
                    memo.setFullDisplayed(false);
                } else {
                    txtMemo.setText(memo.getText());
                    memo.setFullDisplayed(true);
                }
            }
        });

        initSettingsButton();

    }

    private void initSettingsButton() {
        Button ibList =  findViewById(R.id.btnSettings);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MemoSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        databaseAccess.open();
        this.memos = databaseAccess.getAllMemosByDate();
        databaseAccess.close();
        MemoAdapter adapter = new MemoAdapter(this, memos);
        this.listView.setAdapter(adapter);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).getString("sortfield", "date");
        String sortOrder = getSharedPreferences("MyMemoListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        try {

            if(sortBy.equalsIgnoreCase("date")) {
                databaseAccess.open();
                memos = databaseAccess.getAllMemosByDate(sortBy, sortOrder);
                databaseAccess.close();
            } else {
                databaseAccess.open();
                memos = databaseAccess.getAllMemosByPriority(sortBy, sortOrder);
                databaseAccess.close();
            }

            MemoAdapter adapter = new MemoAdapter(this, memos);
            this.listView.setAdapter(adapter);

        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
    }

    public void onAddClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void onDeleteClicked(Memo memo) {
        databaseAccess.open();
        databaseAccess.delete(memo);
        databaseAccess.close();

        ArrayAdapter<Memo> adapter = (ArrayAdapter<Memo>) listView.getAdapter();
        adapter.remove(memo);
        adapter.notifyDataSetChanged();
    }

    public void onEditClicked(Memo memo) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("MEMO", memo);
        startActivity(intent);
    }

    private class MemoAdapter extends ArrayAdapter<Memo> {


        public MemoAdapter(Context context, List<Memo> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_list_item, parent, false);
            }

            ImageView btnEdit = (ImageView) convertView.findViewById(R.id.btnEdit);
            ImageView btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            TextView txtMemo = (TextView) convertView.findViewById(R.id.txtMemo);

            final Memo memo = memos.get(position);
            memo.setFullDisplayed(false);
            txtDate.setText(memo.getDate());
            txtMemo.setText(memo.getShortText());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClicked(memo);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClicked(memo);
                }
            });
            return convertView;
        }
    }
}
