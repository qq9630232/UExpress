package com.wty.app.uexpress.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;


public class SPUtil {
    public static final String FILE_NAME="elita_data";
    public static String PREFERENCE_NAME = "elita_data";
    public static void putStringSet(Context context, String key, HashSet<String> object){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, new HashSet()).commit();//先
        editor.putStringSet(key, object).apply();
    }
    public static HashSet getStringSet(Context context, String key, HashSet<String> object){
        SharedPreferences sp=context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
       return (HashSet) sp.getStringSet(key,object);
    }


//    public static void put(Context context,String key,Object object){
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//
//        if (object instanceof String)
//        {
//            editor.putString(key, (String) object);
//        } else if (object instanceof Integer)
//        {
//            editor.putInt(key, (Integer) object);
//        } else if (object instanceof Boolean)
//        {
//            editor.putBoolean(key, (Boolean) object);
//        } else if (object instanceof Float)
//        {
//            editor.putFloat(key, (Float) object);
//        } else if (object instanceof Long)
//        {
//            editor.putLong(key, (Long) object);
//        } else
//        {
//            editor.putString(key, object.toString());
//        }
//
//        SharedPreferencesCompat.apply(editor);
//    }
//
//    /**
//     * 得到保存的数据，根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
//     * @param context
//     * @param key
//     * @param defalultObject
//     * @return
//     */
//    public static  Object get(Context context,String key,Object defalultObject){
//        SharedPreferences sp=context.getSharedPreferences(key,Context.MODE_PRIVATE);
//        if (defalultObject instanceof String){
//            return sp.getString(key,(String) defalultObject);
//        }else if (defalultObject instanceof Integer){
//            return sp.getInt(key,(Integer) defalultObject);
//        } else if (defalultObject instanceof Boolean){
//            return sp.getBoolean(key,(Boolean) defalultObject);
//        }else if (defalultObject instanceof Float){
//            return sp.getFloat(key,(Float) defalultObject);
//        }else if (defalultObject instanceof Long){
//            return sp.getLong(key,(Long) defalultObject);
//        }
//        return null;
//    }
//    /**
//     * 移除某个key值已经对应的值
//     * @param context
//     * @param key
//     */
//    public static void remove(Context context,String key){
//        SharedPreferences sp=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sp.edit();
//        editor.remove(key);
//        SharedPreferencesCompat.apply(editor);
//
//    }
//
//    /**
//     * 清楚数据
//     * @param context
//     */
//    public  static void clear(Context context){
//        SharedPreferences sp=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sp.edit();
//        editor.clear();
//    }
//
//    /**
//     * 查询某个key是否存在
//     * @param context
//     * @param key
//     * @return
//     */
//    public static boolean contains(Context context, String key){
//        SharedPreferences sp=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
//        return sp.contains(key);
//    }
//
//    /**
//     * 返回所有的键值对
//     *
//     * @param context
//     * @return
//     */
//    public static Map<String,?> getAll(Context context){
//        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
//                Context.MODE_PRIVATE);
//        return sp.getAll();
//    }
//
//    private static class SharedPreferencesCompat{
//        private static final Method sApplyMethod=findApplyMethod();
//        /**
//         * 反射查找apply的方法
//         *
//         * @return
//         */
//        private  static Method findApplyMethod(){
//            try {
//            Class clz=SharedPreferences.Editor.class;
//                return clz.getMethod("apply");
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        public static void apply(SharedPreferences.Editor editor){
//                try {
//                  if (sApplyMethod!=null){
//                    sApplyMethod.invoke(editor);
//                      return;
//                  }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//          editor.commit();
//
//        }
//    }
//


    /**
     * put boolean preferences
     *
     * @param context
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * get boolean preferences
     *
     * @param context
     * @param key     The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a long
     * @see #getBoolean(Context, String, boolean)
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * get boolean preferences
     *
     * @param context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a long
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * put int preferences
     *
     * @param context
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);

        return editor.commit();
    }

    /**
     * get int preferences
     *
     * @param context
     * @param key     The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a long
     * @see #getInt(Context, String, int)
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * get long preferences
     *
     * @param context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a long
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }


    /**
     * put long preferences
     *
     * @param context
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);

        return editor.commit();
    }

    /**
     * get long preferences
     *
     * @param context
     * @param key     The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a long
     * @see #getLong(Context, String, long)
     */
    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    /**
     * get long preferences
     *
     * @param context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a long
     */
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    /**
     * put String preferences
     *
     * @param context
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * get String preferences
     *
     * @param context
     * @param key     The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a long
     * @see #getString(Context, String)
     */
    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    /**
     * get String preferences
     *
     * @param context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a long
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (settings == null) {
            return defaultValue;
        }
        return settings.getString(key, defaultValue);
    }


    /**
     * put String list preferences
     *
     * @param context
     * @param key     The name of the preference to modify
     * @param value   The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putStringList(Context context, String key, Set<String> value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    /**
     * get String preferences
     *
     * @param context
     * @param key     The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a long
     * @see #getString(Context, String)
     */
    public static Set<String> getStringList(Context context, String key) {
        return getStringList(context, key, null);
    }

    /**
     * get String preferences
     *
     * @param context
     * @param key          The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a long
     */
    public static Set<String> getStringList(Context context, String key, Set<String> defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getStringSet(key, defaultValue);
    }


    /**
     * remove obj in preferences
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean removeSharedPreferenceByKey(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean hasSharedPreferenceKey(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.contains(key);
    }
    public static void clearSharedPreference(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }
}
