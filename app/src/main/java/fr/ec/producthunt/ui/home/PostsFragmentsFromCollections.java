package fr.ec.producthunt.ui.home;

import android.annotation.SuppressLint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.DataProvider;
import fr.ec.producthunt.data.JsonPostParser;
import fr.ec.producthunt.data.SyncService;
import fr.ec.producthunt.data.model.Post;
import fr.ec.producthunt.ui.comments.CommentsActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by erwan on 09/06/2018.
 */

public class PostsFragmentsFromCollections extends Fragment {

    private static final int PROGRESS_CHILD = 1;
    private static final int LIST_CHILD = 0;
    private static final String LIST_POSTS_STRING = "listPostString";
    private DataProvider dataProvider;
    private PostAdapter postAdapter;
    private ViewAnimator viewAnimator;
    private List<Post> listPost;

    private JsonPostParser jsonPostParser;
    private PostsFragmentsFromCollections.Callback callback;

    public PostsFragmentsFromCollections() {
        jsonPostParser = new JsonPostParser();
    }


    public static Fragment newInstance(String listPostString) {
        PostsFragmentsFromCollections myFragment = new PostsFragmentsFromCollections();

        Bundle args = new Bundle();
        args.putString(LIST_POSTS_STRING, listPostString);
        myFragment.setArguments(args);

        return myFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //getListPosts
        String listPostsString = getArguments().getString(LIST_POSTS_STRING);
        listPost = jsonPostParser.jsonToPostFromCollection(listPostsString);


        View rootView = inflater.inflate(R.layout.home_list_fragment, container, false);


        postAdapter = new PostAdapter();

        ListView listView = rootView.findViewById(R.id.list_item);
        listView.setEmptyView(rootView.findViewById(R.id.empty_element));


        //click On Post
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Post post = (Post) parent.getAdapter().getItem(position);
                callback.onClickPost(post);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long l) {
                Log.d(TAG, "longclick");

                Intent intent = new Intent(getActivity(), CommentsActivity.class);

                Post post = (Post) parent.getAdapter().getItem(position);

                intent.putExtra(PostsFragments.POST_ID, String.valueOf(post.getId()));
                intent.putExtra(PostsFragments.POST_TITLE, post.getTitle());
                startActivity(intent);
                return true;
            }
        });


        viewAnimator = rootView.findViewById(R.id.main_view_animator);
        listView.setAdapter(postAdapter);
        refreshPosts();


        //swippe
        final SwipeRefreshLayout swippeToRefresh = rootView.findViewById(R.id.swiperefresh);
        swippeToRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.

                        refreshPosts();
                        swippeToRefresh.setRefreshing(false);

                    }
                }
        );

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataProvider = DataProvider.getInstance(getActivity().getApplication());

        loadPosts(listPost);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                refreshPosts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void refreshPosts() {
        Toast.makeText(getContext(), "refreshPosts", Toast.LENGTH_LONG);
    }

    private void loadPosts(List<Post> posts) {
        if (posts != null && !posts.isEmpty()) {
            postAdapter.showPosts(posts);
        }
        viewAnimator.setDisplayedChild(LIST_CHILD);

    }

    public interface Callback {
        void onClickPost(Post post);
    }

}
