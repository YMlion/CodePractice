package com.xyxg.android.unittestexample;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sun.mail.imap.IMAPFolder;
import com.xyxg.android.unittestexample.mail.MailLogin;
import com.xyxg.android.unittestexample.mail.MailOperate;
import com.xyxg.android.unittestexample.mail.User;

import javax.mail.Folder;
import javax.mail.Transport;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";
    @BindView(R.id.et_login_name)
    TextInputEditText nameEt;

    @BindView(R.id.et_login_pwd)
    TextInputEditText pwdEt;

    @BindView(R.id.btn_login)
    Button loginBtn;

    Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        loginBtn.setOnClickListener(this);
        login = new Login();
    }

    private void loginMail(final String name, final String pwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Folder[] folders = null;
                boolean isSSL = true;
                try {
                    folders = MailOperate.getFolders(name.substring(name.indexOf('@') + 1), name, pwd, isSSL);
                } catch (Exception e) {
                    e.printStackTrace();
                    isSSL = false;
                }
                try {
                    if (!isSSL) {
                        folders = MailOperate.getFolders(name.substring(name.indexOf('@') + 1), name, pwd, isSSL);
                    }
                    StringBuilder f = new StringBuilder();
                    for (Folder folder : folders) {
                        IMAPFolder imapFolder = (IMAPFolder) folder;
                        for (String s : imapFolder.getAttributes()) {
                            Log.e(TAG, "getAttributes: " + s);
                        }
                        f.append(folder.getName()).append("\n");
                    }
                    Transport transport = MailLogin.smtpConnect(name.substring(name.indexOf('@') + 1), name, pwd, isSSL)
                            .getTransport("smtp");
                    if (name.endsWith("gmail.com")) {
                        transport.connect("smtp.gmail.com",name, "ya29.GlthBJHkfKVKj-5ombZ1o-osWrxOthWaeppGyFrkH40b4mDBgBmHnQ2doiclfBdMM2v8UGAINyZj_q9JiDkwfd6pQjJucoD61gz0Z9QK9jSVKyPpNOBNjFr74cVM");
                    } else {
                        transport.connect();
                    }
                    if (transport.isConnected()) {
                        transport.close();
//                        SendMailSMTP.sendMultipleEmail("126.com", "xyxgylm@126.com", "123456abc", true);
                        //SendMailSMTP.sendTextEmail(name.substring(name.indexOf('@') + 1), name, pwd, isSSL);
                        AppContext.getContext().setUser(new User(name.substring(name.indexOf('@') + 1), name, pwd));
                        DetailActivity.start(LoginActivity.this, f.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String name = nameEt.getText().toString();
                String pwd = pwdEt.getText().toString();
                if (checkData(name, pwd)) {
                    loginMail(name, pwd);
//                    loginMail("xyxgylm@126.com", "123456abc");
                } else {
                    Toast.makeText(this, "输入有误", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkData(String name, String pwd) {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd);
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}
