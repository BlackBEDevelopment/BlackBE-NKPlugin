package xyz.blackbe.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class BlackBEData {
    protected static final Gson FOR_DISPLAY_GSON_INSTANCE = new GsonBuilder().setPrettyPrinting().create();

    public String toQueryResult() {
        return FOR_DISPLAY_GSON_INSTANCE.toJson(this);
    }
}
