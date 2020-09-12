package com.vva.eventcountdown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateBaseAdapter
{
    DateBaseHelper dbHelper;
    SQLiteDatabase db;

    public DateBaseAdapter(Context context)
    {
        dbHelper = new DateBaseHelper(context.getApplicationContext());
    }

    public void open()
    {
        db = dbHelper.getWritableDatabase();
        //return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    private Cursor getAllEntries(int orderBy ,boolean showOld)
    {
        LocalDate ld = LocalDate.now();
        long ed = ld.toEpochDay();
        String orderClause = null;
        String whereClause = null;
        if(orderBy == DateBaseHelper.ORDER_DESC)
            orderClause = DateBaseHelper.COLUMN_EPOCHDAYS + " DESC";
        else if(orderBy == DateBaseHelper.ORDER_ASC)
            orderClause = DateBaseHelper.COLUMN_EPOCHDAYS + " ASC";
        else
            orderClause = null;

        if(showOld)
            whereClause = DateBaseHelper.COLUMN_EPOCHDAYS + " < " + ed; //номер дня события меньше сегодняшнего
        else
            whereClause = DateBaseHelper.COLUMN_EPOCHDAYS + " >= " + ed;

        String[] clms = new String[7];
        clms[0] = DateBaseHelper.COLUMN_ID;
        clms[1] = DateBaseHelper.COLUMN_TITLE;
        clms[2] = DateBaseHelper.COLUMN_DESCR;
        clms[3] = DateBaseHelper.COLUMN_YEAR;
        clms[4] = DateBaseHelper.COLUMN_MON;
        clms[5] = DateBaseHelper.COLUMN_DAY;
        clms[6] = DateBaseHelper.COLUMN_EPOCHDAYS;
        return db.query(DateBaseHelper.TABLE,clms,whereClause,null,null,null,orderClause);
    }

    public List<MyEvent> getEvents(int orderBy, boolean showOlds)
    {
        ArrayList<MyEvent> events = new ArrayList<>();
        Cursor cursor = getAllEntries(orderBy, showOlds);
        if(cursor.moveToFirst()) {
            do
            {
                int id = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(DateBaseHelper.COLUMN_TITLE));
                String descr = cursor.getString(cursor.getColumnIndex(DateBaseHelper.COLUMN_DESCR));
                int year = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.COLUMN_YEAR));
                int mon = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.COLUMN_MON));
                int day = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.COLUMN_DAY));
                events.add(new MyEvent(id, title,descr,year,mon,day));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    public long count()
    {
        return DatabaseUtils.queryNumEntries(db, dbHelper.TABLE);
    }

    public MyEvent getEvent(long id)
    {
        MyEvent event = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DateBaseHelper.TABLE, DateBaseHelper.COLUMN_ID);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        if(cursor.moveToFirst())
        {
            String title = cursor.getString(cursor.getColumnIndex(DateBaseHelper.COLUMN_TITLE));
            String descr = cursor.getString(cursor.getColumnIndex(DateBaseHelper.COLUMN_DESCR));
            int year = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.COLUMN_YEAR));
            int mon = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.COLUMN_MON));
            int day = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.COLUMN_DAY));
            event = new MyEvent(id, title,descr,year,mon,day);
        }
        cursor.close();
        return event;
    }

    public long insert(MyEvent event)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DateBaseHelper.COLUMN_TITLE,event.getTitle());
        contentValues.put(DateBaseHelper.COLUMN_DESCR,event.getDescription());
        contentValues.put(DateBaseHelper.COLUMN_YEAR,event.getLocalDate().getYear());
        contentValues.put(DateBaseHelper.COLUMN_MON,event.getLocalDate().getMonthValue());
        contentValues.put(DateBaseHelper.COLUMN_DAY, event.getLocalDate().getDayOfMonth());
        contentValues.put(DateBaseHelper.COLUMN_EPOCHDAYS, event.getLocalDate().toEpochDay());
        return db.insert(DateBaseHelper.TABLE,null,contentValues);
    }

    public long delete(long id)
    {
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return db.delete(DateBaseHelper.TABLE, whereClause, whereArgs);
    }

    public long deleteOld()
    {
        LocalDate ld = LocalDate.now();
        long ed = ld.toEpochDay();
        String whereClause = DateBaseHelper.COLUMN_EPOCHDAYS + " < ?";
        String[] whereArgs = new String[]{String.valueOf(ed)};
        return db.delete(DateBaseHelper.TABLE,whereClause,whereArgs);
    }

    public long update(MyEvent event)
    {
        String whereClause = DateBaseHelper.COLUMN_ID+"="+event.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DateBaseHelper.COLUMN_TITLE,event.getTitle());
        contentValues.put(DateBaseHelper.COLUMN_DESCR,event.getDescription());
        contentValues.put(DateBaseHelper.COLUMN_YEAR,event.getLocalDate().getYear());
        contentValues.put(DateBaseHelper.COLUMN_MON,event.getLocalDate().getMonthValue());
        contentValues.put(DateBaseHelper.COLUMN_DAY, event.getLocalDate().getDayOfMonth());
        contentValues.put(DateBaseHelper.COLUMN_EPOCHDAYS, event.getLocalDate().toEpochDay());
        return db.update(DateBaseHelper.TABLE,contentValues,whereClause,null);
    }


}
