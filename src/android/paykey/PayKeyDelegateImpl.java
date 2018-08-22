package org.paykey.keyboard.sample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.scb.mb.tw.SampleApp;
import org.paykey.client.api.LoginViewDelegate;
import org.paykey.client.api.PayeeValidationRequest;
import org.paykey.client.api.SCPayKeyDelegate;
import org.paykey.client.api.SCPaymentRequest;
import org.paykey.keyboard.sample.callStrategy.APIResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.paykey.keyboard.sample.util.Utils.sleep;

public class PayKeyDelegateImpl extends SCPayKeyDelegate {


    private final Settings settings;

    public PayKeyDelegateImpl(Context context) {
        super(context);
        this.settings = ((SampleApp) context.getApplicationContext()).getSettings();
    }

    @Override
    public void login(LoginViewDelegate delegate, SCCompletion completion) {
        APIResult state = settings.getState("login");

        delegate.showProgressView();

        sleep(500);

        if (!state.isSuccess()) {
            switch (state.name()) {
                case "101":
                case "102":
                case "103":
                case "104":
                case "105": {
                    completion.onCompletion(null, new ErrorObjRtn(Integer.parseInt(state.name()), "message"));
                    return;
                }
            }
        }

        delegate.showLocalAuthenticatorView();

        sleep(500);


        if (!state.isSuccess()) {
            completion.onCompletion(null, new ErrorObjRtn(Integer.parseInt(state.name()), "message"));
        } else {
            completion.onCompletion(new SuccessObjRtn(200, null), null);
        }
    }

    @Override
    public void launchApplication() {
        context.startActivity(new Intent(context, PayKeyActivity.class));
    }

    @Override
    public void getContacts(SCCompletion completion) {
        sleep(1000);
        APIResult auth = settings.getState("getContacts");
        switch (auth.name()) {
            case "SUCCESS": {
                Map<String, Object> data = new HashMap<String, Object>();
                Map<String, String> contact1 = new HashMap<>();
                contact1.put("name", "Kevin Robertson");
                contact1.put("phoneEmail", "4564564");
                Map<String, String> contact2 = new HashMap<>();
                contact2.put("name", "Keith Hill");
                contact2.put("phoneEmail", "2169416490");
                Map<String, String> contact3 = new HashMap<>();
                contact3.put("name", "George Bible");
                contact3.put("phoneEmail", "8889977445");
                Map<String, String> contact4 = new HashMap<>();
                contact4.put("name", "Larry V. Meyers");
                contact4.put("phoneEmail", "larryvmeyers@dayrep.com");
                Map<String, String> contact5 = new HashMap<>();
                contact5.put("name", "Lois G. Green");
                contact5.put("phoneEmail", "+610 965 7179");
                Map<String, String> contact6 = new HashMap<>();
                contact6.put("name", "Donnie O. Knotts");
                contact6.put("phoneEmail", "+434 476 1702");
                Map<String, String> contact7 = new HashMap<>();
                contact7.put("name", "Olive Johnson");
                contact7.put("phoneEmail", "olive@mail.com");
                List<Map<String, String>> contacts = new LinkedList<>();
                contacts.add(contact1);
                contacts.add(contact2);
                contacts.add(contact3);
                contacts.add(contact4);
                contacts.add(contact5);
                contacts.add(contact6);
                contacts.add(contact7);

                data.put("contacts", contacts);
                SuccessObjRtn successObjRtn = new SuccessObjRtn(200, data);
                completion.onCompletion(successObjRtn, null);
                break;
            }
            case "NO_PERMISSION": {
                Map<String, Object> data = new HashMap<>();
                List<Map<String, String>> contacts = new LinkedList<>();
                data.put("contacts", contacts);
                SuccessObjRtn successObjRtn = new SuccessObjRtn(200, data);
                completion.onCompletion(successObjRtn, null);
            }

            case "FAILURE": {
                completion.onCompletion(null, new ErrorObjRtn(999, "error message"));


                // 預計開啟SC Mobile
                final Context myContext = this.context;

                // 新增主程式, 並且在後台
                Intent newTask = new Intent();
                newTask.setComponent(new ComponentName("com.scb.mb.tw","com.scb.mb.tw.MainActivity") );
                newTask.putExtra( "cdvStartInBackground", true );
                newTask.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                newTask.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myContext.startActivity(newTask);

                break;
            }
        }
    }

    @Override
    public void getBankList(SCCompletion completion) {
        sleep(1000);
        APIResult auth = settings.getState("getBankList");
        switch (auth.name()) {
            case "SUCCESS": {
                Map<String, Object> data = new HashMap<String, Object>();
                Map<String, String> bank1 = new HashMap<>();
                bank1.put("name", "ABCD Bank");
                bank1.put("code", "12345");
                Map<String, String> bank2 = new HashMap<>();
                bank2.put("name", "DBS Bank");
                bank2.put("code", "88888");
                Map<String, String> bank3 = new HashMap<>();
                bank3.put("name", "Dorta Bank");
                bank3.put("code", "554466");
                List<Map<String, String>> banks = new LinkedList<>();
                banks.add(bank1);
                banks.add(bank2);
                banks.add(bank3);
                data.put("banks", banks);
                SuccessObjRtn successObjRtn = new SuccessObjRtn(200, data);
                completion.onCompletion(successObjRtn, null);
                break;
            }

            case "112":
            case "400":
            case "999":{
                completion.onCompletion(null, new ErrorObjRtn(Integer.parseInt(auth.name()), "message"));
                break;
            }
        }
    }

    @Override
    public void payeeLookup(PayeeValidationRequest request, SCCompletion completion) {
        sleep(1000);
        APIResult auth = settings.getState("payeeLookup");
        switch (auth.name()) {
            case "SUCCESS": {
                Map<String, Object> data = new HashMap<String, Object>();
                List<String> transferCurrencySupported = new LinkedList<>();
                transferCurrencySupported.add("HKD");
                transferCurrencySupported.add("CNY");
                data.put("transferCurrencySupported", transferCurrencySupported);
                data.put("totalDailyLimitAmount", "5000.0");
                data.put("availableDailyLimitAmount", "1000.0");
                data.put("currencyCode", "HKD");
                SuccessObjRtn successObjRtn = new SuccessObjRtn(200, data);
                completion.onCompletion(successObjRtn, null);
                break;
            }
            case "SINGLE CURRENCY HKD": {
                Map<String, Object> data = new HashMap<String, Object>();
                List<String> transferCurrencySupported = new LinkedList<>();
                transferCurrencySupported.add("HKD");
                data.put("transferCurrencySupported", transferCurrencySupported);
                data.put("totalDailyLimitAmount", "5000.0");
                data.put("availableDailyLimitAmount", "1000.0");
                data.put("currencyCode", "HKD");
                SuccessObjRtn successObjRtn = new SuccessObjRtn(200, data);
                completion.onCompletion(successObjRtn, null);
                break;
            }
            case "SINGLE CURRENCY CNY": {
                Map<String, Object> data = new HashMap<String, Object>();
                List<String> transferCurrencySupported = new LinkedList<>();
                transferCurrencySupported.add("CNY");
                data.put("transferCurrencySupported", transferCurrencySupported);
                data.put("totalDailyLimitAmount", "5000.0");
                data.put("availableDailyLimitAmount", "1000.0");
                data.put("currencyCode", "HKD");
                SuccessObjRtn successObjRtn = new SuccessObjRtn(200, data);
                completion.onCompletion(successObjRtn, null);
                break;
            }
            case "ZERO AVAILABLE": {
                Map<String, Object> data = new HashMap<String, Object>();
                List<String> transferCurrencySupported = new LinkedList<>();
                transferCurrencySupported.add("CNY");
                transferCurrencySupported.add("HKD");
                data.put("transferCurrencySupported", transferCurrencySupported);
                data.put("totalDailyLimitAmount", "5000.0");
                data.put("availableDailyLimitAmount", "0.0");
                data.put("currencyCode", "HKD");
                SuccessObjRtn successObjRtn = new SuccessObjRtn(200, data);
                completion.onCompletion(successObjRtn, null);
                break;
            }
            case "113":
            case "114":
            case "115":
            case "400":
            case "999":{
                completion.onCompletion(null, new ErrorObjRtn(Integer.parseInt(auth.name()), "message"));
                break;
            }
        }
    }

    @Override
    public void sendMoney(SCPaymentRequest request, SCCompletion completion) {
        sleep(1000);
        APIResult auth = settings.getState("sendMoney");
        switch (auth.name()) {
            case "SUCCESS": {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("outputText", "Hi, I have sent you HKD12.00 via SC Pay");
                SuccessObjRtn successObjRtn = new SuccessObjRtn(200, data);
                completion.onCompletion(successObjRtn, null);
                break;
            }

            case "116":
            case "117":
            case "118":
            case "119":
            case "400":
            case "999":{
                completion.onCompletion(null, new ErrorObjRtn(Integer.parseInt(auth.name()), "message"));
                break;
            }
        }
    }

    @Override
    public void onFlowStop(Context context) {

    }

    @Override
    public void onFlowStart(Context context) {
        super.onFlowStart( context );
    }
}
