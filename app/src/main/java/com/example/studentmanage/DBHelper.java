package com.example.studentmanage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Student.db";
    private static final String TABLE_NAME = "students";
    private static final String _ID = "id";
    private static final String SD_NAME = "name";
    private static final String SD_CLASS = "class";
    private static final String SD_BIRTH = "birth";
    private static final String SD_ADDRESS = "address";
   public DBHelper(Context context){
       super(context,DATABASE_NAME,null,1);
   }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SD_NAME + " TEXT, " +
                SD_CLASS + " TEXT, " +
                SD_BIRTH + " TEXT, " +
                SD_ADDRESS + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }
    public boolean insertStudent(Students students){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",students.getName());
        contentValues.put("class",students.getClassS());
        contentValues.put("birth",students.getBirth());
        contentValues.put("address",students.getAddress());
        db.insert("students",null,contentValues);
        return true;
    }
    public List getAllStudent(){
        List<Students> arrayList = new ArrayList<Students>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from students"  ,null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
//            ContentValues contentValues = new ContentValues();
            Students students = new Students();
            students.setName(res.getString(res.getColumnIndexOrThrow(SD_NAME)));
            students.setId(res.getString(res.getColumnIndexOrThrow(_ID)));
            students.setClassS(res.getString(res.getColumnIndexOrThrow(SD_CLASS)));
           students.setBirth(res.getString(res.getColumnIndexOrThrow(SD_BIRTH)));
            students.setAddress(res.getString(res.getColumnIndexOrThrow(SD_ADDRESS)));
            arrayList.add(
                    students
            );
            res.moveToNext();
        }
        return arrayList;
    }
    public boolean updateStudent (Students students){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SD_NAME,students.getName());
        contentValues.put(SD_CLASS,students.getClassS());
        contentValues.put(SD_BIRTH,students.getBirth());
        contentValues.put(SD_ADDRESS,students.getAddress());

        db.update("students",contentValues,"id = ?",new String[]{students.getId()});
        return true;
    }
    public boolean deleteStudent(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("students","id = ?" , new String[]{id});
        return true;
    }
}
