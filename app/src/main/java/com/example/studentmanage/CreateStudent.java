package com.example.studentmanage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;

public class CreateStudent extends AppCompatActivity {
    DBHelper db;
    private EditText inputName, inputClass, inputAddress;
    private Timer timer;
    private DatePicker inputBirth;
    private Button btnInsert, btnBirth;
    private DatePickerDialog datePickerDialog;
    private Students alreadyAvailableStudent;
    private AlertDialog dialogDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#273746"));
        actionBar.setBackgroundDrawable(colorDrawable);
        db = new DBHelper(this);
        inputName = findViewById(R.id.inputName);
        inputClass = findViewById(R.id.inputClass);
        btnBirth = findViewById(R.id.inputBirth);
        inputAddress = findViewById(R.id.inputAddress);
        btnInsert = findViewById(R.id.saveStudent);

        getdate();
        btnBirth.setText(getToday());
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStudent();

            }
        });
        if (getIntent().getBooleanExtra("isViewOrUpdate",false)){
            alreadyAvailableStudent = (Students) getIntent().getSerializableExtra("students");
            setViewOrUpdate();
        }
        if (alreadyAvailableStudent != null){

        }
//        String birthST = alreadyAvailableStudent.getBirth();

//
//        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
//        Date date = new Date(birthST);
//        String day = (String) DateFormat.format("dd", date); // 20
////        String monthString  = (String) DateFormat.format("MMM",  date); // Jun
//        String monthNumber = (String) DateFormat.format("MM", date); // 06
//        String year  = (String) DateFormat.format("yyyy", date);
//
//       Toast.makeText(CreateStudent.this, "s"+date, Toast.LENGTH_SHORT).show();
    }
    private void setViewOrUpdate(){
        inputName.setText(alreadyAvailableStudent.getName());
        inputClass.setText(alreadyAvailableStudent.getClassS());
        btnBirth.setText(alreadyAvailableStudent.getBirth());
        inputAddress.setText(alreadyAvailableStudent.getAddress());


    }
    private String getToday() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    private void getdate(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month++;
                    String date = makeDateString(day,month,year);
                    btnBirth.setText(date);
            }
        };
        int year,month,day;
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + getMonthFormat(month) + "/" + year;
    }

    private String getMonthFormat(int month) {
        switch (month){
            case 1:
                return "1";

            case 2:
                return "2";

            case 3:
                return "3";

            case 4:
                return "4";

            case 5:
                return "5";

            case 6:
                return "6";

            case 7:
                return "7";

            case 8:
                return "8";

            case 9:
                return "9";

            case 10:
                return "10";

            case 11:
                return "11";

            case 12:
                return "12";
            default:
                return "1";
        }

    }
    public void datePicker(View view){
        datePickerDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (alreadyAvailableStudent != null){
            getMenuInflater().inflate(R.menu.item_create,menu);
            return true;
        }
       return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.action_delete:
                showDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showDeleteDialog(){
        if (dialogDelete == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateStudent.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete,
                    (ViewGroup) findViewById(R.id.layoutDeleteStudentContainer)
            );
            builder.setView(view);
            dialogDelete = builder.create();
            if (dialogDelete.getWindow() != null){
                dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteStudent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (db.deleteStudent(alreadyAvailableStudent.getId())){
                        Intent intent = new Intent();
                        intent.putExtra("isNoteDeleted",true);
                        setResult(RESULT_OK,intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Xoá thất bại",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDelete.dismiss();
                }
            });
        }
        dialogDelete.show();
    }

    private void saveStudent(){
        if (inputName.getText().toString().trim().isEmpty() ||
                inputClass.getText().toString().trim().isEmpty() ||

                inputAddress.getText().toString().trim().isEmpty() ){
            Toast.makeText(this,"Thông tin cần thiết cần thiếu",Toast.LENGTH_SHORT).show();
        }else{
            final Students students = new Students();
            students.setName(inputName.getText().toString());
            students.setClassS(inputClass.getText().toString());
            students.setAddress(inputAddress.getText().toString());
            students.setBirth(btnBirth.getText().toString());
            if (alreadyAvailableStudent != null){
                students.setId(alreadyAvailableStudent.getId());
                if(db.updateStudent(students)){
                    Intent intent = new Intent(CreateStudent.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Sửa thất bại",Toast.LENGTH_SHORT).show();
                }
            }else{
                if(db.insertStudent(students)){
                    Intent intent = new Intent(CreateStudent.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Sửa thất bại",Toast.LENGTH_SHORT).show();
                }
            }


        }
    }

}