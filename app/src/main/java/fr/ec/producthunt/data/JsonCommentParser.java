package fr.ec.producthunt.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.ec.producthunt.data.model.Comment;
import fr.ec.producthunt.data.model.Post;
import static android.content.ContentValues.TAG;
/**
 * @author Mohammed Boukadir  @:mohammed.boukadir@gmail.com
 */
public class JsonCommentParser {

  public  List<Comment> jsonToComment(String json) {

    try {

      JSONObject commentResponse = new JSONObject(json);
      JSONArray commentsJson = commentResponse.getJSONArray("comments");

      int size = commentsJson.length();
      Log.d(TAG,"commentJson size :"+size);
      ArrayList<Comment> comments = new ArrayList(size);

      for (int i = 0; i < commentsJson.length(); i++) {
        JSONObject commentJson = (JSONObject) commentsJson.get(i);

        comments.add(jsonToComment(commentJson));
      }

      return comments;
    } catch (JSONException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  private  Comment jsonToComment(JSONObject commentJson) throws JSONException {
    Comment comment = new Comment();

    comment.setId(commentJson.getInt("id"));
    comment.setBody(commentJson.getString("body"));

    JSONObject userJson = commentJson.getJSONObject("user");
    comment.setUser(userJson.getString("name"));
    comment.setUsername(userJson.getString("username"));
    comment.setPostId(commentJson.getString("post_id"));
    Log.d(TAG,"comment body :"+comment.getBody());

    return comment;
  }
}
