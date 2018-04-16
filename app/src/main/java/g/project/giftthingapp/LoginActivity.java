package g.project.giftthingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener{

    private static final int REQUEST_READ_CONTACTS = 0;

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference myRef;

    // UI references.
    //Sign in views
    private View mProgressView;
    private EditText sEmailView;
    private EditText sPasswordView;
    private View sLoginFormView;

    //Create account views
    private EditText cName;
    private EditText cEmailView;
    private EditText cBirthday;
    private EditText cAddress;
    private EditText cPasswordView;
    private EditText cConfirmPassword;
    private View cCreateAccountFormView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //sign in Views
        mProgressView = findViewById(R.id.login_progress);
        sEmailView = (EditText) findViewById(R.id.email);
        sPasswordView = (EditText) findViewById(R.id.password);
        sLoginFormView = findViewById(R.id.email_login_form);

        //create account Views
        cName = (EditText) findViewById(R.id.add_name);
        cEmailView = (EditText) findViewById(R.id.add_email);
        cBirthday = (EditText) findViewById(R.id.add_birthday);
        cAddress = (EditText) findViewById(R.id.add_address);
        cPasswordView = (EditText) findViewById(R.id.add_password);
        cConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        cCreateAccountFormView = findViewById(R.id.email_create_account_form);

        //Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.transition_create_account_button).setOnClickListener(this);

        //Start Auth
        mAuth = FirebaseAuth.getInstance();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateCreateAccountForm()) {
            return;
        }



        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            user = mAuth.getCurrentUser();

                            user.getEmail();

                            //create basic profile for new user
                            Profile newProfile = new Profile();
                            newProfile.setUid(user.getUid());
                            newProfile.setName(cName.getText().toString());
                            newProfile.setBirthday(cBirthday.getText().toString());
                            newProfile.setAddress((cAddress.getText().toString()));


                            myRef = database.getReference("Profile/User/" + user.getUid());
                            myRef.setValue(newProfile);

                            final String username = newProfile.getName().toUpperCase().replace(" ", "");

                            myRef = database.getReference("Profile/Usernames/" + username);
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                   int index = 0;
                                   for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                        index++;
                                    }
                                   myRef = database.getReference("Profile/Usernames/" +  username + "/" + index);
                                   myRef.setValue(user.getUid());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("The read failed: " + databaseError.getCode());
                                }
                            });

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Context context = getApplicationContext();
                            Toast toast = Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT);
                            toast.show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateLoginForm()) {
            return;
        }

        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Context context = getApplicationContext();
                            Toast toast = Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT);
                            toast.show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    private boolean validateLoginForm() {
        boolean valid = true;

        String email = sEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            sEmailView.setError("Required.");
            valid = false;
        } else {
            sEmailView.setError(null);
        }

        String password = sPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            sPasswordView.setError("Required.");
            valid = false;
        } else {
            sPasswordView.setError(null);
        }

        return valid;
    }

    private boolean validateCreateAccountForm() {
        boolean valid = true;

        String name = cName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            cName.setError("Required.");
            valid = false;
        } else {
            cName.setError(null);
        }

        String email = cEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            cEmailView.setError("Required.");
            valid = false;
        } else {
            cEmailView.setError(null);
        }

        String password = cPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            cPasswordView.setError("Required.");
            valid = false;
        } else {
            cPasswordView.setError(null);
        }

        String confirmPassword = cConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            cConfirmPassword.setError("Required.");
            valid = false;
        } else if(!password.equals(confirmPassword)) {
            cConfirmPassword.setError("Does not match.");
            valid = false;
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            cConfirmPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_sign_in_button) {
            signIn(sEmailView.getText().toString(), sPasswordView.getText().toString());
        } else if (i == R.id.transition_create_account_button) {
            cCreateAccountFormView.setVisibility(View.VISIBLE);
            sLoginFormView.setVisibility(View.GONE);
        } else if (i == R.id.email_create_account_button) {
            createAccount(cEmailView.getText().toString(), cPasswordView.getText().toString());
        } /*else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }*/
    }
}

