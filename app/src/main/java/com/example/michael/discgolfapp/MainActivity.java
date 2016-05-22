package com.example.michael.discgolfapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button myButton;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    TextView tv7;
    TextView tv8;
    TextView tv9;
    TextView tv10;
    TextView tv11;
    TextView tv12;
    TextView tv13;
    TextView tv14;
    TextView tv15;
    TextView tv16;
    TextView tv17;
    TextView tv18;


    Integer incrementedInteger = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myButton = (Button)findViewById(R.id.btnHurtScore);
        tv1 = (TextView)findViewById(R.id.textView);
        tv2 = (TextView)findViewById(R.id.textView2);
        tv3 = (TextView)findViewById(R.id.textView3);
        tv4 = (TextView)findViewById(R.id.textView4);
        tv5 = (TextView)findViewById(R.id.textView5);
        tv6 = (TextView)findViewById(R.id.textView6);
        tv7 = (TextView)findViewById(R.id.textView7);
        tv8 = (TextView)findViewById(R.id.textView8);
        tv9 = (TextView)findViewById(R.id.textView9);
        tv10 = (TextView)findViewById(R.id.textView10);
        tv11 = (TextView)findViewById(R.id.textView11);
        tv12 = (TextView)findViewById(R.id.textView12);
        tv13 = (TextView)findViewById(R.id.textView13);
        tv14 = (TextView)findViewById(R.id.textView14);
        tv15 = (TextView)findViewById(R.id.textView15);
        tv16 = (TextView)findViewById(R.id.textView16);
        tv17 = (TextView)findViewById(R.id.textView17);
        tv18 = (TextView)findViewById(R.id.textView18);


    }

    public void OnButtonClick(View v){
        incrementedTextNumber();
        tv1.setText(incrementedInteger.toString());
        tv2.setText(incrementedInteger.toString());
        tv3.setText(incrementedInteger.toString());
        tv4.setText(incrementedInteger.toString());
        tv5.setText(incrementedInteger.toString());
        tv6.setText(incrementedInteger.toString());
        tv7.setText(incrementedInteger.toString());
        tv8.setText(incrementedInteger.toString());
        tv9.setText(incrementedInteger.toString());
        tv10.setText(incrementedInteger.toString());
        tv11.setText(incrementedInteger.toString());
        tv12.setText(incrementedInteger.toString());
        tv13.setText(incrementedInteger.toString());
        tv14.setText(incrementedInteger.toString());
        tv15.setText(incrementedInteger.toString());
        tv16.setText(incrementedInteger.toString());
        tv17.setText(incrementedInteger.toString());
        tv18.setText(incrementedInteger.toString());
    }

    private String incrementedTextNumber(){
        incrementedInteger -= 1;
        return incrementedInteger.toString();
    }
}
