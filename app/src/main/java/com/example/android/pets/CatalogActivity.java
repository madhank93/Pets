/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 *
 * Flow:
 * Catalog Activity -> Content resolver -> Pet provider (URI matcher) -> Query pets table
 *                                                                          ↓
 *                                                                          ↓
 *                                                                      Cursor
 */
public class CatalogActivity extends AppCompatActivity {

    private PetDbHelper mDbHelper;

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new PetDbHelper(this);
        displayDatabaseInfo();

    }


    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        String [] projection = {PetEntry._ID,
                                PetEntry.COLUMN_PET_NAME,
                                PetEntry.COLUMN_PET_BREED,
                                PetEntry.COLUMN_PET_GENDER,
                                PetEntry.COLUMN_PET_WEIGHT
                                };

        Cursor cursor = getContentResolver().query(
                PetEntry.CONTENT_URI,projection,
                null,null,null);


        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount()+ "\n\n");

            displayView.append(PetEntry._ID + "\t" + PetEntry.COLUMN_PET_NAME +"\t"+ PetEntry.COLUMN_PET_BREED+ "\t"+ PetEntry.COLUMN_PET_GENDER+ "\t"+ PetEntry.COLUMN_PET_WEIGHT + "\n");

            while(cursor.moveToNext()) {
                int currentPetID = cursor.getInt(cursor.getColumnIndex(PetEntry._ID));
                String currentPetName = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME));
                String currentPetBreed = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED));
                int currentPetGender = cursor.getInt(cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER));
                int currentPetWeight = cursor.getInt(cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT));

                displayView.append("\n" + currentPetID + "\t"+ currentPetName+ "\t" + currentPetBreed + "\t" + currentPetGender + "\t" + currentPetWeight );
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertData() {

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Garfield");
        values.put(PetEntry.COLUMN_PET_BREED, "Tabby");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long insertStatus = db.insert(PetEntry.TABLE_NAME, null, values);

        if (insertStatus == -1) {
            Toast.makeText(this, "Error with saving pet",
                    Toast.LENGTH_LONG).show();
        }

        else {
            Toast.makeText(this, "Pet saved with ID: " + insertStatus,
                    Toast.LENGTH_LONG).show();
        }
    }
}
