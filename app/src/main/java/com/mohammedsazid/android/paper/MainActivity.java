package com.mohammedsazid.android.paper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String PAPER_CONTENT_KEY = "paper_content_key";
    private String mPaperContent = "";
    private EditText mPaperEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
    }

    private void bindViews() {
        mPaperEditText = (EditText) findViewById(R.id.paper_et);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReadPaperTask readPaperTask = new ReadPaperTask();
        readPaperTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPaperEditText != null)
            mPaperContent = mPaperEditText.getText().toString();
        SavePaperTask savePaperTask = new SavePaperTask();
        savePaperTask.execute(mPaperContent);
    }

    class SavePaperTask extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        public SavePaperTask() {
            dialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            savePaper(params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Saving…");
            dialog.show();
        }
    }

    class ReadPaperTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        public ReadPaperTask() {
            dialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (mPaperEditText != null)
                mPaperEditText.setText(mPaperContent);
        }

        @Override
        protected Void doInBackground(Void... params) {
            readPaper();
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading…");
            dialog.show();
        }
    }

    // save the currently open paper
    @SuppressLint("CommitPrefEdits")
    private void savePaper(String paperContent) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        if (paperContent == null)
            paperContent = "";

        editor.putString(PAPER_CONTENT_KEY, paperContent);
        editor.commit();
    }

    // read the paper
    @SuppressLint("CommitPrefEdits")
    private void readPaper() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        mPaperContent = preferences.getString(PAPER_CONTENT_KEY, "");
    }

}
