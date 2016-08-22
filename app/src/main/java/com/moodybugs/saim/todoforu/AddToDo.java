package com.moodybugs.saim.todoforu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddToDo extends AppCompatActivity {
    DatabaseHelper myDatabase;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    EditText inputTitle, inputDescription, inputDate;
    Button btnSaveToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_do);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_appbar);

        Innitialization();
        ShowDatePicker();
        SaveToDo();
    }

    public void Innitialization(){
        myDatabase = new DatabaseHelper(this);

        inputTitle = (EditText) findViewById(R.id.inputTitle);
        inputDescription = (EditText) findViewById(R.id.inputDescription);
        inputDate = (EditText) findViewById(R.id.inputDate);
        btnSaveToDo = (Button) findViewById(R.id.btnSaveToDo);
    }

    public void ShowDatePicker(){
        inputDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    Calendar calendar = Calendar.getInstance();
                    year_x = calendar.get(Calendar.YEAR);
                    month_x = calendar.get(Calendar.MONTH);
                    day_x = calendar.get(Calendar.DAY_OF_MONTH);
                    showDialog(DIALOG_ID);
                }

            }
        });
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID){
            return new DatePickerDialog(this, datePickerListener, year_x, month_x, day_x);
        }else{
            return null;
        }
    }

    public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear+1;
            day_x = dayOfMonth;
            Toast.makeText(getApplicationContext(), "Year : "+ year_x + "\nMonth : " + month_x + "\nDay : " + day_x, Toast.LENGTH_LONG).show();
            inputDate.setText(day_x + "/" + month_x + "/" + year_x);
        }
    };

    public void SaveToDo(){
        btnSaveToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = inputTitle.getText().toString();
                String description = inputDescription.getText().toString();
                String date = inputDate.getText().toString();

                if (title.isEmpty() || description.isEmpty() || date.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Text fields can not be empty!", Toast.LENGTH_LONG).show();
                }else {
                    boolean isInserted = myDatabase.insertData(title, description, date);
                    if (isInserted){
                        Intent intent = new Intent(AddToDo.this, HomePage.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "To do list created for you.", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Something wrong in your system", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
