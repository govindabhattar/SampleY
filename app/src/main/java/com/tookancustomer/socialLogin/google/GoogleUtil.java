package com.tookancustomer.socialLogin.google;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tookancustomer.utility.Utils;

public class GoogleUtil {

    private GoogleSignInClient mGoogleSignInClient;
    private Activity mActvity;
    public static final int RC_SIGN_IN = 1001;

    public GoogleUtil(GoogleBuilder googleBuilder) {
        this.mActvity = googleBuilder.mActvity;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(googleBuilder.mActvity, gso);
    }


    public void login() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mActvity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        if (mActvity instanceof GoogleLoginListener) {
            GoogleLoginListener googleLoginListener = (GoogleLoginListener) mActvity;
            try {
                googleLoginListener.onLogin(task.getResult(ApiException.class));
            } catch (ApiException e) {

                               Utils.printStackTrace(e);
            }
        }
    }

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(mActvity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    public static class GoogleBuilder {
        private Activity mActvity;

        public GoogleBuilder(Activity mActvity) {
            this.mActvity = mActvity;
        }

        public GoogleUtil build() {
            return new GoogleUtil(this);
        }
    }


    public interface GoogleLoginListener {
        void onLogin(GoogleSignInAccount account);
    }
}
