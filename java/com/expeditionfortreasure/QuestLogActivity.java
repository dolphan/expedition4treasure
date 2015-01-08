package com.expeditionfortreasure;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expeditionfortreasure.logic.GameLogic;
import com.expeditionfortreasure.logic.Quest;


public class QuestLogActivity extends Activity {
    GameLogic gl;
    int size;
    TextView[] tv;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_log);
        ll = (LinearLayout) findViewById(R.id.questloglayout);
        gl = GameLogic.getInstance();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quest_log, menu);
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

    @Override
    protected void onResume(){
        super.onResume();
        int size = gl.readQuest().size();
        tv = new TextView[size];
        Quest tmpQ;
        TextView temp;

        for(int i = 0; i < size; i++){
            tmpQ = gl.readQuest().get(i);
            temp = new TextView(this);
            temp.setText("Quest: " + tmpQ.number + " Rewarded " + tmpQ.reward );
            ll.addView(temp);
            tv[i] = temp;
            tv[i].setText("Quest: " + tmpQ.number + " Rewarded " + tmpQ.reward );
        }

    }
}
