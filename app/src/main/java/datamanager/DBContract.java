package datamanager;

import android.net.Uri;

import core.DBUtils;

import static core.DBUtils.ConnectionTable.COL_MEMORY_A;
import static core.DBUtils.ConnectionTable.COL_MEMORY_B;
import static core.DBUtils.MemoryTable.COL_CREATION_TIME;
import static core.DBUtils.MemoryTable.COL_MEMORY_FILE_PATH;
import static core.DBUtils.MemoryTable.COL_MEMORY_ID;
import static core.DBUtils.MemoryTable.COL_MEMORY_TEXT;
import static core.DBUtils.MemoryTable.TABLE_NAME;

/**
 * This class defines constants that help applications work with your DB.
 */

public class DBContract {

//    The base Uri that will be used to connect to the ContentProvider of this app
    //private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final Uri BASE_CONTENT_URI = Uri.parse(DBUtils.Database.INIT_CONTENT + DBUtils.Database.AUTHORITY);

    //    this is the Authority of this APP defined in the DBUtils class
    static final String AUTHORITY = DBUtils.Database.AUTHORITY;
    //    this is the database name retrieved from the DBUtils class
    static final String DATABASE_NAME = DBUtils.Database.DATABASE_NAME;

    //    this is the database version retrieved from the DBUtils class
    static final int DATABASE_VERSION = DBUtils.Database.DATABASE_VERSION;
    //    all the paths defined in the DBUtils class
    static final String PATH_MEMORY = DBUtils.Database.PATH_MEMORY;
    static final String PATH_MEMORIES = DBUtils.Database.PATH_MEMORIES;
    static final String PATH_GET = DBUtils.Database.PATH_GET;
    static final String PATH_INSERT = DBUtils.Database.PATH_INSERT;
    static final String PATH_DELETE = DBUtils.Database.PATH_DELETE;
    static final String PATH_UPDATE = DBUtils.Database.PATH_UPDATE;

    // inner class that holds all necessary data for access info from the MemoryTable
    public static final class MemoryTable {

        public static Uri uriGetMemory() {
//            static import
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_GET).build();
        }

        public static Uri uriGetMemories() {
//            static import
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORIES).appendPath(PATH_GET).build();
        }

        public static Uri uriInsertMemory() {
//            static import
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_INSERT).build();
        }

        public static Uri uriDeleteMemory() {
//            static import
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_DELETE).build();
        }

        public static Uri uriUpdateMemory() {
//            static import
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_UPDATE).build();
        }

        public static String getTableName() {
//            static import
            return TABLE_NAME;
        }

        public static String getColId() {
//            static import
            return COL_MEMORY_ID;
        }

        public static String getColMemoryText() {
//            static import
            return COL_MEMORY_TEXT;
        }

        public static String getColFilePath() {
//            static import
            return COL_MEMORY_FILE_PATH;
        }

        public static String getColCreationTime() {
//            static import
            return COL_CREATION_TIME;
        }
    }

    // inner class that holds all necessary data for access info from the ConnectionTable
    static final class ConnectionTable {

        public static String getTableName() {
            return DBUtils.ConnectionTable.TABLE_NAME;
        }

        public static String getColMemoryA() {
//            static import
            return COL_MEMORY_A;
        }

        public static String getColMemoryB() {
//            static import
            return COL_MEMORY_B;
        }
    }

}
