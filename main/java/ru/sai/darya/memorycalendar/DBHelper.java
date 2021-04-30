package ru.sai.darya.memorycalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import java.sql.Blob;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "myBD", null, 1);
    }

    public static final String TABLE_NAME = "MainTable";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PHOTO_PATH = "photo_path";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY autoincrement," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_PHOTO_PATH + " TEXT," +
                    COLUMN_LAT + " REAL," +
                    COLUMN_LON + " REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        //создание БД
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //пусть пока будет так
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


}