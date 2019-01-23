
package com.criticalblue.reactnative;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.network.OkHttpClientProvider;

import java.lang.reflect.Method;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;

public class CertPinnerModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;
    private static final String TAG = "CertPinnerPackage";
    private CertificatePinner certificatePinner;

    // Default SSL Certificate pinning to false.
    private boolean enableCertPinning = false;


    public CertPinnerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        // create custom certificate pinner.
        // needs to use reflection so that class can be generated
        // outside the package library
        try {
            Class noparams[] = {};
            Class clazz = Class.forName("com.criticalblue.reactnative.GeneratedCertificatePinner");
            Method method = clazz.getDeclaredMethod("instance", noparams);
            certificatePinner = (CertificatePinner) method.invoke(null);
            Log.i(TAG, "Generated Certficate Pinner in use");
        } catch (Exception e) {
            Log.e(TAG, "No Generated Certficate Pinner found - likely a pinset configuration error");
            Log.w(TAG, "CERTIFICATE PINNING NOT BEING USED");
            if (certificatePinner == null) {
                certificatePinner = CertificatePinner.DEFAULT;
            }
        }

//        OkHttpClientProvider.setOkHttpClientFactory(new PinnedClientFactory(certificatePinner));
        this.enableCertPinning(false);
    }

    @ReactMethod
    public void enableCertPinning(boolean enableCertPinning) {
        this.enableCertPinning = enableCertPinning;

        this.resetCertificatePinner();
    }

    private OkHttpClient resetCertificatePinner() {
        CertificatePinner pinner = this.enableCertPinning ? certificatePinner : CertificatePinner.DEFAULT;
        OkHttpClient client = new PinnedClientFactory(pinner).createNewNetworkModuleClient();
        OkHttpClientProvider.replaceOkHttpClient(client);
        return client;
    }

    @Override
    public String getName() {
        return "CertPinner";
    }
}