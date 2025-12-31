package com.yishu.net.ui;

import static android.content.Context.WINDOW_SERVICE;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.yishu.net.R;

public class LoadingDialog {
    private AlertDialog dialog;
    private Context context;
    private int width,height;

    public LoadingDialog(Context context) {
        this.context = context;

        View v = View.inflate(context, R.layout.dialog_download, null);
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setView(v);
        dialog = b.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (display.getHeight() > display.getWidth()) {
            //lp.height = (int) (display.getHeight() * 0.5);
            lp.width = (int) (display.getWidth() * 1.0);
        } else {
            //lp.height = (int) (display.getHeight() * 0.75);
            lp.width = (int) (display.getWidth() * 0.5);
        }
        lp.width=width;
        lp.height=height;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void dissmiss(){
        if(dialog!=null)
            dialog.dismiss();
    }
}
