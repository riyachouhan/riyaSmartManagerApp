package com.ddkcommunity.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.send.SendLinkFragment;
import com.ddkcommunity.model.OurTeamData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OurTeamProfileFragment extends Fragment {

    public OurTeamProfileFragment() {
        // Required empty public constructor
    }

    ImageView ivBanner;
    TextView name_view,designation_view,number_view,email_view,content_view;
    private Context mContext;
    String name,email,mobile_no,designation,content,profile_img;
    String whatsappurl,fburldata,instagramurl,twitterurl,youtubeurl;
    LinearLayout shareoption;
    ImageView btnWhatsapp,btnfacebook,btninstagram,btntwitter,btnyoutube;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_our_team_profile, container, false);
        mContext = getActivity();
        name=getArguments().getString("name");
        mobile_no=getArguments().getString("mobile_no");
        email=getArguments().getString("email");
        designation=getArguments().getString("designation");
        content=getArguments().getString("content");
        profile_img=getArguments().getString("profile_img");
        whatsappurl=getArguments().getString("whatsapp_no");
        fburldata=getArguments().getString("fb_url");
        instagramurl=getArguments().getString("instagram_url");
        twitterurl=getArguments().getString("twitter_url");
        youtubeurl=getArguments().getString("youtube_url");
        ivBanner=view.findViewById(R.id.ivBanner);
        ivBanner=view.findViewById(R.id.ivBanner);
        shareoption=view.findViewById(R.id.shareoption);
        name_view=view.findViewById(R.id.name_view);
        designation_view=view.findViewById(R.id.designation_view);
        number_view=view.findViewById(R.id.number_view);
        email_view=view.findViewById(R.id.email_view);
        content_view=view.findViewById(R.id.content_view);
        btnfacebook= view.findViewById(R.id.btnfacebook);
        btninstagram= view.findViewById(R.id.btninstagram);
        btntwitter= view.findViewById(R.id.btntwitter);
        btnyoutube= view.findViewById(R.id.btnyoutube);
        btnWhatsapp= view.findViewById(R.id.btnWhatsapp);
        name_view.setText(name);
        designation_view.setText(designation);
        number_view.setText(mobile_no);
        email_view.setText(email);
        content_view.setText(content);
        Picasso.with(getContext()).load(Constant.SLIDERIMG + profile_img).into(ivBanner);
        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showUserprofile(whatsappurl);
            }
        });
        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showUserprofile(fburldata);
            }
        });
        btninstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showUserprofile(instagramurl);
            }
        });
        btntwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showUserprofile(twitterurl);
            }
        });
        btnyoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showUserprofile(youtubeurl);
            }
        });
        shareoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareProfile();
            }
        });
        return view;
    }

    public void showUserprofile(String url)
    {
        String linkvalue=url;
        if(linkvalue!=null) {
            //send view
            Fragment fragment = new SendLinkFragment();
            Bundle arg = new Bundle();
            arg.putString("link", linkvalue);
            fragment.setArguments(arg);
            MainActivity.addFragment(fragment, true);
        }else
        {
            Toast.makeText(mContext, "Link not available ", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareProfile() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://smartassetmanagers.com/rommelsantos");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, "Share this profile");
        startActivity(shareIntent);
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.setTitle("Profile");
        MainActivity.enableBackViews(true);
    }

}
