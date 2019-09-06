package cc.aoeiuv020.vpnproxy.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.File;
import java.util.Calendar;

import cc.aoeiuv020.vpnproxy.R;
import cc.aoeiuv020.vpnproxy.core.Constant;
import cc.aoeiuv020.vpnproxy.core.LocalVpnService;
import cc.aoeiuv020.vpnproxy.greendao.bean.InterceptMaliciousStatusBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.VpnProtectStatusBean;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.InterceptMaliciousStatusCtrls;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.VpnProtectStatusCtrls;
import cc.aoeiuv020.vpnproxy.utils.Utils;

public class MainActivity extends AppCompatActivity implements
        OnCheckedChangeListener,
        LocalVpnService.onStatusChangedListener,View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int START_VPN_SERVICE_REQUEST_CODE = 1985;
    private static String GL_HISTORY_LOGS;
    private SwitchCompat switchProxy;
    private TextView textViewLog;
    private ScrollView scrollViewLog;
    private Calendar mCalendar;
    private ImageView ivAdd;;
    private LinearLayout llBackLeft;
    private LinearLayout llVpnProtection;
    private LinearLayout llBlacklistOfWebsites;
    private LinearLayout llWhiteListOfWebsites;
    private LinearLayout llFilteringKeywords;
    private CheckBox cbStates;
    private CheckBox cbMaliciousWebSitesState;

    private void updateTilte() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (LocalVpnService.IsRunning) {
                actionBar.setTitle(getString(R.string.connected));
            } else {
                actionBar.setTitle(getString(R.string.disconnected));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.setColor(MainActivity.this, getResources().getColor(R.color.color_ff7e33), true, R.drawable.fake_status_bar_bg);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        init();


    }


    private void init(){
        ivAdd = findViewById(R.id.iv_add);
        ivAdd.setVisibility(View.GONE);
        scrollViewLog = (ScrollView) findViewById(R.id.scrollViewLog);
        textViewLog = (TextView) findViewById(R.id.textViewLog);
        llBackLeft = findViewById(R.id.ll_back_left);
        cbStates = findViewById(R.id.sb_state);
        cbMaliciousWebSitesState = findViewById(R.id.cb_malicious_web_sites_state);
        assert textViewLog != null;
        textViewLog.setText(GL_HISTORY_LOGS);
        scrollViewLog.fullScroll(ScrollView.FOCUS_DOWN);

        mCalendar = Calendar.getInstance();
        LocalVpnService.addOnStatusChangedListener(this);

        llVpnProtection = findViewById(R.id.vpn_protection);
        llBlacklistOfWebsites = findViewById(R.id.blacklist_of_websites);
        llWhiteListOfWebsites = findViewById(R.id.white_list_of_websites);
        llFilteringKeywords = findViewById(R.id.filtering_keywords);
        llVpnProtection.setOnClickListener(this);
        llBlacklistOfWebsites.setOnClickListener(this);
        llWhiteListOfWebsites.setOnClickListener(this);
        llFilteringKeywords.setOnClickListener(this);
        llBackLeft.setOnClickListener(this);

        //VPN防护
        VpnProtectStatusBean vpnProtectStatusBean = VpnProtectStatusCtrls.getVpnProtectStatus();
        if(vpnProtectStatusBean != null){
            int  vpnStatus = vpnProtectStatusBean.getVpnProtectsSatus();
            cbStates.setChecked(vpnStatus == 1);
        }

        //自动拦截恶意网址
        InterceptMaliciousStatusBean interceptMaliciousStatusBean = InterceptMaliciousStatusCtrls.getVpnProtectStatus();
        if(interceptMaliciousStatusBean != null){
            int interceptStatus = interceptMaliciousStatusBean.getVpnProtectsSatus();
            cbMaliciousWebSitesState.setChecked(interceptStatus == 1);
        }

        //开启vpn防护
        cbStates.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity.this.onCheckedChanged(compoundButton,b);
            }
        });

        //自动拦截恶意网址
        cbMaliciousWebSitesState.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //是否开启
                InterceptMaliciousStatusCtrls.insterInterceptMaliciousStatus(b?1:0,getResources().getString(b?R.string.blocking_malicious_web_sites_open:R.string.close_blocking_malicious_web_sites));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateTilte();
    }

    String getVersionName() {
        PackageManager packageManager = getPackageManager();
        if (packageManager == null) {
            Log.e(TAG, "null package manager is impossible");
            return null;
        }

        try {
            return packageManager.getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "package not found is impossible", e);
            return null;
        }
    }

    boolean isValidUrl(String url) {
        try {
            if (url == null || url.isEmpty()) {
                return false;
            }

            if (url.startsWith("/")) {//file path
                File file = new File(url);
                if (!file.exists()) {
                    onLogReceived(String.format("File(%s) not exists.", url));
                    return false;
                }
                if (!file.canRead()) {
                    onLogReceived(String.format("File(%s) can't read.", url));
                    return false;
                }
            } else { //url
                Uri uri = Uri.parse(url);
                if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())) {
                    return false;
                }
                if (uri.getHost() == null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onLogReceived(String logString) {
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        logString = String.format("[%1$02d:%2$02d:%3$02d] %4$s\n",
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                mCalendar.get(Calendar.SECOND),
                logString);

        Log.d(Constant.TAG, logString);

        if (textViewLog.getLineCount() > 200) {
            textViewLog.setText("");
        }
        textViewLog.append(logString);
        scrollViewLog.fullScroll(ScrollView.FOCUS_DOWN);
        GL_HISTORY_LOGS = textViewLog.getText() == null ? "" : textViewLog.getText().toString();
    }

    @Override
    public void onStatusChanged(String status, Boolean isRunning) {
//        switchProxy.setEnabled(true);
//        switchProxy.setChecked(isRunning);
        onLogReceived(status);
        updateTilte();
//        ToastManage.showCustomContent(status);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (LocalVpnService.IsRunning != isChecked) {
//            switchProxy.setEnabled(false);
            if (isChecked) {
                Intent intent = LocalVpnService.prepare(this);
                if (intent == null) {
                    startVPNService();
                } else {
                    startActivityForResult(intent, START_VPN_SERVICE_REQUEST_CODE);
                }
                VpnProtectStatusCtrls.insterVpnProtectStatus(1,getResources().getString(R.string.vpn_protective_opening));
            } else {
                VpnProtectStatusCtrls.insterVpnProtectStatus(0,getResources().getString(R.string.vpn_protective_shutdown));
                LocalVpnService.IsRunning = false;
            }
        }
    }

    private void startVPNService() {
        textViewLog.setText("");
        GL_HISTORY_LOGS = null;
        onLogReceived("starting...");
        startService(new Intent(this, LocalVpnService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == START_VPN_SERVICE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                startVPNService();
            } else {
                switchProxy.setChecked(false);
                switchProxy.setEnabled(true);
                onLogReceived("canceled.");
            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_switch);
        if (menuItem == null) {
            return false;
        }

        switchProxy = (SwitchCompat) menuItem.getActionView();
        if (switchProxy == null) {
            return false;
        }

        switchProxy.setChecked(LocalVpnService.IsRunning);
        switchProxy.setOnCheckedChangeListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_about:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.app_name) + " " + getVersionName())
                        .setMessage(R.string.about_info)
                        .setPositiveButton(R.string.btn_ok, null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        LocalVpnService.removeOnStatusChangedListener(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ll_back_left:
                    finish();
                break;
            case R.id.vpn_protection:

                break;
            case R.id.blacklist_of_websites:
                 startActivity(new Intent(this,BlacklistOfWebsites.class));
                break;
            case R.id.white_list_of_websites:
                startActivity(new Intent(this,WhiteListOfWebsitesWebsites.class));
                break;
            case R.id.filtering_keywords:
                startActivity(new Intent(this,KeywordWebsites.class));
                break;
        }
    }
}
