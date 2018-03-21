package fr.ec.producthunt.data.database;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import fr.ec.producthunt.data.model.Collection;

/**
 * @author Mohammed Boukadir  @:mohammed.boukadir@gmail.com
 */
public class CollectionDao {

  private final ProductHuntDbHelper productHuntDbHelper;

  public CollectionDao(ProductHuntDbHelper productHuntDbHelper) {
    this.productHuntDbHelper = productHuntDbHelper;
  }

  public long save(Collection collection) {
    return productHuntDbHelper.getWritableDatabase()
      .replace(DataBaseContract.CollectionTable.TABLE_NAME, null, collection.toContentValues());
  }

  public List<Collection> retrieveCollections() {


    Cursor cursor = productHuntDbHelper.getReadableDatabase()
      .query(DataBaseContract.CollectionTable.TABLE_NAME,
        DataBaseContract.CollectionTable.PROJECTIONS,
        null, null, null, null, null);

    List<Collection> collections = new ArrayList<>(cursor.getCount());

    if (cursor.moveToFirst()) {
      do {

        Collection collection = new Collection();

        collection.setId(cursor.getInt(0));
        collection.setTitle(cursor.getString(1));
        collection.setName(cursor.getString(2));
        collection.setBackground_image_url(cursor.getString(3));

        collections.add(collection);


      } while (cursor.moveToNext());
    }

    cursor.close();

    return collections;
  }
}
