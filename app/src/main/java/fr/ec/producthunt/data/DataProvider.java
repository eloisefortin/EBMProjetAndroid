package fr.ec.producthunt.data;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fr.ec.producthunt.data.database.CollectionDao;
import fr.ec.producthunt.data.database.CommentDao;
import fr.ec.producthunt.data.database.PostDao;
import fr.ec.producthunt.data.database.ProductHuntDbHelper;
import fr.ec.producthunt.data.model.Collection;
import fr.ec.producthunt.data.model.Comment;
import fr.ec.producthunt.data.model.Post;

import static android.content.ContentValues.TAG;

public class DataProvider {

    public static final String POST_API_END_POINT =
            "https://api.producthunt.com/v1/posts?sort_by=created_at&&access_token=46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abb";

    public static final String COLLECTION_API_END_POINT =
            "https://api.producthunt.com/v1/collections?search[featured]=true&access_token=46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abb";
    private JsonPostParser jsonPostParser = new JsonPostParser();
    private JsonCollectionParser jsonCollectionParser = new JsonCollectionParser();
    private JsonCommentParser jsonCommentParser = new JsonCommentParser();
    private final PostDao postDao;
    private final CollectionDao collectionDao;
    private static DataProvider dataProvider;
    private CommentDao commentDao;

    public static DataProvider getInstance(Application application) {

        if (dataProvider == null) {
            dataProvider = new DataProvider(ProductHuntDbHelper.getInstance(application));
        }
        return dataProvider;
    }

    public DataProvider(ProductHuntDbHelper dbHelper) {
        collectionDao = new CollectionDao(dbHelper);
        postDao = new PostDao(dbHelper);
        commentDao = new CommentDao(dbHelper);
    }

    private String getShitFromWeb(String apiUrl) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Contiendra la réponse JSON brute sous forme de String .
        String posts = null;

        try {
            // Construire l' URL de l'API ProductHunt
            URL url = new URL(apiUrl);

            // Creer de la requête http vers  l'API ProductHunt , et ouvrir la connexion
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Lire le  input stream et le convertir String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Si le stream est vide, on revoie null;
                return null;
            }
            posts = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        return posts;
    }


    public List<Post> getPostsFromDatabase() {
        return postDao.retrievePosts();
    }

    public List<Collection> getCollectionsFromDatabase() {
        return collectionDao.retrieveCollections();
    }

    public List<Comment> getCommentsFromDataBase(String postId) {
        return commentDao.retrieveComments(postId);
    }


    public Boolean syncPost() {

        List<Post> list = jsonPostParser.jsonToPosts(getShitFromWeb(POST_API_END_POINT));


        int nb = 0;

        for (Post post : list) {
            postDao.save(post);
            nb++;
        }
        return nb > 0;
    }

    public Boolean syncCollection() {
        List<Collection> list = jsonCollectionParser.jsonToCollections(getShitFromWeb(COLLECTION_API_END_POINT));
        int nb = 0;
        for (Collection collection : list) {
            collectionDao.save(collection);
            nb++;
        }
        return nb > 0;
    }

    public Boolean syncComment(String postId, String lastCommentId) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("api.producthunt.com");
        uriBuilder.appendPath("v1");
        uriBuilder.appendPath("posts");
        uriBuilder.appendPath(postId);
        uriBuilder.appendPath("comments");
        uriBuilder.appendQueryParameter("access_token", "46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abb");
        /*if (!lastCommentId.isEmpty()) {
            uriBuilder.appendQueryParameter("newer", lastCommentId);
        }*/
        Log.d(TAG,"syncComment : uri :"+ uriBuilder.build().toString());
        List<Comment> list = jsonCommentParser.jsonToComment(getShitFromWeb(uriBuilder.build().toString()));
        int nb = 0;
        for (Comment comment : list) {
            commentDao.save(comment);
            nb++;
        }
        return nb > 0;
    }


}

