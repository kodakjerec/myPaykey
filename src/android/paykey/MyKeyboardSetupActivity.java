package org.paykey.keyboard.sample;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.scb.mb.tw.R;
import org.paykey.KeyboardSetupControl;
import org.paykey.util.DebouncingOnClickListener;

/**
 * Created by alexkogan on 28/02/2016.
 */
public class MyKeyboardSetupActivity extends AppCompatActivity implements KeyboardSetupControl.OnStateChangedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 123;
    private Handler mHandler = new Handler();
    private KeyboardSetupControl mKeyboardSetupControl;

    private TextView mEnableButton;
    private TextView mSelectButton;

    public static Intent newIntent(Context context) {
        return new Intent(context, MyKeyboardSetupActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_keyboard_setup);

        mEnableButton = (Button) findViewById(R.id.mybutton1);
        mSelectButton = (Button) findViewById(R.id.mybutton2);

        mEnableButton.setText("Enable Keyboard");
        mSelectButton.setText("Select Keyboard");

        final Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(MyKeyboardSetupActivity.this, MyKeyboardSetupActivity.class);

        mKeyboardSetupControl = new KeyboardSetupControl.Builder(MyKeyboardSetupActivity.this)
                .setSetupIntent(intent)
                .setOnStateChangedListener(MyKeyboardSetupActivity.this)
                .build();

        mEnableButton.setOnClickListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                if (!mKeyboardSetupControl.isImeEnabled()) {
                    mKeyboardSetupControl.openEnableImeSettings();
                    Toast.makeText(MyKeyboardSetupActivity.this, "Tap 'Sample App Keyboard'", Toast.LENGTH_SHORT).show();
                } else {
                    mKeyboardSetupControl.openDisableImeSettings();
                    Toast.makeText(MyKeyboardSetupActivity.this, "Tap 'Sample App Keyboard'", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSelectButton.setOnClickListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                mKeyboardSetupControl.openSelectImePicker();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mKeyboardSetupControl.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onStateChanged(@KeyboardSetupControl.SetupState int state) {
        switch (state) {
            case KeyboardSetupControl.STEP_ENABLE_KEYBOARD:
                mEnableButton.setBackgroundColor(Color.GREEN);
                mEnableButton.setTextColor(Color.WHITE);

                mSelectButton.setBackgroundColor(Color.GRAY);
                mSelectButton.setTextColor(Color.BLACK);
                break;

            case KeyboardSetupControl.STEP_SELECT_KEYBOARD:
                mEnableButton.setBackgroundColor(Color.WHITE);
                mEnableButton.setTextColor(Color.BLACK);

                mSelectButton.setBackgroundColor(Color.GREEN);
                mSelectButton.setTextColor(Color.WHITE);
                break;


            case KeyboardSetupControl.STEP_DONE:
                mEnableButton.setBackgroundColor(Color.WHITE);
                mEnableButton.setTextColor(Color.BLACK);

                mSelectButton.setBackgroundColor(Color.WHITE);
                mSelectButton.setTextColor(Color.BLACK);

                setResult(RESULT_OK);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 100);
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the onStore arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mKeyboardSetupControl.contactPermissionGranted();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_CONTACTS)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyKeyboardSetupActivity.this);
                        builder.setTitle("Setup Failed");
                        builder.setMessage("Contacts permission is essential for the payment keyboard.\nMy Bank App accesses your contacts locally and doesn't share it with third party applications.\nPlease reconsider.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyKeyboardSetupActivity.this);
                        builder.setTitle("Setup Failed");
                        builder.setMessage("The payment keyboard can't work without the Contacts permission.\nIf you change your mind please enable the permission manually and run interact again.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    }
                }
            }
        }
    }
}
