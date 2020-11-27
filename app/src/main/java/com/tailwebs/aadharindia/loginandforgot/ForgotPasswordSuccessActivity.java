package com.tailwebs.aadharindia.loginandforgot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_to_login_button)
    Button backToLoginButton;

    @BindView(R.id.message_text_response)
    TextView messageResponse;

    @BindView(R.id.change_number)
    TextView changeNumber;

    @BindView(R.id.resend_link)
    TextView resendLink;


    private FirebaseAnalytics mFirebaseAnalytics;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_success);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + "Forgot Password", null);

        messageResponse.setText(getIntent().getExtras().getString("message"));
        backToLoginButton.setOnClickListener(this);

        changeNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_to_login_button:
                finish();

                Intent intent = new Intent(ForgotPasswordSuccessActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
              break;

            case R.id.change_number:
                finish();
                Intent change_intent = new Intent(ForgotPasswordSuccessActivity.this, ForgotPasswordActivity.class);
                startActivity(change_intent);
                break;
        }
    }
}
