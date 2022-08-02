package sG.EDU.NP.MAD.friendsOnly;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sG.EDU.NP.MAD.friendsOnly.CHAT.Chat;

public class ToDoActivity extends AppCompatActivity {
    private String chatKey;
    Integer requestCode = 0;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

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
                ImageButton attachment = addDialog.findViewById(R.id.attach);
                attachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectImage();

                    }
                });
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
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ToDoActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    someActivityResultLauncher.launch(intent);
                    requestCode = 1;
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    someActivityResultLauncher.launch(intent);
                    requestCode = 2;
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        File f = new File(Environment.getExternalStorageDirectory().toString());
                        if (requestCode == 1) {

                            Bitmap bp = (Bitmap) result.getData().getExtras().get("data");

                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bp.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
                            String path = MediaStore.Images.Media.insertImage(ToDoActivity.this.getContentResolver(), bp, "Title", null);
                            Uri uri = Uri.parse(path);
                            showImage(uri);


                        } else if (requestCode == 2) {

                            Uri selectedImage = result.getData().getData();

                            showImage(selectedImage);

                        }
                    }
                }
            });
    public void showImage(Uri uri) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Dialog builder = new Dialog(ToDoActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });


        builder.setContentView((R.layout.dialog_image));
        ImageView img = builder.findViewById(R.id.popup_image);
        img.setImageURI(uri);
        Button popNo = builder.findViewById(R.id.popup_no);
        Button popYes = builder.findViewById(R.id.popup_yes);

        popNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });

        popYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                progressBar.show(getContext(),"");

                Long timeMillis = System.currentTimeMillis();

                String uniquePhotoId = currentUser.getUid() + timeMillis;

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//                // upload image to Firebase
                StorageReference riversRef = storageRef.child("ToDolistMedia/" + uniquePhotoId + ".jpg");
                UploadTask uploadTask = riversRef.putFile(uri);

                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isComplete()) {
                            throw task.getException();
                        }

                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            final String currentTimestamp = String.valueOf(System.currentTimeMillis());


                            databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("isMedia").setValue(true);
                            databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("mediaUrl").setValue(downloadUri.toString());

                            builder.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });

        builder.show();
    }
}