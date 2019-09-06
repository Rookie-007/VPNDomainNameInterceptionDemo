package cc.aoeiuv020.vpnproxy.utils;

import android.app.AlertDialog;

public interface IDialogCallBack {

    void determine(AlertDialog dialog,String websiteUrl);

    void cancel(AlertDialog dialog);

}
