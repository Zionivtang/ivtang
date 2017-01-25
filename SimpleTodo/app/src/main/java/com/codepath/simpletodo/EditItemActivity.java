package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    EditText etMultiLineEdit;
    int pos;

    //@override means to ignore the same public class of the same name and use below class defined instead
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        EditText etMultiLineEdit = (EditText) findViewById(R.id.etMultiLineEdit);

        etMultiLineEdit.append(getIntent().getStringExtra("itemtext"));
        pos = getIntent().getIntExtra("pos",0);
    }

        public void onAccept(View v) {
            //Just to show button click hook works and button click acknowledge for debugging purposes
            //Toast.makeText(this, "You clicked Save button!", Toast.LENGTH_SHORT).show();
            // closes the activity and returns to first screen
            EditText etMultiLineEdit = (EditText) findViewById(R.id.etMultiLineEdit);
            //prepare data intent
            Intent data = new Intent();
            // return the text to pass back later to update to the Main page
            data.putExtra("itemtext", etMultiLineEdit.getText().toString());
            // return position so later will update the right one in the list
            data.putExtra("pos", pos);
            // Activity finished ok, return the data
            setResult(RESULT_OK, data); // set result code and bundle data for response
            finish(); // closes the activity, pass data to parent
        }
}
