package datamanager;

import android.net.Uri;
import core.DBUtils;

/**
 * This class defines constants that help applications work with your DB.
 */

public class DBContract extends DBUtils.Database {

//    The base Uri that will be used to connect to the ContentProvider of this app
    //private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final Uri BASE_CONTENT_URI = Uri.parse(DBUtils.Database.INIT_CONTENT + DBUtils.Database.AUTHORITY);

    // inner class that holds all necessary data for access info from the MemoryTable
    public static final class MemoryTable extends DBUtils.MemoryTable {

        public static Uri uriGetMemory() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_GET).build();
        }

        public static Uri uriGetMemories() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORIES).appendPath(PATH_GET).build();
        }

        public static Uri uriInsertMemory() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_INSERT).build();
        }

        public static Uri uriDeleteMemory() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_DELETE).build();
        }

        public static Uri uriUpdateMemory() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_UPDATE).build();
        }

        public static Uri uriNewConnection() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONNECTION).appendPath(PATH_INSERT).build();
        }

        public static String getTableName() {
            return TABLE_NAME;
        }

        public static String getColId() {
            return COL_MEMORY_ID;
        }

        public static String getColMemoryText() {
            return COL_MEMORY_TEXT;
        }

        public static String getColFilePath() {
            return COL_MEMORY_FILE_PATH;
        }

        public static String getColConnNum() {
            return COL_MEMORY_NUM_CONN;
        }

        public static String getColCreationTime() {
            return COL_CREATION_TIME;
        }
    }

    // inner class that holds all necessary data for access info from the ConnectionTable
    static final class ConnectionTable extends DBUtils.ConnectionTable {

        public static String getTableName() {
            return TABLE_NAME;
        }

        public static String getColMemoryA() {
            return COL_MEMORY_A;
        }

        public static String getColMemoryB() {
            return COL_MEMORY_B;
        }
    }

}
