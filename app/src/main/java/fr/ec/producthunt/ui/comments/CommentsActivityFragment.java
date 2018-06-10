package fr.ec.producthunt.ui.comments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ViewAnimator;

import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.DataProvider;
import fr.ec.producthunt.data.SyncService;
import fr.ec.producthunt.data.model.Collection;
import fr.ec.producthunt.data.model.Comment;
import fr.ec.producthunt.ui.home.CollectionsFragments;
import fr.ec.producthunt.ui.home.PostsFragments;
import fr.ec.producthunt.ui.home.PostsFragmentsFromCollections;
import static android.content.ContentValues.TAG;
/**
 * A placeholder fragment containing a simple view.
 */
public class CommentsActivityFragment extends Fragment {
    private static final String POST_ID = "POST_ID";
    private static final int PROGRESS_CHILD = 0;
    private static final int LIST_CHILD = 1;
    private DataProvider dataProvider;
    private SyncCommentsReceiver syncCommentsReceiver;
    private CommentAdapter commentAdapter;
    private ViewAnimator viewAnimator;
    private String postId;
    public CommentsActivityFragment() {
    }

    public static Fragment newInstance(String postId) {
        CommentsActivityFragment myFragment = new CommentsActivityFragment();

        Bundle args = new Bundle();
        args.putString(POST_ID, postId);
        myFragment.setArguments(args);

        return myFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG,"CommentActivityFragment : getArguments");
        postId = getArguments().getString(POST_ID);

        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);
        syncCommentsReceiver = new SyncCommentsReceiver();
        commentAdapter = new CommentAdapter();



        //postId ="12453";

        ListView listView = rootView.findViewById(R.id.list_comments);
        listView.setEmptyView(rootView.findViewById(R.id.empty_comment_element));

        viewAnimator = rootView.findViewById(R.id.comments_view_animator);
        listView.setAdapter(commentAdapter);

        refreshComments(postId);
        //swippe
        final SwipeRefreshLayout swippeToRefresh = rootView.findViewById(R.id.swiperefresh);
        swippeToRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.

                        refreshComments(postId);
                        loadComments(postId);
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

        loadComments(postId);
    }
    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SyncCommentsReceiver.ACTION_LOAD_COMMENTS);
        LocalBroadcastManager.getInstance(this.getContext())
                .registerReceiver(syncCommentsReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(syncCommentsReceiver);
    }

    private void refreshComments(String postId) {
        SyncService.startSyncComments(getContext(), postId,""); //TODO
    }

    private void loadComments(String postId) {
        FetchCommentsAsyncTask fetchCommentsAsyncTask = new FetchCommentsAsyncTask();
        fetchCommentsAsyncTask.execute(postId);
    }



    public class SyncCommentsReceiver extends BroadcastReceiver {
        public static final String ACTION_LOAD_COMMENTS = "fr.ec.producthunt.data.action.LOAD_COMMENTS";

        public SyncCommentsReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ACTION_LOAD_COMMENTS)) {
                loadComments(postId);
            }
        }
    }


    private class FetchCommentsAsyncTask extends AsyncTask<String, Void, List<Comment>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "FetchCommentsAsyncTask preExecute");
            viewAnimator.setDisplayedChild(PROGRESS_CHILD);
        }

        @Override
        protected List<Comment> doInBackground(String... postIds) {

            Log.d(TAG, "FetchCommentsAsyncTask doInBackground postIds : " + postIds.length);

            return dataProvider.getCommentsFromDataBase(postIds[0]);
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {

            if (comments != null && !comments.isEmpty()) {
                Log.d(TAG,"FetchCommentsAsyncTask : comments Not Empty" );
                commentAdapter.showComments(comments);
            } else {
                Log.d(TAG,"FetchCommentsAsyncTask : comments Empty" );
            }
            //viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(getView().findViewById(R.id.list_comments)));
            //viewAnimator.setDisplayedChild(LIST_CHILD);
        }
    }
}
