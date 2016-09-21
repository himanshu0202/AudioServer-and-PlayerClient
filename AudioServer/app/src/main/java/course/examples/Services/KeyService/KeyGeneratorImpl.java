package course.examples.Services.KeyService;


import java.util.Calendar;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.content.res.AssetFileDescriptor;
import course.examples.Services.KeyCommon.KeyGenerator;
import java.io.IOException;
import android.media.AudioManager;


public class KeyGeneratorImpl extends Service {

    // Set of already assigned IDs
    // Note: These keys are not guaranteed to be unique if the Service is killed
    // and restarted.
    private static final String TAG = "KeyGeneratorImpl";
    private MediaPlayer mPlayer;
    private DatabaseHelper mDbHelper;
    private int id;
    private int currentSong;
    private String currentStatus;
    private String currentState;



    @Override
    public void onCreate() {
        super.onCreate();

        mDbHelper = new DatabaseHelper(this);

        clearAll();


    }


    // Implement the Stub for this Object
    private final KeyGenerator.Stub mBinder = new KeyGenerator.Stub() {


        public void onStart(String clipName) {


            if (mPlayer != null) {
                if (mPlayer.isPlaying())
                    mPlayer.stop();
                mPlayer.reset();
                currentStatus = "Stop";
                currentState = "Stopped clip number " + currentSong;
                insertRecords("" + currentSong, Calendar.getInstance().getTime().toString(), currentStatus, currentState);


                //from MediaPlayer implementation (link above)
                try {
                    AssetFileDescriptor afd = getResources().openRawResourceFd(id);
                    if (afd == null)
                        return;
                    mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

                    afd.close();
                    mPlayer.prepare();


                } catch (IOException ex) {
                    Log.d(TAG, "create failed:", ex);
                    // failed: return
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "create failed:", ex);
                    // failed: return
                } catch (SecurityException ex) {
                    Log.d(TAG, "create failed:", ex);
                    // failed: return
                }
            } else {
                //player is null
                //it will create new MediaPlayer instance, setDataSource and call prepare
                Log.i(TAG, clipName);


                id = getResources().getIdentifier("clip" + clipName, "raw", getPackageName());
                Log.i(TAG, "" + id);
                mPlayer = MediaPlayer.create(getApplicationContext(), id);
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


            }
            //if everything ok play file
            //in case of any error return from method before (catch)
            id = getResources().getIdentifier("clip" + clipName, "raw", getPackageName());
            Log.i(TAG, "" + id);
            currentStatus = "Play";
            currentSong = Integer.parseInt(clipName);
            currentState = "Playing clip number " + currentSong;
            mPlayer = MediaPlayer.create(getApplicationContext(), id);
            mPlayer.start();
            insertRecords("" + currentSong, Calendar.getInstance().getTime().toString(), currentStatus, currentState);
            //insertRecords("" + currentSong, Calendar.getInstance().getTime().toString(), currentStatus, currentState);
            //mPlayer.start();

        }

        public void onPause() {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    currentStatus = "Pause";
                    currentState = "paused while playing clip number " + currentSong;
                    insertRecords("" + currentSong, Calendar.getInstance().getTime().toString(), currentStatus, currentState);
                }
            }

        }

        public void onResume() {
            if (mPlayer != null) {
                if (!mPlayer.isPlaying()) {
                    mPlayer.start();
                    currentStatus = "Resume";
                    currentState = "Resumed clip number " + currentSong;
                    insertRecords("" + currentSong, Calendar.getInstance().getTime().toString(), currentStatus, currentState);

                }
            }


        }


        public void onStop() {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer = null;
                    currentStatus = "Stop";
                    currentState = "Stopped clip number " + currentSong;
                    insertRecords("" + currentSong, Calendar.getInstance().getTime().toString(), currentStatus, currentState);
                }
            }


        }

        public String[] getRecords() {
            Cursor c = readRecords();
            String[] records = new String[c.getCount()];
            String temp = "";
            int i = 0;
            while (c.moveToNext()) {
                temp = c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3);
                records[i++] = temp;
            }
            c.close();
            return records;
        }
    };

    private void clearAll() {

        mDbHelper.getWritableDatabase().delete(DatabaseHelper.TABLE_NAME, null, null);

    }

    private void insertRecords(String clipname, String timestamp, String requesttype, String currentstate) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper._TIMESTAMP, timestamp);
        values.put(DatabaseHelper.CLIP_NAME, clipname);
        values.put(DatabaseHelper.REQUEST_TYPE, requesttype);
        values.put(DatabaseHelper.CURRENT_STATE, currentstate);


        mDbHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_NAME, null, values);

        values.clear();

    }

    private Cursor readRecords() {
        /*return mDbHelper.getWritableDatabase().query(DatabaseOpenHelper.TABLE_NAME,
				DatabaseOpenHelper.columns, null, new String[]{}, null, null,
				null);*/
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selectQuery = "select * from " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbHelper.getWritableDatabase().close();
        mDbHelper.deleteDatabase();

    }

    // Return the Stub defined above
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}
