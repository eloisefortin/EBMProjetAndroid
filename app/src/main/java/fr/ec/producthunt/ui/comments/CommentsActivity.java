package fr.ec.producthunt.ui.comments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import fr.ec.producthunt.R;
import fr.ec.producthunt.ui.home.PostsFragments;

public class CommentsActivity extends AppCompatActivity {
    Long postId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getIntent().getLongExtra(PostsFragments.POST_ID, 0L);
        String title = getIntent().getStringExtra(PostsFragments.POST_TITLE);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(title);

    }


}
