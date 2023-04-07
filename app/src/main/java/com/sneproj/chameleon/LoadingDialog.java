package com.sneproj.chameleon;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {
    ProgressDialog progressDialog;

    public void showdialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void dismissdialog() {
        progressDialog.dismiss();
    }
}
