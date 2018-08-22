package org.paykey.keyboard.sample.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;

import org.paykey.client.fingerprint.FingerprintServiceHandler;

/**
 * Created by alexkogan on 10/11/2017.
 */

public class DemoFingerprintServiceHandler extends FingerprintServiceHandler {
    private android.os.CancellationSignal mCancellationSignal;
    private Context mContext;

    public DemoFingerprintServiceHandler(Context context) {
        mContext = context;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void start(final FingerprintServiceHandler.Delegate delegate) {
        if (!isFingerprintAuthAvailable(mContext)) {
            return;
        }

        FingerprintManager.CryptoObject cryptoObject = new CypherObjectHelper().getCryptoObject();
        mCancellationSignal = new CancellationSignal();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager manager = (FingerprintManager) mContext.getSystemService(Context.FINGERPRINT_SERVICE);
            //noinspection MissingPermission
            manager.authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    delegate.onAuthenticationError(errString);
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    delegate.onAuthenticationHelp(helpString);
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    delegate.onAuthenticationSucceeded();
                }

                @Override
                public void onAuthenticationFailed() {
                    delegate.onAuthenticationFailed();
                }
            }, null);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void stop() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
        }
        mCancellationSignal = null;
    }


    public static boolean isFingerprintAuthAvailable(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        FingerprintManager manager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return manager != null && manager.isHardwareDetected() && manager.hasEnrolledFingerprints();
    }
}
