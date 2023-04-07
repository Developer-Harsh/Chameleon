package com.sneproj.chameleon;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.databinding.ActivityLoginBinding;
import com.sneproj.chameleon.model.User;
import com.sneproj.chameleon.utils.Constants;
import com.sneva.easyprefs.EasyPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 11;
    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(LoginActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(LoginActivity.this.getColor(R.color.bg_main));
        }

        auth = FirebaseAuth.getInstance();
        dialog = new LoadingDialog();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        binding.back.setOnClickListener(v -> finish());


        binding.google.setOnClickListener(v -> {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_IN);
        });

        binding.facebook.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));

            CallbackManager callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    dialog.showdialog(LoginActivity.this);
                    AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();

                                    GraphRequest request = GraphRequest.newMeRequest(
                                            loginResult.getAccessToken(),
                                            new GraphRequest.GraphJSONObjectCallback() {
                                                @Override
                                                public void onCompleted(JSONObject object, GraphResponse response) {
                                                    try {
                                                        String email = object.getString("email");
                                                        String name = object.getString("name");
                                                        String id = object.getString("id");
                                                        String profilePicUrl = "https://graph.facebook.com/" + id + "/picture?type=large";

                                                        User updateUser = new User();
                                                        updateUser.name = name;
                                                        updateUser.uname = "snechameleon";
                                                        updateUser.bio = "";
                                                        updateUser.email = email;
                                                        updateUser.gender = "NA";
                                                        updateUser.uid = user.getUid();
                                                        updateUser.profile = profilePicUrl;
                                                        updateUser.number = "";
                                                        updateUser.nativeLang = "";
                                                        updateUser.location = "";

                                                        FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS).child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()) {
                                                                    dialog.dismissdialog();
                                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                } else {
                                                                    snapshot.getRef().setValue(updateUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                dialog.dismissdialog();
                                                                                EasyPrefs.use().getBoolean("isNew", true);
                                                                                Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                startActivity(intent);
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                    Bundle parameters = new Bundle();
                                    parameters.putString("fields", "id,name,email");
                                    request.setParameters(parameters);
                                    request.executeAsync();
                                }  // Handle user authentication failure

                            });
                }

                @Override
                public void onCancel() {
                    // Handle authentication cancellation
                }

                @Override
                public void onError(FacebookException error) {
                    // Handle authentication error
                }
            });
        });

        binding.email.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, EmailLoginActivity.class).putExtra("login", true)));
    }

    private void authWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        // Show Progress Bar
        dialog.showdialog(this);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            User updateUser = new User();
                            updateUser.name = user.getDisplayName();
                            updateUser.uname = "snechameleon";
                            updateUser.bio = "";
                            updateUser.email = user.getEmail();
                            updateUser.gender = "NA";
                            updateUser.uid = user.getUid();
                            updateUser.profile = String.valueOf(user.getPhotoUrl());
                            updateUser.number = user.getPhoneNumber();
                            updateUser.nativeLang = "";
                            updateUser.location = "";

                            FirebaseDatabase.getInstance().getReference().child(Constants.COLLECTION_USERS).child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        dialog.dismissdialog();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } else {
                                        snapshot.getRef().setValue(updateUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    dialog.dismissdialog();
                                                    EasyPrefs.use().getBoolean("isNew", true);
                                                    Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                authWithGoogle(account.getIdToken());
            } catch (ApiException ignored) {

            }
        }
    }
}