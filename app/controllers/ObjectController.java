package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This controller contains an action that demonstrates how to write simple
 * asynchronous code in a controller without blocking for HTTP requests.
 */

public class ObjectController extends Controller {

    private HashMap<String, Object> persistentObjects = new HashMap<>();

    //Helper Methods
    public ArrayList<String> bodyToList(String body) {
        body = body.replace("[","").replace("]","");
        ArrayList<String> result = new ArrayList<String>(Arrays.asList(body.split(",")));
        System.out.println(result);
        return result;
    }

    public Object getValue(String key) {
        Object result = persistentObjects.get(key);
        if(result==null) {
            return new Object();
        }
        return result;
    }

    public void setValue(String key, ArrayList<String> value) {
        persistentObjects.put(key, value);
    }

    //REST Based Methods
    public Result getList(String path) {
        return ok(getValue(path).toString());
    }

    public Result putList(String path) {
        String body = request().body().asText();
        setValue(path, bodyToList(body));
        return ok(getValue(path).toString());
    }

    public Result deleteList(String path) {
        persistentObjects.put(path, new ArrayList<String>());
        return ok();
    }

    public Result postValue(String path) {
        String item = request().body().asText();
        Object list = getValue(path);
        return ok(getValue(path).toString());
    }

    //psuedo post with get
    public Result addItem(String path, String value) {
        Object list = getValue(path);
        return ok(getValue(path).toString());
    }

    public Result dumpHash() {
        return ok(persistentObjects.toString());
    }
}
