package com.ddkcommunity.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ddkcommunity.R;
import com.ddkcommunity.utilies.AppConfig;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ddkcommunity.fragment.wallet.FragmentSafeKeeping1.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ShareImageFragment extends Fragment {

    private ShareDialog shareDialog;
    TextView buttonclick;
    private CallbackManager callbackManager;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a callbackManager to handle the login responses.
        callbackManager = CallbackManager.Factory.create();

        shareDialog = new ShareDialog(getActivity());

        // This part is optional
        shareDialog.registerCallback(callbackManager, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_facebook, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setMediaShare(view);
    }

    private void setMediaShare(View view) {

        buttonclick=view.findViewById(R.id.buttonclick);
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_app_logo);
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
//		ShareVideo shareVideo = new ShareVideo.Builder()
//				.setLocalUrl(...)
//				.build();

        ShareMediaContent content = new ShareMediaContent.Builder()
                .addMedium(sharePhoto)
//				.addMedium(shareVideo)
                .build();

        final ShareButton shareButton = (ShareButton) view.findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);

        buttonclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareButton.performClick();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Call callbackManager.onActivityResult to pass login result to the LoginManager via callbackManager.
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.e(TAG, "Successfully posted");
            // Write some code to do some operations when you shared content successfully.
        }

        @Override
        public void onCancel() {
            Log.e(TAG, "Cancel occurred");
            // Write some code to do some operations when you cancel sharing content.
        }

        @Override
        public void onError(FacebookException error) {
            Log.e(TAG, error.getMessage());
            // Write some code to do some operations when some error occurs while sharing content.
        }
    };
}