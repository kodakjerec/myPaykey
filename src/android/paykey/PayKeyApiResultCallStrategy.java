package org.paykey.keyboard.sample;

import android.content.Context;

import org.paykey.client.api.AuthData;
import org.paykey.keyboard.sample.callStrategy.APIResult;
import org.paykey.keyboard.sample.callStrategy.ApiResultCallStrategy;
import org.paykey.keyboard.sample.util.DemoFingerprintServiceHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.paykey.keyboard.sample.callStrategy.APIResult.STATE_FAILURE;
import static org.paykey.keyboard.sample.callStrategy.APIResult.STATE_SUCCESS;

/**
 * Created by alexkogan on 09/11/2017.
 */

public class PayKeyApiResultCallStrategy extends ApiResultCallStrategy {

    public Map<String, List<APIResult>> createCallOptionsMap(Context context) {
        List<APIResult> stateList = Arrays.asList(STATE_SUCCESS,
                STATE_FAILURE);

        List<APIResult> fetchContacts = createStates(Arrays.asList(
                "SUCCESS",
                "NO_PERMISSION",
                "FAILURE"
        ));

        List<APIResult> loginState = createStates(Arrays.asList(
                "SUCCESS",
                "100",
                "101",
                "102",
                "103",
                "104",
                "105",
                "106",
                "107",
                "108",
                "109",
                "400",
                "999"
        ));

        List<APIResult> fetchBankListState = createStates(Arrays.asList(
                "SUCCESS",
                "112",
                "400",
                "999"
        ));

        List<APIResult> payeeLookup = createStates(Arrays.asList(
                "SUCCESS",
                "SINGLE CURRENCY HKD",
                "SINGLE CURRENCY CNY",
                "ZERO AVAILABLE",
                "113",
                "114",
                "115",
                "400",
                "999"
        ));

        List<APIResult> sendMoneyState = createStates(Arrays.asList(
                "SUCCESS",
                "116",
                "117",
                "118",
                "119",
                "400",
                "999"
        ));
        final List<String> list = new ArrayList<>(Arrays.asList(AuthData.Type.namesArray()));

        boolean hasFingerprint = DemoFingerprintServiceHandler.isFingerprintAuthAvailable(context);
        if (!hasFingerprint) {
            list.remove(AuthData.Type.FINGERPRINT.name());
        }

        Map<String, List<APIResult>> settingsMap = new LinkedHashMap<>();
        settingsMap.put("login", loginState);
        settingsMap.put("getContacts", fetchContacts);
        settingsMap.put("getBankList", fetchBankListState);
        settingsMap.put("payeeLookup", payeeLookup);
        settingsMap.put("sendMoney", sendMoneyState );
        return settingsMap;
    }

}
