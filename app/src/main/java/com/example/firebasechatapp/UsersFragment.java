package com.example.firebasechatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapter.UserAdapter;
import model.User;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private Set<User> mUsers;

    private EditText search_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        mUsers = new HashSet<>();
        
        readUsers();


        search_users = v.findViewById(R.id.search_users);

        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    SearchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return v;


    }

    private void SearchUsers(String user) {
     final FirebaseUser TheUser = FirebaseAuth.getInstance().getCurrentUser();
     Query query = FirebaseFirestore.getInstance().collection("User").orderBy("search").startAt(user).endAt(user + "\uf8ff");
     query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
         @Override
         public void onComplete(@NonNull Task<QuerySnapshot> task) {
             if (search_users.getText().toString().equals("")) {
                 mUsers.clear();
                 for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                     User user = documentSnapshot.toObject(User.class);

                     assert TheUser != null;
                     if (!user.getId().equals(TheUser.getUid())) {
                         mUsers.add(user);
                     }
                 }
                 adapter = new UserAdapter(getContext(), mUsers, false);
                 recyclerView.setAdapter(adapter);
             }
         }
         });
     }


    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference reference = FirebaseFirestore.getInstance().collection("User");

       reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()){
                   for (QueryDocumentSnapshot snapshot : task.getResult()){
                       User user = snapshot.toObject(User.class);
                       assert firebaseUser != null;
                       if(!user.getId().equals(firebaseUser.getUid())){
                           mUsers.add(user);
                       }
                   }

                   adapter = new UserAdapter(getContext(), mUsers, false);
                   recyclerView.setAdapter(adapter);
               }
               else{
                   Toast.makeText(getActivity(), "Unable to load data", Toast.LENGTH_SHORT).show();
               }
           }
       });

    }
}