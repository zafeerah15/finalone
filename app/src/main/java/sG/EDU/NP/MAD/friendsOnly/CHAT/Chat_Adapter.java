package sG.EDU.NP.MAD.friendsOnly.CHAT;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import sG.EDU.NP.MAD.friendsOnly.R;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.MyViewHolder> {

    private List<Chat_List> chatLists;
    private final Context context;
    private String userMobile;

    private FirebaseAuth mAuth;


    //Chat Adapter with firebase integration
    public Chat_Adapter(List<Chat_List> chatListList, Context context) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        this.chatLists = chatListList;
        this.context = context;
        //this.userMobile = MemoryData.getData(context);
        this.userMobile = currentUser.getDisplayName();
    }

    //Chat adapter view holder creation and format
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    //on bind view holder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Chat_List list2 = chatLists.get(position);
        //if it is user mobile set the view to myLayout

        if (list2.getMobile().equals(userMobile)) {
            holder.myImage.setVisibility(View.VISIBLE);
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(list2.getMessage());
            //holder.myImage.setImageBitmap(list2);

            if (list2.getIsMedia()) {
                Log.d("media", list2.getIsMedia().toString());

                holder.myMessage.setVisibility(View.GONE);
                Picasso.get().load(list2.getMediaUrl()).into(holder.myImage);
                holder.myImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewImage(list2.getMediaUrl());
                    }
                });

            } else {
                holder.myImage.setVisibility(View.GONE);
            }

            holder.myTime.setText(list2.getDate()+" "+list2.getTime());


        } else {
            //if it is not user mobile set the view to oppoLayout
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);

            holder.oppoMessage.setText(list2.getMessage());

            if (list2.getIsMedia()) {
                holder.oppoMessage.setVisibility(View.GONE);
                Picasso.get().load(list2.getMediaUrl()).into(holder.oppoImage);
                holder.oppoImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewImage(list2.getMediaUrl());
                    }
                });
            } else {
                holder.oppoImage.setVisibility(View.GONE);
            }

            holder.oppoTime.setText(list2.getDate()+" "+list2.getTime());
        }

    }

    //Get item count of chat list
    @Override
    public int getItemCount() { return chatLists.size();}

    //to update the chat list
    public void updateChatList(List<Chat_List> chatLists) { this.chatLists = chatLists; }

    //layouts for myLayout if user and oppoLayout for non user
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout oppoLayout, myLayout;
        private ImageView oppoImage,myImage;
        private TextView oppoMessage, myMessage;
        private TextView oppoTime, myTime;

        //View holder for itemView
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            oppoImage = itemView.findViewById(R.id.oppoMedia);
            myImage = itemView.findViewById(R.id.myMedia);
            oppoTime = itemView.findViewById(R.id.oppoMsgTime);
            myTime = itemView.findViewById(R.id.myMsgTime);


        }
    }

    private void viewImage(String url) {


        Dialog builder = new Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });
        builder.setContentView((R.layout.view_media_full));
        ImageView img = builder.findViewById(R.id.view_image);
        Picasso.get().load(url).into(img);

        Button popNo = builder.findViewById(R.id.popup_cancel);
        popNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });


        builder.show();
    }
}
