package org.paykey.keyboard.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.scb.mb.tw.R;
import com.scb.mb.tw.SampleApp;
import org.paykey.PayKey;
import org.paykey.keyboard.sample.callStrategy.APIResult;
import org.paykey.keyboard.sample.util.KeyboardUtil;
import org.paykey.util.DebouncingOnClickListener;
import org.paykey.util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PayKeyActivity extends AppCompatActivity {

    private static final int KEYBOARD_SETUP_REQUEST_CODE = 100;
    private BaseAdapter adapter;
    private int mRowCount = 0;
    private int mSetupWizardRow;
    private int mKeyboardSettingsRow;
    private int mPayKeySettingsHeader;
    private int mPayKeyShownRow;
    private int mFlowManagerHeader;

    private Map<Integer, String> indexToApiCallMap = new HashMap<>();
    private Settings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSettings = ((SampleApp)getApplicationContext()).getSettings();
        final EditText editText = (EditText) findViewById(R.id.editText);
        final ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mRowCount;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position) {
                if (position == mFlowManagerHeader ||
                        position == mPayKeySettingsHeader)
                    return 0;
                return 1;
            }

            @Override
            public boolean isEnabled(int position) {
                return !(position == mFlowManagerHeader ||
                        position == mPayKeySettingsHeader) && super.isEnabled(position);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView != null) {
                    viewHolder = (ViewHolder) convertView.getTag();
                } else {
                    int id = getItemViewType(position) == 0 ? R.layout.list_header : R.layout.list_row;
                    convertView = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                }

                if (position == mKeyboardSettingsRow) {
                    viewHolder.title.setText("Keyboard Settings");
                    viewHolder.subtitle.setText("");
                } else if (position == mPayKeySettingsHeader) {
                    viewHolder.title.setText("Payment Settings");
                } else if (position == mFlowManagerHeader) {
                    viewHolder.title.setText("Flow Manager Operations");
                } else if (position == mSetupWizardRow) {
                    viewHolder.title.setText("Run PayKeyImpl Setup");
                    String subtitle;
                    if (PayKey.isInputMethodCurrent(getApplicationContext())) {
                        subtitle = "The keyboard is set as default";
                    } else if (PayKey.isInputMethodEnabled(getApplicationContext())) {
                        subtitle = "The keyboard is enabled but isn't default";
                    } else {
                        subtitle = "Tap To Setup Keyboard";
                    }
                    viewHolder.subtitle.setText(subtitle);
                } else if (position == mPayKeyShownRow) {
                    viewHolder.title.setText("Pay Key Visible");
                    viewHolder.subtitle.setText(String.valueOf(mSettings.shouldShowPayKeyButton()));
                } else if (indexToApiCallMap.containsKey(position)) {
                    String apiCallName  = indexToApiCallMap.get(position);
                    viewHolder.title.setText(apiCallName);
                    viewHolder.subtitle.setText(mSettings.getState(apiCallName).name());
                }
                return convertView;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mSetupWizardRow) {
                    startActivityForResult(MyKeyboardSetupActivity.newIntent(PayKeyActivity.this), KEYBOARD_SETUP_REQUEST_CODE);
                } else if (position == mPayKeyShownRow) {
                    KeyboardUtil.hideKeyboard(editText);
                    mSettings.setShowPayKeyButton(!mSettings.shouldShowPayKeyButton());
                    adapter.notifyDataSetChanged();
                } else if (position == mKeyboardSettingsRow) {
                    startActivity(PayKey.createSettingsActivityIntent(PayKeyActivity.this));
                } else if (indexToApiCallMap.containsKey(position)) {
                    final String apiCallName  = indexToApiCallMap.get(position);
                    final List<APIResult> options = mSettings.getCallOptionsMap().get(apiCallName);
                    int index = options.indexOf(mSettings.getState(apiCallName));

                    CharSequence[] stringOptions = new CharSequence[options.size()];
                    int count = 0;
                    for (APIResult state : options) {
                        stringOptions[count] = state.name();
                        count++;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(PayKeyActivity.this);
                    builder.setSingleChoiceItems(stringOptions, index, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            KeyboardUtil.hideKeyboard(editText);
                            dialog.dismiss();
                            APIResult state = options.get(which);
                            mSettings.setState(apiCallName, state);
                            adapter.notifyDataSetChanged();
                        }
                    }).show();

                }
            }
        });


        findViewById(R.id.clearButton).setOnClickListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateIndex();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.info("SampleApp", "isKeyboardSetupFinished '%s'", PayKey.isKeyboardSetupFinished(getApplicationContext()));
        Logger.info("SampleApp", "isInputMethodEnabled '%s'", PayKey.isInputMethodEnabled(getApplicationContext()));
        if (requestCode == KEYBOARD_SETUP_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                Logger.debug("SampleApp", "Woohoo the keyboard interact is done!");
            else
                Logger.debug("SampleApp", "keyboard interact was canceled!");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    void updateIndex() {
        mRowCount = 0;
        mSetupWizardRow = mRowCount++;
        mKeyboardSettingsRow = mRowCount++;
        mPayKeySettingsHeader = mRowCount++;
        mPayKeyShownRow = mRowCount++;
        mFlowManagerHeader = mRowCount++;

        Map<String, List<APIResult>> map = mSettings.getCallOptionsMap();

        for (String key : map.keySet()) {
            int index = mRowCount++;
            indexToApiCallMap.put(index, key);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

       final TextView title;
       final TextView subtitle;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        }
    }
}
