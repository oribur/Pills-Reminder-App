package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class weeklyActivity extends AppCompatActivity {
private String watch_day;
public final static String EXTRA="com.example.Application.example.EXTRA";
    private Button sun;
    private Button mon;
    private Button tues;
    private Button wedn;
    private Button thur;
    private Button fri;
    private Button sat;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);
        Common.curActivity = this;
        setUpUIg();
    }
    /*set up the ui on the screen with the correct id */
    private void setUpUIg()
    {
        sun = (Button)findViewById(R.id.Sunday);
        mon=(Button)findViewById(R.id.Monday);
        tues = (Button)findViewById(R.id.Tuesday);
        wedn=(Button)findViewById(R.id.Wednesday);
        thur = (Button)findViewById(R.id.Thursday);
        fri=(Button)findViewById(R.id.friday);
        sat = (Button)findViewById(R.id.Saturday);
        back=(Button)findViewById(R.id.back);
    }

    /* -checking which day was selected by the user
        -saving it in the var --> watch_day
        -moving to screen that present the pills for the specific day */

    public void buttonClick(View view)
    {
        Button button = (Button)view;
        String operation = button.getText().toString();
        switch (operation)
        {
            case "Sunday":
                watch_day="sunday";
                break;
            case "Monday":
                watch_day="monday";
                break;
            case "Tuesday":
                watch_day="tuesday";
                break;
            case "Wednesday":
                watch_day="wednesday";
                break;
            case "Thursday":
                watch_day="thursday";
                break;
            case "Friday":
                watch_day="friday";
                break;
            case "Saturday":
                watch_day="saturday";
                break;
            default:
                break;
        }

        Intent intent=new Intent(weeklyActivity.this,SpecificDay.class);
        intent.putExtra(EXTRA,watch_day);
        startActivity(intent);
    }

   public String GetWatchDay()
    {
        return watch_day;
    }

/*going back to the screen of the current day of pills */
    public void BackClick(View view)
    {

        Intent intent=new Intent(weeklyActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
