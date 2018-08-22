package org.paykey.keyboard.sample.callStrategy;

/**
 * Created by alexkogan on 09/11/2017.
 */

public class APIResult {

    private String name;
    public static final APIResult STATE_SUCCESS = APIResult.create("SUCCESS");
    public static final APIResult STATE_FAILURE = APIResult.create("FAILURE");

    public static APIResult create(String name) {
        return new APIResult(name);
    }

    private APIResult(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public boolean is(String s) {
        return name.equals(s);
    }

    public boolean isSuccess() {
        return this.equals(STATE_SUCCESS);
    }

    public boolean isFailure() {
        return this.equals(STATE_FAILURE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        APIResult that = (APIResult) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
