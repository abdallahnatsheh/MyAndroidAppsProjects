package com.abdallahn_ameerh.ex3;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class EditorActivity extends AppCompatActivity implements  View.OnClickListener{

    public static NotificationManager notificationManager;
    private static final String CHANNEL_ID = "channel_main";
    private static final CharSequence CHANNEL_NAME = "Main Channel";

    Button btnDatePicker,btnTimePicker,btnAdd;
    EditText txtDate, txtTime,txtTitle,txtDescription;
    TextView txtTodoTxt;
    Calendar c;
    int counter=0;
    public static final String MY_DB_NAME = "todoDB.db";
    SQLiteDatabase todo  = null;
    SharedPreferences sp ;
    String temp = null;
    String appName = null ;
    String todoID = null;
    boolean isUpdate;
    SharedPreferences.Editor editor ;

    @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        appName = sp.getString("username",temp);
        todoID = sp.getString("TodoID",temp);
        isUpdate = sp.getBoolean("updateStatus",isUpdate);

        btnDatePicker =  findViewById(R.id.btn_date);
        btnTimePicker =  findViewById(R.id.btn_time);
        btnAdd =  findViewById(R.id.addButton);
        txtDate = findViewById(R.id.todoDate);
        txtTime =  findViewById(R.id.todoTime);
        txtTitle =  findViewById(R.id.todoTitle);
        txtDescription = findViewById(R.id.todoDescription);
        txtTodoTxt = findViewById(R.id.todoEditTxt);
        if (isUpdate){
            showQuery(todoID,appName);
        }
        todo = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        btnAdd.setOnClickListener(v -> {
            if (txtTitle.getText().toString().isEmpty() ||txtDescription.getText().toString().isEmpty()
                    || txtDate.getText().toString().isEmpty() || txtTime.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "one or more fields are empty", Toast.LENGTH_SHORT).show();
            }else{
                while (checkid(counter)){
                    final Random myRandom = new Random();
                    counter = myRandom.nextInt(10000);
                }
                String datetime = txtDate.getText().toString() + " "+txtTime.getText().toString();
                String title = txtTitle.getText().toString();
                String desc =txtDescription.getText().toString();
                if (isUpdate)
                    updateQuery(todoID,appName,title,desc,datetime);
                else
                    addQuery(counter,appName,title,desc,datetime);
                Intent myIntent = new Intent(EditorActivity.this, ToDoListActivity.class);
                /*ALARM AND NOTIFICATION HERE!!*/
                startAlert();
                notificationsSetup();

                /*ALARM AND NOTIFICATION HERE!!*/
                startActivity(myIntent);
            }
        });
    }


    public void startAlert(){
        String[] spltDate = txtDate.getText().toString().split("-");
        String[] splitTime = txtTime.getText().toString().split(":");

        c.setTimeInMillis(System.currentTimeMillis());

        c.set(Calendar.YEAR, Integer.parseInt(spltDate[2]));
        c.set(Calendar.MONTH, Integer.parseInt(spltDate[1])-1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(spltDate[0]));

        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
        c.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Alarm set ",Toast.LENGTH_LONG).show();
    }
    private void notificationsSetup()
    {
        // 1. Get reference Notification Manager system Service
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // 2. Create Notification-Channel. ONLY for Android 8.0 (OREO API level 26) and higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, 	// Constant for Channel ID
                    CHANNEL_NAME, 	// Constant for Channel NAME
                    NotificationManager.IMPORTANCE_HIGH);  // for popup use: IMPORTANCE_HIGH

            notificationManager.createNotificationChannel(notificationChannel);
        }

        int notificationID = 1;
    }




    private void updateQuery(String todoID, String userName, String title, String desc, String datetime) {
        int todoIDnew = Integer.parseInt(todoID);
        //(id,username,title,description,datetime)
        String sql = "UPDATE  todo  SET  username = '" + userName + "', title =  '" + title +
                "', description = '" + desc + "', datetime = '" + datetime + "' WHERE id = '"+ todoIDnew +"' ";
        todo.execSQL(sql);
    }



    //check if id is already chosen in the table
    private   boolean checkid(int id){
        String idnumber = "SELECT username FROM todo "+" WHERE id = '"+id+ "'";
        Cursor cursorN = todo.rawQuery(idnumber, null);
        if(cursorN.getCount() <= 0){
            cursorN.close();
            return false;}
        else {
            return true;
        }

    }

    private void addQuery(int id,String UserName ,String Title,String Description , String DT ) {
        // Execute SQL statement to insert new data
        String sql = "INSERT INTO todo (id,username,title,description,datetime) VALUES ('" + id + "', '" + UserName + "', '" + Title + "', '" + Description + "', '" + DT + "');";
        todo.execSQL(sql);
        Toast.makeText(getApplicationContext(), "Todo was Added!", Toast.LENGTH_SHORT).show();
        Log.d("debug", "the query:"+sql);
        //dont forget to add alarm when the time come
    }
    private void showQuery(String queryId,String userName){
        todo = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
        int idd = Integer.parseInt(queryId);
        // A Cursor provides read and write access to database results
        String sql = "SELECT * FROM todo WHERE id = "+"'"+idd+"'"+" AND username = "+"'"+userName+"'";
        Cursor cursor = todo.rawQuery(sql, null);

        // Get the index for the column name provided
        int titleColumn = cursor.getColumnIndex("title");
        int descriptionColumn = cursor.getColumnIndex("description");
        int datetimeColumn = cursor.getColumnIndex("datetime");
        // Move to the first row of results
        cursor.moveToFirst();

        String title= cursor.getString(titleColumn);
        String description= cursor.getString(descriptionColumn);
        String datetime= cursor.getString(datetimeColumn);
        String[] splitStr = datetime.split("\\s+");
        /** to add to the EditText view **/
        btnAdd.setText("Update");
        txtTodoTxt.setText("Update Todo id=("+todoID+")");
        txtTitle.setText(title);
        txtDescription.setText(description);
        txtDate.setText(splitStr[0]);
        txtTime.setText(splitStr[1]);
    }


    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if ( year>=mYear && monthOfYear >=mMonth && dayOfMonth >= mDay){
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }else {
                                Toast.makeText(getApplicationContext(), "pick a valid date for the feature", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
             c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> txtTime.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

}
