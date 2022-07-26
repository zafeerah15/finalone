package sG.EDU.NP.MAD.friendsOnly.bottomNav;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import sG.EDU.NP.MAD.friendsOnly.R;
import sG.EDU.NP.MAD.friendsOnly.Session;
import sG.EDU.NP.MAD.friendsOnly.Startup;
import sG.EDU.NP.MAD.friendsOnly.databinding.FragmentChatBinding;
import sG.EDU.NP.MAD.friendsOnly.messages.MessagesAdapter;
import sG.EDU.NP.MAD.friendsOnly.messages.MessagesList;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private final List<MessagesList> messagesLists = new ArrayList<>();
    private String mobile = "";
    //    private String email;
//    private String name;
    private Boolean granted;
    Session session;
    private int unseenMessages = 0;

    private String chatKey = "";

    private boolean dataSet = false;
    private RecyclerView messagesRecyclerView;
    private FloatingActionButton fab;
    private String lastMessage = "";
    private MessagesAdapter messagesAdapter;
    private FirebaseAuth mAuth;
    private String userType = "";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final CircleImageView userProfilePic = root.findViewById(R.id.userProfilePic);
        session = new Session(getContext());
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        fab = root.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Log out?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mAuth.signOut();
                        Intent intent = new Intent(getContext(), Startup.class);


                        startActivity(intent);
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.show();
            }

        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mobile = currentUser.getDisplayName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagesRecyclerView = root.findViewById(R.id.messagesRecyclerView);

        // get intent data from registerpage.class activity
//        mobile = getIntent().getStringExtra("mobile");

        if (mobile.isEmpty()) {
            mobile = getActivity().getIntent().getStringExtra("mobile");
        }

//        email = getActivity().getIntent().getStringExtra("email");
//        Toast.makeText(getContext(), session.getusename(), Toast.LENGTH_SHORT).show();
//        name = getActivity().getIntent().getStringExtra("name");



        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // set adapter to recyclerview
        messagesAdapter = new MessagesAdapter(messagesLists, getContext());
        messagesRecyclerView.setAdapter(messagesAdapter);

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        messagesLists.clear();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String profilepictureUrl = snapshot.child("users").child(mobile).child("profile_pic").getValue(String.class);


//                if (profilepictureUrl == null || !profilepictureUrl.isEmpty()) {
//                    // set profile pic to circle image view
//                    Picasso.get().load(profilepictureUrl).into(userProfilePic);
//                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                unseenMessages = 0;
                chatKey = "";
                granted = false;
                lastMessage = "";
                messagesLists.clear();
                final int userCount = (int) snapshot.child("users").getChildrenCount();


                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {
                    final String getMobile = dataSnapshot.getKey();
//                    final int userCount = (int) dataSnapshot.ge();

                    dataSet = false;
//
                    if (!getMobile.equals(mobile)) {
                        final String getName = dataSnapshot.child("name").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("profile_pic").getValue(String.class);
                        final String getBio = dataSnapshot.child("bio").getValue(String.class);
                        databaseReference.child("chat").addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                lastMessage = "";
                                unseenMessages = 0;
                                userType = "";
                                int getChatCounts = (int) snapshot.getChildrenCount();

                                if (getChatCounts > 0) {

                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                                        final String pToUser = dataSnapshot1.child("permission").child("toUser").getValue(String.class);
                                        final String pFromUser = dataSnapshot1.child("permission").child("fromUser").getValue(String.class);
                                        final Boolean pGranted = dataSnapshot1.child("permission").child("granted").getValue(Boolean.class);

                                        // chat get key msg e.g. "646434681458797"
                                        final String getKey = dataSnapshot1.getKey();

                                        chatKey = getKey;

                                        if (dataSnapshot1.getKey().equals(chatKey) && dataSnapshot1.child("permission").hasChild("toUser")) {

                                            if (chatKey.contains(mobile) && chatKey.contains(getMobile) && pGranted.equals(false) && pToUser.equals(mobile)) {
                                                lastMessage = getName + " wants to chat with you!";
                                                unseenMessages = 1;
                                                userType = "recipient";


                                                // For sender: check if recipient accept

                                            }
                                            // For sender: check if recipient accept
                                            if (chatKey.contains(mobile) && chatKey.contains(getMobile) && pGranted.equals(false) && pFromUser.equals(mobile)) {
                                                userType = "sender";
                                                lastMessage = "Chat request sent!";

                                            }
                                        }

                                    }
                                }

                                dataSet = true;
                                MessagesList messagesList = new MessagesList(getName, getMobile, lastMessage, getProfilePic, unseenMessages,
                                        getMobile, granted, userType, getBio);

                                if (messagesLists.size() + 1 < userCount) {

                                    messagesLists.add(messagesList);
                                    messagesAdapter.updateData(messagesLists);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Inflate the layout for this fragment
        return root;
    }
}