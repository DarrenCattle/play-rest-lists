package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This controller contains an action that demonstrates how to write simple
 * asynchronous code in a controller without blocking for HTTP requests.
 */

public class ListController extends Controller {

    private HashMap<String, Object> persistentObjects;
    private HashMap<String, ArrayList<String>> persistentLists = new HashMap<String, ArrayList<String>>();

    public ArrayList<String> getValue(String key) {
        ArrayList<String> result = persistentLists.get(key);
        if(result==null) {
            return new ArrayList<String>();
        }
        return result;
    }

    public void setValue(String key, ArrayList<String> value) {
        persistentLists.put(key, value);
    }

    //REST Based Methods
    public Result getList(String path) {
        return ok(getValue(path).toString());
    }

    public Result putList(String path) {
        String body = request().body().asText();
        return ok(body);
    }

    public Result deleteList(String path) {
        persistentLists.put(path, new ArrayList<String>());
        return ok();
    }

    public Result postValue(String path) {
        String item = request().body().asText();
        ArrayList<String> list = getValue(path);
        list.add(item);
        setValue(path, list);
        return ok(getValue(path).toString());
    }

    //psuedo post with get
    public Result addItem(String path, String value) {
        ArrayList<String> list = getValue(path);
        list.add(value);
        setValue(path, list);
        return ok(getValue(path).toString());
    }

    public Result dumpHash() {
        return ok(persistentLists.toString());
    }
}
