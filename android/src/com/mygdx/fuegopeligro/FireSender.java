package com.mygdx.fuegopeligro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FireSender extends AppCompatActivity {

    private ImageView fireImage;
    private EditText location;

    private Uri imageUri;
    private Uri imageSend;

    private String fireType;
    private double lattitude;
    private double longitude;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    private FirebaseFirestore db;
    private StorageReference imageDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_sender);
        startDB();

        fireImage = findViewById(R.id.fireImage);
        location = findViewById(R.id.locationField);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button gmaps = findViewById(R.id.gmapsButton);
        Button reset = findViewById(R.id.resetButton);
        Button camera = findViewById(R.id.cameraButton);
        Button submit = findViewById(R.id.sendButton);
        Button back = findViewById(R.id.sendButton2);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("senderName");
        final String number = intent.getStringExtra("senderNumber");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rB1:
                        fireType = "Residential";
                        break;
                    case R.id.rB2:
                        fireType = "School/Institution";
                        break;
                    case R.id.rB4:
                        fireType = "Commercial/Mall";
                        break;
                    case R.id.rB5:
                        fireType = "High Rise/Building";
                        break;
                    case R.id.rB6:
                        fireType = "Industrial/Factory";
                        break;
                    case R.id.rB8:
                        fireType = "Grass/Open Fields";
                        break;
                }
            }
        });

        gmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        lattitude = intent.getDoubleExtra("lattitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        location.setText(String.format("%s, %s", lattitude, longitude));

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setText("");
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStart();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(lattitude == 0.0)) {
                    if(!(longitude == 0.0)) {
                        if(!(imageSend == null)) {
                            if(!(fireType == null)) {
                                sendDataToDB(lattitude, longitude, fireType, imageSend, name, number);
                            } else {
                                Toast.makeText(FireSender.this, "Your location type is not set, " +
                                        "please select one of the Fire Location types.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(FireSender.this, "Your fire image is not set, " +
                                    "please click the Open Camera button.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(FireSender.this, "Your location longitude is not set, " +
                                "please click the Find on Google Maps button.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FireSender.this, "Your location latitude is not set, " +
                            "please click the Find on Google Maps button.", Toast.LENGTH_LONG).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            }
        });
    }

    private void cameraStart() {
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            if (checkPermissionWRITE_EXTERNAL_STORAGE(this)) {
                // Define the file-name to save photo taken by Camera activity
                String fileName = "Camera_Example.jpg";

                // Create parameters for Intent with filename
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");

                // imageUri is the current activity attribute, define and save it for later usage
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                /* EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. ****/

                // Standard Intent action that can be sent to have the camera
                // application capture an image and return it.
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                /* Load Captured Image And Data Start ****************/
                String imageId = convertImageUriToFile(imageUri, FireSender.this);

                //  Create and excecute AsyncTask to load capture image
                new LoadImagesFromSDCard().execute("" + imageId);
                /* Load Captured Image And Data End ****************/

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /************ Convert Image Uri path to physical path **************/
    public static String convertImageUriToFile (Uri imageUri, Activity activity)  {
        Cursor cursor = null;
        int imageID = 0;

        try {
            /* Which columns values want to get *******/
            @SuppressLint("InlinedApi") String [] proj = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            cursor = activity.getContentResolver().query(
                    imageUri,         //  Get data for specific image URI
                    proj,             //  Which columns to return
                    null,             //  WHERE clause; which rows to return (all rows)
                    null,             //  WHERE clause selection arguments (none)
                    null              //  Order-by clause (ascending by name)
            );

            //  Get Query Data
            int columnIndex = 0;
            if (cursor != null) {
                columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            }

            //int orientation_ColumnIndex = cursor.
            //getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
            int size = 0;
            if (cursor != null) {
                size = cursor.getCount();
            }

            /* If size is 0, there are no images on the SD Card. *****/
            if (size != 0) {
                if (cursor.moveToFirst()) {
                    /* Captured image details ************/
                    /* Used to show image on view in LoadImagesFromSDCard class ******/
                    imageID = cursor.getInt(columnIndex);
                    //String orientation =  cursor.getString(orientation_ColumnIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )
        return String.valueOf(imageID);
    }

    /**
     * Async task for loading the images from the SD card.
     *
     * @author John Daniel Esguerra
     *
     */
    // Class with extends AsyncTask class
    @SuppressLint("StaticFieldLeak")
    public class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(FireSender.this);
        Bitmap mBitmap;

        protected void onPreExecute() {
            /* NOTE: You can call UI Element here. *****/
            // Progress Dialog
            Dialog.setMessage(" Loading image from Sdcard..");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Bitmap bitmap;
            Bitmap newBitmap;

            try {
                /*  Uri.withAppendedPath Method Description
                 * Parameters
                 *    baseUri  Uri to append path segment to
                 *    pathSegment  encoded path segment to append
                 * Returns
                 *    a new Uri based on baseUri with the given segment appended to the path
                 */
                imageSend = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);

                /* Decode an input stream into a bitmap. *********/
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageSend));

                if (bitmap != null) {
                    /* Creates a new bitmap, scaled from an existing bitmap. ***********/
                    newBitmap = Bitmap.createScaledBitmap(bitmap, 170, 170, true);
                    bitmap.recycle();

                    if (newBitmap != null) {
                        mBitmap = newBitmap;
                    }
                }
            } catch (IOException e) {
                // Error fetching image, try to recover
                /* Cancel execution of this task. **********/
                cancel(true);
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
            // Close progress dialog
            Dialog.dismiss();

            if (mBitmap != null) {
                // Set Image to ImageView
                fireImage.setImageBitmap(mBitmap);
            }
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog(context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 456;
    public boolean checkPermissionWRITE_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(FireSender.this, "GET_ACCOUNTS Allowed",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FireSender.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    private void showDialog(final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage("External storage" + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    /**
     * @param lat = Latitude value
     * @param lon = Longitude value
     * @param ftype = Fire Type value
     * @param data = Image value
     */
    private void sendDataToDB(
            double lat, double lon, String ftype, final Uri data, String name, String number) {
        Calendar currentTime = Calendar.getInstance();
        Date dates = currentTime.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        int hours = currentTime.get(Calendar.HOUR_OF_DAY);
        int minutes = currentTime.get(Calendar.MINUTE);
        int seconds = currentTime.get(Calendar.SECOND);
        int milliseconds = currentTime.get(Calendar.MILLISECOND);
        String date = simpleDateFormat.format(dates);

        sha1Hashing sha1 = new sha1Hashing();
        final String fireID = "" + sha1.sha1Hashing(data.toString());

        // Firestore info
        Map<String, Object> fireInfo = new HashMap<>();
        fireInfo.put("fire_id", fireID);
        fireInfo.put("fireType", ftype);
        fireInfo.put("senderName", name);
        fireInfo.put("senderNumber", number);
        fireInfo.put("lattitude", lat);
        fireInfo.put("longitude", lon);
        fireInfo.put("date", date);
        fireInfo.put("hour", hours);
        fireInfo.put("minute", minutes);
        fireInfo.put("second", seconds);
        fireInfo.put("milliseconds", milliseconds);
        fireInfo.put("imageLink", "gs://fuegopeligro-4109e.appspot.com/images/" + fireID + ".jpg");

        db.collection("FIRES")
                .document(date + " " + hours + ":" + minutes + ":" + seconds + ":" + milliseconds)
                .set(fireInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        UploadTask uploadTask = imageDB
                                .child("images/" + fireID + ".jpg")
                                .putFile(data);

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(FireSender.this, "Failed to upload image to database, please check your internet connection.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(FireSender.this, "Data submitted, please wait for further instructions.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FireSender.this, "Failed to upload info to database, please check your internet connection.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startDB() {
        db = FirebaseFirestore.getInstance();
        imageDB = FirebaseStorage.getInstance().getReference();
    }
}
