package fr.ec.producthunt.data.model;

import android.content.ContentValues;

import fr.ec.producthunt.data.database.DataBaseContract;

/**
 * Created by erwan on 10/06/2018.
 */

public class Comment {

    private   long  id;
    private   String postId;
    private   String body;
    private   String username;
    private  String user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ContentValues toContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.CommentTable.ID_COLUMN, id);
        contentValues.put(DataBaseContract.CommentTable.POST_ID_COLUMN, postId);
        contentValues.put(DataBaseContract.CommentTable.BODY_COLUMN, body);
        contentValues.put(DataBaseContract.CommentTable.USERNAME_COLUMN, username);
        contentValues.put(DataBaseContract.CommentTable.USER_COLUMN, user);
        return contentValues;
    }
}
