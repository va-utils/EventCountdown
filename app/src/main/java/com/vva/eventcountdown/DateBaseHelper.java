package com.vva.eventcountdown;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DateBaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "evcountdown3.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "events"; // название таблицы в бд
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "_title";
    public static final String COLUMN_DESCR = "_descr";
    public static final String COLUMN_YEAR = "_year";
    public static final String COLUMN_MON = "_mon";
    public static final String COLUMN_DAY = "_day";
    public static final String COLUMN_EPOCHDAYS = "_epdays";

    public static final int ORDER_ASC = 0;
    public static final int ORDER_DESC = 1;
    public static final int ORDER_NONE = 2;

    public DateBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE
                + " TEXT, "     + COLUMN_DESCR
                + " TEXT, "     + COLUMN_YEAR
                + " INTEGER, "  + COLUMN_MON
                + " INTEGER, "  + COLUMN_DAY
                + " INTEGER, "   + COLUMN_EPOCHDAYS + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}
