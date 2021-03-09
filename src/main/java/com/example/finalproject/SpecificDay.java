package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SpecificDay extends AppCompatActivity {
    private TextView chosen_day;
    RecyclerView recyclerView;
    private Button back;
    String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_day);

        Common.curActivity = this;
        /***************** upload the ui on the screen *****************/
        chosen_day=(TextView)findViewById(R.id.chosenday);
        back=(Button)findViewById(R.id.back);
        /**************************************************************/

        /********** getting the intent from the weekly pills screen and the var watch_day *************/
        Intent intent=getIntent();
        day=intent.getStringExtra(weeklyActivity.EXTRA);
        /********************************************************************************************/

        /*********** recycle view for the pills for the selected day ************************/
        recyclerView = this.findViewById(R.id.recycler_view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(this,day.toUpperCase());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       chosen_day.setText(intent.getStringExtra(weeklyActivity.EXTRA));
       /**********************************************************************************/

    }
    /*going back to the screen of the choose a day of pills */
    public void BackClick(View view)
    {
        Intent intent=new Intent(SpecificDay.this,weeklyActivity.class);
        startActivity(intent);
    }
}
