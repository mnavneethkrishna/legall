package com.whattabiz.legall.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.whattabiz.legall.GsonConverter;
import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;
import com.whattabiz.legall.models.CaseModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class AddCaseActivity extends AppCompatActivity {

    public static EditText caseDatePrevEdit, caseDateNextEdit;
    private static String caseDatePrev = "";
    private static String caseDateNext = "";
    // SHARED PREF KEY
    private final String SHARED_PREF_KEY_CASE = "CASE DETAILS";
    private Toolbar toolbar;
    private String caseTitle;
    private String caseDesc;
    private String caseClientName;
    private String caseClientPhone;
    private String caseStatus;
    public EditText
            caseTitleEdit, caseDescEdit, caseClientNameEdit,
            caseClientPhoneEdit, caseStatusEdit;

    private FloatingActionButton fab;
    private ImageView prevEdit, nextEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_case);
        this.setTitle("Add Case Details");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        caseTitleEdit = (EditText) findViewById(R.id.case_title);
        caseDescEdit = (EditText) findViewById(R.id.case_desc);
        caseClientNameEdit = (EditText) findViewById(R.id.case_client_name);
        caseClientPhoneEdit = (EditText) findViewById(R.id.case_client_phone);
        caseStatusEdit = (EditText) findViewById(R.id.case_status);

        caseDatePrevEdit = (EditText) findViewById(R.id.case_date_prev);
        caseDateNextEdit = (EditText) findViewById(R.id.case_date_next);

        prevEdit = (ImageView) findViewById(R.id.prev_edit);
        nextEdit = (ImageView) findViewById(R.id.next_edit);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        nextEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment dialogFragment = new DatePickerNextFragment();
                dialogFragment.show(getSupportFragmentManager(), "dateNext");

            }
        });

        prevEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerPrevFragment();
                dialogFragment.show(getSupportFragmentManager(), "datePrev");
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caseTitle = caseTitleEdit.getText().toString();
                caseDesc = caseDescEdit.getText().toString();
                // caseDatePrev = caseDatePrevEdit.getText().toString();
                caseClientName = caseClientNameEdit.getText().toString();
                caseClientPhone = caseClientPhoneEdit.getText().toString();
                caseStatus = caseStatusEdit.getText().toString();
                //caseDateNext = caseDateNextEdit.getText().toString();
                // todo: validate these things

                if (validate()) {

                    // create a CaseModel and Save that to Lawyers.getInstance().caseArrayList
                    CaseModel caseModel = new CaseModel();
                    caseModel.setCaseTitle(caseTitle);
                    caseModel.setCaseDetails(caseDesc);
                    caseModel.setClientPhone(caseClientPhone);
                    caseModel.setClientNAme(caseClientName);
                    caseModel.setPrevHearing(caseDatePrev);
                    caseModel.setNextHearing(caseDateNext);
                    caseModel.setCaseStatus(caseStatus);


                    Calendar c = Calendar.getInstance();
                    Date time = c.getTime();
                    SimpleDateFormat s = new SimpleDateFormat("h:mm:a", Locale.getDefault());

                    caseModel.setTime(s.format(time));

                    caseModel.setDay(new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(new Date()));

                    // add the Case model into ArrayList
                    Lawyers.getInstance().caseModelArrayList.add(caseModel);
                    // save the Case model into shared prefs
                    saveToSharedPrefs();
                    Log.i("SEND SERVER", "Sending the JSON to server");
                    LawyerDiaryActivity.sendJsonToServer(getApplicationContext());

                    Toast.makeText(AddCaseActivity.this, "Case Details has been added to the Library!", Toast.LENGTH_SHORT).show();
                    supportFinishAfterTransition();
                }
            }
        });
    }

    private void saveToSharedPrefs() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(SHARED_PREF_KEY_CASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // get the JSON STRING of Case Models
        String jsonString = GsonConverter.fromPOJOtoJson(Lawyers.getInstance().caseModelArrayList);
        editor.putString("CASE_JSON", jsonString);
        editor.apply();

        Log.d("JSON to sharedpref", jsonString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        caseDateNextEdit.clearComposingText();
        caseDatePrevEdit.clearComposingText();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    private boolean validate() {
        boolean isCaseTitleValid = false;
        boolean isCaseDescValid = false;
        boolean isCaseClientNameValid = false;
        boolean isCaseClientPhoneValid = false;


        if (!caseTitle.isEmpty() || !caseTitle.contains(Pattern.compile("[0-9]+").toString())) {
            isCaseTitleValid = true;
        } else {
            caseTitleEdit.setError("Invalid Case Title!");
        }
        if (!caseDesc.isEmpty() || !caseDesc.contains(Pattern.compile("[0-9]+").toString())) {
            isCaseDescValid = true;
        } else {
            caseDescEdit.setError("Invalid Case Description!");
        }
        if (!caseClientPhone.isEmpty() || Patterns.PHONE.matcher(caseClientPhone).matches()) {
            isCaseClientPhoneValid = true;
        } else {
            caseClientPhoneEdit.setError("Invalid Phone!");
        }
        if (!caseClientName.isEmpty() || !caseClientName.contains(Pattern.compile("[0-9]").toString())) {
            isCaseClientNameValid = true;
        } else {
            caseClientNameEdit.setError("Invalid Name!");
        }
        return isCaseTitleValid && isCaseDescValid && isCaseClientPhoneValid && isCaseClientNameValid;
    }

    public static class DatePickerNextFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            AddCaseActivity.caseDateNext = String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
            AddCaseActivity.caseDateNextEdit.setText(caseDateNext);
            Log.d("DAte prev", AddCaseActivity.caseDateNext);
        }
    }

    public static class DatePickerPrevFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            AddCaseActivity.caseDatePrev = String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
            Log.d("DAte prev", AddCaseActivity.caseDatePrev);
            AddCaseActivity.caseDatePrevEdit.setText(caseDatePrev);
        }


    }
}
