package sG.EDU.NP.MAD.friendsOnly.story;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sG.EDU.NP.MAD.friendsOnly.R;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.MyViewHolder> {

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
        private ImageView story_url;
        private TextView caption;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilepicture = itemView.findViewById(R.id.s_profile_pic);
            username = itemView.findViewById(R.id.s_username);
            story_url = itemView.findViewById(R.id.story_media);
            caption = itemView.findViewById(R.id.post_caption);
        }

    }


}
