package sG.EDU.NP.MAD.friendsOnly.messages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sG.EDU.NP.MAD.friendsOnly.MemoryData;
import sG.EDU.NP.MAD.friendsOnly.R;
import sG.EDU.NP.MAD.friendsOnly.CHAT.Chat;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<MessagesList> messagesLists;
    private final Context context;
    private String chatKey;
    private String getUserMobile;
    private Boolean chkKey;

    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String getNumber = currentUser.getDisplayName();
        MessagesList list2 = messagesLists.get(position);

        if (!list2.getProfilepicture().isEmpty()) {
            Picasso.get().load(list2.getProfilepicture()).into(holder.profilepicture);
        }
        //if bio is empty
        if (list2.getBio() != null){
            holder.bio.setText(" " + list2.getBio());
        } else {
            holder.bio.setText("");
        }

        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessage());

        if (list2.getUnseenMessages() == 0) {
            holder.unseenMessages.setVisibility(View.GONE);

            if (list2.getUserType().equals("sender")) {
                holder.lastMessage.setTextColor(Color.parseColor("#a83258"));
            } else {
                holder.lastMessage.setTextColor(Color.parseColor("#959595"));
            }


        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list2.getUnseenMessages() + "");
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.theme_color_80));
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                chkKey = false;
                //getUserMobile = MemoryData.getData(view.getContext());
                getUserMobile = getNumber;

                // get data of chatkey
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chatKey = snapshot.child("chat").hasChild(list2.getPhoneno() + getUserMobile) ? list2.getPhoneno() + getUserMobile : getUserMobile + list2.getPhoneno();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                if (list2.getUserType().contains("recipient")) {
                    //Log.d("test", "i am recipient ");

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getRootView().getContext());
                    alertDialog.setMessage("Allow " + list2.getName() + " to chat with you?");
                    alertDialog.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            databaseReference.child("chat").child(chatKey).child("permission").child("granted").setValue(true);

                            Intent intent = new Intent(context, Chat.class);
                            intent.putExtra("mobile", list2.getPhoneno());
                            intent.putExtra("name", list2.getName());
                            intent.putExtra("profile_pic", list2.getProfilepicture());
                            intent.putExtra("chat_key", list2.getChatKey());
                            context.startActivity(intent);


                            databaseReference.child("users").child(list2.getPhoneno()).child("friends").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        // fail means no array inside
                                        ArrayList<String> frenz = new ArrayList<String>();
                                        frenz.add(getUserMobile);
                                        databaseReference.child("users").child(list2.getPhoneno()).child("friends").setValue(frenz);
                                    } else {
                                        ArrayList frenz = new ArrayList<String>();
                                        frenz= (ArrayList) task.getResult().getValue();
                                        if (frenz == null) {
                                            ArrayList<String> newList = new ArrayList<String>();
                                            newList.add(getUserMobile);
                                            databaseReference.child("users").child(list2.getPhoneno()).child("friends").setValue(newList);
                                        } else {
                                            frenz.add(getUserMobile);
                                            databaseReference.child("users").child(list2.getPhoneno()).child("friends").setValue(frenz);
                                        }
                                    }
                                }
                            });

                            databaseReference.child("users").child(getUserMobile).child("friends").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        // fail means no array inside
                                        ArrayList<String> frenz = new ArrayList<String>();
                                        frenz.add(list2.getPhoneno());
                                        databaseReference.child("users").child(getUserMobile).child("friends").setValue(frenz);
                                    } else {
                                        ArrayList frenz = new ArrayList<String>();
                                        frenz=  (ArrayList) task.getResult().getValue();
                                        if (frenz == null) {
                                            ArrayList<String> newList = new ArrayList<String>();
                                            newList.add(list2.getPhoneno());
                                            databaseReference.child("users").child(getUserMobile).child("friends").setValue(newList);
                                        } else {
                                            frenz.add(list2.getPhoneno());
                                            databaseReference.child("users").child(getUserMobile).child("friends").setValue(frenz);


                                        }
                                    }
                                }
                            });



                            dialogInterface.dismiss();
                            messagesLists.clear();

                        }


                    });

                    alertDialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Log.d("test", "disallow it ");
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alert = alertDialog.create();
                    alert.show();

                }

                if (list2.getUserType().contains("sender")) {
                    //Log.d("test", "i am sender ");
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getRootView().getContext());
                    alertDialog.setMessage(list2.getName() + " have not accept your chat request.");

                    alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //Log.d("test", "move on ");
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }

                if (list2.getUserType().isEmpty()) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final Boolean hasKey = snapshot.child("chat").hasChild(chatKey);

                            if (!hasKey) {
                                //Log.d("test", hasKey.toString());
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getRootView().getContext());
                                alertDialog.setMessage("Send chat request to " + list2.getName() + "?");

                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        databaseReference.child("chat").child(list2.getPhoneno() + getUserMobile).child("permission").child("fromUser").setValue(getUserMobile);
                                        databaseReference.child("chat").child(list2.getPhoneno() + getUserMobile).child("permission").child("granted").setValue(false);
                                        databaseReference.child("chat").child(list2.getPhoneno() + getUserMobile).child("permission").child("toUser").setValue(list2.getPhoneno());
                                        Log.d("test", "Request sent! ");



                                        dialogInterface.dismiss();

                                    }
                                });
                                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Log.d("test", "Cancelled req! ");
                                        dialogInterface.dismiss();
                                    }
                                });

                                AlertDialog alert = alertDialog.create();
                                alert.show();

                            } else {
                                // means chat has accepted.
                                Intent intent = new Intent(context, Chat.class);
                                intent.putExtra("mobile", list2.getPhoneno());
                                intent.putExtra("name", list2.getName());
                                intent.putExtra("profile_pic", list2.getProfilepicture());
                                intent.putExtra("chat_key", list2.getChatKey());
                                context.startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                    Log.d("test", "chkkey: " + chkKey);

//                    Intent intent = new Intent(context, Chat.class);
//                    intent.putExtra("mobile", list2.getPhoneno());
//                    intent.putExtra("name", list2.getName());
//                    intent.putExtra("profile_pic", list2.getProfilepicture());
//                    intent.putExtra("chat_key", list2.getChatKey());
//
//                    context.startActivity(intent);
                }
            }
        });
    }
//
    public void updateData(List<MessagesList> messagesLists) {
        this.messagesLists = messagesLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profilepicture;
        private TextView name;
        private TextView lastMessage;
        private TextView unseenMessages;
        private TextView bio;
        private LinearLayout rootLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilepicture = itemView.findViewById(R.id.profilepicture);
            name = itemView.findViewById(R.id.name);
            bio = itemView.findViewById(R.id.bio);
            lastMessage = itemView.findViewById(R.id.lastMessages);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.rootLayout);

        }

    }

}

