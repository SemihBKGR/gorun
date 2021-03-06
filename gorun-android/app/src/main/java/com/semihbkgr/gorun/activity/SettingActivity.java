package com.semihbkgr.gorun.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.semihbkgr.gorun.R;
import com.semihbkgr.gorun.util.ActivityUtils;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = SettingActivity.class.getName();

    private Button deleteSubjectsButton;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActivityUtils.loadActionBar(this, "Setting");

        deleteSubjectsButton = findViewById(R.id.buttonDeleteSubjects);

        deleteSubjectsButton.setOnClickListener(this::onButtonClearSubjectClicked);

    }


    private void onButtonClearSubjectClicked(View v) {
        Log.i(TAG, "onButtonClearSubjectClicked");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}