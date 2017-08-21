package cn.com.zlct.diamondgo.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import cn.com.zlct.diamondgo.R;
import cn.com.zlct.diamondgo.base.BaseActivity;
import cn.com.zlct.diamondgo.util.ActionBarUtil;
import cn.com.zlct.diamondgo.util.Constant;
import cn.com.zlct.diamondgo.util.SharedPreferencesUtil;

/**
 * 我的推荐
 * Created by Administrator on 2017/5/23 0023.
 */
public class MyRecommendActivity extends BaseActivity {

    @BindView(R.id.ll_recommend)
    LinearLayout ll_recommend;
    @BindView(R.id.tv_recommentTJ)
    TextView tv_recommentTJ;
    @BindView(R.id.tv_recommentZS)
    TextView tv_recommentZS;
    @BindView(R.id.rl_recommend)
    RelativeLayout rl_recommend;
    @BindView(R.id.rl_iv)
    ImageView rl_iv;


    String download=Constant.URL.BaseUrl+"Download.html";
    String register = "";

    @Override
    protected int getViewResId() {
        return R.layout.activity_recommend;
    }

    @Override
    protected void init() {
        ActionBarUtil.initActionBar(getSupportActionBar(), "我的推荐人", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        register=Constant.URL.BaseUrl+"userLogin/recommendUrl.do?recommPhone="+ SharedPreferencesUtil.getPhone(this);
        ll_recommend.setVisibility(View.VISIBLE);
        rl_recommend.setVisibility(View.GONE);

        tv_recommentTJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_recommend.setVisibility(View.GONE);
                rl_recommend.setVisibility(View.VISIBLE);
                rl_recommend.setBackgroundResource(R.drawable.download);
                rl_iv.setImageBitmap(createQRImage(download, 2000, 2000));
            }
        });
        tv_recommentZS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_recommend.setVisibility(View.GONE);
                rl_recommend.setVisibility(View.VISIBLE);
                rl_recommend.setBackgroundResource(R.drawable.register);
                rl_iv.setImageBitmap(createQRImage(register, 2000, 2000));
            }
        });


    }
    /**
     * String转二维码bitMap
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    private Bitmap generateBitmap(String content, int width, int height) {
//        compile 'com.journeyapps:zxing-android-embedded:3.3.0'
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Bitmap createQRImage(String url, int QR_WIDTH, int QR_HEIGHT) {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
