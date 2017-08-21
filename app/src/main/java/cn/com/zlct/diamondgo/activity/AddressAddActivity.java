package cn.com.zlct.diamondgo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.ConfirmDialog;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.ChinaCitiesEntity;
import cn.com.zlct.diamondgo.model.GetAddressList;
import cn.com.zlct.diamondgo.model.SingleWordEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.CacheUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import cn.com.zlct.diamondgo.util.ToolBarUtil;
import cn.com.zlct.diamondgo.util.Util;
import okhttp3.FormBody;

/**
 * 添加地址
 */
//AdapterView.OnItemSelectedListener,
public class AddressAddActivity extends BaseActivity implements
        OkHttpUtil.OnDataListener, View.OnClickListener, TextView.OnEditorActionListener {

    @BindView(R.id.et_addressRealName)
    public EditText etRealName;
    @BindView(R.id.et_addressMobile)
    public EditText etMobile;
    //    @BindView(R.id.ll_spinnerMeasure)
//    public LinearLayout spinnerMeasure;
    //    @BindView(R.id.spinner_addressProvice)
//    public Spinner spinnerProvice;
//    @BindView(R.id.spinner_addressCity)
//    public Spinner spinnerCity;
//    @BindView(R.id.spinner_addressCounty)
//    public Spinner spinnerCounty;
    @BindView(R.id.et_addressAddress)
    public EditText etAddress;
    @BindView(R.id.rb_defaultEdit)
    public RadioButton rbDefault;
    @BindView(R.id.addaddress_area)
    public TextView addaddress_area;
    @BindView(R.id.addaddress_area_off)
    public ImageView addaddress_area_off;

    private int type;//0表示添加地址，1表示编辑地址
//    private ArrayAdapter<String> spinnerProviceAdapter;//省 适配器
//    private ArrayAdapter<String> spinnerCityAdapter;//市 适配器
//    private ArrayAdapter<String> spinnerCountyAdapter;//区 适配器
//    private List<ChinaCitiesEntity.DataEntity> citiesData;
//    private List<String> provinceData;//省
//    private List<String> cityData;//市
//    private List<String> countyData;//区
//    private String[] currentData;//默认省 市 区信息
//    private int currentProviceIndex;
//    private int currentCityIndex;
//    private int currentCountyIndex;


    private String userId;
    private LoadingDialog loadingDialog;
    String addressID;


    /**
     * 城市json解析
     *
     * @param savedInstanceState
     */
    String TOG = "aaaJSON";
    String areaNames[];//区
    String provinceNames[];//省
    String cityNames[];//市
    String provinceName;
    int provinceid;
    String areaName;
    int areaid;
    String cityName;
    int cityid;
    String area, province, city;
    boolean isonClick = true;


    @Override
    protected int getViewResId() {
        return R.layout.activity_addaddress;
    }


    @Override
    protected void init() {
        userId = SharedPreferencesUtil.getUserId(this);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        boolean isDefault = intent.getBooleanExtra("isDefault", false);
        String address = intent.getStringExtra("address");
        addressID = intent.getStringExtra("id");

        if (type==1){
            etRealName.setText(intent.getStringExtra("name"));
            etMobile.setText(intent.getStringExtra("phone"));
        }

        initData();
        rbDefault.setSelected(isDefault);

        etAddress.setOnEditorActionListener(this);
    }

    /**
     * 初始化相关数据
     */
    private void initData() {

        if (type == 0) {
            ActionBarUtil.initActionBar(getSupportActionBar(), "新增收货地址", this, "保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String realName = etRealName.getText().toString();
                    String address = etAddress.getText().toString();
                    if (etRealName.getText().length() == 0) {
                        ToastUtil.initToast(AddressAddActivity.this, "请填写收货人");
                    } else if (realName.contains("\"") || realName.contains("\\") || realName.contains("|")) {
                        etRealName.setText(realName.replace("\"", " ").replace("\\", " ").replace("|", " ").trim());
                        etRealNameChanged(etRealName.getText());
                        saveAddress(v);
                    } else if (etMobile.getText().length() == 0) {
                        ToastUtil.initToast(AddressAddActivity.this, "请填写手机");
                    } else if (!etMobile.getText().toString().matches(Constant.Strings.RegexMobile)) {
                        ToastUtil.initToast(AddressAddActivity.this, "请输入正确的手机号码");
                    } else if (etAddress.getText().length() == 0) {
                        ToastUtil.initToast(AddressAddActivity.this, "请填写详细地址");
                    } else if (address.contains("\"") || address.contains("\\") || address.contains("|") || address.contains("_")) {
                        etAddress.setText(address.replace("\"", " ").replace("\\", " ").replace("|", " ").replace("_", " ").trim());
                        etAddressChanged(etAddress.getText());
                        saveAddress(v);
                    } else {
                        PhoneUtil.hideKeyboard(v);
                        loadingDialog = LoadingDialog.newInstance("保存中");
                        loadingDialog.show(getFragmentManager(), "loading");
                        String s = province + city + area + etAddress.getText().toString();
                        if (type == 0) {//添加
                            OkHttpUtil.postAddress(Constant.URL.AddAddress, "", userId, s, rbDefault.isSelected() ? "1" : "0", etRealName.getText().toString(), etMobile.getText().toString(), AddressAddActivity.this);
                        } else if (type == 1) {//修改
                            OkHttpUtil.postAddress(Constant.URL.AddAddress, addressID, userId, s, rbDefault.isSelected() ? "1" : "0", etRealName.getText().toString(), etMobile.getText().toString(), AddressAddActivity.this);
                        }
                    }
                }
            });
        } else {
            ActionBarUtil.initActionBar(getSupportActionBar(), "编辑收货地址", this);
        }
    }


    @Override
    protected void loadData() {
//        if (addressList != null) {
//            int index = 0;
//            for (int i = 0; i < addressList.size(); i++) {
//                provinceData.add(addressList.get(i).getname());
//                Log.i("aaa", currentData[0]);
//                Log.i("aaa", provinceData.get(i));
//                if (!TextUtils.isEmpty(currentData[0]) && currentData[0].equals(provinceData.get(i))) {
//                    index = i;
//                }
//            }
//            spinnerProviceAdapter.notifyDataSetChanged();
//            spinnerProvice.setSelection(index);
//        }
    }


    @Override
    public void onResponse(String url, String json) {
        try {
            if (!TextUtils.isEmpty(json)) {
                if (Constant.URL.getAddr.equals(url)) {//获取中国省市区信息
                    Log.e("loge", "地址信息"+json);
                        setaddress(json);
                        CacheUtil.setUrlCache(this, Constant.URL.getAddr, json);
//                    }
                } else {
                    String decrypt = json;
//                    Log.e("loge", "添加收获地址: " + decrypt);
                    SingleWordEntity addressEntity = new Gson().fromJson(decrypt, SingleWordEntity.class);
                    if (Constant.URL.AddAddress.equals(url)) {//添加收获地址

                        ToastUtil.initToast(this, addressEntity.getMsg());
                        Intent data = new Intent();
                        setResult(RESULT_OK, data);
                        finish();

                    } else if (Constant.URL.UpdateAddress.equals(url)) {//修改收获地址
                        ToastUtil.initToast(this, addressEntity.getMsg());
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onFailure(String url, String error) {
    }

    /**
     * EditText内容变化监听事件 -- 真实姓名
     */
    @OnTextChanged(value = R.id.et_addressRealName, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void etRealNameChanged(CharSequence text) {
        PhoneUtil.isEditError3(text.toString(), etRealName);
    }

    /**
     * EditText内容变化监听事件 -- 详细地址
     */
    @OnTextChanged(value = R.id.et_addressAddress, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void etAddressChanged(CharSequence text) {
        PhoneUtil.isEditError4(text.toString(), etAddress);
    }


    private void setaddress(String json) {
        if (isonClick) {
            isonClick = false;
            try {
                final JSONArray jsonArray = new JSONObject(json).getJSONArray("data");
                /**
                 * 省解析
                 */
                JSONObject jsonProvinceNameObject = null;
                provinceNames = new String[jsonArray.length()];
                for (int province = 0; province < jsonArray.length(); province++) {
                    jsonProvinceNameObject = jsonArray.getJSONObject(province);
                    provinceNames[province] = jsonProvinceNameObject.getString("areaName");
//                    Log.i(TOG, "省" + jsonProvinceNameObject.toString());
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddressAddActivity.this);
                final JSONObject finalJsonProvinceNameObject = jsonProvinceNameObject;

                dismissLoading();
                builder.setTitle("选择城市").setItems(provinceNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        provinceName = provinceNames[which];
                        provinceid = which;
                        province = provinceName;
                        Toast.makeText(AddressAddActivity.this, provinceName, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                        /**
                         * 市解析
                         */
                        try {
                            JSONObject jsonCityObject = jsonArray.getJSONObject(provinceid);
                            //{"Layer":1,"AreaName":"北京","list":[{"Layer":2,"AreaName":"北京市","list":[{"Layer":3,"AreaName":"西城区","list":[{"Layer":4,"AreaName":"西长安街街道","list":[],"ParentId":"110102","AreaId":"110102001"},{"Layer":4,"AreaName":"新街口街道","list":[],"ParentId":"110102","AreaId":"110102003"},{"Layer":4,"AreaName":"月坛街道","list":[],"ParentId":"110102","AreaId":"110102007"},{"Layer":4,"AreaName":"展览路街道","list":[],"ParentId":"110102","AreaId":"110102009"},{"Layer":4,"AreaName":"德胜街道","list":[],"ParentId":"110102","AreaId":"110102010"},{"Layer":4,"AreaName":"金融街街道","list":[],"ParentId":"110102","AreaId":"110102011"},{"Layer":4,"AreaName":"什刹海街道","list":[],"ParentId":"110102","AreaId":"110102012"},{"Layer":4,"AreaName":"大栅栏街道","list":[],"ParentId":"110102","AreaId":"110102013"},{"Layer":4,"AreaName":"天桥街道","list":[],"ParentId":"110102","AreaId":"110102014"},{"Layer":4,"AreaName":"椿树街道","list":[],"ParentId":"110102","AreaId":"110102015"},{"Layer":4,"AreaName":"陶然亭街道","list":[],"ParentId":"110102","AreaId":"110102016"},{"Layer":4,"AreaName":"广安门内街道","list":[],"ParentId":"110102","AreaId":"110102017"},{"Layer":4,"AreaName":"牛街街道","list":[],"ParentId":"110102","AreaId":"110102018"},{"Layer":4,"AreaName":"白纸坊街道","list":[],"ParentId":"110102","AreaId":"110102019"},{"Layer":4,"AreaName":"广安门外街道","list":[],"ParentId":"110102","AreaId":"110102020"}],"ParentId":"110100","AreaId":"110102"},{"Layer":3,"AreaName":"崇文区","list":[],"ParentId":"110100","AreaId":"110103"},{"Layer":3,"AreaName":"宣武区","list":[],"ParentId":"110100","AreaId":"110104"},{"Layer":3,"AreaName":"朝阳区","list":[{"Layer":4,"AreaName":"建外街道","list":[],"ParentId":"110105","AreaId":"110105001"},{"Layer":4,"AreaName":"朝外街道","list":[],"ParentId":"110105","AreaId":"110105002"},{"Layer":4,"AreaName":"呼家楼街道","list":[],"ParentId":"110105","AreaId":"110105003"},{"Layer":4,"AreaName":"三里屯街道","list":[],"ParentId":"110105","AreaId":"110105004"},{"Layer":4,"AreaName":"左家庄街道","list":[],"ParentId":"110105","AreaId":"110105005"},{"Layer":4,"AreaName":"香河园街道","list":[],"ParentId":"110105","AreaId":"110105006"},{"Layer":4,"AreaName":"和平街街道","list":[],"ParentId":"110105","AreaId":"110105007"},{"Layer":4,"AreaName":"安贞街道","list":[],"ParentId":"110105","AreaId":"110105008"},{"Layer":4,"AreaName":"亚运村街道","list":[],"ParentId":"110105","AreaId":"110105009"},{"Layer":4,"AreaName":"小关街道","list":[],"ParentId":"110105","AreaId":"110105010"},{"Layer":4,"AreaName":"酒仙桥街道","list":[],"ParentId":"110105","AreaId":"110105011"},{"Layer":4,"AreaName":"麦子店街道","list":[],"ParentId":"110105","AreaId":"110105012"},{"Layer":4,"AreaName":"团结湖街道","list":[],"ParentId":"110105","AreaId":"110105013"},{"Layer":4,"AreaName":"六里屯街道","list":[],"ParentId":"110105","AreaId":"110105014"},{"Layer":4,"AreaName":"八里庄街道","list":[],"ParentId":"110105","AreaId":"110105015"},{"Layer":4,"AreaName":"双井街道","list":[],"ParentId":"110105","AreaId":"110105016"},{"Layer":4,"AreaName":"劲松街道","list":[],"ParentId":"110105","AreaId":"110105017"},{"Layer":4,"AreaName":"潘家园街道","list":[],"ParentId":"110105","AreaId":"110105018"},{"Layer":4,"AreaName":"垡头街道","list":[],"ParentId":"110105","AreaId":"110105019"},{"Layer":4,"AreaName":"南磨房","list":[],"ParentId":"110105","AreaId":"110105021"},{"Layer":4,"AreaName":"高碑店","list":[],"ParentId":"110105","AreaId":"110105022"},{"Layer":4,"AreaName":"将台","list":[],"ParentId":"110105","AreaId":"110105023"},{"Layer":4,"AreaName":"太阳宫","list":[],"ParentId":"110105","AreaId":"1
                            final JSONArray jsonCityArray = jsonCityObject.getJSONArray("chirldData");
                            cityNames = new String[jsonCityArray.length()];
                            for (int city = 0; city < jsonCityArray.length(); city++) {
                                JSONObject o1 = jsonCityArray.getJSONObject(city);
                                cityNames[city] = o1.getString("areaName");
//                                Log.i(TOG, "市" + o1.toString());
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddressAddActivity.this);
                            builder.setTitle(provinceName).setItems(cityNames, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cityName = cityNames[which];
                                    cityid = which;
                                    city = cityName;
                                    Toast.makeText(AddressAddActivity.this, cityName, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                    /**
                                     * 区解析
                                     */
                                    try {
                                        JSONObject jsonAreaObject = jsonCityArray.getJSONObject(cityid);
                                        JSONArray jsonAreaArray = jsonAreaObject.getJSONArray("chirldData");
                                        areaNames = new String[jsonAreaArray.length()];

                                        if (jsonAreaArray.length() > 0) {
                                            for (int area = 0; area < jsonAreaArray.length(); area++) {
                                                JSONObject o1 = jsonAreaArray.getJSONObject(area);
                                                areaNames[area] = o1.getString("areaName");
//                                                Log.i(TOG, "区" + o1.toString());
                                            }
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddressAddActivity.this);
                                            builder.setTitle(provinceName + "  " + cityName).setItems(areaNames, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    areaName = areaNames[which];
                                                    areaid = which;
                                                    area = areaName;
                                                    Toast.makeText(AddressAddActivity.this, provinceName + "  " + cityName + "  " + areaName, Toast.LENGTH_SHORT).show();

                                                    isonClick = false;
                                                    addaddress_area.setText(provinceName + "  " + cityName + "  " + areaName);
                                                    addaddress_area_off.setVisibility(View.VISIBLE);
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.create().show();
                                        } else {
                                            area = "";
                                            isonClick = false;
                                            addaddress_area.setText(provinceName + "  " + cityName);
                                            addaddress_area_off.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            builder.create().show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.create().show();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 保存按钮 默认地址设置
     */
    @OnClick({R.id.rb_defaultEdit, R.id.addaddress_area, R.id.addaddress_area_off})
    public void saveAddress(View v) {
        switch (v.getId()) {
            case R.id.rb_defaultEdit://默认地址设置
                rbDefault.setSelected(!rbDefault.isSelected());
                break;
            case R.id.addaddress_area:
                //获取中国省市区信息
                if (TextUtils.isEmpty(CacheUtil.getUrlCache(Constant.URL.getAddr))) {//未读取到缓存
                    loadingDialog = LoadingDialog.newInstance("获取省市信息中...");
                    loadingDialog.show(getFragmentManager(), "loading");
                    OkHttpUtil.postJson(Constant.URL.getAddr, "0", this);
                } else {
                    setaddress(CacheUtil.getUrlCache(Constant.URL.getAddr));
                }
                break;
            case R.id.addaddress_area_off:
                addaddress_area.setText("");
                addaddress_area_off.setVisibility(View.GONE);
                province = "";
                city = "";
                area = "";
                isonClick = true;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back:
            case R.id.actionbar_detail_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            PhoneUtil.hideKeyboard(v);
            return true;
        }
        return false;
    }
}
