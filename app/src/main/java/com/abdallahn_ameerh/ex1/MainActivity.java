package com.abdallahn_ameerh.ex1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button start_btn = findViewById(R.id.start_btn);
        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch MusicSwitch = (Switch) findViewById(R.id.music_switch);
        MusicSwitch.setChecked(sp.getBoolean("switch",false));
        getSupportActionBar().setTitle("Puzzle 15");
        start_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //move to the next activity
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                //implementation of music switch it will save the state of the switch
                //even after exit it
                if(MusicSwitch.isChecked()) {
                    editor.putBoolean("switch",true);
                    editor.apply();
                }
                if(!MusicSwitch.isChecked()) {
                    editor.putBoolean("switch",false);
                    editor.apply();
                }
                editor.commit();
                startActivity(intent);
            }
        });
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
            case R.id.exit_base_menu:    // when exit clicked in menu
                exitDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void AboutMenu() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("About App");
        alertDialog.setMessage("Puzzle15(com.abdallahn_ammerh.ex1)\nBy Abdallah Natsheh and Ameer Haddad , 24/3/21");
        alertDialog.setCancelable(true);
        alertDialog.setIcon(R.drawable.puzzle);
        alertDialog.show(); }

    private void exitDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("do you really want to exit Puzzle 15 ?");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.exit);
        alertDialog.setPositiveButton("Yes", (dialogInterface, dummy) -> finish());
        alertDialog.setNegativeButton("No", (dialogInterface, dummy) -> { });
        alertDialog.show();
    }
}