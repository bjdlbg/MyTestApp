package com.example.a84640.mytestapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * @author: J.xiang
 * @date: On 2018/11/3
 */
@SuppressLint("Registered")
public class SignupActivity extends AppCompatActivity {
    private static final String TAG ="SignupActivity";

    @BindView(R.id.input_name)EditText _nameText;
    @BindView(R.id.input_address)EditText _addressText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUP();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成注册返回登陆界面
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                //添加切换效果
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
    }

    private void signUP() {
        Log.d(TAG,"signup");
        //输入的格式验证
        if (!valiDate()){
            onSignupFiled();
            return;
        }

        _signupButton.setEnabled(false);

     //TODO: 添加progressbar

//        String mName=_nameText.getText().toString();
//        String mAddress=_addressText.getText().toString();
//        String mEmail=_emailText.getText().toString();
//        String mMobile=_mobileText.getText().toString();
//        String mPassword=_passwordText.getText().toString();
//        String mReEnterPassword=_reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onSignupSuccess();
                        //TODO: close the dialog
                    }
                },3000);


    }

    private void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK,null);
        finish();
    }

    private void onSignupFiled() {
        Toast.makeText(getBaseContext(),"Login failed",Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    /**
     * 简单格式验证
     * @return
     */
    private boolean valiDate() {
        boolean valid =true;

        String mName=_nameText.getText().toString();
        String mAddress=_addressText.getText().toString();
        String mEmail=_emailText.getText().toString();
        String mMobile=_mobileText.getText().toString();
        String mPassword=_passwordText.getText().toString();
        String mReEnterPassword=_reEnterPasswordText.getText().toString();

        if (mName.isEmpty()||mName.length()<3){
            _nameText.setError("at least 3 characters");
            valid=false;
        }else {
            _nameText.setError(null);
        }

        if (mAddress.isEmpty()){
            _addressText.setError("Enter Valid Address");
            valid=false;
        }else {
            _addressText.setError(null);
        }

        if (mMobile.isEmpty()||mMobile.length()!=11){
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        }else {
            _mobileText.setError(null);
        }

        if (mEmail.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            _emailText.setError("Enter a valid email address");
            valid=false;
        }else {
            _emailText.setError(null);
        }

        if (mPassword.isEmpty()||mPassword.length()<6||mPassword.length()>20){
            _passwordText.setError("between 6 and 20 alphanumeric characters");
            valid=false;
        }else {
            _passwordText.setError(null);
        }

        if (mReEnterPassword.isEmpty()||mReEnterPassword.length()<6||mReEnterPassword.length()>20){
            _passwordText.setError("Password Do not match");
            valid=false;
        }else {
            _reEnterPasswordText.setError(null);
        }
        return valid;
    }


}
