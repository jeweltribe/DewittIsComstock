package com.example.varun.app4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "movies";
    private static final String COLUMN_MOVIE_ID = "movieId";
    private static final String COLUMN_MOVIE_TEXT = "movie";
    private static final String COLUMN_MOVIE_DATE = "date";
    private static final String COLUMN_MOVIE_RATING = "rating";

    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    // create the database with the following string
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s INTEGER)", TABLE_NAME, COLUMN_MOVIE_ID, COLUMN_MOVIE_TEXT, COLUMN_MOVIE_DATE, COLUMN_MOVIE_RATING);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void storeMovie(List<Movies> list) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_NAME, null, null);
            int id = 0;
            for (Movies m: list) {
                m.setMovieID(id);
                ContentValues values = new ContentValues();
                values.put(COLUMN_MOVIE_ID, m.getMovieID());
                values.put(COLUMN_MOVIE_TEXT, m.getMovieName());
                values.put(COLUMN_MOVIE_DATE, m.getMovieDate());
                values.put(COLUMN_MOVIE_RATING, m.getMovieRating());
                db.insert(TABLE_NAME,null, values);
                id++;
            }
            db.close();
        } catch (SQLiteException e) {
            Log.d("DBHelper: ", e.toString());
        }

    }

    /*public void delete(String ID) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, COLUMN_MOVIE_ID + "=?", new String[] {ID});
    }*/

    public List<Movies> getMovie() {
        List<Movies> movieList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format("SELECT * FROM %s ORDER BY %s", TABLE_NAME, COLUMN_MOVIE_ID);
        Cursor cursor = db.rawQuery(sql, null);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                float rating = cursor.getInt(3);
                movieList.add(new Movies(id, name, date, rating));
            }
        } catch (SQLiteException e) {
            Log.d("DBHelper:","Exception", e);
        }
        cursor.close();
        db.close();
        return movieList;
    }




}
