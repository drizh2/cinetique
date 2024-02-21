package com.dadry.cinetique.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.dadry.cinetique.entity.Film;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseConstants.DB_NAME, null, DatabaseConstants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DatabaseConstants.TABLE_NAME + " (" +
                DatabaseConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConstants.COLUMN_TITLE + " TEXT, " +
                DatabaseConstants.COLUMN_WATCHED + " INTEGER, " +
                DatabaseConstants.COLUMN_MARK + " INTEGER, " +
                DatabaseConstants.COLUMN_REVIEW + " TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.DB_NAME);
        onCreate(db);
    }

    public void addFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int watched = film.isWatched() ? 1 : 0;

        contentValues.put(DatabaseConstants.COLUMN_TITLE, film.getTitle());
        contentValues.put(DatabaseConstants.COLUMN_WATCHED, watched);

        if (film.isWatched()) {
            contentValues.put(DatabaseConstants.COLUMN_MARK, film.getMark());
            contentValues.put(DatabaseConstants.COLUMN_REVIEW, film.getReview());
        }

        long result = db.insert(DatabaseConstants.TABLE_NAME, null, contentValues);

        String message;

        if (result == -1) {
            message = "Failed!";
        } else {
            message = "Added successfully!";
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public void updateFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int watched = film.isWatched() ? 1 : 0;

        contentValues.put(DatabaseConstants.COLUMN_TITLE, film.getTitle());
        contentValues.put(DatabaseConstants.COLUMN_WATCHED, watched);

        if (film.isWatched()) {
            contentValues.put(DatabaseConstants.COLUMN_MARK, film.getMark());
            contentValues.put(DatabaseConstants.COLUMN_REVIEW, film.getReview());
        }

        long result = db.update(DatabaseConstants.TABLE_NAME, contentValues, "_id=?", new String[]{String.valueOf(film.getId())});

        String message;

        if (result == -1) {
            message = "Failed!";
        } else {
            message = "Updated successfully!";
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void deleteRow(String id) {
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(DatabaseConstants.TABLE_NAME, "_id=?", new String[]{id});

        String message;

        if (result == -1) {
            message = "Failed!";
        } else {
            message = "Deleted successfully!";
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
