package com.example.memo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBAccess {
    private SQLiteDatabase database;
    private DBHelper openHelper;
    private static volatile DBAccess instance;

    private DBAccess(Context context) {
        this.openHelper = new DBHelper(context);
    }

    public static synchronized DBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void save(Memo memo) {
        ContentValues values = new ContentValues();
        values.put("date", memo.getTime());
        values.put("memo", memo.getText());
        values.put("priority", memo.getPriority());
        database.insert(DBHelper.TABLE, null, values);
    }

    public void update(Memo memo) {
        ContentValues values = new ContentValues();
        values.put("date", new Date().getTime());
        values.put("memo", memo.getText());
        values.put("priority", memo.getPriority());
        String date = Long.toString(memo.getTime());
        database.update(DBHelper.TABLE, values, "date = ?", new String[]{date});
    }

    public void delete(Memo memo) {
        String date = Long.toString(memo.getTime());
        database.delete(DBHelper.TABLE, "date = ?", new String[]{date});
    }

    public List getAllMemosByDate(String sortField, String sortOrder) {
        List memos = new ArrayList<>();

        try {
            String query = "SELECT * From memo ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long time = cursor.getLong(0);
                String text = cursor.getString(1);
                int priority = cursor.getInt(2);
                memos.add(new Memo(time, text, priority));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            memos = new ArrayList<Memo>();
        }
        return memos;
    }

    public List getAllMemosByPriority(String sortField, String sortOrder) {
        List memos = new ArrayList<>();

        try {
            String query = "SELECT * From memo ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long time = cursor.getLong(0);
                String text = cursor.getString(1);
                int priority = cursor.getInt(2);
                memos.add(new Memo(time, text, priority));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            memos = new ArrayList<Memo>();
        }
        return memos;
    }

    public Memo getSpecificMemo(int memoDate) {
        Memo memo = new Memo();
        String query = "SELECT  * FROM memo WHERE date =" + memoDate;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            memo.setTime(cursor.getInt(0));
            memo.setText(cursor.getString(1));
            memo.setPriority(cursor.getInt(2));

            cursor.close();
        }

        return memo;
    }

}