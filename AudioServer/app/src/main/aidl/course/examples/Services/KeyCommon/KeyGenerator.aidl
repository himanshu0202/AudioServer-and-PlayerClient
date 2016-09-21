package course.examples.Services.KeyCommon;

interface KeyGenerator {

    void onStart(in String clipName);
    void onPause();
    void onResume();
    void onStop();
    String[] getRecords();
}