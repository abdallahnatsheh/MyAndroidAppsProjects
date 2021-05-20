package com.abdallahn_ameerh.ex3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Vector;

public class ToDoListActivity extends AppCompatActivity {
    SharedPreferences sp ;
    SharedPreferences.Editor editor ;
    String userName = null;
    boolean isloggedin ;
    public static final String MY_DB_NAME = "todoDB.db";
    SQLiteDatabase todo = null;
    private Vector<String> titles , descriptions , times,dates , ids; //this for the todo list
    private ListView list;
    String appName; //contain the username from SharedPreferences
    SearchView editsearch;
    private ListAdapter lAdapter; //this to list the todo list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        list=findViewById(R.id.todoList);
        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        appName = sp.getString("username",userName);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Todo List ("+appName+")");
        isloggedin = sp.getBoolean("isLogged",isloggedin);
        Log.d("debug", "check"+isloggedin);
        editor = sp.edit();

        createDB();
        showTodoList();

        FloatingActionButton fab = findViewById(R.id.fab);
        //when i press the + button it will move me to add new todo
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoListActivity.this, EditorActivity.class);
                editor.putBoolean("updateStatus",false);
                editor.apply();
                editor.commit();
                startActivity(intent);

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                // it will create alert box to ask before deletion
                int idd = Integer.parseInt(ids.get(pos));
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ToDoListActivity.this);
                alertDialog.setTitle("Delete Todo");
                alertDialog.setMessage("do you really want to delete it  ?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", (dialogInterface, dummy) ->  deleteQuery(idd));
                alertDialog.setNegativeButton("No", (dialogInterface, dummy) -> { });
                alertDialog.show();
                Log.v("debug","long click pos: " +" "+ idd +" "+appName);
                return true;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//in case of listView pressed
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //here i need to update the queries
                Intent intent = new Intent(ToDoListActivity.this, EditorActivity.class);
                editor.putString("TodoID",ids.get(pos));
                editor.putBoolean("updateStatus",true);
                editor.apply();
                editor.commit();
                startActivity(intent);

                Log.v(" debug","click pos: " + pos);

            }

        });
        editsearch = (SearchView) findViewById(R.id.searchBar);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                search(text);
                Log.v("debug","search text :"+text);
                return false;
            }
        });
    }

    //app bar menu with buttons and their actions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.todo_list_menu,menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_btn:   // when about clicked in menu
                logMeOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //log the user out
    private void logMeOut() {
        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit() ;
        Intent intent = new Intent(ToDoListActivity.this, LoginActivity.class);
        editor.clear();
        editor.apply();
        finish();
        startActivity(intent);
    }

    //print all the todo rows from the todo table in the list view
    public void showTodoList() {
        // A Cursor provides read and write access to database results
        String sql = "SELECT * FROM todo WHERE username = "+"'"+appName+"'";
        Cursor cursor = todo.rawQuery(sql, null);

        titles=new Vector<>();// to save the title in
        descriptions=new Vector<>();//to save the description in
        times=new Vector<>(); //save time
        dates=new Vector<>(); //save date
        ids = new Vector<>();
        String titlesArr[] , descriptionsArr[] ,timesArr[],datesArr[] ;// used in the adapter


        // Get the index for the column name provided
        int titleColumn = cursor.getColumnIndex("title");
        int descriptionColumn = cursor.getColumnIndex("description");
        int datetimeColumn = cursor.getColumnIndex("datetime");
        int idColumn = cursor.getColumnIndex("id");



        // Move to the first row of results
        cursor.moveToFirst();

        // Verify that we have results
        if(cursor != null && (cursor.getCount() > 0)){

            do{
                // Get the results and store them in a String
                String title= cursor.getString(titleColumn);
                String description= cursor.getString(descriptionColumn);
                String datetime= cursor.getString(datetimeColumn);
                String id = cursor.getString(idColumn);
                String[] splitStr = datetime.split("\\s+");
                /** to add to the vectors **/
                titles.add(title);
                descriptions.add(description);
                dates.add(splitStr[0]);
                times.add(splitStr[1]);
                ids.add(id);

                // Keep getting results as long as they exist
            }while(cursor.moveToNext());


            titlesArr=new String[titles.size()];//to save the tasks & dates together
            descriptionsArr=new String[descriptions.size()];
            timesArr=new String[times.size()];
            datesArr=new String[dates.size()];

            for (int j=0;j<titlesArr.length;j++){//to fill the arrays used in the adapter with info
                titlesArr[j]=titles.elementAt(j);
                descriptionsArr[j]=descriptions.elementAt(j);
                timesArr[j]=times.elementAt(j);
                datesArr[j]=dates.elementAt(j);
            }


            ListAdapter lAdapter = new ListAdapter(ToDoListActivity.this, titlesArr, descriptionsArr, timesArr, datesArr);
            list.setAdapter(lAdapter);


        } else {
            Toast.makeText(this, "No list to Show", Toast.LENGTH_SHORT).show();
        }


    }
    //here it will create the main table for the todo list
    public void createDB() {
        try
        {
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            todo = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);

            // build an SQL statement to create 'todoDB' table (if not exists)
            String sql = "CREATE TABLE IF NOT EXISTS todo (id integer primary key, username VARCHAR, title VARCHAR, description VARCHAR, datetime VARCHAR);";
            todo.execSQL(sql);
            Log.d("debug", "todo Database created");
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Creating todo Database");
        }
    }

    //find the row that contins the same todo id and the username then delete it and show the new list
    public void deleteQuery(int id){
        try {
            String sql = "DELETE FROM todo WHERE id = "+"'"+id+"'"+"AND username = "+"'"+appName+"'";
            todo.execSQL(sql);
            Toast.makeText(this, "Todo was DELETED", Toast.LENGTH_SHORT).show();
            showTodoList();
        }catch (Exception e){
            Log.d("debug", "Error deleting todo list row");

        }

    }
    //it will take the string from search view and print the matches on the list view
    public void search(String searchText){
        String titlesArr[] , descriptionsArr[] ,timesArr[],datesArr[] ;// used in the adapter

        boolean doesExist=false;//to check if sub is found or not
        // if query empty show all list
        if (searchText.isEmpty() && !doesExist){
            showTodoList();
        }
        else {
            //check how many titles and descriptions match the query
            int subTitleCount=numOfElements(titles,searchText);//to know the num of subs to initialize the arrays
            int subDescriptionCount=numOfElements(descriptions,searchText);//to know the num of subs to initialize the arrays
            //if matches a title add it to the list
            if (subTitleCount != 0){
                titlesArr=new String[subTitleCount];
                descriptionsArr=new String[subTitleCount];
                timesArr=new String[subTitleCount];
                datesArr=new String[subTitleCount];

                for (int i=0,j=0;i<titles.size();i++){
                    if (titles.elementAt(i).toLowerCase().contains(searchText.toLowerCase())){

                        titlesArr[j]=titles.elementAt(i);
                        descriptionsArr[j]=descriptions.elementAt(i);
                        timesArr[j]=times.elementAt(i);
                        datesArr[j]=dates.elementAt(i);

                        j++;
                        doesExist=true;
                    }
                }
                lAdapter = new ListAdapter(ToDoListActivity.this, titlesArr, descriptionsArr, timesArr, datesArr);
            }
            // if matches a description add it to the same list then show it in the app
            else if(subDescriptionCount != 0){

                titlesArr=new String[subDescriptionCount];
                descriptionsArr=new String[subDescriptionCount];
                timesArr=new String[subDescriptionCount];
                datesArr=new String[subDescriptionCount];

                for (int i=0,j=0;i<titles.size();i++){
                    if (descriptions.elementAt(i).toLowerCase().contains(searchText.toLowerCase())){
                        titlesArr[j]=titles.elementAt(i);
                        descriptionsArr[j]=descriptions.elementAt(i);
                        timesArr[j]=times.elementAt(i);
                        datesArr[j]=dates.elementAt(i);
                        j++;
                        doesExist=true;
                    }
                }
                lAdapter = new ListAdapter(ToDoListActivity.this, titlesArr, descriptionsArr, timesArr, datesArr);
            }
            //here show the list that contains the matches from titles and descriptions
            list.setAdapter(lAdapter);

        }
        //if there's no match and the query not empty then print empty list
        if (!doesExist && !searchText.isEmpty()){// if the sub does not exist
            list.setAdapter(null);
            Toast.makeText(this, "No Results Found", Toast.LENGTH_SHORT).show();
        }


    }

    private int numOfElements(Vector<String> v , String sub){// to return the number of subs in vector
        int count=0;
        if (!sub.equalsIgnoreCase("")){
            for (int i=0;i<v.size();i++){
                if (v.elementAt(i).toLowerCase().contains(sub.toLowerCase()))
                    count++;
            }
        }
        return count;
    }

    protected void onDestroy()
    {
        todo.close();
        super.onDestroy();
    }

}

