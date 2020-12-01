package com.itkolyuk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseActivity extends AppCompatActivity {

    private static final String TAG = "FireBase" ;
    public static int RC_SIGN_IN=1;

    private Button btnFbSend,btnFbSend2,btnFbSend3;

    private FirebaseListAdapter<Message> adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                Toast.makeText(FirebaseActivity.this, "Авторизация успешна!", Toast.LENGTH_SHORT).show();

               // finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(FirebaseActivity.this, "Отмена авторизации!", Toast.LENGTH_SHORT).show();


                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(FirebaseActivity.this, "НЕт интернет соединения!", Toast.LENGTH_SHORT).show();

                    return;
                }

                Toast.makeText(FirebaseActivity.this, "НЕизвестная ошибка!", Toast.LENGTH_SHORT).show();

                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        btnFbSend = findViewById(R.id.btnFbSend);
        btnFbSend2 = findViewById(R.id.btnFbSend2);
        btnFbSend3 = findViewById(R.id.btnFbSend3);


       if (FirebaseAuth.getInstance().getCurrentUser()==null) {

           List <AuthUI.IdpConfig> prov = Arrays.asList(
                   new AuthUI.IdpConfig.EmailBuilder().build(),
                   new AuthUI.IdpConfig.PhoneBuilder().build());

           // Create and launch sign-in intent
           startActivityForResult(
                   AuthUI.getInstance()
                           .createSignInIntentBuilder()
                           .setAvailableProviders(prov)
                           .build(),
                   RC_SIGN_IN);
       }




        btnFbSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");
                Snackbar.make(v, "Запуск пошел!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                myRef.setValue("Hello, World! Im Net test!").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(FirebaseActivity.this, "Все отлично!", Toast.LENGTH_SHORT).show();
                              } else {
                            Toast.makeText(FirebaseActivity.this, "Все плохо!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) ;
            }

        });
        btnFbSend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");


                myRef.push().setValue(new Message("test1","test2"));

               if (true) return;


                Snackbar.make(v, "Запуск пошел!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                HashMap<String,Object> profileMap = new HashMap<>();
                      profileMap.put("name","Петро");
                profileMap.put("status","готов!");
                profileMap.put("name","Степан");
                profileMap.put("status"," НЕ готов!");

                myRef.child("xxx").setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(FirebaseActivity.this, "Все отлично!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FirebaseActivity.this, "Все плохо!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) ;


            }
        });
        btnFbSend3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayAllMessases();

//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference mDatabase = database.getReference("message500");
//                Snackbar.make(v, "Запуск пошел3!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//
//
//                // Create new post at /user-posts/$userid/$postid and at
//                // /posts/$postid simultaneously
//                String key = mDatabase.child("posts").push().getKey();
//                Post post = new Post("userId", "username", "title", "body");
//                Map<String, Object> postValues = post.toMap();
//
//                Map<String, Object> childUpdates = new HashMap<>();
//                childUpdates.put("/posts/" + key, postValues);
//                childUpdates.put("/user-posts/" + "userId" + "/" + key, postValues);
//
//                mDatabase.updateChildren(childUpdates);

                }});
         }

    private void displayAllMessases() {

        ListView listView = findViewById(R.id.lvFb);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                TextView textView_user = view.findViewById(R.id.msgUser);
                                                String txt = textView_user.getText().toString();
                                               // Snackbar.make(this, "Запуск пошел3!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                Toast.makeText(FirebaseActivity.this, "Привет "+txt, Toast.LENGTH_SHORT).show();
                                            }
                                        });


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("message");

        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLayout(R.layout.list_item)
                .setLifecycleOwner(this)
                .build();

        adapter = new FirebaseListAdapter<Message>(options)
       {
            @Override
            protected void populateView(@NonNull View v, Message model, int position) {
                TextView textView_user, textView_text;
                textView_user = v.findViewById(R.id.msgUser);
                textView_text = v.findViewById(R.id.msgText);
                textView_user.setText(model.getUser_id());
                textView_text.setText(model.getText());


            }
        };

                listView.setAdapter(adapter);


                }
                }

