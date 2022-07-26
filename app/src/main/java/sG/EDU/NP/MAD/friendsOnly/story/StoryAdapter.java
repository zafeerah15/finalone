package sG.EDU.NP.MAD.friendsOnly.story;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sG.EDU.NP.MAD.friendsOnly.R;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.MyViewHolder> {
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<StoryList> storyLists;
    private final Context context;

    public StoryAdapter(List<StoryList> storyLists, Context context) {

        this.storyLists = storyLists;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.story_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String getNumber = currentUser.getDisplayName();


        StoryList storyList = storyLists.get(position);

        if (!storyList.getProf_url().isEmpty()) {
            Picasso.get().load(storyList.getProf_url()).into(holder.profilepicture);
        }

        holder.username.setText(storyList.getUsername());
        Picasso.get().load(storyList.getStory_url()).into(holder.story_url);

        if (storyList.getCaption().isEmpty()) {
            holder.caption.setVisibility(View.INVISIBLE);
        } else {
            holder.caption.setText(storyList.getCaption());
        }

        if (storyList.isLike()) {
            holder.unfav.setVisibility(View.INVISIBLE);
            holder.fav.setVisibility(View.VISIBLE);
        } else {
            holder.unfav.setVisibility(View.VISIBLE);
            holder.fav.setVisibility(View.INVISIBLE);
        }


        holder.unfav.setOnClickListener(new View.OnClickListener() {

            // means user Like Post
            @Override
            public void onClick(View view) {
                Log.d("testt", "lolol");
                databaseReference.child("users").child(getNumber).child("likes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        ArrayList likes = new ArrayList<String>();
                        likes = (ArrayList) task.getResult().getValue();
                        if (likes == null) {
                            ArrayList<String> newList = new ArrayList<String>();
                            newList.add(storyList.getStoryKey());
                            databaseReference.child("users").child(getNumber).child("likes").setValue(newList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    holder.unfav.setVisibility(View.INVISIBLE);
                                    holder.fav.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, "You Like a Post!", Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {
                            likes.add(storyList.getStoryKey());
                            databaseReference.child("users").child(getNumber).child("likes").setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    holder.unfav.setVisibility(View.INVISIBLE);
                                    holder.fav.setVisibility(View.VISIBLE);

                                    Toast.makeText(context, "You Dislike a Post", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            // Means user dislike, therefore array still in
            public void onClick(View view) {
                Log.d("testt", "loldsdol");
                databaseReference.child("users").child(getNumber).child("likes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        ArrayList likes = new ArrayList<String>();
                        likes = (ArrayList) task.getResult().getValue();
                        if (likes == null) {
                            // failsafe
                            ArrayList<String> newList = new ArrayList<String>();
                            newList.remove(storyList.getStoryKey());
                            databaseReference.child("users").child(getNumber).child("likes").setValue(newList);

                        } else {
                            Log.d("testt",storyList.getStoryKey());
                            likes.remove(storyList.getStoryKey());
                            databaseReference.child("users").child(getNumber).child("likes").setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    holder.unfav.setVisibility(View.INVISIBLE);
                                    holder.fav.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                });
            }
        });


    }


    public void updateData(List<StoryList> storyLists) {
        this.storyLists = storyLists;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return storyLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profilepicture;
        private TextView username;
        private ImageView story_url, fav, unfav;
        private TextView caption;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilepicture = itemView.findViewById(R.id.s_profile_pic);
            username = itemView.findViewById(R.id.s_username);
            story_url = itemView.findViewById(R.id.story_media);
            caption = itemView.findViewById(R.id.post_caption);
            fav = itemView.findViewById(R.id.fav);
            unfav = itemView.findViewById(R.id.unFav);
        }

    }


}
