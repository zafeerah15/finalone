package sG.EDU.NP.MAD.friendsOnly.messages;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.os.CancellationSignal;

import sG.EDU.NP.MAD.friendsOnly.R;

public class CustomProgressBar {

    private Dialog dialog;

    public Dialog show(Context context) {
        return show(context, null);
    }

    public Dialog show(Context context, CharSequence title) {
        return show(context, title, false);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable) {
        return show(context, title, cancelable, null);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable,
                       CancellationSignal.OnCancelListener cancelListener) {
        LayoutInflater inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflator.inflate(R.layout.loading_full, null);
//        if(title != null) {
//            final TextView tv = (TextView) view.findViewById(R.id.id_title);
//            tv.setText(title);
//        }

        dialog = new Dialog(context, R.style.NewDialog);
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener((DialogInterface.OnCancelListener) cancelListener);
        dialog.show();

        return dialog;
    }

    public Dialog getDialog() {
        return dialog;
    }

}
