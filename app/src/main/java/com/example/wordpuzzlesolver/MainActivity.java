package com.example.wordpuzzlesolver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private Button searchButton;
    private WordUtil wordUtil;
    String inputString;
    private ArrayList<String> possibleStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this,"Note : Every word that is displayed " +
                "has a meaning",Toast.LENGTH_LONG).show();

        /*
        * Instantiating UI elements and resultant list
         */
        inputText = (EditText) findViewById(R.id.input);
        searchButton = (Button) findViewById(R.id.search);
        possibleStrings = new ArrayList<String>();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputString = inputText.getText().toString();
                if(inputString.length() == 0) {
                    Toast.makeText(MainActivity.this,
                            "Please give the input",Toast.LENGTH_LONG).show();
                } else {
                    wordUtil = new WordUtil(getApplicationContext());
                    wordUtil.getPermutation(inputString);
                    possibleStrings = wordUtil.generatePossibleStrings();
                    Collections.sort(possibleStrings,(a,b)->Integer.compare(a.length(),b.length()));
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1,possibleStrings);

                    ListView listView = (ListView) findViewById(R.id.list_item);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });

    }

}
