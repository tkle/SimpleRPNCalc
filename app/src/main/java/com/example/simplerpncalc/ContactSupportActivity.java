package com.example.simplerpncalc;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ContactSupportActivity extends AppCompatActivity {
    private final String DEBUG_TAG = "ContactSupportActivity";
    private final String EMAIL_CONTACT = "tkle3838@gmail.com";
    private EditText subjectEditText;
    private EditText descriptionEditText;
    private FloatingActionButton submitFloatingActionButton;

    // Watch subject and description EditText for changes then enable/disable submit button
    private final TextWatcher inputFieldChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No-Op
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateSubmitFloatingActionButton();
        }

        @Override
        public void afterTextChanged(Editable s) {
            // No-Op
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_support_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        subjectEditText = findViewById(R.id.subjectEditText);
        subjectEditText.addTextChangedListener(inputFieldChangedListener);

        descriptionEditText = findViewById(R.id.descriptionEditTextMultiLine);
        descriptionEditText.addTextChangedListener(inputFieldChangedListener);

        submitFloatingActionButton = findViewById(R.id.submitFloatingActionButton);
        submitFloatingActionButton.setEnabled(false);  // disable submit button initially
        submitFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.submitFloatingActionButton) {
                    /*
                    Sent request through email
                     */
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_CONTACT});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                            getString(R.string.app_name) + " : " + subjectEditText.getText().toString());
                    emailIntent.putExtra(Intent.EXTRA_TEXT, descriptionEditText.getText().toString());
                    //emailIntent.setType("message/rfc822"); // email type
                    PackageManager packageManager = getPackageManager();
                    if (emailIntent.resolveActivity(packageManager) != null)
                        startActivity(Intent.createChooser(emailIntent, getString(R.string.email_support)));
                }
                finish();
            }
        });
    }

    // Enable/Disable submit button when EditText changed
    private void updateSubmitFloatingActionButton() {
        String subjectText = subjectEditText.getText().toString();
        String descriptionText = descriptionEditText.getText().toString();
        submitFloatingActionButton.setEnabled(
                subjectText.trim().length() != 0 && descriptionText.trim().length() != 0);
    }
}
