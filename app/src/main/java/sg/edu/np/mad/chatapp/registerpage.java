package sg.edu.np.mad.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class registerpage extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);

        final EditText name = findViewById(R.id.r_name);
        final EditText bio = findViewById(R.id.r_desc);
        final EditText phone = findViewById(R.id.r_phoneno);
        final EditText email = findViewById(R.id.r_email);
        final AppCompatButton registerBtn = findViewById(R.id.r_registerBtn);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        // check if you already logged in
        if (!MemoryData.getData(this).isEmpty()) {
            Intent intent = new Intent ( registerpage.this, MainActivity.class);
            intent.putExtra("mobile", MemoryData.getData(this));
            intent.putExtra("name", MemoryData.getName(this));
            intent.putExtra("email", "");
            startActivity(intent);
            finish();
        }
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                progressDialog.show();

                final String nameTxt = name.getText().toString();
                final String phoneTxt = phone.getText().toString();
                final String emailTxt = email.getText().toString();
                final String bioTxt = bio.getText().toString();

                if (nameTxt.isEmpty() || phoneTxt.isEmpty() || emailTxt.isEmpty()) {
                    Toast.makeText(registerpage.this, "All Fields are Required !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            progressDialog.dismiss();

                            if (snapshot.child("users").hasChild(phoneTxt)){
                                Toast.makeText(registerpage.this,"Phone no. already exists", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                databaseReference.child("users").child(phoneTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(phoneTxt).child("name").setValue(nameTxt);
                                databaseReference.child("users").child(phoneTxt).child("bio").setValue(bioTxt);
                                databaseReference.child("users").child(phoneTxt).child("profile_pic").setValue("");

                                // save mobile to memory
                                MemoryData.saveData(phoneTxt, registerpage.this);

                                // save name to memory
                                MemoryData.saveName(nameTxt, registerpage.this);

                                Toast.makeText(registerpage.this,"Success", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent ( registerpage.this, MainActivity.class);
                                intent.putExtra("mobile", phoneTxt);
                                intent.putExtra("name", nameTxt);
                                intent.putExtra("email", emailTxt);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }});

    }}