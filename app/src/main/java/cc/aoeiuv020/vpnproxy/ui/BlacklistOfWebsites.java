package cc.aoeiuv020.vpnproxy.ui;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import cc.aoeiuv020.vpnproxy.greendao.bean.BlacklistOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.BlacklistOfWebsitesCtrls;
import cc.aoeiuv020.vpnproxy.utils.CustomDialog;
import cc.aoeiuv020.vpnproxy.utils.IDialogCallBack;
import cc.aoeiuv020.vpnproxy.utils.Utils;

public class BlacklistOfWebsites extends AppCompatActivity implements View.OnClickListener {
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    BlackMenuItmeAdapter mMenuAdapter;
    LinearLayout llBackLeft;
    TextView tvTitle;
    ImageView ivAdd;
    LinearLayout llNotData;
    List<BlacklistOfWebsitesBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist_of_websites);
        Utils.setColor(BlacklistOfWebsites.this, getResources().getColor(R.color.color_ff7e33), true, R.drawable.fake_status_bar_bg);
        initView();
    }


    public void initView() {
        ivAdd = findViewById(R.id.iv_add);
        llBackLeft = findViewById(R.id.ll_back_left);
        llBackLeft.setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getResources().getString(R.string.blacklist_of_websites));
        llNotData = findViewById(R.id.ll_not_data);
        ivAdd.setOnClickListener(this);
        swipeMenuRecyclerView = findViewById(R.id.swipelistView);
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        list = BlacklistOfWebsitesCtrls.getAllBlacklistOfWebsitesBean();
        llNotData.setVisibility(list.size()>0? View.GONE:View.VISIBLE);
        // 设置菜单创建器。
        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        mMenuAdapter = new BlackMenuItmeAdapter(this,list,llNotData);
        swipeMenuRecyclerView.setAdapter(mMenuAdapter);
        // 设置菜单Item点击监听。
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
    }


    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();

            BlacklistOfWebsitesBean bean = list.get(adapterPosition);

            String msg = getResources().getString(R.string.whether_to_delete)+"'"+bean.getWebsiteUrl()+"'"+getResources().getString(R.string.blacklist_data);

            CustomDialog.getInstance().deleteData(BlacklistOfWebsites.this, new IDialogCallBack() {
                @Override
                public void determine(AlertDialog dialog, String websiteUrl) {
                    dialog.dismiss();
                    BlacklistOfWebsitesCtrls.deleteBlacklistOfWebsitesBean(bean.getWebsiteUrl());
                    mMenuAdapter.notifycation();
                }

                @Override
                public void cancel(AlertDialog dialog) {
                    dialog.dismiss();
                }
            },getResources().getString(R.string.blacklist_of_websites),msg);
        }
    };


    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(BlacklistOfWebsites.this)
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
                CustomDialog.getInstance().addRelativePhoneDialog(BlacklistOfWebsites.this, new IDialogCallBack() {
                    @Override
                    public void determine(AlertDialog dialog,String websiteUrl) {
                        dialog.dismiss();
                        //插入黑名单数据
                        BlacklistOfWebsitesCtrls.insterBlacklistOfWebsitesBean("",websiteUrl);
                        //刷新数据
                        mMenuAdapter.notifycation();
                    }
                    @Override
                    public void cancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                },0,getResources().getString(R.string.blacklist_of_websites),"","");
                break;
        }
    }
}
