package com.example.lab8_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity {
    Button bt_saveDB;
    Button bt_read,bt_write;
    EditText et_input;
    EditText et_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_saveDB=(Button)findViewById(R.id.buttonSaveDB);
        et_input=(EditText)findViewById(R.id.editTextTextMultiLine);
        et_result=(EditText)findViewById(R.id.editTextTextMultiLine2);
        bt_read=(Button)findViewById(R.id.buttonRead);
        bt_write=(Button)findViewById(R.id.buttonWrite);
        bt_write.setOnClickListener(view -> {
            String noteInput=et_input.getText().toString();
            SharedPreferences sharedPrefWrite= getSharedPreferences("MyPreferences",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPrefWrite.edit();
            editor.putString("note", noteInput);
            editor.apply(); //Update data don't return anything
            //editor.commit(); //Update data return:True/False
        });
        bt_read.setOnClickListener(view -> {
            SharedPreferences sharedPref= getSharedPreferences("MyPreferences",
                    MODE_PRIVATE);
            String noteContent=sharedPref.getString("note", "Your note");
            et_result.setText(noteContent);
        });

        bt_saveDB.setOnClickListener(view -> {
            String noteInput=et_input.getText().toString();
            LabDatabase db=new LabDatabase(this);
            if(db.countRecord()==0)
            {
                db.createNote("First note");
                db.createNote("Second note");
                db.createNote("Third note");
            }
            else{
                db.createNote(noteInput); // save your note
            }
            // After save note into DB, show them in Result
            Cursor c=db.getAllNotes();
            c.moveToFirst();
            String noteContent="";
            do
            {
                noteContent+=c.getString(0)+ " ";
                noteContent+=c.getString(1)+"\n";
            }while(c.moveToNext());
            et_result.setText(noteContent);
            db.close();
            c.close();
        });
    }
}