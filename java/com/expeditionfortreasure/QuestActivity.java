package com.expeditionfortreasure;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.expeditionfortreasure.logic.GameLogic;


public class QuestActivity extends ActionBarActivity {
    GameLogic gl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        gl = GameLogic.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newQuest(View view){

        gl.newQuest();
        int number = gl.getCurrentQuest().number;
        Log.v("Quest", "New quest " + number);
    }

    public void abortQuest(View view){

        gl.abortQuest();
        Log.v("Quest","Aborted quest");
    }

    public void readQuest(View view){
        Intent intent = new Intent(this, QuestLogActivity.class);
        Log.v("Quest","Starting questlog");
        startActivity(intent);

    }

}
