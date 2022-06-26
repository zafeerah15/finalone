package sG.EDU.NP.MAD.friendsOnly.CHAT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import sG.EDU.NP.MAD.friendsOnly.MemoryData;
import sG.EDU.NP.MAD.friendsOnly.R;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.MyViewHolder> {

    private List<Chat_List> chatLists;
    private final Context context;
    private String userMobile;

    private FirebaseAuth mAuth;



    public Chat_Adapter(List<Chat_List> chatListList, Context context) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        this.chatLists = chatListList;
        this.context = context;
        //this.userMobile = MemoryData.getData(context);
        this.userMobile = currentUser.getDisplayName();
    }

    @NonNull
    @Override
    public Chat_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_Adapter.MyViewHolder holder, int position) {

        Chat_List list2 = chatLists.get(position);

        if (list2.getMobile().equals(userMobile)) {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(list2.getMessage());
            //holder.myImage.setImageBitmap(list2);
            holder.myTime.setText(list2.getDate()+" "+list2.getTime());

        } else {
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);

            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getDate()+" "+list2.getTime());
        }

    }

    @Override
    public int getItemCount() { return chatLists.size();}

    public void updateChatList(List<Chat_List> chatLists) { this.chatLists = chatLists; }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout oppoLayout, myLayout;
        //private ImageView oppoImage,myImage;
        private TextView oppoMessage, myMessage;
        private TextView oppoTime, myTime;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            //oppoImage = itemView.findViewById(R.id.oppoImage);
            //myImage = itemView.findViewById(R.id.myImage);
            oppoTime = itemView.findViewById(R.id.oppoMsgTime);
            myTime = itemView.findViewById(R.id.myMsgTime);

        }
    }
}
