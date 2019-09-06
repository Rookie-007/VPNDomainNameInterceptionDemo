package cc.aoeiuv020.vpnproxy;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import cc.aoeiuv020.vpnproxy.greendao.DaoMaster;
import cc.aoeiuv020.vpnproxy.greendao.DaoSession;
import cc.aoeiuv020.vpnproxy.greendao.updatadb.CusOpenHelper;
import cc.aoeiuv020.vpnproxy.ui.JumpPageActivity;
import cc.aoeiuv020.vpnproxy.utils.ConstantUtils;

/*
 Created by Little Q on date
 Page:
 Notes:
 Date: 2019/8/13
 Time: 18:03
 */public class MyApplication extends Application {
    private static MyApplication instance;
    private CusOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private static Handler handler;

    public static Handler getActivity() {
        return handler;
    }

    public static void setActivity( Handler handler) {
        MyApplication.handler = handler;
    }



    @Override
    public void onCreate() {
        super.onCreate();

        if (instance == null) {
            instance = this;
        }

        setDatabase();
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new CusOpenHelper(this, ConstantUtils.DB_NAME);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
