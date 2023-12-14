package com.example.epilog;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "UILab";
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private ActivityResultLauncher<IntentSenderRequest> oneTapUILauncher;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedPreferences preferences;
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignInButton btn = (SignInButton) findViewById(R.id.google_button);

        configOneTapSignUpOrSignInClient();
        initFirebaseAuth();

        db = FirebaseFirestore.getInstance();

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d(TAG, "이미 로그인 했음");
            Log.d(TAG, "onStart: "+currentUser.getUid());

            db.collection("user")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " " + currentUser.getUid());

                                    if(document.getId().equals(currentUser.getUid())){
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("uid", currentUser.getUid());
                                        editor.putString("nickname", document.getString("nickname"));
                                        editor.putString("email", document.getString("email"));
                                        editor.commit();
                                        gotoHome();
                                        return;
                                    }
                                }

                                gotoRegister(currentUser);
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            Log.d(TAG, "아직 로그인 안했음");
        }
    }

    private void configOneTapSignUpOrSignInClient() {
        oneTapClient = Identity.getSignInClient(this);

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        //.setFilterByAuthorizedAccounts(true) // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false) // Show all accounts on the device.
                        .build())
                .setAutoSelectEnabled(true)
                .build();

        oneTapUILauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken != null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithCredential:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Log.d(TAG, "onComplete: "+user);
                                            Log.d(TAG, "onComplete: "+user.getEmail());
                                            db.collection("user")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    if(document.getId().equals(user.getUid())){
                                                                        SharedPreferences.Editor editor = preferences.edit();
                                                                        editor.putString("uid", user.getUid());
                                                                        editor.putString("nickname", document.getString("nickname"));
                                                                        editor.putString("email", document.getString("email"));
                                                                        editor.commit();
                                                                        gotoHome();
                                                                        return;
                                                                    }
                                                                }

                                                                gotoRegister(user);
                                                            } else {
                                                                Log.w(TAG, "Error getting documents.", task.getException());
                                                            }
                                                        }
                                                    });

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    Log.d(TAG, "onActivityResult: fail");
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    // https://developers.google.com/identity/one-tap/android/get-saved-credentials
    private void signIn() {
        Log.d(TAG, "singin");
        // check whether the user has any saved credentials for your app. -> onSuccess or onFailure
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult beginSignInResult) {
                        IntentSender intentSender = beginSignInResult.getPendingIntent().getIntentSender();
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(intentSender).build();
                        oneTapUILauncher.launch(intentSenderRequest);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });
    }

    private void gotoRegister(FirebaseUser user) {
        Log.d(TAG, "goto register");
        if (user != null) {
            Intent intent = new Intent(this, NicknameActivity.class);
            setResult(999999, intent);
            intent.putExtra("nickname", user.getDisplayName());
            intent.putExtra("uid", user.getUid());
            intent.putExtra("email", user.getEmail());
            startActivity(intent);
            finish();
        }
    }

    private void gotoHome() {
        Toast.makeText(this,"돌아오신것을 환영해요!",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "goto home");
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}