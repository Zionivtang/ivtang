package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hook up the listvew object to lvItems
        lvItems = (ListView) findViewById(R.id.lvItems);
        // stop creating new arraylist so that it will retain the earlier entered data
        // items = new ArrayList<String>();
        //read the last content from a file saved
        readItems();
        //hook up the list view with arrayadapter
        itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, items);
        //associate the arrayadapter with the list view used here
        lvItems.setAdapter(itemsAdapter);
        //items.add("First Item");
        //items.add("Second Item");
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                               View item, int pos, long id) {
                        //get the selected list text at the selected position and store to string itemStr
                        String itemStr = itemsAdapter.getItem(pos);
                        launchComposeView(itemStr, pos);
                    }
                }
         );
    }

    //REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;

    public void launchComposeView(String itemStr, int pos) {
        // first parameter is the context java file, second is the class of the activity java file to launch
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("itemtext", itemStr);
        i.putExtra("pos", pos);
        // brings up the second activity with expectations for return results
        startActivityForResult(i, REQUEST_CODE);
    }

    // MainActivity.java, time to handle the result of the sub-activity
    //@override means to ignore the same public class of the same name and use below class defined instead
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String item = data.getExtras().getString("itemtext");
            int code = data.getExtras().getInt("pos",0);
            // Toast the name to display temporarily on screen before updating the list item for debugging purposes
            //Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
            //update the selected list item text by the code(pos)
            items.set(code,item);
            //refresh the display of the adapter that was hook up to the list
            itemsAdapter.notifyDataSetChanged();
            //save to the text files for next loading
            writeItems();
        }
    }


    public void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
            catch (IOException e) {
                items = new ArrayList<String>();
        }
    }

    public void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }
}
