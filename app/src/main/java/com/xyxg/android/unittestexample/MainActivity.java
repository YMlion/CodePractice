package com.xyxg.android.unittestexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.xyxg.android.unittestexample.mail.MailInfo;
import com.xyxg.android.unittestexample.mail.MailLogin;
import com.xyxg.android.unittestexample.mail.MailOperate;
import com.xyxg.android.unittestexample.mail.User;
import com.xyxg.android.unittestexample.other.AESCrypt;
import com.xyxg.android.unittestexample.util.RxUtil;
import com.xyxg.android.unittestexample.util.SubscriberWrap;
import io.reactivex.Flowable;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public class MainActivity extends AppCompatActivity {

    String[][] mailboxes = new String[][] {
            new String[] { "qq.com", "498079564@qq.com", "Nq123456" },
            new String[] { "163.com", "lm_yao@163.com", "123456abc" },
            new String[] { "126.com", "xyxgylm@126.com", "123456abc" }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        try {
            String code = MimeUtility.decodeText("=?GB18030?B?suLK1A==?=");
            Log.e("TAG", "onCreate: " + code);
            String b = new String(Base64.decode("suLK1A==", 0), "GB18030");
            Log.e("TAG", "onCreate: " + b);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String file = Environment.getExternalStorageDirectory()
                                 .getAbsolutePath()
                + File.separator
                + "Download"
                + File.separator
                + "ActivityManagerService.txt";
        Flowable.generate(
                () -> new BufferedReader(new InputStreamReader(new FileInputStream(file))),
                (buffer, emitter) -> {
                    try {
                        String line;
                        if ((line = buffer.readLine()) != null) {
                            emitter.onNext(line);
                        } else {
                            emitter.onComplete();
                        }
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                    return buffer;
                }, BufferedReader::close)
                .ofType(String.class)
                .compose(RxUtil.applySchedulerF())
                .subscribe(new SubscriberWrap<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(10000);
                    }

                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                });
        List<String> strings = new ArrayList<>();
        Flowable.just(strings)
                .flatMap(Flowable::fromIterable)
                .filter(s -> false).subscribe();
    }

    public void mailQQ(View view) {
        AppContext.getContext()
                  .setUser(new User(mailboxes[0][0], mailboxes[0][1], mailboxes[0][2]));
        new Thread(() -> {
            try {
                Folder[] folders =
                        MailOperate.getFolders(mailboxes[0][0], mailboxes[0][1], mailboxes[0][2],
                                               true);
                StringBuilder f = new StringBuilder();
                for (Folder folder : folders) {
                    Log.e("TAG", "folder name : " + folder.getFullName());
                    f.append(folder.getFullName())
                     .append("\n");
                    if (folder.getFullName()
                              .equals("其他文件夹")) {
                        Folder[] folders1 = folder.list("*");
                        if (folders1 != null) {
                            for (Folder folder1 : folders1) {
                                Log.e("TAG", "folder name : " + folder1.getFullName());
                            }
                        }
                    }
                }

                Transport transport =
                        MailLogin.smtpConnect(mailboxes[0][0], mailboxes[0][1], mailboxes[0][2],
                                              true)
                                 .getTransport();
                transport.connect();
                if (transport.isConnected()) {
                    transport.close();
                    DetailActivity.start(MainActivity.this, f.toString());
                }
            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(
                        () -> Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT)
                                   .show());
            }
        }).start();
    }

    public void mail163(View view) {
        loginMailbox(mailboxes[1]);
    }

    private void loginMailbox(String[] mailbox) {
        AppContext.getContext()
                  .setUser(new User(mailbox[0], mailbox[1], mailbox[2]));
        Flowable.just(mailbox)
                .concatMap(strings -> Flowable.fromArray(
                        MailOperate.getFolders(strings[0], strings[1], strings[2], true)))
                .map(Folder::getFullName)
                .reduce((s, s2) -> s + "\n" + s2)
                .doOnSuccess(s -> {
                    Transport transport =
                            MailLogin.smtpConnect(mailbox[0], mailbox[1], mailbox[2], true)
                                     .getTransport();
                    transport.connect();
                    if (transport.isConnected()) {
                        transport.close();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        DetailActivity.start(MainActivity.this, s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT)
                             .show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void mail126(View view) {
        loginMailbox(mailboxes[2]);
    }

    public void others(View view) {
        try {
            String es = AESCrypt.encrypt("duoyi14@henhaoji.com", "123456");
            Log.e("TAG", "others: " + es);

            String ds = AESCrypt.decrypt("duoyi14@henhaoji.com", es);
            Log.e("TAG", "others: " + ds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void parseEml() {
        InputStream in = null;
        try {
            Properties props = System.getProperties();
            Session mailSession = Session.getDefaultInstance(props, null);
            in = new BufferedInputStream(new FileInputStream(
                    Environment.getExternalStorageDirectory()
                               .getAbsolutePath() + File.separator + "data.eml"));
            MimeMessage mm = new MimeMessage(mailSession, in);
            Log.e("parseEml", "parseEml: " + MailInfo.getSubject(mm));
            Log.e("parseEml", "parseEml: " + MailInfo.getFrom(mm));
            StringBuilder sb = new StringBuilder();
            MailInfo.getMailTextContent(mm, sb);
            Log.e("parseEml", "parseEml: " + sb);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
