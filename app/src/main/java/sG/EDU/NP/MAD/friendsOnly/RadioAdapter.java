package sG.EDU.NP.MAD.friendsOnly;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RadioAdapter extends RecyclerView.Adapter<RadioViewHolder>{
    private ArrayList<Radio> radioList;
    private Context context;

    //Constructor
    public RadioAdapter(ArrayList<Radio> input, Context context){
        radioList = input;
        this.context = context;
    }
    @NonNull
    @Override
    public RadioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.radio_item,
                parent,
                false
        );
        return new RadioViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioViewHolder holder, int position) {
        Radio radio = radioList.get(position);

        //Setting the radio station name
        holder.radioTitle.setText(radio.getRadioName());

        //Setting whether radio is offline or online
        if (radio.isOffline == false) {
            holder.radioOffline.setText("Online");
        }
        else{
            holder.radioOffline.setText("Offline");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sending data to radioactivity
                Bundle extras = new Bundle();
                extras.putString("Link",radio.radioLink);
                extras.putString("Title", radio.radioName);
                extras.putBoolean("IsOffline", radio.isOffline);

                Intent intent = new Intent(context, RadioActivity.class);
                intent.putExtras(extras);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return radioList.size();
    }
}

