package cn.com.zlct.diamondgo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import butterknife.BindView;
import cn.com.zlct.diamondgo.activity.LoginActivity;
import cn.com.zlct.diamondgo.activity.ProjectDetailActivity;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.base.BaseFragment;
import cn.com.zlct.diamondgo.custom.CircleView;
import cn.com.zlct.diamondgo.custom.DownloadDialog;
import cn.com.zlct.diamondgo.custom.NewVersionDialog;
import cn.com.zlct.diamondgo.fragment.CartListFragment;
import cn.com.zlct.diamondgo.fragment.ClassFragment;
import cn.com.zlct.diamondgo.fragment.HomeFragment;
import cn.com.zlct.diamondgo.fragment.MineFragment;
import cn.com.zlct.diamondgo.fragment.ZoneFragment;
import cn.com.zlct.diamondgo.model.GetVersionQueryEntity;
import cn.com.zlct.diamondgo.model.LoginEntity;
import cn.com.zlct.diamondgo.util.AnimationUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.PreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.Util;
import okhttp3.FormBody;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener
        , HomeFragment.OnItemClickListener, BaseFragment.OnCartUpListener, BaseActivity.OnCartUpListener {

    @BindView(R.id.rg_main)
    public RadioGroup radioGroup;
    @BindView(R.id.tv_mainRedDot)
    public CircleView tvRed;

    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private ZoneFragment zoneFragment;
    private ClassFragment classFragment;
    private CartListFragment cartListFragment;
    private MineFragment mineFragment;
    private int page;//要启动的Fragment的页数

    private boolean isExit = false;//退出标识
    private String userId;
    private Handler handler = new Handler();

    private String newAppUrl;
    private NewVersionDialog newDialog;
    private DownloadDialog downloadDialog;
    private String filePath;
    private String versionName;
    private SharedPreferences isFirst;

    @Override
    protected int getViewResId() {
        // setSystemUI();
        return R.layout.activity_main;
    }

    private void setSystemUI() {
        View decorView = getWindow().getDecorView();
        int systemUI = decorView.getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        systemUI |= flags;
        decorView.setSystemUiVisibility(systemUI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void init() {
        userId = PreferencesUtil.getUserId(this);
        page = getIntent().getIntExtra("page", 0);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.getChildAt(page).performClick();
        tvRed.setBackgroundColor(Color.RED);
        PhoneUtil.upCartDot(getApplication(), tvRed);

        new ProjectDetailActivity().setOnCartUpListener(this);

        isFirst = getSharedPreferences("isFirst", MODE_PRIVATE);
        boolean newVersion = isFirst.getBoolean("IsVerFirst", false);
            if (newVersion) {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("versionType", 0 + "");
            OkHttpUtil.postJson(Constant.URL.appGetVersionQuery, builder, new OkHttpUtil.OnDataListener() {
                @Override
                public void onResponse(String url, String json) {
                    try {
                        if (!TextUtils.isEmpty(json)) {
                            Log.e("loge", "版本更新:" + json);
                            GetVersionQueryEntity appVersion = new Gson().fromJson(json, GetVersionQueryEntity.class);
                            if (!("v" + PhoneUtil.getAppVersion(MainActivity.this)).equals(appVersion.getList().get(0).getVersionNumber())) {
                                UpVersion(appVersion);
//                            } else if (!("V" + PhoneUtil.getAppVersion(MainActivity.this)).equals(appVersion.getList().get(0).getVersionNumber())) {
//                                UpVersion(appVersion);
//                            } else {

                            }
                            Log.e("loge", PhoneUtil.getAppVersion(MainActivity.this));
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(String url, String error) {
                }
            });
            SharedPreferences.Editor editor = isFirst.edit();
            editor.putBoolean("newVersion", false);
            editor.commit();
        }

        checkStoragePermission();
    }

    private void UpVersion(GetVersionQueryEntity appVersion) {
        newAppUrl = appVersion.getList().get(0).getVersionURL();
        versionName=appVersion.getList().get(0).getVersionNumber();
        String newContent = appVersion.getList().get(0).getVersionDescribe() + "</br>(建议在wifi下更新)";
        newDialog = NewVersionDialog.newInstance("发现新版本 " + appVersion.getList().get(0).getVersionNumber(),
                newContent.replace("</br>", "\n"), "取消", "更新");
        newDialog.setOnBtnClickListener(new NewVersionDialog.OnBtnClickListener() {
            @Override
            public void onBtnClick(View view) {
                if (newDialog != null) {
                    newDialog.dismiss();
                }
                downloadDialog = DownloadDialog.newInstance(PhoneUtil.getAppName(MainActivity.this) + versionName, false);
                downloadDialog.show(getFragmentManager(), "download");
                filePath = Constant.Cache.ApkDownDir + "/" + PhoneUtil.getAppName(MainActivity.this) + "_" + versionName + ".apk";
                Log.e("loge", "DownPath: " + filePath);
                OkHttpUtil.fileDownload(newAppUrl, filePath, new OkHttpUtil.OnProgressListener() {
                    @Override
                    public void onProgress(final int rate) {
                        ProgressBar progressBar = downloadDialog.getProgressBar();
                        final TextView button = downloadDialog.getButton();
                        if (progressBar != null) {
                            progressBar.setProgress(rate);
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (button != null) {
                                    button.setText("下载中(" + rate + "%)");
                                    if (rate == 100) {
                                        button.setText("安装");
                                    }
                                }
                            }
                        });
                    }
                }, new OkHttpUtil.OnDataListener() {
                    @Override
                    public void onResponse(String url, String json) {
                        if (!TextUtils.isEmpty(newAppUrl) && newAppUrl.equals(url)) {//下载完成
                            TextView button = downloadDialog.getButton();
                            button.setSelected(true);
                            button.setText("安装");
                            button.setClickable(true);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    jumpInstall();
                                }
                            });
                            jumpInstall();
                        }
                    }

                    @Override
                    public void onFailure(String url, String error) {
                    }
                });
            }
        });
        newDialog.show(getFragmentManager(), "update");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (checkedId) {
            case R.id.rb_main0://首页
                page = 0;
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    homeFragment.setOnItemClickListener(this);
                    transaction.add(R.id.fl_main, homeFragment, page + "");
                } else {
                    transaction.show(homeFragment);
//                    homeFragment.onRefresh();
                }
                break;
            case R.id.rb_main1://推广信息
                page = 1;
                if (zoneFragment == null) {
                    zoneFragment = new ZoneFragment();
                    transaction.add(R.id.fl_main, zoneFragment, page + "");
                } else {
                    transaction.show(zoneFragment);
                    zoneFragment.onRefresh();
                    PhoneUtil.upCartDot(getApplication(), tvRed);
                }
                break;
            case R.id.rb_main2://分类
                page = 2;
                if (classFragment == null) {
                    classFragment = new ClassFragment();
                    transaction.add(R.id.fl_main, classFragment, page + "");
                } else {
                    transaction.show(classFragment);
                    classFragment.onRefresh();
                    PhoneUtil.upCartDot(getApplication(), tvRed);
                }
                break;
            case R.id.rb_main3://购物车
                page = 3;
                if (!isLogin()) {//当前未登录
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    if (cartListFragment == null) {
                        cartListFragment = new CartListFragment();
                        cartListFragment.setOnCartUpListener(this);
                        transaction.add(R.id.fl_main, cartListFragment, page + "");
                    } else {
                        transaction.show(cartListFragment);
                        cartListFragment.onRefresh();
                        PhoneUtil.upCartDot(getApplication(), tvRed);
                    }
                }
                break;
            case R.id.rb_main4://我的
                page = 4;
                if (!isLogin()) {//当前未登录
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    if (mineFragment == null) {
                        mineFragment = new MineFragment();
                        transaction.add(R.id.fl_main, mineFragment, page + "");

                    } else {
                        transaction.show(mineFragment);
                        mineFragment.onRefresh();
                        PhoneUtil.upCartDot(getApplication(), tvRed);
                    }
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 先隐藏所有Fragment
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (zoneFragment != null) {
            transaction.hide(zoneFragment);
        }
        if (classFragment != null) {
            transaction.hide(classFragment);
        }
        if (cartListFragment != null) {
            transaction.hide(cartListFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }


    /**
     * 跳转到安装页面
     */
//    private void jumpInstall() {
//        File apkFile = new File(filePath);
//        if (apkFile.exists()) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
//                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//
//            }else {
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//给目标应用一个临时的授权
//                Uri uriForFile = FileProvider.getUriForFile(this, "cn.com.zlct.diamondgo.FileProvider", apkFile);
//                intent.setDataAndType(uriForFile,"application/vnd.android.package-archive");
//            }
//            startActivity(intent);
//            android.os.Process.killProcess(android.os.Process.myPid());
//        }
//    }

//    /**
//     * 跳转到安装页面
//     */
    private void jumpInstall() {
        File apkFile = new File(filePath);
        if (apkFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }

    public boolean isLogin() {
        userId = PreferencesUtil.getUserId(this);
        if ("default".equals(userId)) {
            return false;
        } else {
            return true;
        }
    }

    private void checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constant.Code.PermissionStorageCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.Code.PermissionStorageCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.initToast(this, getString(R.string.storagePer));
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isLogin() && (page > 2)) {
            radioGroup.getChildAt(0).performClick();
        } else {
            int checkedId = radioGroup.getCheckedRadioButtonId();
            int position = radioGroup.indexOfChild(radioGroup.findViewById(checkedId));
            if (page == position) {
                onCheckedChanged(radioGroup, checkedId);
            } else {
                radioGroup.getChildAt(page).performClick();
            }
        }

        PhoneUtil.upCartDot(getApplication(), tvRed);
    }

    @Override
    public void onBackPressed() {
        if (isExit) {
            System.exit(0);
        } else {
            ToastUtil.initToast(this, "再按一次退出" + Util.getAppName(this));
            isExit = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 5000);//5秒内再按后退键真正退出
        }
    }


    @Override
    public void onItemClick(View v) {
        switch (v.getId()) {
            case R.id.tv_homeShortcut2:
                radioGroup.getChildAt(4).performClick();
                break;
            case R.id.iv_homefl:
                radioGroup.getChildAt(2).performClick();
                break;
        }
    }

    /**
     * 加入清单 动画
     */
    @Override
    public void onCartAnim(View v, String url) {
    }

    /**
     * 更改清单红点数量
     */
    @Override
    public void addCartUp() {
        int count = ((AppContext) getApplication()).getCartCount();
        ((AppContext) getApplication()).setCartCount(count + 1);
        PhoneUtil.upCartDot(getApplication(), tvRed);
    }

    /**
     * 更改清单红点数量
     */
    @Override
    public void onCartUp(int count) {
        ((AppContext) getApplication()).setCartCount(count);
        PhoneUtil.upCartDot(getApplication(), tvRed);
    }

}
