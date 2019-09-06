package cc.aoeiuv020.vpnproxy.greendao.updatadb;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import cc.aoeiuv020.vpnproxy.greendao.BlacklistOfWebsitesBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.DaoMaster;

public class CusOpenHelper extends DaoMaster.OpenHelper {
 
    private static final String TAG = "CusOpenHelper";
 
    public CusOpenHelper(Context context, String name) {
        super(context, name);
    }
 
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.e(TAG, "onUpgrade=========: oldVersion:" + oldVersion + ",newVersion==========:" + newVersion);
        //第三个参数是那些表修改了就要传进去那些
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
 
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }
 
            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        },  BlacklistOfWebsitesBeanDao.class);
               ;
    }
}