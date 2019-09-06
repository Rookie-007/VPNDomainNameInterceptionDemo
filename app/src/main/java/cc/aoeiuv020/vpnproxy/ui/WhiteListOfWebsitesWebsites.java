package cc.aoeiuv020.vpnproxy.ui;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

import cc.aoeiuv020.vpnproxy.R;
import cc.aoeiuv020.vpnproxy.adapter.BlackMenuItmeAdapter;
import cc.aoeiuv020.vpnproxy.adapter.WhiteMenuItmeAdapter;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlacklistOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.WhiteListOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.BlacklistOfWebsitesCtrls;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.WhitelistOfWebsitesCtrls;
import cc.aoeiuv020.vpnproxy.utils.CustomDialog;
import cc.aoeiuv020.vpnproxy.utils.IDialogCallBack;
import cc.aoeiuv020.vpnproxy.utils.Utils;

public class WhiteListOfWebsitesWebsites extends AppCompatActivity implements View.OnClickListener {
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    WhiteMenuItmeAdapter mMenuAdapter;
    LinearLayout llBackLeft;
    TextView tvTitle;
    ImageView ivAdd;
    LinearLayout llNotData;
    List<WhiteListOfWebsitesBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist_of_websites);
        Utils.setColor(WhiteListOfWebsitesWebsites.this, getResources().getColor(R.color.color_ff7e33), true, R.drawable.fake_status_bar_bg);
        initView();
    }


    public void initView() {
        llBackLeft = findViewById(R.id.ll_back_left);
        tvTitle = findViewById(R.id.tv_title);
        ivAdd = findViewById(R.id.iv_add);
        llNotData = findViewById(R.id.ll_not_data);
        ivAdd.setOnClickListener(this);
        llBackLeft.setOnClickListener(this);
        tvTitle.setText(getResources().getString(R.string.white_list_of_websites));
        swipeMenuRecyclerView = findViewById(R.id.swipelistView);
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        list = WhitelistOfWebsitesCtrls.getAllBlacklistOfWebsitesBean();
        llNotData.setVisibility(list.size()>0? View.GONE:View.VISIBLE);
        // 设置菜单创建器。
        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        mMenuAdapter = new WhiteMenuItmeAdapter(this,list,llNotData);
        swipeMenuRecyclerView.setAdapter(mMenuAdapter);
        // 设置菜单Item点击监听。
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
    }


    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();

            WhiteListOfWebsitesBean bean = list.get(adapterPosition);

            String msg = getResources().getString(R.string.whether_to_delete)+"'"+bean.getWebsiteUrl()+"'"+getResources().getString(R.string.white_list_data);

            CustomDialog.getInstance().deleteData(WhiteListOfWebsitesWebsites.this, new IDialogCallBack() {
                @Override
                public void determine(AlertDialog dialog, String websiteUrl) {
                    dialog.dismiss();
                    WhitelistOfWebsitesCtrls.deleteBlacklistOfWebsitesBean(bean.getWebsiteUrl());
                    mMenuAdapter.notifycation();
                }

                @Override
                public void cancel(AlertDialog dialog) {
                    dialog.dismiss();
                }
            },getResources().getString(R.string.white_list_of_websites),msg);
        }
    };


    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(WhiteListOfWebsitesWebsites.this)
                    .setBackgroundDrawable(R.color.red)
                    .setText("删除") // 文字。
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(160)
                    .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。.
            // 上面的菜单哪边不要菜单就不要添加。
        }
    };

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ll_back_left:
                    finish();
                break;
            case R.id.iv_add:
                CustomDialog.getInstance().addRelativePhoneDialog(WhiteListOfWebsitesWebsites.this, new IDialogCallBack() {
                    @Override
                    public void determine(AlertDialog dialog,String websiteUrl) {
                        dialog.dismiss();
                        //插入黑名单数据
                        WhitelistOfWebsitesCtrls.insterBlacklistOfWebsitesBean("",websiteUrl);
                        //刷新数据
                        mMenuAdapter.notifycation();
                    }
                    @Override
                    public void cancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                },0,getResources().getString(R.string.white_list_of_websites),"","");
                break;
        }
    }
}
