package com.whattabiz.legall.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.whattabiz.legall.FileUtils;
import com.whattabiz.legall.adapters.FileUploadAdapter;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;
import com.whattabiz.legall.models.FileModel;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;



import static android.support.v4.content.FileProvider.getUriForFile;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class DocumentScrutinyActivity extends AppCompatActivity {


    public static String EXTERNAL_DIRECTORY = "Legall/Document_Scrutiny";
    public static DocumentScrutinyActivity dsActivity;
    private static TextView filesSelected;
    public static List<FileModel> filesList = new ArrayList<>();
    private final int CAMERA_REQUEST_CODE = 1;
    private final int GALLERY_REQUEST_CODE = 2;
    private final int PDF_REQUEST_CODE = 3;
    private String UPLOAD_URL_DOC = "http://legall.co.in/web/androidFileUpload.php";
    private String UPLOAD_URL_PRO = "http://legall.co.in/web/processOutSourcing.php";
    private String UPLOAD_URL;
    Boolean exceptionFlag = false;
    Bitmap img;
    private TextView folderName, folderDesc, add, upload;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Uri fileUri;
    private String userID, lawyerID, fileName, filePath;

    private MultipartUploadRequest multipartUploadRequest;
    public static File mediaFile;

    private static File getOutputMedia() {
        //External sdCard Location
        File mediaDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), EXTERNAL_DIRECTORY);
        //Create the storage if doesnt exists
        if (!mediaDir.exists()) {
            if (!mediaDir.mkdirs()) {
                Log.d("CAMERA_DIR", "FAILED");
                return null;
            }
        }

        //Creting a mediaFileName
        String TIME = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        mediaFile = new File(mediaDir.getPath() + File.separator + "DOC_SC_IMG_" + TIME + ".jpg");

        return mediaFile;
    }

    public static DocumentScrutinyActivity getInstance() {
        dsActivity = new DocumentScrutinyActivity();

        return dsActivity;
    }

    public int whichActivity() {
        Intent intent = getIntent();
        return intent.getIntExtra("WHICH_ACTIVITY", 2);
    }

    public static void updateFilesSelected() {
        filesSelected.setText(filesList.size() + " files selected");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_scrutiny);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //(whichActivity() == 0)?(getSupportActionBar().setTitle("Document Scrutiny")):(getSupportActionBar().setTitle("Process Outsourcing"));
        switch (whichActivity()) {
            case 0:
                getSupportActionBar().setTitle("Document Scrutiny");
                break;
            case 1:
                getSupportActionBar().setTitle("Process Outsourcing");
                break;
        }
        Log.d("WHICH_ACTIVITY_CODE", " " + whichActivity());

        folderName = (TextView) findViewById(R.id.folder_name);
        folderDesc = (TextView) findViewById(R.id.folder_desc);
        filesSelected = (TextView) findViewById(R.id.files_selected);
        add = (TextView) findViewById(R.id.add_btn);
        upload = (TextView) findViewById(R.id.upload_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        checkForInternet();
        preInitialiser();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFile(v);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              uploadFile();


            }
        });

    }

    public boolean checkForInternet() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                //connected
                return true;
            }
        } else return false;
        return false;
    }

    public void preInitialiser() {


        filesList.clear();

        //initialise userID or lawyerID
        int which = whichActivity();
        switch (which) {
            case 0:
                userID = User.Id;
                return;
            case 1:
                lawyerID = User.lawyerId;
                return;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //initialise recycler view
        adapter = new FileUploadAdapter(filesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        refreshFilesList();
    }

    public void addFile(View view) {
        fileName = "";
        fileUri = null;
        filePath = "";
        exceptionFlag = false;
        mediaFile = null;
        CharSequence[] chooser = new CharSequence[]{"Camera", "Gallery", "PDF"};
        //To select the option CAMERA GALLERY or PDF
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Choose from...")
                .setItems(chooser, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //when the option is selected
                        switch (which) {
                            case 0: //CAMERA is selected
                                if (filesList.size() < 5) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    fileUri = getOutputMediaUri();
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                    startActivityForResult(intent, CAMERA_REQUEST_CODE);

                                } else {
                                    Toast.makeText(getApplicationContext(), "Max 5 Files, Please upload a scanned PDF instead...", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 1: //GALLERY is selected
//                                if (Build.VERSION.SDK_INT >= 23) {
//                                    requestPermission();
//                                }
                                if (filesList.size() < 5) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_GET_CONTENT)
                                            .setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent, "Select Picker..."), GALLERY_REQUEST_CODE);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Max 5 Files, Please upload a scanned PDF instead...", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 2: //PDF is selected

//                                if (Build.VERSION.SDK_INT >= 23) {
//                                    requestPermission();
//                                }
                                if (filesList.size() < 5) {
                                    openLibExplorer();
//                                    try {
//                                        Intent intent = new Intent();
//                                        intent.setAction(Intent.ACTION_GET_CONTENT)
//                                                .setType("application/pdf");
//                                        startActivityForResult(intent, PDF_REQUEST_CODE);
//                                    } catch (ActivityNotFoundException e) {
//                                        e.printStackTrace();
//                                        openLibExplorer(); //used in case if there is no app to open pdf file
//                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Max 5 Files, Please upload a scanned PDF instead...", Toast.LENGTH_LONG).show();
                                }
                                break;
                        }
                    }
                });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: //from CAMERA
                if (resultCode == RESULT_OK) {
                    Log.d("STATUS ", "RESULT OK");
                    try {
                        Log.d("DOC SCRU", " " + fileUri);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            fileUri = data.getData();
                            filePath = getRealPathFromUri(fileUri);
                        } else {
                            fileUri = FileUtils.getUri(mediaFile);
                            filePath = FileUtils.getPath(this, fileUri);
                        }
                        fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                        //log
                        Log.d("DOCUMENT SCRUTINY", "URI: " + fileUri + "PATH: " + filePath + "NAME: " + fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //add these details to the arraylist
                    if (filePath != null) {
                        filesList.add(new FileModel(fileName, filePath, null));
                        refreshFilesList(); //refresh the arrayList
                    } else
                        Toast.makeText(DocumentScrutinyActivity.this, "Some error Occured", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Some Error Occured! Please, try again", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2: //from GALLERY
                if (resultCode == RESULT_OK && data.getData() != null) {
                    try {
                        fileUri = data.getData();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            filePath = getRealPathFromUri(fileUri);
                        } else {
                            filePath = FileUtils.getPath(this, fileUri);
                            Log.d("PATH", filePath);
                        }
//                        getRealPathFromUri(fileUri);
                        fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

                        //log
                        Log.d("DOCUMENT SCRUTINY", "URI: " + fileUri + "PATH: " + filePath + "NAME: " + fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //add these details to the arraylist
                    if (filePath != null) {
                        filesList.add(new FileModel(fileName, filePath, null));
                        refreshFilesList(); //refresh the arrayList
                    } else
                        Toast.makeText(DocumentScrutinyActivity.this, "Some error Occured", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Some Error Occured! Please, try again", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3: //fromPDF
                if (resultCode == RESULT_OK) {
                    try {
//                        fileUri = data.getData();
//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//                            filePath = getRealPathFromUri(fileUri);
//                        } else {
//                            filePath = FileUtils.getPath(this, fileUri);
//                            Log.d("PATH", filePath);
//                        }

                        filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

                        fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

                        //log
                        Log.d("DOCUMENT SCRUTINY", "URI: " + fileUri + "PATH: " + filePath + "NAME: " + fileName);
                    } catch (Exception e) {
                        Toast.makeText(this, "Some Error Occured! Please try again.", Toast.LENGTH_SHORT).show();
                        exceptionFlag = true;
                        e.printStackTrace();
                    }
                    //add these details to the arraylist
                    if (!exceptionFlag) {
                        filesList.add(new FileModel(fileName, filePath, null));
                        refreshFilesList(); //refresh the arrayList

                    } else
                        Toast.makeText(DocumentScrutinyActivity.this, "Some error Occured", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Some Error Occured! Please, try again", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String getRealPathFromUri(Uri contentURI) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cur = getContentResolver().query(contentURI, projection, null, null, null);
        if (cur == null) {
            return contentURI.getPath();
        } else {
            cur.moveToFirst();
            Log.d("GETREALPATH", " " + DatabaseUtils.dumpCursorToString(cur));
            int index = cur.getColumnIndex(projection[0]);
            String path = cur.getString(index);
            cur.close();

            Log.d("OBT_PATH", path + " ");
            return path;
        }
    }

    public void uploadFile() {
        checkForInternet();
        if (validate()) {
            Log.d("IS VALID", "YES");
            for (int i = 0; i < filesList.size(); i++) {
                String uploadID = UUID.randomUUID().toString();
                filesList.get(i).setUploadID(uploadID);
                try {
                    switch (whichActivity()) {
                        case 0:
                            UPLOAD_URL = UPLOAD_URL_DOC;
                            break;
                        case 1:
                            UPLOAD_URL = UPLOAD_URL_PRO;
                            break;
                    }

                    Log.d("UPLOAD URL", UPLOAD_URL);
                    multipartUploadRequest = new MultipartUploadRequest(getApplicationContext(), uploadID, UPLOAD_URL)
                            .addFileToUpload(filesList.get(i).getPath(), "file" + (i + 1))
                            .setMethod("POST")

                            .addParameter("Connection", "Keep-Alive")
                            .addParameter("ENCTYPE", "multipart/form-data")

                            //user defined parameters
                            .addParameter("fileNumber", "" + (i + 1))
                            .addParameter("name", filesList.get(i).getFilename())
                            .addParameter("user_id", User.Id)
                            .addParameter("filePath", filesList.get(i).getPath())
                            .addParameter("folderName", folderName.getText().toString())
                            .addParameter("desc", folderDesc.getText().toString())

                            .setMaxRetries(1)
                            .setNotificationConfig(new UploadNotificationConfig()
                                    .setTitle(filesList.get(i).getFilename())
                                    .setIcon(R.drawable.uploading)
                                    .setErrorIcon(R.drawable.failed)
                                    .setCompletedIcon(R.drawable.uploaded)
                                    .setErrorMessage("Failed to upload " + filesList.get(i).getFilename()));
                    if (checkForInternet()) {
                        multipartUploadRequest.startUpload();
                        Toast.makeText(this, "File upload in Progress...\nPlease check the notification.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(this, "NO INTERNET CONNECTION!!!", Toast.LENGTH_SHORT).show();
                } catch (MalformedURLException | FileNotFoundException e) {
                    e.printStackTrace();
                }
                UploadServiceBroadcastReceiver receiver = new UploadServiceBroadcastReceiver() {
                    @Override
                    public void onProgress(UploadInfo uploadInfo) {
                        super.onProgress(uploadInfo);
                    }

                    @Override
                    public void onError(UploadInfo uploadInfo, Exception exception) {
                        super.onError(uploadInfo, exception);
                        Log.d("UPLOAD STATUS", "ERROR  " + uploadInfo);
                        exception.printStackTrace();

                        String upload_id = uploadInfo.getUploadId();
                        for (int i = 0; i < filesList.size(); i++) {
                            if (upload_id.equals(filesList.get(i).getUploadID())) {
                                Toast.makeText(getApplicationContext(), "Failed to upload file:\n" + filesList.get(i).getFilename() + "\nUpload again manually", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                };
                receiver.register(this);
            }
        }
    }

    public void refreshFilesList() {
        adapter.notifyDataSetChanged();
        filesSelected.setText("" + filesList.size() + " files selected.");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void openLibExplorer() {


        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(3)
                .withFilter(Pattern.compile(".*\\.pdf$"))
                .withFilterDirectories(false)
                .withHiddenFiles(true)
                .start();


//        try{
//            FileChooserDialog.Builder builder = new FileChooserDialog.Builder(FileChooserDialog.ChooserType.FILE_CHOOSER, new FileChooserDialog.ChooserListener() {
//                @Override
//                public void onSelect(String path) {
//                    String fileName = path.substring(path.lastIndexOf("/") + 1);
//                    filesList.add(new FileModel(fileName, path, null));
//                    refreshFilesList();
//                }
//            })
//                    .setTitle("Select one PDF file")
//                    .setFileFormats(new String[]{".pdf",".doc",".docx"});
//
//            builder.build().show(getSupportFragmentManager(), null);
//        } catch (ExternalStorageNotAvailableException | NullPointerException e) {
//            e.printStackTrace();
//            Log.d("Legall", "FILE EXPLORER ERROR");
//        }
    }

    public Boolean validate() {
        if (filesList.size() <= 0) {
            Toast.makeText(this, "Select Atleast one File", Toast.LENGTH_LONG).show();
        } else if (folderName.getText().length() < 5) {
            folderName.setError("Invalid Title! Atleast 5 characters");
            Toast.makeText(this, "Title or Caption Required ", Toast.LENGTH_LONG);
        } else if (filesList.size() > 5) {
            Toast.makeText(this, "5 Files maximum, try uploading PDF", Toast.LENGTH_LONG);
        } else {

            AlertDialog.Builder dialog = new AlertDialog.Builder(DocumentScrutinyActivity.this);
            dialog.setTitle("FILE UPLOAD")
                    .setMessage("Your files are ready to upload. Please cross check the data provided. We'll contact you soon once the files are being uploaded.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setNegativeButton("BACK",null)
                    .show();

            return true;
        }
        return false;
    }

    public Uri getOutputMediaUri() {
        return FileProvider.getUriForFile(this, this.getPackageName() + ".provider", getOutputMedia());
    }

}
