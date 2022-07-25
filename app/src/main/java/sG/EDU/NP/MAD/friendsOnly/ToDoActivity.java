package sG.EDU.NP.MAD.friendsOnly;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ToDoActivity extends AppCompatActivity {

    //Usage of sql database
    DatabaseHandler databaseHandler = new DatabaseHandler(this,null,null,1);
    //List to store all the to do list items
    ArrayList<ToDo> toDoList = new ArrayList<>();
    //Custom add dialog
    Dialog addDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        //getSupportActionBar().hide();
        //Access the add button
        FloatingActionButton addTaskbutton = findViewById(R.id.addTaskbutton);

        //Custom add dialog
        addDialog = new Dialog(this);


        //Get all the task from the database
        toDoList = databaseHandler.getAllTask();

        //Binding of data, viewholder and adapter to the to do to do rv
        RecyclerView todorv = findViewById(R.id.todorv);
        ToDoAdapter todoAdapter = new ToDoAdapter(toDoList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        todorv.setLayoutManager(linearLayoutManager);
        todorv.setAdapter(todoAdapter);

        //Add new task
        addTaskbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Custom add dialog
                addDialog.setContentView(R.layout.add_task_dialog);
                addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button addBtn = addDialog.findViewById(R.id.addBtn);
                Button cancelBtn = addDialog.findViewById(R.id.cancelBtn);
                EditText editText = addDialog.findViewById(R.id.addTask);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                        String title = editText.getText().toString();

                        //If user input is empty
                        if (title==null || title.trim().equals(""))
                        {
                            addDialog.cancel();
                        }

                        //Else add new task
                        else {
                            ToDo task = new ToDo();
                            task.setTitle(title);
                            task.setStatus(0);
                            task.setUpdateDate(currentDate);

                            //Add task to database
                            databaseHandler.addTask(task);
                            toDoList.add(task);
                            addDialog.dismiss();
                        }
                        todoAdapter.notifyDataSetChanged();
                        finish();
                        startActivity(getIntent());
                    }
                });

                //User cancels
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDialog.cancel();
                    }
                });

                addDialog.show();

            }
        });

    }

    //Prevent activity from stacking
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}