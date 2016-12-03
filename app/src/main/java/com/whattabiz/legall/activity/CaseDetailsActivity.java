package com.whattabiz.legall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;

public class CaseDetailsActivity extends AppCompatActivity {
    String caseTitle;
    String caseDesc;
    String nextDate;
    String phone;
    String name;
    String prevDate;
    String caseStatus;
    int pos;
    private Toolbar toolbar;
    private TextView caseDetailsTitle;
    private TextView caseDetailsUserName;
    private TextView caseDetailsDate;
    private TextView caseDetailDesc;
    private TextView caseDetailsClientName;
    private TextView caseDetailsClientPhone;
    private TextView caseDetailsPrevDate;
    private TextView caseDetailsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Case details");


        caseDetailsTitle = (TextView) findViewById(R.id.case_details_title);
        caseDetailsClientName = (TextView) findViewById(R.id.client_name);
        caseDetailsStatus = (TextView) findViewById(R.id.case_details_status);
        caseDetailsClientPhone = (TextView) findViewById(R.id.client_phone);
        caseDetailsPrevDate = (TextView) findViewById(R.id.case_details_court_prev);
        caseDetailsDate = (TextView) findViewById(R.id.case_details_date);
        caseDetailDesc = (TextView) findViewById(R.id.case_details_description);

        caseTitle = getIntent().getStringExtra("TITLE");
        caseDesc = getIntent().getStringExtra("DESC");
        nextDate = getIntent().getStringExtra("NEXT_DATE");
        phone = getIntent().getStringExtra("PHONE");
        name = getIntent().getStringExtra("NAME");
        prevDate = getIntent().getStringExtra("PREV_DATE");
        caseStatus = getIntent().getStringExtra("CASE_STATUS");

        pos = getIntent().getIntExtra("pos", -1);
        Log.d("pos", String.valueOf(pos));
        Log.d("CASE STatus", caseStatus);

        caseDetailsTitle.setText(caseTitle);
        caseDetailDesc.setText(caseDesc);
        caseDetailsClientPhone.setText(phone);
        caseDetailsClientName.setText(name);
        caseDetailsDate.setText(nextDate);
        caseDetailsPrevDate.setText(prevDate);
        caseDetailsStatus.setText(caseStatus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String text = "Next Court Hearing: " + nextDate + "\n" + "Case Status: " + caseStatus;

        switch (item.getItemId()) {
            case R.id.share_text:
                Intent shareText = new Intent(Intent.ACTION_SEND);
                shareText.setType("text/plain");
                shareText.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(shareText, "Share the Case Details using"));
                break;
            case R.id.edit_case_details:
                CaseDetailsActivity.this.finish();
                Intent editIntent = new Intent(CaseDetailsActivity.this, EditCaseDetails.class);
                editIntent.putExtra("pos", pos);
                editIntent.putExtra("nextD", nextDate);
                editIntent.putExtra("prevD", prevDate);
                startActivity(editIntent);
                break;
            case R.id.edit_case_details_delete:
                Lawyers.getInstance().caseModelArrayList.remove(pos);
                CaseDetailsActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
