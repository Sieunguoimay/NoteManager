package com.sieunguoimay.vuduydu.notemanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Provider;
import java.util.Arrays;
import java.util.Date;

public class StartActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private SignInButton signInButton;
    private static GoogleApiClient mGoogleApiClient;
    private static GoogleSignInClient mGoogleSignInClient =null;
    private static final int RC_SIGN_IN = 1;
    private static final int RC_API_CHECK = 2;

    private static final String TAG="START_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        signInButton = (SignInButton)findViewById(R.id.googleBtn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                //getIdToken();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
// Build a GoogleSignInClient with the options specified by gso.
        if(mGoogleSignInClient==null)
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


//
//
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestServerAuthCode(getString(R.string.YOUR_CLIENT_ID))
//                .requestEmail()
//                .requestScopes(new Scope(Scopes.PLUS_LOGIN),new Scope(PeopleServiceScopes.CONTACTS_READONLY)
//                ,new Scope(PeopleServiceScopes.USER_PHONENUMBERS_READ))
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
//                .enableAutoManage(this,new GoogleApiClient.OnConnectionFailedListener(){
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                        Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_SHORT).show();
//                    }})
//                .addConnectionCallbacks(this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
//                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        GoogleApiAvailability mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = mGoogleApiAvailability.getErrorDialog(this, connectionResult.getErrorCode(), RC_API_CHECK);
        dialog.show();
    }
    @Override
    public void onConnected(Bundle bundle){

    }

    @Override
    public void onConnectionSuspended(int error){

    }


    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void getIdToken() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if(result.isSuccess()){
//                GoogleSignInAccount acct = result.getSignInAccount();
//                String code = acct.getServerAuthCode();
//                try{
//                    setupGooglePeople(code);
//                }catch(IOException e){
//
//                }
//                //firebaseAuthWithGoogle(acct);
//            }
//
////            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
////            try {
////                // Google Sign In was successful, authenticate with Firebase
////                GoogleSignInAccount account = task.getResult(ApiException.class);
////                firebaseAuthWithGoogle(account);
////            } catch (ApiException e) {
////                // Google Sign In failed, update UI appropriately
////                Log.w(TAG, "Google sign in failed", e);
////                // ...
////            }
//        }
//
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }
//
//        switch (requestCode) {
//            case RC_SIGN_IN:
//                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//
//                if (result.isSuccess()) {
//                    GoogleSignInAccount acct = result.getSignInAccount();
//                    Log.d(TAG, "onActivityResult:GET_TOKEN:success:" + result.getStatus().isSuccess());
//                    Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_SHORT).show();
//                    // This is what we need to exchange with the server.
//                    //new PeoplesAsync().execute(acct.getServerAuthCode());
////                    firebaseAuthWithGoogle(acct);
//                    String code = acct.getServerAuthCode();
//                    try{
//                        setUp(getApplicationContext(),code);
//                    }catch(IOException e){
//
//                    }
//                } else {
//                    Log.d(TAG, result.getStatus().toString() + "\nmsg: " + result.getStatus().getStatusMessage());
//                }
//                break;
//        }
//    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(), "Authentication Successful.", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(StartActivity.this,MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });


    }

    public static PeopleService setUp(Context context, String serverAuthCode) throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Redirect URL for web based applications.
        // Can be empty too.
        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";

        Log.d(TAG, "onActivityResult:Inside 0 ");

        // STEP 1
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                jsonFactory,
                context.getString(R.string.YOUR_CLIENT_ID),
                context.getString(R.string.YOUR_CLIENT_SECRET),
                "",
                redirectUrl).execute();
        Log.d(TAG, "onActivityResult:Inside 1");

        // STEP 2
        GoogleCredential credential = new GoogleCredential.Builder()
                .setClientSecrets(context.getString(R.string.YOUR_CLIENT_ID), context.getString(R.string.YOUR_CLIENT_SECRET))
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .build();

        credential.setFromTokenResponse(tokenResponse);


        // STEP 3
        //PeopleService peopleService =
        return new PeopleService.Builder(httpTransport, jsonFactory, credential).build();
    }


    private void setupGooglePeople(String code)throws IOException{
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        String clientId = getString(R.string.YOUR_CLIENT_ID);
        String clientSecret = getString(R.string.YOUR_CLIENT_SECRET);
        String redirectUrl = "https://notemanager-8107d.firebaseapp.com/__/auth/handler";//

        // Step 2: Exchange -->
        // Exchange auth code for access token
        GoogleTokenResponse tokenResponses = new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                jsonFactory,
                clientId,
                clientSecret,
                code,
                redirectUrl).execute();

        // End of Step 2 <--
        Toast.makeText(getApplicationContext(),"OK BABE",Toast.LENGTH_SHORT).show();

//
//        GoogleCredential credential = new GoogleCredential.Builder()
//                .setTransport(httpTransport)
//                .setJsonFactory(jsonFactory)
//                .setClientSecrets(clientId, clientSecret)
//                .build()
//                .setFromTokenResponse(tokenResponse);
//
//        PeopleService peopleService =
//                new PeopleService.Builder(httpTransport, jsonFactory, credential).build();
//        Toast.makeText(getApplicationContext(),"You are ready to go "+peopleService.getServicePath(),Toast.LENGTH_SHORT).show();
    }


    public static GoogleSignInClient getGoogleSignInClient(){

        return mGoogleSignInClient;
    }
    public static void signOut(){
        mGoogleSignInClient.signOut();
//        if(mGoogleApiClient.isConnected()){
//            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//            mGoogleApiClient.connect();
//        }
    }
}
