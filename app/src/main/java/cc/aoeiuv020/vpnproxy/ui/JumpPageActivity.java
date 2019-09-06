package cc.aoeiuv020.vpnproxy.ui;

import cc.aoeiuv020.vpnproxy.MyApplication;
import cc.aoeiuv020.vpnproxy.R;
import cc.aoeiuv020.vpnproxy.core.LocalVpnService;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlockingMaliciousWebSitesBean;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.BlacklistOrAutomaticInterceptionNumberCtrls;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.BlockingMaliciousWebSitesCtrls;
import cc.aoeiuv020.vpnproxy.utils.ConstantUtils;
import cc.aoeiuv020.vpnproxy.utils.CustomDialog;
import cc.aoeiuv020.vpnproxy.utils.IDialogCallBack;
import cc.aoeiuv020.vpnproxy.utils.IPermissionCallBack;
import cc.aoeiuv020.vpnproxy.utils.LogUtils;
import cc.aoeiuv020.vpnproxy.utils.NetworkInterceptionTool;
import cc.aoeiuv020.vpnproxy.utils.PermissionUtils;
import cc.aoeiuv020.vpnproxy.utils.Utils;
import io.reactivex.functions.Consumer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.List;

public class JumpPageActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnNetworkInterception;
//    Button btn_game_management;
//    Button btn_applicaiton_management;
    private TextView tvTitle;
    private ImageView ivAdd;
    ;
    private LinearLayout llBackLeft;
    private RxPermissions rxPermissions;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//                //弹窗是否确定
//                CustomDialog.getInstance().baseDialogCustom(JumpPageActivity.this, new IDialogCallBack() {
//                    @Override
//                    public void determine(AlertDialog dialog, String websiteUrl) {
//                        CustomDialog.getInstance().hideCustom();
//                        synchronized (LocalVpnService.Instance){
//                            LocalVpnService.Instance.notify();
//                        }
//
//                        NetworkInterceptionTool.isInterceptionTool = true;
//                        LocalVpnService.isBlock = false;
//                        LocalVpnService.isNotyfy = true;
//                        CustomDialog.getInstance().closeDisposable();
//                    }
//                    @Override
//                    public void cancel(AlertDialog dialog) {
//                        CustomDialog.getInstance().hideCustom();
//                        synchronized (LocalVpnService.Instance){
//                            LocalVpnService.Instance.notify();
//                        }
//                        NetworkInterceptionTool.isInterceptionTool = false;
//                        LocalVpnService.isBlock = false;
//                        LocalVpnService.isNotyfy = true;
//                        CustomDialog.getInstance().closeDisposable();
//                    }
//                }, "", "", "", "");

            if(ConstantUtils.FLIP_WINDOW == msg.what){
                CustomDialog.getInstance().baseDialogStopAccessCustom(JumpPageActivity.this, new IDialogCallBack() {
                    @Override
                    public void determine(AlertDialog dialog, String websiteUrl) {
                        CustomDialog.getInstance().hideCustom();
                        synchronized (LocalVpnService.Instance){
                            LocalVpnService.Instance.notify();
                        }
                        NetworkInterceptionTool.isInterceptionTool = true;
                        CustomDialog.getInstance().closeDisposable();
                    }

                    @Override
                    public void cancel(AlertDialog dialog) {
                        CustomDialog.getInstance().hideCustom();
                        NetworkInterceptionTool.isInterceptionTool = false;
                        synchronized (LocalVpnService.Instance){
                            LocalVpnService.Instance.notify();
                        }
                        CustomDialog.getInstance().closeDisposable();
                    }
                });
            }else if(ConstantUtils.FLIP_WINDOW_BLACK_OR_INTERCEPT == msg.what){
                if(CustomDialog.getInstance().getmView()== null){
                    int size = BlacklistOrAutomaticInterceptionNumberCtrls.getBlacklistNumber();
                    CustomDialog.getInstance().baseDialogNumberOfInterceptionsCustom(JumpPageActivity.this, new IDialogCallBack() {
                        @Override
                        public void determine(AlertDialog dialog, String websiteUrl) {
                            CustomDialog.getInstance().hideCustom();
                        }

                        @Override
                        public void cancel(AlertDialog dialog) {
                            CustomDialog.getInstance().hideCustom();
                        }
                    },size,1);
                }
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_page);
        MyApplication.setActivity(handler);
        List<BlockingMaliciousWebSitesBean> lsit = BlockingMaliciousWebSitesCtrls.getAllBlacklistOfWebsitesBean();
        if (lsit == null || lsit.size() <= 0) {
            BlockingMaliciousWebSitesCtrls.insterBlacklistOfWebsitesBean("www.baidu.com");
        }
        rxPermissions = new RxPermissions(this);
        Utils.setColor(JumpPageActivity.this, getResources().getColor(R.color.color_ff7e33), true, R.drawable.fake_status_bar_bg);
        init();

        PermissionUtils.getInstence().permission(this, PermissionUtils.lsit, new IPermissionCallBack() {
            @Override
            public void callback() {
//                PermissionUtils.isWind(LoginActivity.this);
            }
        });

    }

    private void init() {
        llBackLeft = findViewById(R.id.ll_back_left);
        ivAdd = findViewById(R.id.iv_add);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getResources().getString(R.string.functional_demo));
        ivAdd.setVisibility(View.GONE);
        llBackLeft.setVisibility(View.GONE);
        btnNetworkInterception = findViewById(R.id.btn_network_interception);
//        btn_applicaiton_management = findViewById(R.id.btn_application_management);
//        btn_game_management = findViewById(R.id.btn_game_management);
        btnNetworkInterception.setOnClickListener(this);
//        btn_game_management.setOnClickListener(this);
//        btn_applicaiton_management.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_network_interception:
                startActivity(new Intent(this, MainActivity.class));
                break;
//            case R.id.btn_visual_protection:
//                startActivity(new Intent(this, VisualProtectionActivity.class));
//                break;
//            case R.id.btn_educational_information_list:
//                startActivity(new Intent(this, EducationalConsultation.class));
//                break;
//            case R.id.btn_advertising_access:
//                startActivity(new Intent(this, AdvertisingFlashPage.class));
//                break;
//            case R.id.btn_historical_footprint:
//                gotoBaiduMovingTrajectory();
//                break;
//            case R.id.btn_game_management:
//                gotoGameManage();
//                break;
//            case R.id.btn_application_management:
//                gotoAppManage();
//                break;
        }
    }
//
//    private void gotoGameManage() {
//        rxPermissions.request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE).subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) throws Exception {
//                if (aBoolean) {
//                    Intent intent = new Intent(JumpPageActivity.this, GameManagementActivity.class);
//                    intent.putExtra(GameManagementActivity.ACCOUNT, Constants.TEST_CHILD_ACCOUNT);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(JumpPageActivity.this, "权限已被拒绝", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//
//    private void gotoAppManage() {
//        rxPermissions.request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE).subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) throws Exception {
//                if (aBoolean) {
//                    Intent intent = new Intent(JumpPageActivity.this, AppManagementActivity.class);
//                    intent.putExtra(AppManagementActivity.ACCOUNT, Constants.TEST_CHILD_ACCOUNT);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(JumpPageActivity.this, "权限已被拒绝", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    private void gotoBaiduMovingTrajectory() {
//        Intent intent = new Intent(JumpPageActivity.this, BaiduMovingTrajectoryActivity.class);
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void Event(Integer integer) {
//        switch (integer) {
//            case 1234:
//                synchronized (JumpPageActivity.this) {
//                    handler.sendEmptyMessageDelayed(0, 500);
//                    try {
//                        LocalVpnService.Instance.wait();
//                    } catch (InterruptedException e) {
//                        LogUtils.e("eee ==============InterruptedException异常=====================");
//                        e.printStackTrace();
//                    }
//                }
//                break;
//        }
//
//
//    }
}
