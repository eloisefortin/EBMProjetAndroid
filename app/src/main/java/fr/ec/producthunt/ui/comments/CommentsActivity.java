package fr.ec.producthunt.ui.comments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import fr.ec.producthunt.R;
import fr.ec.producthunt.ui.home.PostsFragments;
import fr.ec.producthunt.ui.home.PostsFragmentsFromCollections;
import static android.content.ContentValues.TAG;
public class CommentsActivity extends AppCompatActivity {
    String postId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getIntent().getExtras().getString(PostsFragments.POST_ID);
        String title = getIntent().getStringExtra(PostsFragments.POST_TITLE);
        setContentView(R.layout.activity_comments);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(title);

        FragmentManager fm = getSupportFragmentManager();
        Log.d(TAG,"postId :"+postId);
        fm.beginTransaction().replace(R.id.content_frame, CommentsActivityFragment.newInstance(postId)).commit();
    }


}
