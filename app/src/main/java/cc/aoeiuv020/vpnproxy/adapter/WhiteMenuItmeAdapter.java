package cc.aoeiuv020.vpnproxy.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

import cc.aoeiuv020.vpnproxy.R;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlacklistOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.WhiteListOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.BlacklistOfWebsitesCtrls;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.WhitelistOfWebsitesCtrls;
import cc.aoeiuv020.vpnproxy.utils.CustomDialog;
import cc.aoeiuv020.vpnproxy.utils.IDialogCallBack;
import cc.aoeiuv020.vpnproxy.utils.LogUtils;
import cc.aoeiuv020.vpnproxy.utils.Utils;

public class WhiteMenuItmeAdapter extends SwipeMenuAdapter {
    List<WhiteListOfWebsitesBean> menuList;
    private Activity activity;
    private LinearLayout llNotData;

    public WhiteMenuItmeAdapter(Activity activity, List<WhiteListOfWebsitesBean> relativePhoneBeanList, LinearLayout llNotData) {
        this.menuList = relativePhoneBeanList;
        this.activity = activity;
        this.llNotData = llNotData;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relativephone, parent, false);
        return view;
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        ContactViewHolder viewHolder = new ContactViewHolder(realContentView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ContactViewHolder holder1 = (ContactViewHolder) viewHolder;

        String  oldWebsiteUrl=  menuList.get(i).getWebsiteUrl();
        holder1.tv_name.setText(oldWebsiteUrl);
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFastClick()) {
                    //TODO
                    CustomDialog.getInstance().addRelativePhoneDialog(activity, new IDialogCallBack() {
                        @Override
                        public void determine(AlertDialog dialog, String websiteUrl) {
                            dialog.dismiss();
                            //插入黑名单数据
                            WhitelistOfWebsitesCtrls.insterBlacklistOfWebsitesBean(oldWebsiteUrl,websiteUrl);
                            //刷新数据
                            notifycation();
                        }

                        @Override
                        public void cancel(AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    },1,activity.getResources().getString(R.string.white_list_of_websites),oldWebsiteUrl,"");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        LogUtils.e("getItemCount长度===========" + menuList.size());
        return menuList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);

        }

        @Override
        public void onClick(View view) {

        }
    }

    /**
     * 刷新数据
     */
    public void notifycation(){
        List<WhiteListOfWebsitesBean> list = WhitelistOfWebsitesCtrls.getAllBlacklistOfWebsitesBean();
        llNotData.setVisibility(list.size()>0? View.GONE:View.VISIBLE);
        menuList.clear();
        menuList.addAll(list);
        notifyDataSetChanged();
    }


}
