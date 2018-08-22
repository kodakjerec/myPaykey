package org.paykey.keyboard.sample.callStrategy;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by alexkogan on 09/11/2017.
 */

public abstract class ApiResultCallStrategy {

    public abstract Map<String, List<APIResult>> createCallOptionsMap(Context context);

    public List<APIResult> createStates(List<String> list) {
        List<APIResult> result = new ArrayList<>();
        for (String s : list) {
        result.add(APIResult.create(s));
        }
        return result;
    }



}
