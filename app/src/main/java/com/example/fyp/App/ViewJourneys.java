package com.example.fyp.App;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fyp.Adapters.RecyclerViewAdapter;
import com.example.fyp.R;

import java.util.ArrayList;

public class ViewJourneys extends AppCompatActivity {
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journeys);

        initImageBitMaps();
    }

    private void initImageBitMaps() {

        mImageUrls.add("https://img.maximummedia.ie/joe_ie/eyJkYXRhIjoie1widXJsXCI6XCJodHRwOlxcXC9cXFwvbWVkaWEtam9lLm1heGltdW1tZWRpYS5pZS5zMy5hbWF6b25hd3MuY29tXFxcL3dwLWNvbnRlbnRcXFwvdXBsb2Fkc1xcXC8yMDE4XFxcLzAyXFxcLzA3MTMwMTI3XFxcL3F1aXptYXJrLmpwZ1wiLFwid2lkdGhcIjo2NDAsXCJoZWlnaHRcIjozNjAsXCJkZWZhdWx0XCI6XCJodHRwczpcXFwvXFxcL3d3dy5qb2UuaWVcXFwvYXNzZXRzXFxcL2ltYWdlc1xcXC9qb2VcXFwvbm8taW1hZ2UucG5nP2lkPTI2NGEyZGJlMzcwZjJjNjc1ZmNkXCIsXCJvcHRpb25zXCI6W119IiwiaGFzaCI6IjU4YWIyYzNjZjllYjRlZDgwYTFmZTQ4NzkwZWVhNmFiODc1ZmIyNDUifQ==/quizmark.jpg");
        mNames.add("General Knowledge");

        mImageUrls.add("https://imagesvc.timeincapp.com/v3/mm/image?url=https%3A%2F%2Fcdn-s3.si.com%2Fs3fs-public%2Fimages%2F8-muhammad-ali-1965-fs.jpg&w=800&q=85");
        mNames.add("Sports");


        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recylerview);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
