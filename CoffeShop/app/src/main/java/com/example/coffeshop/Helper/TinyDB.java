package com.example.coffeshop.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.coffeshop.Domain.ItemsModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Safe TinyDB implementation (updated for Android 10+)
 */
public class TinyDB {

    private final SharedPreferences preferences;
    private final Context context;
    private String lastImagePath = "";

    public TinyDB(Context appContext) {
        this.context = appContext.getApplicationContext();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // ====================== IMAGE HANDLING ======================

    public Bitmap getImage(String path) {
        try {
            return BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSavedImagePath() {
        return lastImagePath;
    }

    public String putImage(String folderName, String imageName, Bitmap bitmap) {
        if (folderName == null || imageName == null || bitmap == null) return null;

        File folder = new File(context.getExternalFilesDir(null), folderName);
        if (!folder.exists() && !folder.mkdirs()) {
            Log.e("TinyDB", "Failed to create folder: " + folder.getAbsolutePath());
            return null;
        }

        File imageFile = new File(folder, imageName);
        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            lastImagePath = imageFile.getAbsolutePath();
            return lastImagePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteImage(String path) {
        if (path == null) return false;
        File file = new File(path);
        return file.exists() && file.delete();
    }

    // ====================== PRIMITIVE GETTERS ======================

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0L);
    }

    public float getFloat(String key) {
        return preferences.getFloat(key, 0f);
    }

    public double getDouble(String key) {
        try {
            return Double.parseDouble(preferences.getString(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    // ====================== LIST GETTERS ======================

    public ArrayList<String> getListString(String key) {
        String saved = preferences.getString(key, "");
        if (saved.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(TextUtils.split(saved, "‚‗‚")));
    }

    public ArrayList<Integer> getListInt(String key) {
        ArrayList<Integer> list = new ArrayList<>();
        for (String s : getListString(key)) {
            try {
                list.add(Integer.parseInt(s));
            } catch (NumberFormatException ignored) {}
        }
        return list;
    }

    public ArrayList<Double> getListDouble(String key) {
        ArrayList<Double> list = new ArrayList<>();
        for (String s : getListString(key)) {
            try {
                list.add(Double.parseDouble(s));
            } catch (NumberFormatException ignored) {}
        }
        return list;
    }

    public ArrayList<Long> getListLong(String key) {
        ArrayList<Long> list = new ArrayList<>();
        for (String s : getListString(key)) {
            try {
                list.add(Long.parseLong(s));
            } catch (NumberFormatException ignored) {}
        }
        return list;
    }

    public ArrayList<Boolean> getListBoolean(String key) {
        ArrayList<Boolean> list = new ArrayList<>();
        for (String s : getListString(key)) {
            list.add(s.equals("true"));
        }
        return list;
    }

    public ArrayList<ItemsModel> getListObject(String key) {
        Gson gson = new Gson();
        ArrayList<ItemsModel> list = new ArrayList<>();
        for (String json : getListString(key)) {
            try {
                list.add(gson.fromJson(json, ItemsModel.class));
            } catch (Exception ignored) {}
        }
        return list;
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String json = getString(key);
        if (json.isEmpty()) return null;
        try {
            return new Gson().fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    // ====================== PUT METHODS ======================

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public void putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    public void putDouble(String key, double value) {
        preferences.edit().putString(key, String.valueOf(value)).apply();
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value == null ? "" : value).apply();
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public void putListString(String key, ArrayList<String> list) {
        preferences.edit().putString(key, TextUtils.join("‚‗‚", list)).apply();
    }

    public void putListInt(String key, ArrayList<Integer> list) {
        ArrayList<String> strList = new ArrayList<>();
        for (Integer i : list) strList.add(String.valueOf(i));
        putListString(key, strList);
    }

    public void putListDouble(String key, ArrayList<Double> list) {
        ArrayList<String> strList = new ArrayList<>();
        for (Double d : list) strList.add(String.valueOf(d));
        putListString(key, strList);
    }

    public void putListLong(String key, ArrayList<Long> list) {
        ArrayList<String> strList = new ArrayList<>();
        for (Long l : list) strList.add(String.valueOf(l));
        putListString(key, strList);
    }

    public void putListBoolean(String key, ArrayList<Boolean> list) {
        ArrayList<String> strList = new ArrayList<>();
        for (Boolean b : list) strList.add(b ? "true" : "false");
        putListString(key, strList);
    }

    public void putObject(String key, Object obj) {
        String json = new Gson().toJson(obj);
        putString(key, json);
    }

    public void putListObject(String key, ArrayList<ItemsModel> list) {
        Gson gson = new Gson();
        ArrayList<String> jsonList = new ArrayList<>();
        for (ItemsModel item : list) jsonList.add(gson.toJson(item));
        putListString(key, jsonList);
    }

    // ====================== PREF MANAGEMENT ======================

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
