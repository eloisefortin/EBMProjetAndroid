package fr.ec.producthunt.data.database;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import fr.ec.producthunt.data.model.Comment;
import fr.ec.producthunt.data.model.Post;

/**
 * @author Mohammed Boukadir  @:mohammed.boukadir@gmail.com
 */
public class CommentDao {

  private final ProductHuntDbHelper productHuntDbHelper;

  public CommentDao(ProductHuntDbHelper productHuntDbHelper) {
    this.productHuntDbHelper = productHuntDbHelper;
  }

  public long save(Comment comment) {
    return productHuntDbHelper.getWritableDatabase()
        .replace(DataBaseContract.CommentTable.TABLE_NAME, null, comment.toContentValues());
  }

  public List<Comment> retrievePosts() {


    Cursor cursor = productHuntDbHelper.getReadableDatabase()
        .query(DataBaseContract.CommentTable.TABLE_NAME,
            DataBaseContract.CommentTable.PROJECTIONS,
            null, null, null, null, null);

    List<Comment> comments = new ArrayList<>(cursor.getCount());

    if (cursor.moveToFirst()) {
      do {

        Comment comment = new Comment();

        comment.setId(cursor.getInt(0));
        comment.setPostId(cursor.getString(1));
        comment.setBody(cursor.getString(2));
        comment.setUsername(cursor.getString(3));
        comment.setUser(cursor.getString(4));
        comments.add(comment);


      } while (cursor.moveToNext());
    }

    cursor.close();

    return comments;
  }
}
