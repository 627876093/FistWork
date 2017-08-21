package cn.com.zlct.diamondgo.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.activity.AddressActivity;
import cn.com.zlct.diamondgo.activity.MyCircleActivity;
import cn.com.zlct.diamondgo.activity.MyCollectionActivity;
import cn.com.zlct.diamondgo.activity.MyIntegralActivity;
import cn.com.zlct.diamondgo.activity.MyMoneyActivity;
import cn.com.zlct.diamondgo.activity.MyRebateActivity;
import cn.com.zlct.diamondgo.activity.MyRecommendActivity;
import cn.com.zlct.diamondgo.activity.OrderActivity;
import cn.com.zlct.diamondgo.activity.SettingActivity;
import cn.com.zlct.diamondgo.activity.UserInfoActivity;
import cn.com.zlct.diamondgo.adapter.MineLVAdapter;
import cn.com.zlct.diamondgo.base.BaseFragment;
import cn.com.zlct.diamondgo.custom.ListViewInScrollView;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.LocalEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.Util;

/**
 * 我的 Fragment
 */
public class MineFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener {

    @BindView(R.id.srl_mine)
    public SwipeRefreshLayout refreshLayout;
    @BindView(R.id.sdv_mineHead)
    public SimpleDraweeView sdv_mineHead;
    @BindView(R.id.tv_mineAlias)
    public TextView tv_mineAlias;
    @BindView(R.id.tv_mineId)
    public TextView tv_mineId;

    @BindView(R.id.tv_mineCarat)
    public TextView tv_mineCarat;
    @BindView(R.id.tv_mineCoin)
    public TextView tv_mineCoin;
    @BindView(R.id.tv_mineMoney)
    public TextView tv_mineMoney;

    @BindView(R.id.lv_mine)
    public ListViewInScrollView listView;
    private Gson gson = new GsonBuilder().create();
    private LoadingDialog loadingDialog;
    UserInfoEntity userInfoEntity;

    @Override
    protected int getViewResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(View v) {
        List<LocalEntity> list = new ArrayList<>();
        for (int i = 0; i < Constant.Strings.MineNames.length; i++) {
            int img = Util.getResId(getActivity(), "mine_icon" + i, "drawable");
            list.add(new LocalEntity(img, Constant.Strings.MineNames[i]));
        }
        listView.setFocusable(false);
        listView.setAdapter(new MineLVAdapter(getActivity(), list));
        listView.setOnItemClickListener(this);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
    }

    @Override
    protected void loadData() {
        //查询个人信息
        String phone = SharedPreferencesUtil.getPhone(getActivity());
        OkHttpUtil.postJson(Constant.URL.getUserInfo, "phone", phone, new OkHttpUtil.OnDataListener() {
            @Override
            public void onResponse(String url, String json) {
                Log.e("loge", "个人信息: " + json);
                userInfoEntity = new Gson().fromJson(json, UserInfoEntity.class);
                dismissLoading();
                setUIView(userInfoEntity);
                SharedPreferencesUtil.saveUserInfo(getActivity(), DesUtil.encrypt(json, DesUtil.LOCAL_KEY));
            }

            @Override
            public void onFailure(String url, String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.initToast(getContext(), "刷新失败，请重试");
                        dismissLoading();
                    }
                });
            }
        });
    }

    @OnClick({R.id.iv_mineSetting, R.id.ll_mineInfo, R.id.tv_mineOrder,
            R.id.tv_mineOrderMark0, R.id.tv_mineOrderMark1, R.id.tv_mineOrderMark2, R.id.tv_mineOrderMark3})
    public void setting(View v) {

        switch (v.getId()) {
            case R.id.iv_mineSetting://设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.ll_mineInfo://个人信息
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.tv_mineOrder://商城订单
                Intent intent0 = new Intent(getActivity(), OrderActivity.class);
                intent0.putExtra("type", 0);
                startActivity(intent0);
                break;
            case R.id.tv_mineOrderMark0://待付款
                Intent intent1 = new Intent(getActivity(), OrderActivity.class);
                intent1.putExtra("type", 1);
                startActivity(intent1);
                break;
            case R.id.tv_mineOrderMark1://待发货
                Intent intent2 = new Intent(getActivity(), OrderActivity.class);
                intent2.putExtra("type", 2);
                startActivity(intent2);
                break;
            case R.id.tv_mineOrderMark2://待收货
                Intent intent3 = new Intent(getActivity(), OrderActivity.class);
                intent3.putExtra("type", 3);
                startActivity(intent3);
                break;
            case R.id.tv_mineOrderMark3://待退款
                Intent intent4 = new Intent(getActivity(), OrderActivity.class);
                intent4.putExtra("type", 4);
                startActivity(intent4);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0://我的收藏
                startActivity(new Intent(getActivity(), MyCollectionActivity.class));
                break;
            case 1://我的推广二维码
                startActivity(new Intent(getActivity(), MyRecommendActivity.class));
                break;
            case 2://我的圈子
                startActivity(new Intent(getActivity(), MyCircleActivity.class));
                break;
            case 3://我的克拉
                startActivity(new Intent(getActivity(), MyRebateActivity.class).putExtra("money", tv_mineCarat.getText()));
                break;
            case 4://我的余额
                startActivity(new Intent(getActivity(), MyMoneyActivity.class).putExtra("money", userInfoEntity.getAccountbalance()));
                break;
            case 5://收货地址管理
                startActivity(new Intent(getActivity(), AddressActivity.class));
                break;
            case 6://客服热线  400-0355-189
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("是否拨打客服热线？");
//                builder.setTitle("是否拨打客服热线？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intentDial = new Intent(Intent.ACTION_DIAL);//调出拨号界面
                        String hotLine = "400-0355-189";
                        intentDial.setData(Uri.parse("tel://" + hotLine.replace("-", "")));
                        startActivity(intentDial);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case 7://客服微信 zzgjjt8
                PhoneUtil.copyBoard(getContext(), "zzgjjt8");
                ToastUtil.initToast(getContext(), "已复制到剪切板");
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    public void setUIView(UserInfoEntity u) {
        sdv_mineHead.setImageURI(Constant.URL.BaseImg + u.getHeadURL());
        tv_mineAlias.setText(u.getName());
        tv_mineId.setText("会员号: " + PhoneUtil.getMobile(u.getPhone()));
        tv_mineCarat.setText(u.getCarat());
        tv_mineCoin.setText(u.getCurrency());
        tv_mineMoney.setText("¥" + u.getAccountbalance());

    }
}
