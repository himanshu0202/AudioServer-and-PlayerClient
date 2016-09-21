package course.examples.Services.KeyService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by himanshuanand on 4/6/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    final static String TABLE_NAME = "clipplayerlog";
    final static String CLIP_NAME = "name";
    final static String _TIMESTAMP = "timestamp";
    final static String REQUEST_TYPE = "requesttype";
    private static final String TAG = "DatabaseHelper";

    final static String CURRENT_STATE ="currentstate";
    final static String[] columns = { _TIMESTAMP,CLIP_NAME,REQUEST_TYPE,CURRENT_STATE };

    final private static String CREATE_CMD =

            "CREATE TABLE "+ TABLE_NAME + "( "
                    + _TIMESTAMP + " TEXT NOT NULL, "
                    + CLIP_NAME + " TEXT NOT NULL, "
                    + REQUEST_TYPE + " TEXT NOT NULL, "
                    + CURRENT_STATE + " TEXT NOT NULL)";


    final private static String NAME = "activityrecord_db";
    final private static Integer VERSION = 1;
    final private Context mContext;


    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
        Log.i(TAG, "In on Constru");
        this.mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,"In on create");
        db.execSQL(CREATE_CMD);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void deleteDatabase() {
        mContext.deleteDatabase(NAME);
    }
}
