package sG.EDU.NP.MAD.friendsOnly.messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sG.EDU.NP.MAD.friendsOnly.Session;
import sG.EDU.NP.MAD.friendsOnly.bottomNav.NavMainPage;

import sG.EDU.NP.MAD.friendsOnly.MainActivity;
import sG.EDU.NP.MAD.friendsOnly.R;

//Login page with firebase authentication
public class Loginpage extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        session = new Session(Loginpage.this);


        final AppCompatButton loginBtn = findViewById(R.id.l_loginBtn);

        final EditText email = findViewById(R.id.l_email);
        final EditText password = findViewById(R.id.l_password);

        //loginBtn on click listener
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


                //Sign in authentication using firebase
                mAuth.signInWithEmailAndPassword(emailTxt, passwordTxt)
                        .addOnCompleteListener(Loginpage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String id = task.getResult().getUser().getDisplayName();
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String username = snapshot.child("users").child(id).child("name").getValue(String.class);
                                            String user_prof = snapshot.child("users").child(id).child("profile_pic").getValue(String.class);

                                            session.setusername(username);
                                            session.setprofilePic(user_prof);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    Intent intent = new Intent(Loginpage.this, NavMainPage.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(Loginpage.this, "Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}