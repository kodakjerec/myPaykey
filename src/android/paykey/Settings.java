package org.paykey.keyboard.sample;

import android.content.Context;

import org.paykey.PKSharedPreferences;
import org.paykey.PayKeySDK;
import org.paykey.keyboard.sample.callStrategy.APIResult;
import org.paykey.keyboard.sample.callStrategy.ApiResultCallStrategy;

import java.util.List;
import java.util.Map;

import static org.paykey.keyboard.sample.callStrategy.APIResult.STATE_SUCCESS;

/**
 * Created by alexkogan on 13/01/2016.
 */
public class Settings {

    private final PKSharedPreferences.MultiprocessSharedPreferences mSharedPreferences;
    private final Map<String, List<APIResult>> mCallOptionsMap;


    public Settings(Context context, ApiResultCallStrategy strategy) {
        mSharedPreferences = PKSharedPreferences.getDefaultSharedPreferences(context);
        mCallOptionsMap = strategy.createCallOptionsMap(context);

        for (String key : mCallOptionsMap.keySet()) {
            List<APIResult> s = mCallOptionsMap.get(key);
            writeApiStateValue(key, s.get(0).name());
        }
    }

    public Map<String, List<APIResult>> getCallOptionsMap() {
        return mCallOptionsMap;
    }

    public APIResult getState(String name) {
        return APIResult.create(readApiStateValue(name));
    }

    public void setState(String name, APIResult value) {
        writeApiStateValue(name, value.name());
    }

    private String readApiStateValue(String key) {
        return mSharedPreferences.getString("api_"+key, STATE_SUCCESS.name());
    }

    private void writeApiStateValue(String key, String value) {
        mSharedPreferences.edit().putString("api_"+key, value).apply();
    }

    public boolean shouldShowPayKeyButton() {
        return PayKeySDK.getPayKeySettings().showPayKeyButton();
    }

    public void setShowPayKeyButton(boolean showPayKeyButton) {
        PayKeySDK.getPayKeySettings().setShowPayKeyButton(showPayKeyButton);
    }
}
