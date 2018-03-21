package fr.ec.producthunt.data.model;

import android.content.ContentValues;

import fr.ec.producthunt.data.database.DataBaseContract;

/**
 * Created by hazegard on 21/03/18.
 */

public class Collection {
  private long id;
  private String name;
  private String title;
  private String background_image_url;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBackground_image_url() {
    return background_image_url;
  }

  public void setBackground_image_url(String background_image_url) {
    this.background_image_url = background_image_url;
  }

  public ContentValues toContentValues() {

    ContentValues contentValues = new ContentValues();
    contentValues.put(DataBaseContract.CollectionTable.ID_COLUMN, id);
    contentValues.put(DataBaseContract.CollectionTable.TITLE_COLUMN, title);
    contentValues.put(DataBaseContract.CollectionTable.NAME_COLUMN, name);
    contentValues.put(DataBaseContract.CollectionTable.BACKGROUND_IMAGE_URL_COLUMN, background_image_url);
    return contentValues;
  }
}
