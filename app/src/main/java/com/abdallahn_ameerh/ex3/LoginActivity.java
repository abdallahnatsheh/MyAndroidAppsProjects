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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    public static final String MY_DB_NAME = "todoDB.db";

    private SQLiteDatabase users = null;
    private Button btnLogin;
    private EditText edtName, edtPass;
    SharedPreferences sp ;
    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Todo Login");
        btnLogin = findViewById(R.id.btnLogin);
        edtName = findViewById(R.id.UserName);
        edtPass = findViewById(R.id.UserPassword);
        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sp.edit();
        createDB();
        btnLogin.setOnClickListener(v -> checkUser());
    }
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void checkUser() {
        Intent intent = new Intent(LoginActivity.this, ToDoListActivity.class);

        // Get the contact name and email entered
        String userName = edtName.getText().toString();
        String password = edtPass.getText().toString();
        String goodUser = userName.replaceAll("[^a-zA-Z0-9]", "");

        //if name and password not existed add them
        if (!goodUser.isEmpty() && !password.isEmpty()) {
            switch (CheckIsUserAlreadyInDBorNot("users", goodUser, password)){
                case 0:
                    Log.d("debug", "user not existed");
                    addUser(goodUser, password);
                    editor.putString("username",goodUser);
                    editor.putBoolean("isLogged",true);
                    editor.apply();
                    editor.commit();
                    startActivity(intent);
                    /*i need to go to todo activity here*/
                    break;
                case 1:
                    Log.d("debug", "user is existed");
                    editor.putString("username",goodUser);
                    editor.putBoolean("isLogged",true);
                    editor.apply();
                    editor.commit();
                    startActivity(intent);
                    break;
                case -1:
                    Toast.makeText(this, "user name or password is wrong", Toast.LENGTH_SHORT).show();
                    break;
            }

        }else{
            Toast.makeText(this, "username or password is empty", Toast.LENGTH_SHORT).show();
        }
    }
    public  int CheckIsUserAlreadyInDBorNot(String TableName, String UserName ,String Password) {
        String HashedPassword = md5(Password);
        String queryName = "SELECT username FROM  "+TableName+" WHERE username = '"+UserName+ "'";
        String passName = "SELECT username,password FROM  "+TableName+" WHERE username = '"+UserName+ "' AND "+ "password = '"+ HashedPassword+"'";
        Cursor cursorN = users.rawQuery(queryName, null);
        Cursor cursorP = users.rawQuery(passName, null);

        if(cursorN.getCount() <= 0){
            cursorN.close();
            return 0;
        }else if (cursorP.getCount() <= 0){
            cursorP.close();
            return -1;
        }else
            return 1;
    }

    public void createDB() {
        try
        {
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            users = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);

            // build an SQL statement to create 'users' table (if not exists)
            String sql = "CREATE TABLE IF NOT EXISTS users (username VARCHAR primary key, password VARCHAR);";
            users.execSQL(sql);
            Log.d("debug", "Database created");
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Creating Database");
        }
    }
    public void addUser(String UserName ,String Password ) {
        String HashedPassword = md5(Password);
        // Execute SQL statement to insert new data
        String sql = "INSERT INTO users (username, password) VALUES ('" + UserName + "', '" + HashedPassword + "');";
        users.execSQL(sql);
        Log.d("debug", UserName + " was insert!" + " tha pass:" + Password);
    }
    //app bar menu with buttons and their actions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.base_menu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_base_menu:   // when about clicked in menu
                AboutMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void AboutMenu() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("About App");
        alertDialog.setMessage("Todo App(com.abdallahn_ammerh.ex3)\nBy Abdallah Natsheh and Ameer Haddad , 19/05/2021");
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

}