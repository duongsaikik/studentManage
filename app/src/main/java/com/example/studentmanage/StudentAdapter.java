package com.example.studentmanage;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Students> students;
    private StudentListerner studentListerner;
    private Timer timer;
    private List<Students> stSource;
    public StudentAdapter(List<Students> students,StudentListerner studentListerner) {
        this.students = students;
        this.studentListerner=studentListerner;
        stSource = students;
    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(
                LayoutInflater.from(
                        parent.getContext()).inflate(
                                R.layout.container_item,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.setStudent(students.get(position));

            holder.layoutStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    studentListerner.onStudentClick(students.get(position),position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textName,textId, textClass;
        LinearLayout layoutStudent;
         StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textNameST);
            textId = itemView.findViewById(R.id.textIdST);
            textClass = itemView.findViewById(R.id.textClassST);
            layoutStudent = itemView.findViewById(R.id.layoutStudentItem);
        }
        void setStudent(Students students){

            textName.setText(students.getName());
            textId.setText(students.getId());
            textClass.setText(students.getClassS());

        }
    }
    public void searchStudent(final String keyWord){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (keyWord.trim().isEmpty()){
                    students = stSource;
                }else{
                    ArrayList<Students> temp = new ArrayList<>();
                    for (Students i: stSource) {
                        if (i.getName().toLowerCase().contains(keyWord.toLowerCase()) ||
                        i.getId().toLowerCase().contains(keyWord.toLowerCase()) ||
                        i.getClassS().toLowerCase().contains(keyWord.toLowerCase())){
                            temp.add(i);
                        }
                    }
                    students = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        },500);
    }
    public void CancelTimer(){
        if (timer != null){
            timer.cancel();
        }
    }

}
