package sG.EDU.NP.MAD.friendsOnly;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//To do view holder
public class ToDoViewHolder extends RecyclerView.ViewHolder {
    TextView txtTitle, txtDate;
    CheckBox checkBox;
    ImageView editIcon;

    public ToDoViewHolder(View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.taskTitle);
        txtDate = itemView.findViewById(R.id.taskDate);
        checkBox = itemView.findViewById(R.id.checkBox);
        editIcon = itemView.findViewById(R.id.editIcon);
    }
}
