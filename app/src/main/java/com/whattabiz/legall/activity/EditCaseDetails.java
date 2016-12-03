package com.whattabiz.legall.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.whattabiz.legall.GsonConverter;
import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;
import com.whattabiz.legall.databinding.ActivityEditCaseDetailsBinding;
import com.whattabiz.legall.models.CaseModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class EditCaseDetails extends AppCompatActivity {

    // fuck the memory leak
    public static ActivityEditCaseDetailsBinding editCaseDetailsBinding;
    private static String caseDatePrev = "";
    private static String caseDateNext = "";
    private final String SHARED_PREF_KEY_CASE = "CASE DETAILS";
    private String caseTitle;
    private String caseDesc;
    private String caseClientName;
    private String caseClientPhone;
    private String caseStatus;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // using DataBinding here
        editCaseDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_case_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Edit Case Detials");

        caseDateNext = getIntent().getStringExtra("nextD");
        caseDatePrev = getIntent().getStringExtra("prevD");
        final int pos = getIntent().getIntExtra("pos", -1);
        if (pos != -1) {
            // just in case
            editCaseDetailsBinding.caseClientName.setText(Lawyers.getInstance().caseModelArrayList.get(pos).getClientNAme());
            editCaseDetailsBinding.caseClientPhone.setText(Lawyers.getInstance().caseModelArrayList.get(pos).getClientPhone());
            editCaseDetailsBinding.caseDateNext.setText(Lawyers.getInstance().caseModelArrayList.get(pos).getNextHearing());
            editCaseDetailsBinding.caseDatePrev.setText(caseDatePrev);
            editCaseDetailsBinding.caseDesc.setText(caseDateNext);
            editCaseDetailsBinding.caseTitle.setText(Lawyers.getInstance().caseModelArrayList.get(pos).getCaseTitle());
            editCaseDetailsBinding.caseStatus.setText(Lawyers.getInstance().caseModelArrayList.get(pos).getCaseStatus());
        }

        editCaseDetailsBinding.nextEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerNextFragment();
                dialogFragment.show(getSupportFragmentManager(), "dateNext");

            }
        });

        editCaseDetailsBinding.prevEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerPrevFragment();
                dialogFragment.show(getSupportFragmentManager(), "datePrev");
            }
        });

        editCaseDetailsBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caseTitle = editCaseDetailsBinding.caseTitle.getText().toString();
                caseDesc = editCaseDetailsBinding.caseDesc.getText().toString();

                caseClientName = editCaseDetailsBinding.caseClientName.getText().toString();
                caseClientPhone = editCaseDetailsBinding.caseClientPhone.getText().toString();
                caseStatus = editCaseDetailsBinding.caseStatus.getText().toString();

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

                    // change the Case model from ArrayList
                    Lawyers.getInstance().caseModelArrayList.set(pos, caseModel);
                    // save the Case model into shared prefs
                    saveToSharedPrefs();

                    LawyerDiaryActivity.sendJsonToServer(getApplicationContext());

                    Toast.makeText(EditCaseDetails.this, "Case Details edited Successfully!", Toast.LENGTH_SHORT).show();
                    supportFinishAfterTransition();
                }
            }
        });
    }

    private boolean validate() {
        boolean isCaseTitleValid = false;
        boolean isCaseDescValid = false;
        boolean isCaseClientNameValid = false;
        boolean isCaseClientPhoneValid = false;


        if (!caseTitle.isEmpty() || !caseTitle.contains(Pattern.compile("[0-9]+").toString())) {
            isCaseTitleValid = true;
        } else {
            editCaseDetailsBinding.caseTitle.setError("Invalid Case Title!");
        }
        if (!caseDesc.isEmpty() || !caseDesc.contains(Pattern.compile("[0-9]+").toString())) {
            isCaseDescValid = true;
        } else {
            editCaseDetailsBinding.caseDesc.setError("Invalid Case Description!");
        }
        if (!caseClientPhone.isEmpty() || Patterns.PHONE.matcher(caseClientPhone).matches()) {
            isCaseClientPhoneValid = true;
        } else {
            editCaseDetailsBinding.caseClientPhone.setError("Invalid Phone!");
        }
        if (!caseClientName.isEmpty() || !caseClientName.contains(Pattern.compile("[0-9]").toString())) {
            isCaseClientNameValid = true;
        } else {
            editCaseDetailsBinding.caseClientName.setError("Invalid Name!");
        }
        return isCaseTitleValid && isCaseDescValid && isCaseClientPhoneValid && isCaseClientNameValid;
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

    public static class DatePickerNextFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            caseDateNext = String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
            editCaseDetailsBinding.caseDateNext.setText(caseDateNext);
        }
    }

    public static class DatePickerPrevFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            caseDatePrev = String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
            editCaseDetailsBinding.caseDatePrev.setText(caseDatePrev);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
