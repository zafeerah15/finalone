package sG.EDU.NP.MAD.friendsOnly;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "toDoList.db";
    public static final String TABLE_TODO = "todo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_UPDATEDATE = "updatedate";

    public DatabaseHandler(Context context, String name,
                           SQLiteDatabase.CursorFactory factory,
                           int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //Creation of TO DO database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " +
                TABLE_TODO + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_STATUS + " TEXT," + COLUMN_TITLE + " TEXT,"
                + COLUMN_UPDATEDATE + " TEXT" + ")";

        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        //Create tables again
        onCreate(db);
    }

    //Allows the users the add a task and store it in the database
    public void addTask(ToDo toDo){
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, 0);
        values.put(COLUMN_TITLE, toDo.getTitle());
        values.put(COLUMN_UPDATEDATE, toDo.getUpdateDate());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    //Retrieve all the task from the database and stores it as an arraylist
    @SuppressLint("Range")
    public ArrayList<ToDo> getAllTask(){
        ArrayList<ToDo> tList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            ToDo toDo = new ToDo();
            toDo.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            toDo.setStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)));
            toDo.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            toDo.setUpdateDate(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATEDATE)));
            tList.add(toDo);
        }
        cursor.close();

        return tList;
    }

    //Retrieve task from database
    @SuppressLint("Range")
    public ToDo getTask(int id){
        ToDo task = new ToDo();
        String query = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()){
            if(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)) == id) {
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.setStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                task.setUpdateDate(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATEDATE)));
                break;
            }
        }

        return task;
    }

    //Updates database if checkbox is ticked or not
    public void updateCheckBox(int id, int status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATUS, status);
        db.update(TABLE_TODO, cv, COLUMN_ID + "= ?", new String[] {String.valueOf(id)});
    }

    //Updates task from database
    public void updateTask(int id, String title, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_UPDATEDATE, date);
        db.update(TABLE_TODO, cv, COLUMN_ID + "= ?", new String[] {String.valueOf(id)});
    }

    //Deletes task from database
    public void deleteTask(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, COLUMN_ID + "= ?", new String[] {String.valueOf(id)});
        db.close();
    }

}
