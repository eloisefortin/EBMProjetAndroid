package fr.ec.producthunt.ui.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.DataProvider;
import fr.ec.producthunt.data.JsonPostParser;
import fr.ec.producthunt.data.SyncService;
import fr.ec.producthunt.data.model.Collection;
import fr.ec.producthunt.data.model.Post;

import static android.content.ContentValues.TAG;

public class CollectionsFragments extends Fragment {

  private static final int PROGRESS_CHILD = 1;
  private static final int LIST_CHILD = 0;

  private DataProvider dataProvider;
  private CollectionAdapter collectionAdapter;
  private ViewAnimator viewAnimator;

  private SyncCollectionReceiver syncCollectionReceiver;

  private Callback callback;


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setHasOptionsMenu(true);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    //TODO Gérer le click sur une collection
    callback = (Callback) getActivity();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.home_list_fragment, container, false);

    syncCollectionReceiver = new SyncCollectionReceiver();

    collectionAdapter = new CollectionAdapter();

    ListView listView = rootView.findViewById(R.id.list_item);
    listView.setEmptyView(rootView.findViewById(R.id.empty_element));


    //click on collection
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         Collection collection = (Collection) parent.getAdapter().getItem(position);
         loadCollectionsPosts(collection.getId());


      }
    });
    viewAnimator = rootView.findViewById(R.id.main_view_animator);
    listView.setAdapter(collectionAdapter);
    refreshCollections();

    //swippe
    final SwipeRefreshLayout swippeToRefresh = rootView.findViewById(R.id.swiperefresh);
    swippeToRefresh.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                // This method performs the actual data-refresh operation.
                // The method calls setRefreshing(false) when it's finished.

                refreshCollections();
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
    loadCollections();
  }

  @Override
  public void onStart() {
    super.onStart();
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(SyncCollectionReceiver.ACTION_LOAD_COLLECTIONS);
    LocalBroadcastManager.getInstance(this.getContext())
      .registerReceiver(syncCollectionReceiver, intentFilter);
  }

  @Override
  public void onStop() {
    super.onStop();
    LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(syncCollectionReceiver);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    inflater.inflate(R.menu.main, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {

      case R.id.refresh:
        refreshCollections();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public class SyncCollectionReceiver extends BroadcastReceiver {
    public static final String ACTION_LOAD_COLLECTIONS = "fr.ec.producthunt.data.action.LOAD_COLLECTIONS";

    public SyncCollectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent != null && intent.getAction().equals(ACTION_LOAD_COLLECTIONS)) {
        loadCollections();
      }
    }
  }

  private void refreshCollections() {
    SyncService.startSyncCollections(getContext()); //TODO
  }

  private void loadCollections() {
    FetchCollectionsAsyncTask fetchCollectionAsyncTask = new FetchCollectionsAsyncTask();
    fetchCollectionAsyncTask.execute();
  }
  private void loadCollectionsPosts(Long collectionId) {
    FetchCollectionsPostsAsyncTask fetchCollectionsPostsAsyncTask = new FetchCollectionsPostsAsyncTask();
    fetchCollectionsPostsAsyncTask.execute(collectionId);
  }
  private class FetchCollectionsAsyncTask extends AsyncTask<Void, Void, List<Collection>> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      viewAnimator.setDisplayedChild(PROGRESS_CHILD);
    }

    @Override
    protected List<Collection> doInBackground(Void... params) {

      return dataProvider.getCollectionsFromDatabase();
    }

    @Override
    protected void onPostExecute(List<Collection> collections) {
      if (collections != null && !collections.isEmpty()) {
        collectionAdapter.showCollections(collections);
      }
      viewAnimator.setDisplayedChild(LIST_CHILD);
    }
  }

  private class FetchCollectionsPostsAsyncTask extends AsyncTask<Long, Void, String >{

    private JsonPostParser jsonPostParser;
    private static final String COLLECTION_DETAIL_API_ENPOINT = "https://api.producthunt.com/v1/collections/";
    private static final String QUERY_ACCESS_TOKEN = "?access_token=46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abb";
    public FetchCollectionsPostsAsyncTask() {
      this.jsonPostParser = new JsonPostParser();
    }


    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      viewAnimator.setDisplayedChild(PROGRESS_CHILD);
    }

    @Override
    protected String doInBackground(Long... ids) {

      String response = "";
      List<Post> listPosts=  new ArrayList<>();
      for (Long id : ids) {
        DefaultHttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(COLLECTION_DETAIL_API_ENPOINT+id+QUERY_ACCESS_TOKEN);
        Log.d(TAG, "doInBackground: url : "+COLLECTION_DETAIL_API_ENPOINT+id+QUERY_ACCESS_TOKEN);
        try {
          HttpResponse execute = client.execute(httpGet);
          InputStream content = execute.getEntity().getContent();

          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
          String s = "";
          while ((s = buffer.readLine()) != null) {
            response += s;
          }
          Log.d(TAG, "doInBackground: "+ response);

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return response;
    }

    @Override
    protected void onPostExecute(String posts) {

      callback.onClickCollection(posts);

    }
  }

  public interface Callback {
    void onClickCollection(String collectionString);
  }
}
