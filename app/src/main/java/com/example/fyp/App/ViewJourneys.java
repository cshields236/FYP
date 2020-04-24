package com.example.fyp.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.fyp.Adapters.RecyclerViewAdapter;
import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewJourneys extends AppCompatActivity {
    private static final String TAG = "ViewJourneys";

    private ArrayList<String> journeys = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journeys);

        initImageBitMaps();
    }

    private void initImageBitMaps() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref =   db.collection("users").document(user.getUid()).collection("Journeys");

        //Getting all journeys from database
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    Log.d(TAG, "onComplete: "  + task.getResult().getDocuments());
                    for (QueryDocumentSnapshot doc : task.getResult()) {

                        journeys.add(doc.getId());
//                        If Journeys are in the day set picture to daytime
                        if (Integer.parseInt(doc.getId().split(" ")[1].split(":")[0]) < 19 && Integer.parseInt(doc.getId().split(" ")[1].split(":")[0]) > 6) {
                            mImageUrls.add("data:image/jpeg;base64,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");
                        } else {
                            // If at night set night time picture
                            mImageUrls.add("https://img.rasset.ie/00141d5b-500.jpg");
                        }


                    }
                    initRecyclerView();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recylerview);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, journeys, mImageUrls);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
