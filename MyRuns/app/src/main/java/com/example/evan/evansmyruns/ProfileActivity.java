package com.example.evan.evansmyruns;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends Activity {

    public final static int INDICATES_PHOTO_IS_FROM_CAMERA = 0;

    // This URI is created when the user clicks the "change [picture]" button.
    //      It's given to the cropping app, which puts the cropped image in "croppedUnapprovedUri"
    private Uri initialPhotoUri;

    // this is the URI given as a "destination" to the crop function -
    // but once a crop has been accepted, it's moved to "Accepted Photo URI"
    // (approval is when the user accepts the crop of the image, but before saving as a new picture)
    private Uri croppedUnapprovedUri;

    // this URI contains a photo that have COMPLETED the crop process successfully:
    //    the user has clicked "ok" in the cropping app, but hasn't saved the picture yet.
    //    The picture stays in here until the activity is closed (either with Save or Cancel).
    private Uri acceptedPhotoUri;

    // This code allows the photo URIs to stick around when the app is reloaded for
    //   some reason (e.g. when the phone is rotated or another app is opened)
    //   (code in onCreate reloads them)
    private static final String TEMP_URI_KEY = "temp_uri";
    private static final String CROPPED_URI_KEY = "cropped_uri";
    private static final String ACCEPTED_URI_KEY = "accepted_uri";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background
        outState.putParcelable(TEMP_URI_KEY, initialPhotoUri);
        outState.putParcelable(CROPPED_URI_KEY, croppedUnapprovedUri);
        outState.putParcelable(ACCEPTED_URI_KEY, acceptedPhotoUri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // if the app was running before, reload the temporaryPhotoUri:
        if (savedInstanceState != null) {
            initialPhotoUri = savedInstanceState.getParcelable(TEMP_URI_KEY);
            croppedUnapprovedUri = savedInstanceState.getParcelable(CROPPED_URI_KEY);
            acceptedPhotoUri = savedInstanceState.getParcelable(ACCEPTED_URI_KEY);
        }

        // loads the non-image profile information (name, etc.):
        loadSavedProfileData();

        // Loading the temporary file...
        ImageView profileImageView = (ImageView) findViewById(R.id.profilePhoto);

        if (acceptedPhotoUri != null){      // checks if the temporaryPhotoUri has a photo in it:
            Log.d("image_loading", "the uri isn't null - loading an image from the uri");
            profileImageView.setImageURI(acceptedPhotoUri);
        }
        else {
            Log.d("image_loading", "the uri is null - no temporary picture to load");
            try {
                Bitmap profileImage = loadImage(getString(R.string.profile_photo_file_name));
                profileImageView.setImageBitmap(profileImage);
                Log.d("image_loading", "loaded from saved profile picture");
            } catch (Exception e) {
                Log.d("image_loading", "looks like there isn't a saved profile picture, or a Uri, so loading the default from Resources");
                profileImageView.setImageResource(R.drawable.trump_exp1_cropped);
            }
        }
    }

    // sets up the "action bar" (the thing at the top with the app name)
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_activity_profile, menu);
        return true;
    }

    // a helper function for loading photos from the iPhone's storage
    //      throws an exception if the photo can't be found
    public Bitmap loadImage(String photo_file_name) throws Exception{
        try {
            FileInputStream fis = openFileInput(photo_file_name);
            Bitmap theImage = BitmapFactory.decodeStream(fis);
            fis.close();
            return theImage;
        }
        catch (IOException e){
            throw new Exception();
        }
    }

    // called when the user clicks the "change" button next to the profile picture
    public void changePhoto(View view){

        displayDialog(MyRunsDialogFragment.DIALOG_ID_PHOTO_PICKER);

    }

    public void openCamera(){
        // this Intent will bring up the camera app for taking a photo:
        Intent getPhotoIntent;
        getPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // creates a temporary file to store the new photo:
        File tempFile;
        String fileName;
        fileName = "temp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        tempFile = new File(Environment.getExternalStorageDirectory(), fileName);

        // sets the Uniform Resource Identifier for referring to that file:
        initialPhotoUri = Uri.fromFile(tempFile);

        // puts the identifier into the Intent so the camera app knows where to put the photo:
        getPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, initialPhotoUri);

        // ...
        getPhotoIntent.putExtra("return-data", true);

        // executes, with an indicator so onActivityResult can tell the result is a new photo
        // coming from the camera app (rather than a cropped photo from cropping app):
        startActivityForResult(getPhotoIntent, INDICATES_PHOTO_IS_FROM_CAMERA);
    }

    // modified from: http://codetheory.in/android-pick-select-image-from-gallery-with-intents/
    public static final int PICK_IMAGE_REQUEST = 69;
    public void chooseFromGallery(){

        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void displayDialog(int id){
        DialogFragment fragment = MyRunsDialogFragment.newInstance(id);
        fragment.show(getFragmentManager(),
                "display string B");
    }

    // This function handles returning activities that were launched "for result".
    // It has to use the integer enums to figure out if the returning activity was from
    //  the initial picture-taking or from a later cropping activity.
    public void onActivityResult(int requestCode, int resultCode, Intent completedActionIntent){

        Log.d("test", "some activity returned");

        // figures out what the returning Intent was doing:
        switch (requestCode){

            case INDICATES_PHOTO_IS_FROM_CAMERA:
                Log.d("test", "camera activity returned");

                if (resultCode == RESULT_OK){

                    Log.d("test", "the user DID take a picture with the camera");

                    // creates a new temporary file to hold the cropped image:
                    String fileName = "crop_temp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    File tempFile = new File(Environment.getExternalStorageDirectory(), fileName);

                    // initializes the URI to hold the temporary file:
                    croppedUnapprovedUri = Uri.fromFile(tempFile);
                    Crop.of(initialPhotoUri, croppedUnapprovedUri).asSquare().start(this);

                }
                else {

                    // if the user didn't accept the picture (or there was an error), delete
                    // the initialUri's file:
                    if (initialPhotoUri != null) {
                        File f = new File(initialPhotoUri.getPath());
                        if (f.exists()) {
                            f.delete();
                        }
                        initialPhotoUri = null;
                    }
                }
                break;

            // this means the photo was chosen from the gallery:
            case PICK_IMAGE_REQUEST:

                if (resultCode == RESULT_OK){
                    initialPhotoUri = completedActionIntent.getData();
                    try {

                        // creates a new temporary file to hold the cropped image:
                        String fileName = "crop_temp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                        File tempFile = new File(Environment.getExternalStorageDirectory(), fileName);

                        // initializes the URI to hold the temporary file:
                        croppedUnapprovedUri = Uri.fromFile(tempFile);
                        Crop.of(initialPhotoUri, croppedUnapprovedUri).asSquare().start(this);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }


                break;

            case Crop.REQUEST_CROP:

                Log.d("test", "crop activity returned");

                if (resultCode == RESULT_OK){
                    Log.d("test", "crop DID complete");

                    // now the file can be transferred to the final temporary-image URI:
                    //      (it's hard to copy URIs or get a bitmap from a URI, so I'll feed it through the image view:
                    ImageView profilePictureDisplayer = (ImageView) findViewById(R.id.profilePhoto);
                    profilePictureDisplayer.setImageURI(croppedUnapprovedUri);

                    File imgFile = new File(croppedUnapprovedUri.getPath());
                    acceptedPhotoUri = Uri.fromFile(imgFile);
                    croppedUnapprovedUri = null;

                    // note: temporary files are cleared after the if/else statement
                }
                else {
                    Log.d("test", "IMPORTANT - crop did not complete");

                    // note: temporary files are cleared after the if/else statement
                }

                // clear the other two URIs:
                if (initialPhotoUri != null){
                    File f = new File(initialPhotoUri.getPath());
                    if (f.exists())
                        f.delete();
                    initialPhotoUri = null;
                }
                if (croppedUnapprovedUri != null){
                    File f = new File(croppedUnapprovedUri.getPath());
                    if (f.exists())
                        f.delete();
                    croppedUnapprovedUri = null;
                }

                break;
        }
    }

    private static final String SAVED_DATA_PACKAGE_ID = "saved_data_ID";
    private static final String SAVED_NAME_ID = "name_ID";
    private static final String SAVED_EMAIL_ID = "email_ID";
    private static final String SAVED_PHONE_ID = "phone_ID";
    private static final String SAVED_GENDER_ID = "gender_ID";
    private static final String SAVED_CLASS_ID = "class_ID";
    private static final String SAVED_MAJOR_ID = "major_ID";

    private void loadSavedProfileData(){

        SharedPreferences profileData = getSharedPreferences(SAVED_DATA_PACKAGE_ID, MODE_PRIVATE);

        String savedName = profileData.getString(SAVED_NAME_ID, " ");
        String savedEmail = profileData.getString(SAVED_EMAIL_ID, " ");
        String savedPhone = profileData.getString(SAVED_PHONE_ID, " ");
        String savedClass = profileData.getString(SAVED_CLASS_ID, " ");
        String savedMajor = profileData.getString(SAVED_MAJOR_ID, " ");
        int savedGender = profileData.getInt(SAVED_GENDER_ID, -1);

        TextView nameField = (TextView) findViewById(R.id.name_field);
        TextView emailField = (TextView) findViewById(R.id.email_field);
        TextView phoneField = (TextView) findViewById(R.id.phone_field);
        TextView classField = (TextView) findViewById(R.id.class_field);
        TextView majorField = (TextView) findViewById(R.id.major_field);

        if (savedName != " ")
            nameField.setText(savedName);
        if (savedEmail != " ")
            emailField.setText(savedEmail);
        if (savedPhone != " ")
            phoneField.setText(savedPhone);
        if (savedClass != " ")
            classField.setText(savedClass);
        if (savedMajor != " ")
            majorField.setText(savedMajor);

        // sets the gender buttons to display the saved gender, if there is one
        if (savedGender != -1){

            RadioGroup genderButtons = (RadioGroup) findViewById(R.id.gender_buttons);
            RadioButton savedGenderButton = (RadioButton) genderButtons.getChildAt(savedGender);
            savedGenderButton.setChecked(true);

        }

    }

    private void saveProfileData(){

        TextView nameField = (TextView) findViewById(R.id.name_field);
        TextView emailField = (TextView) findViewById(R.id.email_field);
        TextView phoneField = (TextView) findViewById(R.id.phone_field);
        TextView classField = (TextView) findViewById(R.id.class_field);
        TextView majorField = (TextView) findViewById(R.id.major_field);
        RadioGroup genderButtons = (RadioGroup) findViewById(R.id.gender_buttons);

        String nameString = nameField.getText().toString();
        String emailString = emailField.getText().toString();
        String phoneString = phoneField.getText().toString();
        String classString = classField.getText().toString();
        String majorString = majorField.getText().toString();
        int genderIndicator = genderButtons.indexOfChild(findViewById(genderButtons.getCheckedRadioButtonId()));

        SharedPreferences profileData = getSharedPreferences(SAVED_DATA_PACKAGE_ID, MODE_PRIVATE);
        SharedPreferences.Editor profileDataEditor = profileData.edit();
        profileDataEditor.clear();

        profileDataEditor.putString(SAVED_NAME_ID, nameString);
        profileDataEditor.putString(SAVED_EMAIL_ID, emailString);
        profileDataEditor.putString(SAVED_PHONE_ID, phoneString);
        profileDataEditor.putString(SAVED_CLASS_ID, classString);
        profileDataEditor.putString(SAVED_MAJOR_ID, majorString);
        profileDataEditor.putInt(SAVED_GENDER_ID, genderIndicator);

        profileDataEditor.commit();
    }

    public void onSaveClicked(View view){

        saveProfileData();

        // saves the profile image:
        if (acceptedPhotoUri != null) { // if we haven't accepted a picture, there's nothing to change
            ImageView pictureDisplay = (ImageView) findViewById(R.id.profilePhoto);
            pictureDisplay.buildDrawingCache();
            Bitmap bmap = pictureDisplay.getDrawingCache();
            try {
                FileOutputStream fos = openFileOutput(
                        getString(R.string.profile_photo_file_name), MODE_PRIVATE);
                bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        // make toast indicating save
        Toast saveNotice = Toast.makeText(getApplicationContext(), getString(R.string.save_message_text), Toast.LENGTH_SHORT);
        saveNotice.show();

        // clears all the image URIs so they don't interfere the next time the activity runs:
        cleanAndExit();
    }

    public void onCancelClicked(View view){

        // clears all the image URIs so they don't interfere the next time the activity runs:
        cleanAndExit();

    }

    public void cleanAndExit(){

        if (initialPhotoUri != null){
            File f = new File(initialPhotoUri.getPath());
            if (f.exists())
                f.delete();
            initialPhotoUri = null;
        }
        if (croppedUnapprovedUri != null){
            File f = new File(croppedUnapprovedUri.getPath());
            if (f.exists())
                f.delete();
            croppedUnapprovedUri = null;
        }
        if (acceptedPhotoUri != null){
            File f = new File(acceptedPhotoUri.getPath());
            if (f.exists())
                f.delete();
            acceptedPhotoUri = null;
        }

        finish();
    }

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 9; // arbitrary number

    @TargetApi(Build.VERSION_CODES.M)
    public void checkCameraPermission(){

        Log.d("permissions", "camera permissions check called");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("permissions", "no permission - requesting permission:");


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
        else {
            Log.d("permissions", "camera permission is already granted");
        }
    }
}
