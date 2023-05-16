package com.example.mylocationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.IdentityScope;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "LocationAlarm.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_location";
    private static final String TABLE_USER ="login";

    private static final String NOTIFICATION_ID = "_id";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String RANGE = "range";
    private static final String CHECKBOX_NOTIFICATION = "check_notification";
    private static final String CHECKBOX_VOICE = "check_voice";
    private static final String CHECKBOX_VIBRATE = "check_vibrate";


    private static  final String ID = "id";
    private static  final String Title = "title";
    private static  final String About = "about";




    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE + " TEXT, " +
                TIME + " TEXT, " +
                TITLE + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                LATITUDE + " DOUBLE, " +
                LONGITUDE + " DOUBLE, " +
                RANGE + " DOUBLE, " +
                CHECKBOX_NOTIFICATION + " TEXT, " +
                CHECKBOX_VOICE + " TEXT, " +
                CHECKBOX_VIBRATE + " TEXT);";

        String TABLE_CREATE_USERQUERY ="CREATE TABLE "+TABLE_USER+" " +
                "("
                +NOTIFICATION_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Title+" TEXT,"
                +About+" TEXT"+
                ");";


        db.execSQL(query);
        db.execSQL(TABLE_CREATE_USERQUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        String DROP_TABLE_QUERY2 = "DROP TABLE IF EXISTS "+ TABLE_USER;
        db.execSQL(DROP_TABLE_QUERY2);
        onCreate(db);
    }

    //mine




    public void addToDoUSER(ToDo toDo){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(Title,toDo.getTitle());

        contentValues.put(About,toDo.getAbout());

        //saving to database
        sqLiteDatabase.insert(TABLE_USER,null,contentValues);
        sqLiteDatabase.close();
    }

    void addReminder(String date, String time,
                     String title, String description,
                     double latitude, double longitude,
                     double range, String notification,
                     String voice, String vibrate){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DATE, date);
        cv.put(TIME, time);
        cv.put(TITLE, title);
        cv.put(DESCRIPTION, description);
        cv.put(LATITUDE, latitude);
        cv.put(LONGITUDE, longitude);
        cv.put(RANGE, range);
        cv.put(CHECKBOX_NOTIFICATION, notification);
        cv.put(CHECKBOX_VOICE, voice);
        cv.put(CHECKBOX_VIBRATE, vibrate);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Added Successfully !!!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    public Boolean checkusernamepassword(String username , String password){
        System.out.println("kasuni");
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from "+TABLE_USER+" where title = ? and about = ?",new String[] {username,password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }


    void updateData(String row_id, String date, String time, String title, String description, String lat, String lon, String range){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DATE, date);
        cv.put(TIME, time);
        cv.put(TITLE, title);
        cv.put(DESCRIPTION, description);
        cv.put(LATITUDE, lat);
        cv.put(LONGITUDE, lon);
        cv.put(RANGE, range);

        long result = db.update(TABLE_NAME, cv, "_id = ?", new String[]{row_id});

        if(result == -1){
            Toast.makeText(context, "Fail to Update!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();
        }


    }
}
