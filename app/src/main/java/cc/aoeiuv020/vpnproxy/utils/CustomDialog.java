package cc.aoeiuv020.vpnproxy.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.concurrent.TimeUnit;

import cc.aoeiuv020.vpnproxy.R;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;


/**
 * 2018-6-22 17:27
 *
 * @author hxl
 */
public class CustomDialog {

    private static CustomDialog customDialog;
    private View baseView;
    private TextView tvTitle;
    private TextView tvMessage;
    private Button btnCancel;
    private Button btnOk;
    private TextView tvContinumConfrim;
    static AlertDialog.Builder builder;
    private Disposable disposabl;
    static AlertDialog dialog;
    private Dialog mLoadingDialog;
    private TextView loadingText;
    private WindowManager.LayoutParams mParams;
    private FloatingManager mWindowManager;
    private View mView;

    public static CustomDialog getInstance() {
        if (customDialog == null) {
            customDialog = new CustomDialog();
        }
        return customDialog;
    }


    /**
     * 通用提示框
     *
     * @param context
     * @param callBack
     */
    public void baseDialog(Activity context, final IDialogCallBack callBack
            , String title, String contex) {
//        boolean isEqually = previousActivity !=null ?!(previousActivity == context):false;
//        if(baseView == null){
        // 创建View
        baseView = LayoutInflater.from(context).inflate(R.layout.dialog_base, null);
        //baseView=    View.inflate(context,R.layout.dialog_base,null);
        // 查找控件
        tvTitle = baseView.findViewById(R.id.tv_dialog_base_title);
        tvMessage = baseView.findViewById(R.id.tv_dialog_base_message);
        btnCancel = baseView.findViewById(R.id.btn_dialog_base_cancel);
        btnOk = baseView.findViewById(R.id.btn_dialog_base_ok);
        Button btnOnly = baseView.findViewById(R.id.btn_dialog_base_only);
        LinearLayout llTwoBtn = baseView.findViewById(R.id.ll_dialog_base_two_btn);
        builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.setView(baseView);
//        }
        setTitle(title);
        setContext(contex);
//        dialog.getWindow().setDimAmount(0);//设置昏暗度为0
        dialog.setCanceledOnTouchOutside(false);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.determine(dialog,"");
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.cancel(dialog);
            }
        });
        if (!context.isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }
    }


    /**
     * 停止访问
     *
     * @param context
     * @param callBack
     */
    @SuppressLint({"StringFormatInvalid", "LocalSuppress"})
    public void baseDialogStopAccessCustom(Activity context, final IDialogCallBack callBack
           ) {
        // 创建View
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_stop_access, null);
        //baseView=    View.inflate(context,R.layout.dialog_base,null);
        // 查找控件
        tvContinumConfrim = mView.findViewById(R.id.tv_continum_confrim);
        tvMessage = (TextView) mView.findViewById(R.id.tv_dialog_base_message);
        btnOk = (Button) mView.findViewById(R.id.btn_dialog_base_ok);
        builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.setView(mView);
        //        dialog.getWindow().setDimAmount(0);//设置昏暗度为0
        dialog.setCanceledOnTouchOutside(false);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.determine(dialog,"");
            }
        });
        tvContinumConfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.cancel(dialog);
            }
        });
        mWindowManager = FloatingManager.getInstance(context);

        show();


    }

    /**
     * 拦截次数 - 黑名单
     *
     * @param context
     * @param callBack
     */
    @SuppressLint({"StringFormatInvalid", "LocalSuppress"})
    public void baseDialogNumberOfInterceptionsCustom(Activity context, final IDialogCallBack callBack
            , int size,int type) {//type 1 表示黑名单   2 表示自动拦截次数
        if(mWindowManager != null){
            hideCustom();
        }
        // 创建View
            mView = LayoutInflater.from(context).inflate(type == 1?R.layout.dialog_blacklist_number_of_interceptions:R.layout.dialog_number_of_interceptions, null);
        // 查找控件
        tvMessage = (TextView) mView.findViewById(R.id.tv_dialog_base_message);
        btnOk = (Button) mView.findViewById(R.id.btn_dialog_base_ok);
        builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.setView(mView);
        String message = String.format(context.getResources().getString(type == 1?R.string.number_of_interceptions_blacklist:R.string.number_of_interceptions_blacklist),size);
        tvMessage.setText(message);
        //        dialog.getWindow().setDimAmount(0);//设置昏暗度为0
        dialog.setCanceledOnTouchOutside(false);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.determine(dialog,"");
            }
        });
        mWindowManager = FloatingManager.getInstance(context);

        show();
    }

    //view
    public View  getmView(){
        return mView;
    }


    /**
     * 通用提示框
     *
     * @param context
     * @param callBack
     */
    public void baseDialogCustom(Activity context, final IDialogCallBack callBack
            , String title, String contex, String sure, String cancel) {
//        boolean isEqually = previousActivity !=null ?!(previousActivity == context):false;
        // 创建View
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_base, null);
        //baseView=    View.inflate(context,R.layout.dialog_base,null);
        // 查找控件
        tvTitle = (TextView) mView.findViewById(R.id.tv_dialog_base_title);
        tvMessage = (TextView) mView.findViewById(R.id.tv_dialog_base_message);
        btnCancel = (Button) mView.findViewById(R.id.btn_dialog_base_cancel);
        btnOk = (Button) mView.findViewById(R.id.btn_dialog_base_ok);
        Button btnOnly = (Button) mView.findViewById(R.id.btn_dialog_base_only);
        LinearLayout llTwoBtn = (LinearLayout) mView.findViewById(R.id.ll_dialog_base_two_btn);
        builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.setView(mView);
//        previousActivity = context;
        setTitle(title);
        setContext(contex);
        if ("".equals(sure)) {
            sure = "确定";
            cancel = "取消";
        }
        btnOk.setText(sure);
        btnCancel.setText(cancel);
        //        dialog.getWindow().setDimAmount(0);//设置昏暗度为0
        dialog.setCanceledOnTouchOutside(false);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.determine(dialog,"");
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.cancel(dialog);
            }
        });
        mWindowManager = FloatingManager.getInstance(context);

        show();


    }

    public void hideCustom() {
        if(mView != null){
            mWindowManager.removeView(mView);
            mView = null;
        }
    }

    public void show() {
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.CENTER;
        mParams.x = 0;
        mParams.y = 100;
        //总是出现在应用程序窗口之上
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        mWindowManager.addView(mView, mParams);
    }

    public void hide() {
        mWindowManager.removeView(mView);
    }




    public void startDownTime() {
        final int count = 10;//倒计时10秒
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return count - aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//ui线程中进行控件更新
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposabl = d;

            }

            @Override
            public void onNext(Long num) {
                cc.aoeiuv020.vpnproxy.utils.LogUtils.e("剩余" + num + "秒");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                dialog.dismiss();
                disposabl = null;
            }
        });
    }

    //取消定时
    public void closeDisposable(){
        if(disposabl!=null){
            disposabl.dispose();//取消订阅
            disposabl = null;
        }
    }


    public void setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    public void setContext(String context) {
        if (tvMessage != null) {
            tvMessage.setText(context);
        }
    }



    /**
     * 亲情号码对话框
     *
     * @param context
     * @param callBack
     */
    public void addRelativePhoneDialog(Activity context, final IDialogCallBack callBack, int isAdd
            , String title, String name, String phone
    ) {//0   表示添加   其他修改
//        boolean isEqually = previousActivity !=null ?!(previousActivity == context):false;

        baseView = LayoutInflater.from(context).inflate(R.layout.dialog_addrelatviephone, null);

        // 查找控件
        TextView tv_title = baseView.findViewById(R.id.tv_title);
        TextView tv_cancel = baseView.findViewById(R.id.tv_cancel);
        TextView tv_finish = baseView.findViewById(R.id.tv_finish);
        EditText et_parentname = baseView.findViewById(R.id.et_parentname);
        et_parentname.addTextChangedListener(new ByteLimitWatcher(et_parentname, null, 21));
//        if(isAdd == 0){
//            tv_finish.setTextColor(context.getResources().getColor(R.color.color_333333));
//            tv_finish.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_addrelativephone_gray));
//        }
        Disposable check_result = Observable.combineLatest(ObservaEditbleCheck(et_parentname), ObservaEditbleCheck(et_parentname),
                new BiFunction<String, String, Boolean>() {
                    @Override
                    public Boolean apply(String s, String s2) throws Exception {
                        return s.length()>=2;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
//                if (aBoolean) {
//                    tv_finish.setTextColor(context.getResources().getColor(R.color.color_333333));
//                    tv_finish.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_addrelativephone));
//                    tv_finish.setEnabled(true);
//                } else {
//                    tv_finish.setTextColor(context.getResources().getColor(R.color.color_A9A9A9));
//                    tv_finish.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_addrelativephone_gray));
//                    tv_finish.setEnabled(false);
//                }
            }
        });
        tv_title.setText(title);
        //如果不是添加则是修改
        if (isAdd != 0) {
            et_parentname.setText(name);
            if (name != null && name.length() > 0) {
                et_parentname.setSelection(name.length());//将光标移至文字末尾
            }
        }

        builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.setView(baseView);


        Window window = dialog.getWindow();
        //这一句消除白块
        window.setBackgroundDrawable(new BitmapDrawable());


//        dialog.getWindow().setDimAmount(0);//设置昏暗度为0
        dialog.setCanceledOnTouchOutside(false);
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String websiteUrl = et_parentname.getText().toString().trim();
                if(websiteUrl == null || websiteUrl.length() <= 0){
                    ToastUtils.showShort(context.getResources().getString(R.string.website_address_cannot_be_empty));
                    return;
                }
                callBack.determine(dialog,websiteUrl);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.cancel(dialog);
            }
        });
        if (!context.isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }
    }



    /**
     * 返回Edittext的Observable
     */
    private io.reactivex.Observable<String> ObservaEditbleCheck(EditText editText) {
        return PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        emitter.onNext(s.toString());
                    }
                });
            }
        });
    }



    /**
     * 删除提示
     */

    public void deleteData(Activity activity, final IDialogCallBack callBack, String title, String content) {
        baseView = LayoutInflater.from(activity).inflate(R.layout.dialog_no_register, null);
        TextView tv_title = baseView.findViewById(R.id.tv_title);
        TextView tv_message = baseView.findViewById(R.id.tv_message);
        tv_message.setText(content);
        tv_message.post(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("这个Thread: "+Thread.currentThread().getName());
                tv_message.setText(autoSplitText(tv_message));
            }
        });
        TextView tv_cancel = baseView.findViewById(R.id.tv_cancel);
        TextView tv_finish = baseView.findViewById(R.id.tv_finish);
        tv_title.setText(title);

        tv_cancel.setText("取消");
        tv_finish.setText("确定");
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.cancel(dialog);
            }
        });
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.determine(dialog,"");
            }
        });
        dialog = new AlertDialog.Builder(activity).create();
        dialog.setView(baseView);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        //这一句消除白块
        window.setBackgroundDrawable(new BitmapDrawable());
        if (!activity.isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }
    }

    private String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度
        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }
        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }




    /**
     * 亲情号码对话框
     *
     * @param context
     * @param
     */
    public void showAd(Activity context,String img, String title, String content) {

        baseView = LayoutInflater.from(context).inflate(R.layout.dialog_advertising_page, null);

        // 查找控件
        ImageView ivImg = baseView.findViewById(R.id.iv_img);
        TextView tvTitleName = baseView.findViewById(R.id.tv_title_name);
        TextView tvContent = baseView.findViewById(R.id.tv_content);
        TextView tvOhISee = baseView.findViewById(R.id.tv_oh_i_see);

        if(img.length()>=2){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.icon_sure);
            requestOptions.error(R.drawable.icon_sure);
            Glide.with(context).load(img).apply(requestOptions).into(ivImg);
        }

        tvTitleName.setText(title);
        tvContent.setText(content);

        builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.setView(baseView);


        Window window = dialog.getWindow();
        //这一句消除白块
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setWindowAnimations(R.style.mystyle);  //添加动画
        dialog.setCanceledOnTouchOutside(false);
        tvOhISee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (!context.isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }
    }




}