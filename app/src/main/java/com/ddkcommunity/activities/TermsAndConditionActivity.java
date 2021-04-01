package com.ddkcommunity.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ddkcommunity.R;

public class TermsAndConditionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        TextView tvTerms = findViewById(R.id.tvTextTerms);
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTerms.setText("Please read the terms and conditions carefully.\n" +
                "\n" +
                "By using SMART ASSET MANAGERS DIGITAL WALLET APPLICATION, it is\n" +
                "understood that you fully agree on the Terms and Conditions of this e-wallet.\n" +
                "Please stop using this service if you do not agree.\n" +
                "SMART ASSET MANAGERS DIGITAL WALLET APPLICATION is currently undergoing\n" +
                "development and the content of the Terms &amp; Conditions maybe revised, changed\n" +
                "or edited anytime and the amendments are effective immediately after its\n" +
                "publication on the official website or in its application.\n" +
                "SMART ASSET MANAGERS DIGITAL WALLET APPLICATION is a third party company\n" +
                "that links services to users, thus users can utilize its digital assets and obtain\n" +
                "rewards from the platform by contributing to the community.\n" +
                "SMART ASSET MANAGERS DIGITAL WALLET APPLICATION always focuses on the\n" +
                "security of the online digital services. However, we cannot guarantee our e-wallet\n" +
                "is free from security compromises that may arise anytime.\n" +
                "Therefore, SMART ASSET MANAGERS DIGITAL WALLET APPLICATION AND ITS\n" +
                "OWNERS have no liability nor any accountability for any loss or misplacement of\n" +
                "any asset stored in the digital wallet and it is not responsible for the content\n" +
                "provided by any third party service providers listed within the application.\n" +
                "NO INVESTMENT ADVICE\n" +
                "The Content is for the purpose of information only. You should not interpret any\n" +
                "such information or other material as legal, tax, investment, financial or other\n" +
                "advice. The Site’s contents do not constitute a solicitation, recommendation,\n" +
                "endorsement or offer by owners or any third party service provider. Likewise,\n" +
                "there is no offer to buy or sell any securities or other financial instruments in this\n" +
                "\n" +
                "or in any other jurisdiction, in which such solicitation or offer is deemed unlawful\n" +
                "under the securities laws of such jurisdiction.\n" +
                "All contents on this Site is information of a general nature and do not address the\n" +
                "circumstances of any particular individual or entity. Nothing in the Site constitutes\n" +
                "professional and/or financial advice. No information on the Site offers a\n" +
                "comprehensive explanation of the matters discussed or the law relative to it.\n" +
                "Owners are not a trustee due to the individual’s use of or access to the Site or\n" +
                "Content. As a Site user, you alone assume the sole responsibility of evaluating the\n" +
                "merits and risks associated with the use of any information or other Content on\n" +
                "the Site. Any decision arrived at based on information or content of this Site is\n" +
                "your own accountability.\n" +
                "As a user of our Site, the Owners, their affiliates or any third party service\n" +
                "provider should not be held liable for any possible claim for damages arising from\n" +
                "any decision made based on information or other Content of this Site.\n" +
                "INVESTMENT RISKS\n" +
                "Remember that there are risks associated with investing in crypto. Crypto\n" +
                "investment, just like investing in stocks, bonds, exchange traded funds, mutual\n" +
                "funds and money market funds involve risks of loss of principal or capital. Crypto\n" +
                "is a high-risk investment.\n" +
                "SMART ASSET MANAGERS DEVELOPMENT PROJECTS\n" +
                "Joining SMART ASSET MANAGERS DEVELOPMENT PROJECTS OPPORTUNITIES is\n" +
                "not an invitation to invest or buy in to SMART ASSET MANAGERS DEVELOPMENT\n" +
                "PROJECTS business.\n" +
                "When taking chance in the opportunities offered by SMART ASSET MANAGERS\n" +
                "DEVELOPMENT PROJECTS, you are only lending your DDKoin and any other\n" +
                "acceptable crypto currencies and fiat currency to SMART ASSET MANAGERS\n" +
                "DEVELOPMENT PROJECTS.\n" +
                "\n" +
                "In the detailed presentation, information disclosed in the apps, you will read more\n" +
                "and we hope that it will give you a better understanding of how the whole\n" +
                "concept works.\n" +
                "As a matter of rule, all assets lent to SMART ASSET MANAGERS DEVELOPMENT\n" +
                "PROJECTS are not subject to any locked-in contract, meaning your original lent\n" +
                "asset is available for you to withdraw at anytime and all withdrawals must be\n" +
                "received the same day the withdrawal transaction is processed. Because of your\n" +
                "lending contribution to the SMART ASSET MANAGERS DEVELOPMENT PROJECTS\n" +
                "as part of the DDK AND SAM Community, you will receive certain percentage of\n" +
                "your original amount lent or loaned as rewards token on a daily basis distributed\n" +
                "in DDKOIN Crypto Currency.\n" +
                "All your intent to lend Fiat Currency or any acceptable crypto currency will be\n" +
                "converted to DDKOIN and in turn lent to SMART ASSET MANAGERS\n" +
                "DEVELOPMENT PROJECTS. Based on the fiat value converted to DDKOIN, not from\n" +
                "the total numbers, you will receive a percentage of your original total dollar value\n" +
                "and not from the total quantity of DDkoin.\n" +
                "SMART ASSET MANAGERS DEVELOPMENT PROJECTS also wants to make it clear\n" +
                "that you will receive level referral rewards up to 5 levels on the Phase 1 Pre-\n" +
                "Development Capital Raising.\n" +
                "Should you decide to withdraw your capital loan value from SMART ASSET\n" +
                "MANAGERS DEVELOPMENT PROJECTS, your level 1-5 referral rewards program\n" +
                "will be cancelled automatically, too. There is no restore program available unless\n" +
                "you create a new account again.\n" +
                "All Phase 1 pioneers will enjoy all privileges in SMART ASSET MANAGERS\n" +
                "DEVELOPMENT PROJECTS income. This is however, only applicable if you have an\n" +
                "active participation in the SMART ASSET MANAGERS DEVELOPMENT PROJECTS."+"\n\n\n");
    }

}
