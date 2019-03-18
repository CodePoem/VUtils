package com.vdreamers.vutilsandroid;

import android.app.Application;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import androidx.annotation.IntDef;
import androidx.annotation.StringRes;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class ToastUtils {

    private static Toast toast = null;  // Global Toast
    private static final String TAG = "AppToast";
    private static WeakReference<Application> app;

    /**
     * AppToast initialization
     *
     * @param application Application <br>
     *                    This initialization is done in the onCreate method of the
     *                    corresponding Application class.<br>
     *                    <b>Do not forget to configure the android: name attribute under the
     *                    application node of the AndroidManifest file.</b>
     */
    public static void init(Application application) {
        app = new WeakReference<Application>(application);
    }

    /**
     * It is not allowed to instantiate this class.
     */
    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    @IntDef({LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    /**
     * Display Toast
     *
     * @param resId The resource id of the string resource to use.  Can be formatted text.
     */
    public static void showToast(@StringRes int resId) {
        clearToast();
        toast = Toast.makeText(app.get(), resId, LENGTH_SHORT);
        toast.show();
    }

    /**
     * Display Toast
     *
     * @param msg The text to show.  Can be formatted text.
     */
    public static void showToast(CharSequence msg) {
        clearToast();
        toast = Toast.makeText(app.get(), msg, LENGTH_SHORT);
        toast.show();
    }

    /**
     * Display Toast
     *
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message. Either
     *                 {@link  android.widget.Toast#LENGTH_SHORT} or
     *                 {@link android.widget.Toast#LENGTH_LONG}
     */
    public static void showToast(@StringRes int resId, @Duration int duration) {
        clearToast();
        toast = Toast.makeText(app.get(), resId, duration);
        toast.show();
    }

    /**
     * Display Toast
     *
     * @param msg      The text to show.  Can be formatted text.
     * @param duration How long to display the message. Either
     *                 {@link  android.widget.Toast#LENGTH_SHORT} or
     *                 {@link android.widget.Toast#LENGTH_LONG}
     */
    public static void showToast(CharSequence msg, @Duration int duration) {
        clearToast();
        toast = Toast.makeText(app.get(), msg, duration);
        toast.show();
    }

    /**
     * Get a Toast object <br>
     * Need to call show() method to be displayed
     *
     * @return Toast object.
     */
    public static Toast getToast() {
        clearToast();
        toast = Toast.makeText(app.get(), "", LENGTH_SHORT);
        return toast;
    }

    /**
     * Get a global Application
     *
     * @return global Application
     */
    @Deprecated
    public static Application getApplication() {
        return app.get();
    }

    /**
     * Clear an existing CusToast.
     */
    private static void clearToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}
