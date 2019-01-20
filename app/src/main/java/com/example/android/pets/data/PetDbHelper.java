package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.pets.data.PetContract.PetEntry;


/*
*  This class provides a connection to a database and also helps creating a database
*  if one doesn't already exists
*/

public class PetDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;

    /*
     * We need to call the super i.e. parent class constructor
     * And we need to pass 4 parameters
     * 1. Context context -> It is the context object we will get it from the activity while creating the instance
     * 2. String databasename -> It is the name of the database and here we are passing the constant that we already defined
     * 3. CursorFactory cursorFactory -> If we want a cursor to be initialized on the creation we can use cursor factory,
     * it is optionall and that is why we passed null here
     * 4. int version -> It is an int defining our database version
     * */
    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PETS_TABLE  = "CREATE TABLE " + PetEntry.TABLE_NAME + " (\n" +
                "    " + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,\n" +
                "    " + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL,\n" +
                "    " + PetEntry.COLUMN_PET_BREED + " TEXT ,\n" +
                "    " + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL,\n" +
                "    " + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0 \n" +
                ");";

        db.execSQL(CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
