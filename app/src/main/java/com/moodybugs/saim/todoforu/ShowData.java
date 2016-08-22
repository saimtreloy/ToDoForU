package com.moodybugs.saim.todoforu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class ShowData extends AppCompatActivity {
    DatabaseHelper myDatabase;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    EditText inputTitle, inputDescription,inputDate;
    Button btnEdit,btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_appbar);

        Initialization();
        populateData();

        ShowDatePicker();
    }

    public void Initialization(){
        myDatabase = new DatabaseHelper(this);
        inputTitle = (EditText) findViewById(R.id.inputTitle);
        inputDescription = (EditText) findViewById(R.id.inputDescription);
        inputDate = (EditText) findViewById(R.id.inputDate);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
    }
    String getIdFromListView;
    public void populateData(){
        getIdFromListView = null;
        String getTitleFromListView = null;
        String getDescriptionFromListView = null;
        String getDateFromListView = null;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            getIdFromListView = bundle.getString("KeyID");
            getTitleFromListView = bundle.getString("KeyTitle");
            getDescriptionFromListView = bundle.getString("KeyDescription");
            getDateFromListView = bundle.getString("KeyDate");
            //Toast.makeText(getApplicationContext(), getIdFromListView + getTitleFromListView+ getDescriptionFromListView+getDateFromListView , Toast.LENGTH_LONG).show();
        }

        inputTitle.setText(getTitleFromListView);
        inputDescription.setText(getDescriptionFromListView);
        inputDate.setText(getDateFromListView);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTitle.setEnabled(true);
                inputDescription.setEnabled(true);
                inputDate.setEnabled(true);
                v.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIdFromListView;
                String title = inputTitle.getText().toString();
                String description = inputDescription.getText().toString();
                String date = inputDate.getText().toString();

                if (title.isEmpty() || description.isEmpty() || date.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Text fields can not be empty!", Toast.LENGTH_LONG).show();
                }else {
                    boolean isUpdated = myDatabase.updateData(id.toString(), title.toString(), description.toString(), date.toString());
                    if (isUpdated == true){
                        Intent intent = new Intent(ShowData.this, HomePage.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "To do list updated for you.", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Something wrong in your system", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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
}
