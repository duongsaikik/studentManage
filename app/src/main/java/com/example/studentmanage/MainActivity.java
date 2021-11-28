package com.example.studentmanage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StudentListerner{
    private RecyclerView studentRecycle;
    private List<Students> studentsList;
    private StudentAdapter studentAdapter;
    public static final int REQUEST_CODE_ADD_STUDENT = 1;
    private static final int REQUEST_CODE_UPDATE =2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;
    private int noteClickedPosition = -1;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHelper(this);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#273746"));
        actionBar.setBackgroundDrawable(colorDrawable);
        studentRecycle = findViewById(R.id.studentRecycleView);
        studentRecycle.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        );
        studentsList = new ArrayList<>();
        studentAdapter = new StudentAdapter(studentsList,this);
        studentRecycle.setAdapter(studentAdapter);
            getStudentList(REQUEST_CODE_SHOW_NOTES,false);
        EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    studentAdapter.CancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                        if (studentsList.size() !=0){
                            studentAdapter.searchStudent(s.toString());
                        }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.action_add:
                startActivityForResult(
                        new Intent(getApplicationContext(),CreateStudent.class),
                        REQUEST_CODE_ADD_STUDENT
                );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getStudentList(final int requestCode,final boolean isNoteDeleted){

        class GetList extends AsyncTask<Void, Void,List<Students>>{

            @Override
            protected List<Students> doInBackground(Void... voids) {
                return db.getAllStudent();
            }

            @Override
            protected void onPostExecute(List<Students> students) {
                super.onPostExecute(students);
                        if (requestCode == REQUEST_CODE_SHOW_NOTES){
                            studentsList.addAll(students);
                            studentAdapter.notifyDataSetChanged();
                        }else if (requestCode == REQUEST_CODE_ADD_STUDENT){
                            students.add(0,students.get(0));
                            studentAdapter.notifyItemInserted(0);
                            studentRecycle.smoothScrollToPosition(0);
                        }else if (requestCode == REQUEST_CODE_UPDATE){
                            studentsList.remove(noteClickedPosition);
                            if (isNoteDeleted){
                                studentAdapter.notifyItemRemoved(noteClickedPosition);
                            }else{
                                students.add(noteClickedPosition,students.get(noteClickedPosition));
                                studentAdapter.notifyItemChanged(noteClickedPosition);
                            }
                        }
            }

        }
        new GetList().execute();

    }
    @Override
    public void onStudentClick(Students students, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(),CreateStudent.class);
        intent.putExtra("isViewOrUpdate",true);
        intent.putExtra("students",students);
        startActivityForResult(intent,REQUEST_CODE_UPDATE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SHOW_NOTES && resultCode == RESULT_OK){
            getStudentList(REQUEST_CODE_SHOW_NOTES,false);
        }else if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK){
            if (data != null){
                getStudentList(REQUEST_CODE_UPDATE,data.getBooleanExtra("isNoteDeleted",false));
            }
        }
    }


}