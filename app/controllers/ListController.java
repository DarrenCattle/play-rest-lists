package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * This controller contains an action that demonstrates how to write simple
 * asynchronous code in a controller without blocking for HTTP requests.
 */

public class ListController extends Controller {

    private static HashMap<String, ArrayList<String>> persistentLists = new HashMap<String, ArrayList<String>>();
    static {
        try {
            persistentLists = load();
        }
        catch(IOException e) {
            System.out.println("exception: " + e);
        }
    }

    //Helper Methods
    public static ArrayList<String> getValue(String key) {
        ArrayList<String> result = persistentLists.get(key);
        if(result==null) {
            return new ArrayList<String>();
        }
        return result;
    }

    public static void setValue(String key, ArrayList<String> value) {
        persistentLists.put(key, value);
    }

    public static ArrayList<String> bodyToList(String body) {
        body = body.replace("[","").replace("]","");
        ArrayList<String> result = new ArrayList<String>(Arrays.asList(body.split(",")));
        return result;
    }

    public static String decode(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, "UTF-8");
    }

    public static HashMap<String, ArrayList<String>> load() throws IOException {
        HashMap<String, ArrayList<String>> hash = new HashMap<String, ArrayList<String>>();
        Properties properties = new Properties();
        properties.load(new FileInputStream("lists.hash"));
        for (String key : properties.stringPropertyNames()) {
            ArrayList<String> value = bodyToList(properties.get(key).toString());
            hash.put(key, value);
        }
        return hash;
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
    public Result addItem(String path, String value) throws UnsupportedEncodingException {
        ArrayList<String> list = getValue(path);
        list.add(decode(value));
        setValue(path, list);
        return ok(getValue(path).toString());
    }

    public Result putItem(String path, String value) throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<String>();
        list.add(decode(value));
        setValue(path, list);
        return ok(getValue(path).toString());
    }

    public Result dumpHash() {
        String result = persistentLists.toString().replace("],", "], \n");
        return ok(result);
    }

    //data store manipulation
    public Result loadHash() throws IOException {
        HashMap<String, ArrayList<String>> result = load();
        persistentLists = result;
        return ok(persistentLists.toString());
    }

    public Result saveHash() throws IOException {
        //save to file
        Properties properties = new Properties();
        for(Map.Entry<String, ArrayList<String>> entry : persistentLists.entrySet()) {
            properties.put(entry.getKey(), entry.getValue().toString());
        }
        properties.store(new FileOutputStream("lists.hash"), null);
        return ok(properties.toString());
    }
}