package cn.com.zlct.diamondgo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.adapter.SunImgRVAdapter;
import cn.com.zlct.diamondgo.base.AbsRecyclerViewAdapter;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.custom.ConfirmDialog;
import cn.com.zlct.diamondgo.custom.DepthProgressBar;
import cn.com.zlct.diamondgo.custom.LoadingDialog;
import cn.com.zlct.diamondgo.model.SingleWordEntity;
import cn.com.zlct.diamondgo.model.UserInfoEntity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.BitMapUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.DesUtil;
import cn.com.zlct.diamondgo.util.OkHttpUtil;
import cn.com.zlct.diamondgo.util.PhoneUtil;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;
import cn.com.zlct.diamondgo.util.ToastUtil;
import okhttp3.FormBody;

/**
 * 评论
 */
public class CommentAddActivity extends BaseActivity implements OkHttpUtil.OnDataListener, View.OnClickListener,
        TextView.OnEditorActionListener, OkHttpUtil.OnProgressListener, ConfirmDialog.OnBtnClickListener, AbsRecyclerViewAdapter.OnItemClickListener {

    @BindView(R.id.et_feedBack)
    public EditText etFeedBack;
    @BindView(R.id.ib_delFeedBack)
    public ImageButton btnDel;
    @BindView(R.id.tv_feedBackLimit)
    public TextView tvLimit;
    @BindView(R.id.rv_feedBack)
    public RecyclerView recyclerView;

//    @BindView(R.id.dpb_feed0)
//    public DepthProgressBar progress0;
//    @BindView(R.id.dpb_feed1)
//    public DepthProgressBar progress1;
//    @BindView(R.id.dpb_feed2)
//    public DepthProgressBar progress2;
    private String phone;
    private String nickname;
    private String headURL;
    private String userVIP;
    private String code;
    private String commodity_id;

    private int imgLimit = 3;//图片数量上限
    private Uri addIcon;//添加图片的图标
    private List<Uri> imgList;//选择的图片集合
    private List<Bitmap> alreadyUploadImg;//已上传图片的FilePath集合
    private SunImgRVAdapter recyclerViewAdapter;
    private LoadingDialog loadingDialog;
    private Handler handler = new Handler();
    private Gson gson = new GsonBuilder().create();

    @Override
    protected int getViewResId() {
        return R.layout.activity_commentadd;
    }

    @Override
    protected void init() {
        phone = SharedPreferencesUtil.getUserId(this);
        ActionBarUtil.initActionBar(getSupportActionBar(), "发表评论", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }, "发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
        UserInfoEntity info = PhoneUtil.getUserInfo(this);
        if (info != null) {
            nickname = info.getName();
            headURL = info.getHeadURL();
            userVIP = info.getUserVIP();
        } else {
            return;
        }
        code=getIntent().getStringExtra("code");
        commodity_id=getIntent().getStringExtra("commodity_id");

        etFeedBack.setOnEditorActionListener(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        imgList = new ArrayList<>();
        alreadyUploadImg = new ArrayList<>();
        addIcon = Uri.parse("res://" + getPackageName() + "/" + R.drawable.camera_add);
        imgList.add(addIcon);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewAdapter = new SunImgRVAdapter(this, imgList, this);
        recyclerViewAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onItemClick(View v, int position) {
        if (position == imgList.size() - 1) {//点击最后一个添加图片
            if (imgList.size() == imgLimit + 1) {//已选三张
                ToastUtil.initToast(this, "已选三张了，请删除后再选");
            } else {
                selectImg();
            }
        }
    }

    @Override
    public void onClick(View v) {//删除按钮
        Uri uri = (Uri) v.getTag();
        alreadyUploadImg.remove(uri);
        recyclerViewAdapter.upAlreadyUp(alreadyUploadImg);
        int pos = getImgPosition(uri);
        if (pos >= 0 && pos < imgList.size()) {
            imgList.remove(pos);
            recyclerViewAdapter.notifyItemRemoved(pos);
        }
    }

    /**
     * 选择图库中的图片
     */
    private void selectImg() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.Code.AlbumCode);
    }

    private int getImgPosition(Uri uri) {
        int pos = -1;
        for (int i = 0; i < imgList.size(); i++) {
            if (uri.equals(imgList.get(i))) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uriAlbum = data.getData();
            if (getImgPosition(uriAlbum) >= 0) {
                ToastUtil.initToast(this, "该图片已存在", Toast.LENGTH_LONG);
            } else {
                imgList.remove(imgList.size() - 1);
                imgList.add(uriAlbum);
                imgList.add(addIcon);
                int index = imgList.size() - 2;
                recyclerViewAdapter.notifyItemInserted(index);
                Bitmap bitmap = BitMapUtil.Uri2Bitmap4Review(this, uriAlbum);
                alreadyUploadImg.add(bitmap);
//                显示删除按钮
                View view = recyclerView.getChildAt(index);
                if (view != null) {
                    view.findViewById(R.id.ib_sunImgDel).setVisibility(View.VISIBLE);
                }
                recyclerViewAdapter.upAlreadyUp(alreadyUploadImg);
            }
        }
    }

    @OnTextChanged(value = R.id.et_feedBack, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void etTextChanged(CharSequence text) {
        PhoneUtil.isEditError2WithDelWithNum(text.toString(), etFeedBack, btnDel, tvLimit);
    }

    /**
     * 文字清空
     */
    @OnClick(R.id.ib_delFeedBack)
    public void btnDel() {
        etFeedBack.setText(null);
    }

//
//    private String handleFilePath() {
//        if (imgList.size() > 1) {
//            if (imgList.size() == alreadyUploadImg.size() + 1 || alreadyUploadImg.size() == imgLimit) {
//                StringBuffer stringBuffer = new StringBuffer();
//                for (int i = 0; i < imgList.size() - 1; i++) {
//                    stringBuffer.append(alreadyUploadImg.get(i));
//                    if (i < alreadyUploadImg.size() - 1) {
//                        stringBuffer.append("|");
//                    }
//                }
////                if (alreadyUploadImg.size() == imgLimit) {
////                    Log.i("aaa",alreadyUploadImg.size()+"   "+imgList.size());
////                    stringBuffer.append(alreadyUploadImg.get(imgList.size() - 1));
////                }
//                return stringBuffer.toString();
//            } else {
////                ToastUtil.initToast(this, "图片还在上传中，请稍候再试");
//                return "default";
//            }
//        } else {
//            return "default";
//        }
//    }

    @Override
    public void onResponse(String url, String json) {
        try {
            if (!TextUtils.isEmpty(json)) {
                Log.e("loge", "上传评论: " + json);
                dismissLoading();
                if (Constant.URL.appCommentAdd.equals(url)) {//上传评论
                    JSONObject object=new JSONObject(json);
//                    {"msg":"success"}
                    if (object.getString("msg").equals("success")){
                        ToastUtil.initToast(this, "评论提交成功!");
                        finish();
                    }else {
                        ToastUtil.initToast(this, "评论提交失败!");
                    }
//                    SingleWordEntity info = new Gson().fromJson(decrypt, SingleWordEntity.class);
//                    if ("0".equals(info.getErrNum())) {
//                        ToastUtil.initToast(this, "反馈提交成功，感谢您的参与！");
//                        finish();
//                    } else {
//                        ToastUtil.initToast(this, info.getErrMsg());
//                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void commit() {
        String content = etFeedBack.getText().toString();
//        if (alreadyUploadImg.size() != imgList.size() - 1) {
//            ToastUtil.initToast(this, "图片正在上传中，请稍候");
//        } else
        if (etFeedBack.getText().length() == 0) {
            ToastUtil.initToast(this, "填写完意见后再来提交吧");
            etFeedBack.requestFocus();
//        } else if (content.contains("\"") || content.contains("\\")) {
//            etFeedBack.setText(content.replace("\"", " ").replace("\\", " ").trim());
//            etTextChanged(etFeedBack.getText());
//            commit();
        } else {
//            String filePath = handleFilePath();
//            if (filePath == null) {
//                return;
//            }
            PhoneUtil.hideKeyboard(etFeedBack);
            loadingDialog = LoadingDialog.newInstance("提交中");
            loadingDialog.show(getFragmentManager(), "loading");

            Map<String, String> params =new HashMap<>();
            params.put("id", "-1");// 评论ID
            params.put("comment_content", content);// 评论内容
            params.put("picture_path", "");// 评论图片
            params.put("comment_cell_phone", phone);// 评论手机号码
            params.put("commodity_id", commodity_id);// 商品ID
            params.put("pId", "0");// 父评论ID
            params.put("nickname", nickname);// 昵称
            params.put("headURL", headURL);// 头像
            params.put("userVIP", userVIP);// vip

            OkHttpUtil.postStream(Constant.URL.appCommentAdd,params,0,alreadyUploadImg ,new OkHttpUtil.OnProgressMultiListener(){

                @Override
                public void onProgressMulti(int index, int rate) {
                    Log.i("aaa", "index:" +index+ "  rate:"+rate);
//                    if (rate==100){
//                        progressBar.setVisibility(View.GONE);
//                    }
                }
            },this);

        }
    }

    @Override
    public void onFailure(String url, String error) {
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            PhoneUtil.hideKeyboard(v);
            return true;
        }
        return false;
    }

    /**
     * 处理后退事件
     */
    @Override
    public void onBackPressed() {
        if (imgList.size() > 1) {
            ConfirmDialog confirmDialog = ConfirmDialog.newInstance("真的要返回上一页吗?", "再想想", "返回");
            confirmDialog.show(getFragmentManager(), "back");
            confirmDialog.setOnBtnClickListener(this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBtnClick(View view) throws Exception {
        finish();
    }

    @Override
    public void onProgress(int rate) {

    }
}
