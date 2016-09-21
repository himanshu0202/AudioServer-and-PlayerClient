// KeyGenerator.aidl
package course.examples.Services.KeyCommon;

// Declare any non-default types here with import statements

interface KeyGenerator {

    void onStart(in String clipName);
    void onPause();
    void onResume();
    void onStop();
    String[] getRecords();

}

