package sG.EDU.NP.MAD.friendsOnly.bottomNav;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import sG.EDU.NP.MAD.friendsOnly.CustomProgressBar;
import sG.EDU.NP.MAD.friendsOnly.R;
import sG.EDU.NP.MAD.friendsOnly.Session;
import sG.EDU.NP.MAD.friendsOnly.databinding.FragmentStoryBinding;
import sG.EDU.NP.MAD.friendsOnly.story.StoryAdapter;
import sG.EDU.NP.MAD.friendsOnly.story.StoryList;


public class StoryFragment extends Fragment {

    private List<StoryList> storyLists = new ArrayList<>();
    private StorageReference mediaRef;
    private FragmentStoryBinding binding;
    private FloatingActionButton fab;
    Integer requestCode = 0;
    private FirebaseAuth mAuth;
    private RecyclerView storyRecyclerView;
    private StoryAdapter storyAdapter;
    Session session;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private static CustomProgressBar progressBar = new CustomProgressBar();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        storyRecyclerView = root.findViewById(R.id.storyRecyclerView);
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.refresh_story);
        getData();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                pullToRefresh.setRefreshing(false);
            }
        });

        session = new Session(getContext());

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CAMERA}, 101);
        }


        storyRecyclerView.setHasFixedSize(true);
        storyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // set adapter to recyclerview
        storyAdapter = new StoryAdapter(storyLists, getContext());
        storyRecyclerView.setAdapter(storyAdapter);

        fab = root.findViewById(R.id.fab_story);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });



        return root;
    }



    private void getData() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String getUser = currentUser.getDisplayName();
        storyLists.clear();


        databaseReference.child("users").child(getUser).child("friends").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {


            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {


                } else {



                    ArrayList frenz = new ArrayList<String>();
                    frenz = (ArrayList) task.getResult().getValue();

                    Query postAscending = databaseReference.child("stories").orderByChild("date");

                    ArrayList finalFrenz = frenz;
                    Log.d("tesyyy",getUser.toString());
                    postAscending.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            databaseReference.child("users").child(getUser).child("likes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    ArrayList likes = new ArrayList<String>();
                                    likes = (ArrayList) task.getResult().getValue();

                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        final String storyId = dataSnapshot.child("userId").getValue(String.class);
                                        final String key = dataSnapshot.getKey();
                                        if (getUser.equals(storyId) || (finalFrenz !=null && finalFrenz.contains(storyId))) {
                                            final String getName = dataSnapshot.child("username").getValue(String.class);
                                            final String getProfilePic = dataSnapshot.child("prof_url").getValue(String.class);
                                            final String getstory_url = dataSnapshot.child("story_url").getValue(String.class);
                                            final String caption = dataSnapshot.child("caption").getValue(String.class);
                                            final Long time = dataSnapshot.child("date").getValue(Long.class);

                                            Boolean like = (likes !=null && likes.contains(key));

                                            StoryList storyList = new StoryList(key, storyId, getName, getProfilePic, getstory_url, caption, like, time);
                                            storyLists.add(storyList);
                                        }
                                    }
                                    Collections.reverse(storyLists);
                                    storyAdapter.updateData(storyLists);
                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bp, "Title", null);
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
        Dialog builder = new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });


        builder.setContentView((R.layout.dialog_image));
        EditText caption = builder.findViewById(R.id.edit_caption);
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

                progressBar.show(getContext(),"");

                Long timeMillis = System.currentTimeMillis();
//
//                // create id -> userUniquekey + timestamp
                String uniquePhotoId = currentUser.getUid() + timeMillis;

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//                // upload image to Firebase
                StorageReference riversRef = storageRef.child("stories/" + uniquePhotoId + ".jpg");
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

                            // add story object into user
                            final String userKey = mAuth.getCurrentUser().getDisplayName();
                            String id = databaseReference.child("users").child(userKey).child("stories").push().getKey();
                            StoryList storyList = new StoryList("", userKey ,session.getusename(), session.getprofilePic(), downloadUri.toString(),caption.getText().toString(), false, timeMillis);



                            databaseReference.child("users").child(userKey).child("stories").child(id).setValue(storyList);
                            databaseReference.child("stories").child(id).setValue(storyList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getData();
                                }
                            });
                            progressBar.getDialog().dismiss();
                            builder.dismiss();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressBar.getDialog().dismiss();
                        Toast.makeText(getContext(), "Something wrong, please try again.", Toast.LENGTH_SHORT).show();
                        getData();
                    }
                });

            }
        });
        getData();

        builder.show();
    }

}