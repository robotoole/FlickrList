package com.robotoole.flickrlist.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.robotoole.flickrlist.R;
import com.robotoole.flickrlist.model.Picture;

import java.sql.SQLException;
import java.util.List;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "pictures.db";
    public static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<Picture, Integer> pictureDao = null;
    private RuntimeExceptionDao<Picture, Integer> pictureRuntimeExceptionDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Picture.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number.
     * This allows you to adjust the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Picture.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Picture, Integer> getDao() throws SQLException {
        if (pictureDao == null) {
            pictureDao = getDao(Picture.class);
        }
        return pictureDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Picture, Integer> getPictureDao() {
        if (pictureRuntimeExceptionDao == null) {
            pictureRuntimeExceptionDao = getRuntimeExceptionDao(Picture.class);
        }
        return pictureRuntimeExceptionDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        pictureDao = null;
        pictureRuntimeExceptionDao = null;
    }

    /**
     * Save the last result of XML to the database after clearing it first.
     */
    public void saveCache(List<Picture> pictures) {
        // get our dao
        RuntimeExceptionDao<Picture, Integer> pictureDao = getPictureDao();
        // query for all of the data objects in the database
        List<Picture> list = pictureDao.queryForAll();
        // our string builder for building the content-view
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(list.size()).append(" entries in DB in ").append("save").append("()\n");

        // if we already have items in the database, we need to delete them.
        int simpleC = 1;
        for (Picture picture : list) {
            sb.append("#").append(simpleC).append(": ").append(picture).append("\n");
            simpleC++;
        }
        sb.append("------------------------------------------\n");
        sb.append("Deleted ids:");
        for (Picture picture : list) {
            pictureDao.delete(picture);
            sb.append(' ').append(picture.getId());
            Log.i(TAG, "deleting picture(" + picture.getId() + ")");
            simpleC++;
        }
        sb.append("\n");
        sb.append("------------------------------------------\n");

        int createNum;
        do {
            createNum = pictures.size();
        } while (createNum == list.size());
        sb.append("Creating ").append(createNum).append(" new entries:\n");
        for (int i = 0; i < createNum; i++) {
            // store it in the database
            pictureDao.create(pictures.get(i));
            // output it
            sb.append('#').append(i + 1).append(": ");
            sb.append(pictures.get(i).getTitle()).append("\n");
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                // ignore
            }
        }

        Log.d(TAG, sb.toString());
    }

    /**
     * Get all the pictures in the table.
     *
     * @return
     */
    public List<Picture> getAllPictures() {
        // get our dao
        RuntimeExceptionDao<Picture, Integer> pictureDao = getPictureDao();
        // query for all of the data objects in the database
        List<Picture> list = pictureDao.queryForAll();
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    /**
     * Clear all the pictures in the table.
     */
    public void clearPictures() {
        List<Picture> pictures = getAllPictures();

        RuntimeExceptionDao<Picture, Integer> pictureDao = getPictureDao();
        for (Picture picture : pictures) {
            pictureDao.delete(picture);
            Log.i(TAG, "deleting picture(" + picture.getId() + ")");
        }
    }
}