package fr.ec.producthunt.data.database;

import android.provider.BaseColumns;

/**
 * @author Mohammed Boukadir  @:mohammed.boukadir@gmail.com
 */
public final class DataBaseContract {

  public static final String DATABASE_NAME = "database";
  public static final int DATABASE_VERSION = 3;

  public static final String TEXT_TYPE = " TEXT";
  public static final String COMM_SPA = ",";
  public static final String INTEGER_TYPE = " INTEGER";

  /**
   * Description de la table des Posts
   **/
  public static final class PostTable implements BaseColumns {

    public static final String TABLE_NAME = "post";

    public static final String ID_COLUMN = "id";
    public static final String TITLE_COLUMN = "title";
    public static final String SUBTITLE_COLUMN = "subtitle";
    public static final String IMAGE_URL_COLUMN = "imageurl";
    public static final String POST_URL_COLUMN = "postUrl";
    public static final String NB_COMMENTS_COLUMN = "nbComments";

    public static final String SQL_CREATE_POST_TABLE =
      "CREATE TABLE " + PostTable.TABLE_NAME + " (" +
        PostTable.ID_COLUMN + INTEGER_TYPE + " PRIMARY KEY" + COMM_SPA +
        PostTable.TITLE_COLUMN + TEXT_TYPE + COMM_SPA +
        PostTable.SUBTITLE_COLUMN + TEXT_TYPE + COMM_SPA +
        PostTable.IMAGE_URL_COLUMN + TEXT_TYPE + COMM_SPA +
        PostTable.POST_URL_COLUMN + TEXT_TYPE +COMM_SPA +
        PostTable.NB_COMMENTS_COLUMN +TEXT_TYPE+
        ")";

    public static final String SQL_DROP_POST_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String[] PROJECTIONS = new String[]{
      ID_COLUMN,
      TITLE_COLUMN,
      SUBTITLE_COLUMN,
      IMAGE_URL_COLUMN,
      POST_URL_COLUMN,
      NB_COMMENTS_COLUMN,
    };
  }

  public static final class CollectionTable implements BaseColumns {

    public static final String TABLE_NAME = "collection";

    public static final String ID_COLUMN = "id";
    public static final String TITLE_COLUMN = "title";
    public static final String NAME_COLUMN = "name";
    public static final String BACKGROUND_IMAGE_URL_COLUMN = "imageurl";


    public static final String SQL_CREATE_COLLECTION_TABLE =
      "CREATE TABLE " + CollectionTable.TABLE_NAME + " (" +
        ID_COLUMN + INTEGER_TYPE + " PRIMARY KEY" + COMM_SPA +
        TITLE_COLUMN + TEXT_TYPE + COMM_SPA +
        NAME_COLUMN + TEXT_TYPE + COMM_SPA +
        BACKGROUND_IMAGE_URL_COLUMN + TEXT_TYPE +
        ")";

    public static final String SQL_DROP_COLLECTION_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String[] PROJECTIONS = new String[]{
      ID_COLUMN,
      TITLE_COLUMN,
      NAME_COLUMN,
      BACKGROUND_IMAGE_URL_COLUMN,
    };
  }
  public static final class CommentTable implements BaseColumns {

    public static final String TABLE_NAME = "comments";

    public static final String ID_COLUMN = "id";
    public static final String POST_ID_COLUMN = "postIdColumn";
    public static final String BODY_COLUMN = "bodyColumn";
    public static final String USERNAME_COLUMN = "usernameColumn";
    public static final String USER_COLUMN = "userColumn";


    public static final String SQL_CREATE_COMMENT_TABLE =
            "CREATE TABLE " + CommentTable.TABLE_NAME + " (" +
                    ID_COLUMN + INTEGER_TYPE + " PRIMARY KEY" + COMM_SPA +
                    POST_ID_COLUMN + TEXT_TYPE + COMM_SPA +
                    BODY_COLUMN + TEXT_TYPE + COMM_SPA +
                    USERNAME_COLUMN + TEXT_TYPE + COMM_SPA +
                    USER_COLUMN + TEXT_TYPE +
                    ")";

    public static final String SQL_DROP_COMMENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String[] PROJECTIONS = new String[]{
            ID_COLUMN,
            POST_ID_COLUMN,
            BODY_COLUMN,
            USERNAME_COLUMN,
            USER_COLUMN,
    };
  }
}
