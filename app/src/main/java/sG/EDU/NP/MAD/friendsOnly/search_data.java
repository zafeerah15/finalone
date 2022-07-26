package sG.EDU.NP.MAD.friendsOnly;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


// Search Data activity for the search feature


public class search_data extends AppCompatActivity {

    DatabaseReference mref;
    private ListView listdata;
    private AutoCompleteTextView txtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);



        mref= FirebaseDatabase.getInstance().getReference("users");
        listdata=(ListView)findViewById(R.id.listData);
        txtSearch=(AutoCompleteTextView)findViewById(R.id.txtSearch);


        ValueEventListener event= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mref.addListenerForSingleValueEvent(event);
    }


    // method for retrieving user info. from search

    private void populateSearch(DataSnapshot snapshot) {
        ArrayList<String> names=new ArrayList<>();
        if(snapshot.exists())
        {
            for(DataSnapshot ds:snapshot.getChildren())
            {
                String name=ds.child("name").getValue(String.class);
                names.add(name);
            }

            ArrayAdapter adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
            txtSearch.setAdapter(adapter);
            txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String name=txtSearch.getText().toString();
                    searchUser(name);

                }
            });

        }else{
            Log.d("users", "No data found");
        }

    }


    //Search user method
    private void searchUser(String name) {
        Query query=mref.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    ArrayList<String> listusers=new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        User user= new User(ds.child("name").getValue(String.class),ds.child("email").getValue(String.class));
                        listusers.add(user.getName()+"\n"+user.getEmail());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, listusers);
                    listdata.setAdapter(adapter);

                }else{
                    Log.d("users", "No Data Found");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //user class info.
    static class User
    {

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public User() {
        }
        public String getName() { return name; }

        public String getEmail() { return email; }

        public String name;
        public String email;
    }
}