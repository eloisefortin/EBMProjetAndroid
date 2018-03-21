package fr.ec.producthunt.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.ec.producthunt.data.model.Collection;
import fr.ec.producthunt.data.model.Post;

/**
 * @author Mohammed Boukadir  @:mohammed.boukadir@gmail.com
 */
public class JsonCollectionParser {

  public List<Collection> jsonToCollections(String json) {

    try {

      JSONObject collectionsResponse = new JSONObject(json);
      JSONArray collectionsJson = collectionsResponse.getJSONArray("collections");

      int size = collectionsJson.length();

      ArrayList<Collection> collections = new ArrayList(size);

      for (int i = 0; i < collectionsJson.length(); i++) {
        JSONObject collectionJson = (JSONObject) collectionsJson.get(i);

        collections.add(jsonToCollections(collectionJson));
      }

      return collections;
    } catch (JSONException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  private Collection jsonToCollections(JSONObject collectionJson) throws JSONException {
    Collection collection = new Collection();
    //"thumbnail": {
    //  "id": 139278,
    //      "media_type": "image",
    //      "image_url": "https://ph-files.imgix.net/b27175ba-ff99-4099-9092-1e8e8fb1cc77?auto=format&w=430&h=570&fit=max",
    //      "metadata": {}
    //},
    collection.setId(collectionJson.getInt("id"));
    collection.setTitle(collectionJson.getString("title"));
    collection.setName(collectionJson.getString("name"));
    collection.setBackground_image_url(collectionJson.getString("background_image_url"));

    return collection;
  }
}
