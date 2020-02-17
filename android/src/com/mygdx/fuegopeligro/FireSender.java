package com.mygdx.fuegopeligro;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class FireSender extends AppCompatActivity {

    private ImageView fireImage;
    private EditText location;

    private Uri imageUri;
    private Uri imageSend;

    private static String fireType;
    private double lattitude;
    private double longitude;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_sender);

        fireImage = findViewById(R.id.fireImage);
        location = findViewById(R.id.locationField);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
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

        Button gmaps = findViewById(R.id.gmapsButton);
        gmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        lattitude = intent.getDoubleExtra("lattitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        location.setText(String.format("%s, %s", lattitude, longitude));

        Button reset = findViewById(R.id.resetButton);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setText("");
            }
        });

        Button camera = findViewById(R.id.cameraButton);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStart();
            }
        });

        Button submit = findViewById(R.id.sendButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(lattitude == 0.0)) {
                    if(!(longitude == 0.0)) {
                        if(!(fireType == null)) {
                            if(!(imageSend == null)) {
                                sendEmail(lattitude, longitude, fireType, imageSend);
                            } else {
                                Toast.makeText(FireSender.this, "Your fire image is not set, " +
                                        "please click the Open Camera button.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(FireSender.this, "Your location type is not set, " +
                                    "please select one of the Fire Location types.", Toast.LENGTH_LONG).show();
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

        Button mms = findViewById(R.id.mmsButton);
        mms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(lattitude == 0.0)) {
                    if (!(longitude == 0.0)) {
                        if (!(fireType == null)) {
                            if(!(imageSend == null)) {
                                sendMMS(lattitude, longitude, fireType, imageSend);
                            } else {
                                Toast.makeText(FireSender.this, "Your fire image is not set, " +
                                        "please click the Open Camera button.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(FireSender.this, "Your location type is not set, " +
                                    "please select one of the Fire Location types.", Toast.LENGTH_LONG).show();
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

        Button back = findViewById(R.id.sendButton2);
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
        if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if ( resultCode == RESULT_OK) {

                /* Load Captured Image And Data Start ****************/
                String imageId = convertImageUriToFile(imageUri, FireSender.this);

                //  Create and excecute AsyncTask to load capture image
                new LoadImagesFromSDCard().execute("" + imageId);
                /* Load Captured Image And Data End ****************/

            } else if ( resultCode == RESULT_CANCELED) {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /************ Convert Image Uri path to physical path **************/
    public static String convertImageUriToFile (Uri imageUri, Activity activity )  {
        Cursor cursor = null;
        int imageID = 0;

        try {
            /* Which columns values want to get *******/
            String [] proj={
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            cursor = activity.managedQuery(
                    imageUri,         //  Get data for specific image URI
                    proj,             //  Which columns to return
                    null,             //  WHERE clause; which rows to return (all rows)
                    null,             //  WHERE clause selection arguments (none)
                    null              //  Order-by clause (ascending by name)

            );

            //  Get Query Data
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            //int orientation_ColumnIndex = cursor.
            //getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
            int size = cursor.getCount();

            /* If size is 0, there are no images on the SD Card. *****/
            if (size != 0) {
                int thumbID;
                if (cursor.moveToFirst()) {
                    /* Captured image details ************/
                    /* Used to show image on view in LoadImagesFromSDCard class ******/
                    imageID     = cursor.getInt(columnIndex);
                    thumbID     = cursor.getInt(columnIndexThumb);
                    String Path = cursor.getString(file_ColumnIndex);

                    //String orientation =  cursor.getString(orientation_ColumnIndex);
                    String capturedImageDetails = " CapturedImageDetails : \n\n"
                            + " ImageID :" + imageID + "\n"
                            + " ThumbID :" + thumbID + "\n"
                            + " Path :" + Path + "\n";
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )
        return "" + imageID;
    }

    /**
     * Async task for loading the images from the SD card.
     *
     * @author John Daniel Esguerra
     *
     */
    // Class with extends AsyncTask class
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
                    showDialog("External storage", context,
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
                    showDialog("External storage", context,
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
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
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

    private void showDialog(final String msg, final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
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

    public void sendEmail(double lat, double lon, String ftype, Uri data, String... urls) {
        Date currentTime = Calendar.getInstance().getTime();
        int hours = currentTime.getHours();
        int minutes = currentTime.getMinutes();
        int seconds = currentTime.getSeconds();
        int day = currentTime.getDay();
        int month = currentTime.getMonth();
        int year = currentTime.getYear();

        try {
            String email = "fuegopeligromail@gmail.com";
            String subject =
                    "(ALERT) Fire Incident at: " + currentTime + " " + month + "/" + day + "/" + year +
                    ", on: " + lat + " N ," + lon + " E";
            String message =
                    "ALERT: Fire Incident" +
                    "\nLocation type is a " + ftype +
                    "\nTime sent is at " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds" +
                    "\nLocated at " + lat + " N ," + lon + " E" +
                    "\nGoogle Maps: https://www.google.com/maps/@" + lat + "," + lon + ",17z" +
                    "\nFire image is included as an attachment below.";
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setPackage("com.google.android.gm");
            emailIntent.setDataAndType(Uri.parse(email), "image/jpeg");
            /*emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { email });*/
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            emailIntent.putExtra(Intent.EXTRA_STREAM, data);
            startActivity(emailIntent);
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void sendMMS(double lat, double lon, String ftype, Uri data, String... urls) {
        Date currentTime = Calendar.getInstance().getTime();
        int hours = currentTime.getHours();
        int minutes = currentTime.getMinutes();
        int seconds = currentTime.getSeconds();
        int day = currentTime.getDay();
        int month = currentTime.getMonth();
        int year = currentTime.getYear();

        try {
            String number = "09564735728";
            String message = "ALERT: Fire Incident" +
                            "\nLocation type: " + ftype +
                            "\nTime sent: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds" +
                            "\nDate sent: " + month + "/" + day + "/" + year +
                            "\nLocated at " + lat + " N ," + lon + " E" +
                            "\nGoogle Maps: https://www.google.com/maps/@" + lat + "," + lon + ",17z" +
                            "\nFire image is included as an attachment below.";
            Intent mmsIntent = new Intent(android.content.Intent.ACTION_SEND, data);
            mmsIntent.setDataAndType(Uri.parse("smsto:" + number), "image/*");
            mmsIntent.putExtra("address", new String[] { number });
            mmsIntent.putExtra("sms_body", message);
            mmsIntent.putExtra(Intent.EXTRA_STREAM, data);
            startActivity(mmsIntent);
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
