package cn.com.zlct.diamondgo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.base.BaseDialog;
import cn.com.zlct.diamondgo.custom.CircleImageView;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.custom.SectorProgressBar;
import cn.com.zlct.diamondgo.custom.UploadImgDialog;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.BitMapUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 基本信息
 */
public class UserInfoActivity extends BaseActivity implements BaseDialog.OnItemClickListener {

    @BindView(R.id.toolbar_text)
    public Toolbar toolbar;
    @BindView(R.id.sdv_userHead)
    public SimpleDraweeView sdvHead;
    @BindView(R.id.iv_userHead)
    public CircleImageView ivHead;
    @BindView(R.id.spb_userHead)
    public SectorProgressBar progressBar;

    @BindView(R.id.tv_userAlias)
    public TextView tvAlias;
    @BindView(R.id.tv_userGender)
    public TextView tvGender;

    private boolean isUpdating;//图片是否正在上传
    UserInfoEntity userInfoEntity;
    LoadingDialog loadingDialog;

    @Override
    protected int getViewResId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void init() {
        userInfoEntity = PhoneUtil.getUserInfo(this);
        ToolBarUtil.initToolBar(toolbar, "基本信息", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void loadData() {
        sdvHead.setImageURI(Constant.URL.BaseImg + userInfoEntity.getHeadURL());
        tvAlias.setText(userInfoEntity.getName().equals("-") ? "点击修改" : userInfoEntity.getName());
        tvGender.setText(userInfoEntity.getSex().equals("-") ? "点击修改" : userInfoEntity.getSex());


    }

    //    , R.id.tv_userCard
    @OnClick({R.id.ll_userHead, R.id.ll_userAlias, R.id.ll_userGender})
    public void head(View v) {
        switch (v.getId()) {
            case R.id.ll_userHead://头像
                if (isUpdating) {
                    ToastUtil.initToast(this, "头像正在修改中，请稍候");
                } else {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, sdvHead.getHeight());
                    ivHead.setLayoutParams(lp);
                    UploadImgDialog upImgDialog = UploadImgDialog.newInstance();
                    upImgDialog.setOnItemClickListener(this);
                    upImgDialog.show(getFragmentManager());
                }
                break;
            case R.id.ll_userAlias://昵称
                Intent intentAlias = new Intent(this, EditAliasActivity.class);
                intentAlias.putExtra("old", tvAlias.getText().toString());
                startActivityForResult(intentAlias, Constant.Code.EditAliasCode);
                break;
            case R.id.ll_userGender://性别
                final String[] arrayFruit = new String[]{"男", "女"};
                Dialog alertDialog = new AlertDialog.Builder(this).
                        setTitle("请选择性别")
                        .setItems(arrayFruit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                loadingDialog = LoadingDialog.newInstance("绑定中...");
                                loadingDialog.show(getFragmentManager());
                                String newGender = arrayFruit[which];
                                OkHttpUtil.postJson(Constant.URL.updateUserInfo, userInfoEntity.getPhone(),
                                        userInfoEntity.getName(), newGender, userInfoEntity.getHeadURL(),
                                        new OkHttpUtil.OnDataListener() {
                                            @Override
                                            public void onResponse(String url, String json) {
                                                Log.e("loge", "修改性别" + json);
                                                loadingDialog.dismiss();
                                                if (true) {
                                                    getUserInfo();
                                                }
                                            }

                                            @Override
                                            public void onFailure(String url, String error) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ToastUtil.initToast(UserInfoActivity.this, "修改失败，请再次上传");
                                                        loadingDialog.dismiss();
                                                    }
                                                });
                                            }
                                        });

                            }
                        }).
//                                setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                }).
        create();

                alertDialog.show();

                break;
//            case R.id.tv_userCard://我的银行卡
//                startActivity(new Intent(this, BankCardActivity.class));
//                break;
        }
    }

    @Override
    public void onItemClick(View v) {
        switch (v.getId()) {
            case R.id.btn_openAlbum://打开相册
                Intent intentAlbum = new Intent(Intent.ACTION_PICK);
                intentAlbum.setType("image/*");
                startActivityForResult(intentAlbum, Constant.Code.AlbumCode);
                break;
            case R.id.btn_openCamera://拍照
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            Constant.Code.PermissionCode);
                    break;
                }
                openCamera();
                break;
        }
    }

    private void openCamera() {
        Intent intent = new Intent();
        Intent intentCamera = getPackageManager().getLaunchIntentForPackage("com.android.camera");
        if (intentCamera != null) {
            intent.setPackage("com.android.camera");
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constant.Code.CameraCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constant.Code.PermissionCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.Code.AlbumCode://打开相册
                    Uri uriAlbum = data.getData();
                    Bitmap bitmapAlbum = BitMapUtil.decodeBitmap(BitMapUtil.Uri2Bitmap(this, uriAlbum));
                    ivHead.setBackgroundColor(Color.WHITE);
                    ivHead.setImageBitmap(bitmapAlbum);
                    decodeBm(bitmapAlbum);
                    break;
                case Constant.Code.CameraCode://拍照
                    Bitmap bm = data.getParcelableExtra("data");
                    Bitmap bitmapCamera = BitMapUtil.decodeBitmap(bm);
                    ivHead.setBackgroundColor(Color.WHITE);
                    ivHead.setImageBitmap(bitmapCamera);

                    decodeBm(bitmapCamera);
                    break;
                case Constant.Code.EditAliasCode://昵称
                    tvAlias.setText(data.getStringExtra("data"));
                    break;
                case Constant.Code.EditGenderCode://性别
                    tvGender.setText(data.getStringExtra("data"));
                    break;
            }
        }
    }

    private void decodeBm(Bitmap bitmap) {
        if (bitmap == null) {
            ToastUtil.initToast(this, "图片错误");
        } else {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.initProgress();
            }
            isUpdating = true;
            OkHttpUtil.postStream(Constant.URL.appUpHeadImg, userInfoEntity.getPhone(), 0, bitmap, new OkHttpUtil.OnProgressMultiListener() {
                @Override
                public void onProgressMulti(int index, int rate) {
                    progressBar.setProgress(rate);
//                    if (rate==100){
//                        progressBar.setVisibility(View.GONE);
//                    }
                }
            }, new OkHttpUtil.OnDataListener() {
                @Override
                public void onResponse(String url, String json) {
                    Log.e("loge", json);
                    ToastUtil.initToast(UserInfoActivity.this, "上传成功");
                    isUpdating = false;
                    getUserInfo();
                }

                @Override
                public void onFailure(String url, String error) {
                    isUpdating = false;
                }
            });
        }
    }

    protected void getUserInfo() {
        //查询个人信息
        String phone = SharedPreferencesUtil.getPhone(this);
        OkHttpUtil.postJson(Constant.URL.getUserInfo, "phone", phone, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", json);
                UserInfoEntity userInfoEntity = new Gson().fromJson(json, UserInfoEntity.class);
//                setUIView(userInfoEntity);//设置UI
                SharedPreferencesUtil.saveUserInfo(UserInfoActivity.this, DesUtil.encrypt(json, DesUtil.LOCAL_KEY));
            }

            @Override
            public void onFailure(String url, String error) {

            }
        });
    }
}
