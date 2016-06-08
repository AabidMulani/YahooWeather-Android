package in.abmulani.yahooweather.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import timber.log.Timber;

/**
 * Created by aabid on 6/8/16.
 */
public class Utils {

    public static void showThisMsg(Context activity, String message,
                                   DialogInterface.OnClickListener
                                           onOkClickListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(null);
            builder.setMessage(message);
            builder.setPositiveButton(activity.getString(android.R.string.ok), onOkClickListener);
            Dialog dialog = builder.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        } catch (NullPointerException | IllegalStateException e) {
            Timber.e(Log.getStackTraceString(e));
        }
    }


    public static void showSnackBar(Activity activity, String msg) {
        try {
            View parentView = activity.findViewById(android.R.id.content);
            Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG).show();
        } catch (Exception ex) {
            Timber.e(Log.getStackTraceString(ex));
        }
    }


}
