package com.ayushigarg.whygoalone.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ayushigarg.whygoalone.Interfaces.OnAuthentication;
import com.ayushigarg.whygoalone.Models.Restaurant;
import com.ayushigarg.whygoalone.Models.User2;
import com.ayushigarg.whygoalone.R;
import com.desmond.squarecamera.CameraActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    EditText inputEmail, inputPassword, inputName, inputAge, inputSex, inputCity, inputContactNo;
    Button btnSignIn, btnSignUp, btnResetPassword;
    ProgressBar progressBar;
    FirebaseAuth auth;
    DatabaseReference firebaseDatabase;
    FirebaseDatabase firebaseInstance;
    FirebaseUser userr;
    ImageView userImage;
    FloatingActionButton getPic;
    StorageReference storageReference;
    Uri userPicUri = null;
    String userPicUrl = null;
    OnAuthentication onAuth;
    String name, city, email, password, contactNo;
    Integer age, sex;

    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int galler = 1;
    private static final int REQ_CODE = 1991;
    private static final int REQUEST_CAMERA = 0101;

    String userId;

    public static final String TAG = "DD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
//        userr = auth.getCurrentUser();
//        userId = userr.getUid();
        getSupportActionBar().setTitle("SignUp");

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputAge = (EditText) findViewById(R.id.age);
        inputName = (EditText) findViewById(R.id.name);
        inputSex = (EditText) findViewById(R.id.sex);
        inputCity = (EditText) findViewById(R.id.city);
        inputContactNo = (EditText) findViewById(R.id.contactNo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        userImage = (ImageView) findViewById(R.id.userImage);
        getPic = (FloatingActionButton) findViewById(R.id.getPic);

        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("userss");

        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseInstance.getReference("zinder-dc0b2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String appTitle = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = inputName.getText().toString().trim();
                final String stringAge = inputAge.getText().toString().trim();
//                age = Integer.parseInt(stringAge);
                String stringSex = inputSex.getText().toString().trim();
                if (stringSex.equals("MALE") || stringSex.equals("Male") || stringSex.equals("M") || stringSex.equals("m") || stringSex.equals("male")){
                    sex = 1;
                }
                else{
                    sex = 2;
                }
                city = inputCity.getText().toString().trim();
                contactNo = inputContactNo.getText().toString();
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(stringAge)) {
                    Toast.makeText(getApplicationContext(), "Enter Age!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(stringSex)) {
                    Toast.makeText(getApplicationContext(), "Enter Sex!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(city)){
                    Toast.makeText(getApplicationContext(), "Enter City!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(contactNo)){
                    Toast.makeText(getApplicationContext(), "Enter Contact Number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email Address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    inputPassword.setError("Password too short, enter minimum 6 characters!");
                    return;
                }

                age = Integer.parseInt(stringAge);

                if (age < 0 || age > 100) {
                    inputAge.setError("Enter Correct Age!");
                    return;
                }

                if (age < 18 || age > 50) {
                    inputAge.setError("You are not Eligible!");
                    return;
                }

                if (userPicUri == null){
                    Toast.makeText(SignupActivity.this, "Please Upload Profile Picture", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignupActivity.this, "Sign Up Hogya", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
//                                    finish();
                                    uploadImage(userPicUri);
//                                    if (userPicUrl != null) {
//                                        if (TextUtils.isEmpty(userId)) {
//                                            createUser(name, age, sex, city, email, password);
//                                        } else {
//                                            updateUser(name, age, sex, city, email, password);
//                                        }
//                                    }
                                }
                            }
                        });

            }
        });

        onAuth = new OnAuthentication() {
            @Override
            public void onSuccess(Bitmap a) {
                if (TextUtils.isEmpty(userId)) {
                    createUser(name, age, sex, city, contactNo, email, password);
                } else {
                    updateUser(name, age, sex, city, contactNo, email, password);
                }
            }
        };

    }
    public void createUser(String name, Integer age, Integer sex, String city, String contactNo, String email, String password) {

        if (TextUtils.isEmpty(userId)) {
            userr = auth.getCurrentUser();
            userId = userr.getUid();
        }

        ArrayList<Restaurant> al = new ArrayList<>();

        User2 user = new User2(name, age, sex, city, contactNo, userPicUrl, email, password, al, null);

        firebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    private void addUserChangeListener() {
        firebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User2 user = dataSnapshot.getValue(User2.class);

                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.getName());

                Toast.makeText(SignupActivity.this, "User Created", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read user", error.toException());
                Toast.makeText(SignupActivity.this, "Error aagyi bc!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUser(String name, Integer age, Integer sex, String city, String contactNo, String email, String password) {

        if (!TextUtils.isEmpty(name))
            firebaseDatabase.child(userId).child("name").setValue(name);

        if (age != null)
            firebaseDatabase.child(userId).child("age").setValue(age);

        if (sex != null)
            firebaseDatabase.child(userId).child("sex").setValue(sex);

        if (city != null)
            firebaseDatabase.child(userId).child("city").setValue(sex);

        if (contactNo != null)
            firebaseDatabase.child(userId).child("contactNo").setValue(sex);

        if (!TextUtils.isEmpty(email))
            firebaseDatabase.child(userId).child("email").setValue(email);

        if (!TextUtils.isEmpty(password))
            firebaseDatabase.child(userId).child("password").setValue(password);

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void getImages() {

        CharSequence cameraOptions[] = new CharSequence[] {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(cameraOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    int cameraPerm = ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.CAMERA);
                    if (cameraPerm != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SignupActivity.this, new String[]{
                                Manifest.permission.CAMERA
                        }, REQ_CODE);
                    }
                    else {
                        takeFromCamera();
                    }
                }
                else if (which == 1){
                    Intent i=new Intent(Intent.ACTION_PICK);
                    i.setType("image/*");
                    startActivityForResult(i,INTENT_REQUEST_GET_IMAGES);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {

        if(requestCode==INTENT_REQUEST_GET_IMAGES && resuleCode==RESULT_OK)
        {
            Uri image = intent.getData();
            CropImage.activity(image)
                    .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                    .setAspectRatio(100,100)
                    .start(SignupActivity.this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(intent);
            if (resuleCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                userPicUri = resultUri;
                userImage.setImageURI(resultUri);
            } else if (resuleCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (resuleCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            Uri photoUri = intent.getData();
            CropImage.activity(photoUri)
                    .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                    .setAspectRatio(100,100)
                    .start(SignupActivity.this);
        }

        super.onActivityResult(requestCode, resuleCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQ_CODE) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Permission Not Given", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
            takeFromCamera();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void takeFromCamera(){
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    private void uploadImage(Uri uri){

        if (uri != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Image...");

            StorageReference profileRef = storageReference.child("profileImages/" + inputEmail.getText().toString().trim());

            final Bitmap noUse = null;

            profileRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            userPicUrl = taskSnapshot.getDownloadUrl().toString();
                            onAuth.onSuccess(noUse);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage(((int) progress) + "% Uploaded...");
                        }
                    })
            ;
        }
    }
}
