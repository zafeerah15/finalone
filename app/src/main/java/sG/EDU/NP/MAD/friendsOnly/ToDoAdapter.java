package sG.EDU.NP.MAD.friendsOnly;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoViewHolder> {
    private ArrayList<ToDo> toDoList;
    private DatabaseHandler databaseHandler;

    //Constructor
    public ToDoAdapter(ArrayList<ToDo> input, Context context) {
        toDoList = input;
        databaseHandler = new DatabaseHandler(context, null, null, 1);
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.todo_item,
                parent,
                false
        );
        return new ToDoViewHolder(item);
    }

    //Bind input to viewholder
    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        ToDo d = toDoList.get(position);
        holder.txtTitle.setText(d.getTitle());
        holder.checkBox.setChecked(convertToBool(d.getStatus()));

        //If user checks the checkbox, it will be saved to the database
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            //If user checks or uncheck the checkbox
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Update database bool value to true
                    //used value 1 cause database cannot store true or false only 1 or 0
                    databaseHandler.updateCheckBox(d.getId(), 1);

                    //If user checks the box, prompts user to deleted
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Completed task!");
                    builder.setMessage("Do you want to remove this task?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseHandler.deleteTask(d.getId());
                            toDoList = databaseHandler.getAllTask();
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                } else {
                    databaseHandler.updateCheckBox(d.getId(), 0);
                }
            }

        });


        //Editing of title
        holder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Custom edit dialog
                Dialog editDialog;
                editDialog = new Dialog(context);

                editDialog.setContentView(R.layout.edit_task_dialog);
                editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button editBtn = editDialog.findViewById(R.id.confirmBtn);
                Button cancelBtn = editDialog.findViewById(R.id.editCancel);
                EditText editText = editDialog.findViewById(R.id.edittedTask);


                //Click on edit button
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                        String title = editText.getText().toString();

                        //If input is empty
                        if (title == null || title.trim().equals("")){
                            editDialog.cancel();
                        }
                        //update database when title editted
                        else {
                            databaseHandler.updateTask(d.getId(), title, currentDate);
                            toDoList = databaseHandler.getAllTask();
                            notifyDataSetChanged();
                            editDialog.dismiss();
                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.cancel();
                    }
                });
                editDialog.show();

            }
        });
        //Set latest task edit date
        holder.txtDate.setText((d.getUpdateDate()));

        //Deleting of task when press and hold
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Dialog for deleting task
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Task");
                builder.setMessage("Are you sure you want to remove this task?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHandler.deleteTask(d.getId());
                        toDoList = databaseHandler.getAllTask();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    //Converting into to bool values
    private boolean convertToBool(int i){
        if (i == 0){
            return false;
        }
        else{
            return true;
        }
    }


    @Override
    public int getItemCount() {
        return toDoList.size();
    }
}
