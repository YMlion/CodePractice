package com.xyxg.android.unittestexample;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import com.xyxg.android.unittestexample.job.PullJobService;
import com.xyxg.android.unittestexample.mail.MailInfo;
import com.xyxg.android.unittestexample.mail.MailLogin;
import com.xyxg.android.unittestexample.mail.User;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.ConnectionAdapter;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.MimeMessage;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    @BindView(R.id.textView2)
    TextView folderTv;
    @BindView(R.id.textView4)
    TextView mailTv;

    public static void start(Context context, String folders) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra("f", folders);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        folderTv.setText(intent.getStringExtra("f"));
        getMails();
        //        parseEml();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                if (Build.VERSION.SDK_INT > 20) {
                    JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                    ComponentName name = new ComponentName(getApplication(), PullJobService.class);
                    JobInfo job = new JobInfo.Builder(1, name)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                            .setPeriodic(3000)
                            .build();
                    scheduler.schedule(job);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 20) {
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.cancel(1);
        }
    }

    private void parseEml() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = new BufferedInputStream(new FileInputStream(
                            Environment.getExternalStorageDirectory()
                                    + File.separator
                                    + "attachimage.eml"));
                    MimeMessage message = new MimeMessage(null, in);
                    final StringBuilder mail = new StringBuilder();
                    mail
                            .append(MailInfo.getSubject(message))
                            .append("\n")
                            .append(MailInfo.getFrom(message))
                            .append("\n")
                            .append(MailInfo.getReceiveAddress(message, Message.RecipientType.TO));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mailTv.setText(mail.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getMails() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = AppContext.getContext().getUser();
                try {
                    IMAPStore store =
                            (IMAPStore) MailLogin.imapConnect(user.getHost(), user.getAccount(),
                                    user.getPwd(), !user.getHost().equals("2980.com"));
                    store.addConnectionListener(new ConnectionListener() {
                        @Override
                        public void opened(ConnectionEvent e) {
                            Log.e(TAG, "store opened.");
                        }

                        @Override
                        public void disconnected(ConnectionEvent e) {
                            Log.e(TAG, "store disconnected.");
                        }

                        @Override
                        public void closed(ConnectionEvent e) {
                            Log.e(TAG, "store closed.");
                        }
                    });
                    store.connect();
                    if (store.hasCapability("ID")) {
                        store.id(new HashMap<String, String>() {
                            {
                                put("name", "2980Android");
                                put("version", "1.0");
                            }
                        });
                    }
                    Log.e(TAG, "server is support idle : " + store.hasCapability("IDLE"));
                    Log.e(TAG, "server is support notify : " + store.hasCapability("NOTIFY"));

                    final IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
                    folder.addConnectionListener(new ConnectionAdapter() {
                        @Override
                        public void closed(ConnectionEvent e) {
                            super.closed(e);
                            Log.d(TAG, "fold closed.");
                        }

                        @Override
                        public void opened(ConnectionEvent e) {
                            super.opened(e);
                            Log.d(TAG, "fold opened.");
                        }
                    });
                    folder.open(Folder.READ_ONLY);
                    addMessageListener(store, folder);
                    int count = folder.getMessageCount();
                    if (count > 2) {
                        count = 2;
                    }
                    Message[] messages = folder.getMessages(1, count);
                    //                    Message[] messages = new Message[1];
                    //messages[0] = folder.getMessageByUID(1480657571);
                    //messages[0] = folder.getMessageByUID(1343717111);
                    //messages[0] = folder.getMessageByUID(1615);
                    FetchProfile fp = new FetchProfile();
                    fp.add(FetchProfile.Item.ENVELOPE);
                    fp.add("UID");
                    folder.fetch(messages, fp);
                    final StringBuilder mail = new StringBuilder();
                    int i = 1;
                    for (Message message : messages) {

                        IMAPMessage imapMessage = (IMAPMessage) message;
                        mail.append("************ ").append(i++).append(" *****************\n");
                        long uid = folder.getUID(message);
                        StringBuilder content = new StringBuilder();
                        long start = System.currentTimeMillis();
                        MailInfo.getMailTextContent(imapMessage, content);
                        long end = System.currentTimeMillis();
                        Log.e("TAG", "parse content : " + (end - start));
                        start = System.currentTimeMillis();
                        boolean hasAttach = MailInfo.isContainAttachment(imapMessage);
                        end = System.currentTimeMillis();
                        Log.e("TAG", "parse attach : " + (end - start));
                        mail
                                .append(MailInfo.getSubject(imapMessage))
                                .append("\n")
                                .append(MailInfo.getFrom(imapMessage))
                                .append("\n")
                                .append(MailInfo.getReceiveAddress(imapMessage,
                                        Message.RecipientType.TO))
                                .append("\n")
                                .append(uid)
                                .append("\n")
                                .append(MailInfo.isRead(imapMessage) ? "已读" :
                                        "未读"/*content.toString()*/)
                                .append("\n")
                                .append(MailInfo.isStar(imapMessage) ? "星标" :
                                        "普通"/*content.toString()*/)
                                //                                .append("\n")
                                //                                .append(content.toString())
                                .append("\n")
                                .append(hasAttach)
                                .append("\n")
                                .append(imapMessage.getReceivedDate())
                                .append("\n")
                                .append(imapMessage.getMessageNumber())
                                .append("\n");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mailTv.setText(mail.toString());
                        }
                    });
                    try {
                        folder.idle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //folder.close(false);
                    //store.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

    private void addMessageListener(final Store store, IMAPFolder folder)
            throws MessagingException {
        if (store.isConnected() && store instanceof IMAPStore) {
            if (((IMAPStore) store).hasCapability("IDLE")) {
                folder.addMessageCountListener(new MessageCountAdapter() {
                    @Override
                    public void messagesAdded(MessageCountEvent e) {
                        super.messagesAdded(e);
                        Message[] messages = e.getMessages();
                        try {
                            Log.e(TAG, "messagesAdded: " + MailInfo.getSubject(
                                    (MimeMessage) messages[0]));
                            Folder f = (Folder) e.getSource();
                            if (f.isOpen()) {
                                f.close();
                            }
                            SystemClock.sleep(1000);
                            IMAPFolder newF = (IMAPFolder) store.getFolder("INBOX");
                            addMessageListener(store, newF);
                            if (!newF.isOpen()) {
                                newF.open(Folder.READ_ONLY);
                            }
                            newF.idle();
                        } catch (MessagingException e1) {
                            e1.printStackTrace();
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void messagesRemoved(MessageCountEvent e) {
                        super.messagesRemoved(e);
                        Message[] messages = e.getMessages();
                        Log.e(TAG, "messagesAdded: " + messages.length);
                    }
                });
            } else {
                // TODO 后台服务间隔获取邮件
            }
        }
    }
}
