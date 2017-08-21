package cn.com.zlct.diamondgo.util;

import android.os.Environment;

/**
 * 常量
 */
public interface Constant {

    interface URL {
//        String BaseUrl = "http://192.168.1.253:8888/shoppingManagementSystem/";
//        String BaseImg = "http://192.168.1.253:8888";
        String BaseUrl = "http://www.zgsccn.com:8888/shoppingManagementSystem/";
        String BaseImg = "http://www.zgsccn.com:8888";


        //注册验证码
        String userRegist = BaseUrl + "userLogin/userRegist.do?";
        //注册
        String userRegist3 = BaseUrl + "userLogin/userRegist3.do?";
        //登录
        String userLoginApp = BaseUrl + "userLogin/userLoginApp.do?";
        //通宝登陆
        String getAppCurrencyUser=BaseUrl + "userLogin/getAppCurrencyUser.do?";
        //修改密码  确定登录手机号
        String goUpdatePassword = BaseUrl + "userLogin/goUpdatePassword.do?phone=%s";
        //修改密码
        String updatePasswod = BaseUrl + "userLogin/updatePasswod.do?";
        //除了注册 其他获取手机验证码 都调
        String getVerificationCode = BaseUrl + "userLogin/getVerificationCode.do?";


        //注册
        String Registered = BaseUrl + "userLogin/userRegist3.do?phone=%s&recommPhone=%s&password=%s";
        //登陆
        String Login = BaseUrl + "userLogin.do?phone=%s&password=%s";
        /* //修改密码
         String goUpdatePassword = BaseUrl + "userLogin/goUpdatePassword.do?phone=%s&password=%s";*/

        //修改个人信息
        String goUpdateUserInfo = BaseUrl + "userLogin/goUpdateUserInfo.do?phone=%s";

        //获取中国省、市、区县信息
        String getAddr = BaseUrl + "userLogin/getAddr.do";
        String GetChinaCitys =  "http://192.168.1.150:8410/Index.asmx/GetChinaCitys";

        //获取APP版本
        String GetAppVersion = BaseUrl + "GetAppVersion";
        //轮播图片
        String GetRotateImageList = BaseUrl + "appgetPushInfo.do";
        //查询会员等级与价格
        String getVipInfo = BaseUrl + "getVipInfo.do";
        //大健康专区
        String getSellingCommodity = BaseUrl + "getSellingCommodity.do";
        //理财通宝：
        String getAppQueryfin = BaseUrl + "getAppQueryfin.do";
        //活动专区：
        String appgetCommodityClassList = BaseUrl + "appgetCommodityClassList.do";


        //商品详情
        String appgetCommoditybyId=BaseUrl + "appgetCommoditybyId.do";
        //查询个人信息
        String getUserInfo=BaseUrl + "userLogin/getUserInfo.do";
        //修改个人信息
        String updateUserInfo=BaseUrl+"userLogin/updateUserInfo.do";
        //上传头像
        String appUpHeadImg=BaseUrl+"userLogin/appUpHeadImg.do";
        //获取我的银行卡列表
        String appUserBankBindingQuery=BaseUrl+"appbankcard/appUserBankBindingQuery.do";
        //添加银行卡
        String appUserBankBinding=BaseUrl+"appbankcard/appUserBankBindingadd.do";
        //删除银行卡
        String appUserBankBindingDelete=BaseUrl+"appbankcard/appUserBankBindingDelete.do";
        //订单列表
        String appgetOrderList=BaseUrl+"appgetOrderList.do";
        //查询收藏
        String getCollectionList=BaseUrl+"CollectionManage/getCollectionList.do";
        //分销列表
        String getRecom=BaseUrl+"userLogin/getRecom.do";
        //查询返利
        String getRebate=BaseUrl+"getRebate.do";
        //获取提现列表
        String appgetWithdraw=BaseUrl+"appgetWithdraw.do";
        //获取提现列表
        String appgetIntegral=BaseUrl+"appgetIntegral.do";
        //获取收货地址列表
        String appgetAddress=BaseUrl+"appgetAddress.do";
        //删除收货地址
        String DeleteAddress=BaseUrl+"appdeleteAddress.do";
        //修改收货地址
        String UpdateAddress=BaseUrl+"appupdateAddress.do";
        //设置默认收货地址
        String setDefaultAddress=BaseUrl+"appsetDefaultAddress.do";
        //添加收货地址
        String AddAddress=BaseUrl+"appaddAddress.do";
        //获取推广信息
        String appgetPushInfo=BaseUrl+"appgetPushInfo.do";
        //获取推广信息
        String appgetCommodityClass=BaseUrl+"appgetCommodityClass.do";
        //获取商品列表
        String appgetCommodity=BaseUrl+"appgetCommodity.do";
        //获取购物车信息
        String getShoppingCart=BaseUrl+"/shoppingCartManage/getShoppingCart.do";
        //修改购物车商品数量
        String updateCommodityCount=BaseUrl+"/shoppingCartManage/updateCommodityCount.do";
        //删除购物车商品
        String deleteCommodity=BaseUrl+"/shoppingCartManage/deleteCommodity.do";
        //查询商品评论
        String appQuery=BaseUrl+"/appccomment/appQuery.do";
        //查询商品是否收藏
        String getCommodityCollection=BaseUrl+"/getCommodityCollection.do";
        //查询商品是否加入购物车
        String checkShopping=BaseUrl+"shoppingCartManage/checkShopping.do";
        //购物车新增
        String shoppingCartAdd=BaseUrl+"/shoppingCartManage/shoppingCartAdd.do";
        //收藏新增
        String collectionAdd=BaseUrl+"/CollectionManage/collectionAdd.do";
        //收藏删除
        String deleteCollection=BaseUrl+"/CollectionManage/deleteCollection.do";
        //生成订单
        String appaddOrder=BaseUrl+"appaddOrder.do";
        //支付接口
        String userPayment=BaseUrl+"/userLogin/userPayment.do";
        //删除订单
        String appdeleteOrder=BaseUrl+"/appdeleteOrder.do";
        //确认发货
        String confirmReceipt=BaseUrl+"/userLogin/confirmReceipt.do";
        //申请退款
        String userRefund=BaseUrl+"/userLogin/userRefund.do";
        //订单详情
        String appgetOrder=BaseUrl+"/appgetOrder.do";
        //上传评论
        String appCommentAdd=BaseUrl+"/appccomment/appCommentAdd.do";
        //返利克拉列表
        String getUserCaratList=BaseUrl+"/userLogin/getUserCaratList.do";
        //克拉提现
        String userWithdrawals=BaseUrl+"/userLogin/userWithdrawals.do";
        //版本更新
        String appGetVersionQuery=BaseUrl+"/version/appGetVersionQuery.do";
        //余额提现
        String addWithdrawals=BaseUrl+"withdrawalsManage/addWithdrawals.do";
        //余额提现明细
        String appGetWithdrawalList=BaseUrl+"withdrawalsManage/appGetWithdrawalList.do";
        //获取物流信息
        String queryLogisticsNo=BaseUrl+"/queryLogisticsNo.do";


        //首页全局统计
        String GetIndexStatistics = BaseUrl + "GetIndexStatistics";
        //获取用户未读私信数
        String GetUserLogUnreadTotal = BaseUrl + "GetUserLogUnreadTotal";
        //获取用户私信明细
        String GetPageUserLog = BaseUrl + "GetPageUserLog";
        //修改私信阅读状态
        String UpdateUserLogReadMark = BaseUrl + "UpdateUserLogReadMark";
        //获取商家列表
        String GetPageUsers = BaseUrl + "GetPageUsers";
        //商家入驻字典类型
        String GetDataItemList = BaseUrl + "GetDataItemList";
        //获取用户实体信息
        String GetEntityUser = BaseUrl + "GetEntityUser";
        //获取统计中心数据
        String GetToDayStatistics = BaseUrl + "GetToDayStatistics";

        //获取银行卡
        String GetPageUserBankCard = BaseUrl + "GetPageUserBankCard";
        //添加银行卡
        String InsertUserBankCard = BaseUrl + "InsertUserBankCard";
        //修改银行卡
        String UpdateUserBankCard = BaseUrl + "UpdateUserBankCard";
        //删除银行卡
        String DeleteUserBankCard = BaseUrl + "DeleteUserBankCard";
        //提现申请
        String WithdrawApplication = BaseUrl + "WithdrawApplication";
        //获取用户提现明细
        String GetPageUserWithdraw = BaseUrl + "GetPageUserWithdraw";

        //获取收货地址
        String GetPageUserAddress = BaseUrl + "GetPageUserAddress";
        //添加收货地址
        String InsertUserAddress = BaseUrl + "InsertUserAddress";
        //删除收货地址
        String DeleteUserAddress = BaseUrl + "DeleteUserAddress";
        //修改收货地址
        String UpdateUserAddress = BaseUrl + "UpdateUserAddress";

        //会员中心
        String UserVip = BaseUrl +"/jsp/common/demo.html";
        //常见问题
        String Replicate = BaseUrl +"_AppReplicate.html";
        //关于我们
        String Synopsis = BaseUrl +"_AppSynopsis.html";
        //服务协议
        String ServiceAgreement = BaseUrl +"_AppServiceAgreement.html";
        //帮助中心
        String Help = "http://web.zhdzxyg.com/News/Detail/360528c9-4900-443f-b300-6a9da197b0cf";
        //联系我们
        String ContactUs = "http://web.zhdzxyg.com/News/Detail/0acd8bdd-b801-4cf2-9a66-b3052db032c1";
        //客服QQ
        String QQService = "mqqwpa://im/chat?chat_type=wpa&uin=%s&version=1";
    }

    interface Strings {
        //手机号正则
        String RegexMobile = "^1(3[0-9]|4[5,7]|5[0-9]|7[0-9]|8[0-9])\\d{8}$";
        //邮箱正则
        String RegexEmail = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        //身份证正则
        String RegexIdNum = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

        //注册类别
        String[] MineNames = { "我的收藏",  "我的推广二维码", "我的圈子",
                 "我的克拉","我的余额", "收货地址管理","客服热线","客服微信"};
        String[] ProjectTabNames={"商品详情","评价"};

        //权限提醒
        String PermissionFileTips = "本应用保存数据时被系统拒绝，请手动授权。\n" +
                "授权方式：点击设置按钮进入应用设置页面，选择权限(或权限管理)->存储空间\n请选择允许";
        //权限提醒
        String PermissionCameraTips = "本应用未获取到拍照权限，请手动授权。\n" +
                "授权方式：点击设置按钮进入应用设置页面，选择权限(或权限管理)->相机\n请选择允许";

        // " 号错误提示
        String ErrorTips1 = "不可包含 \" 号";
        // \ 号错误提示
        String ErrorTips2 = "不可包含 \\ 号";
        // | 号错误提示
        String ErrorTips3 = "不可包含 | 号";
        // _ 号错误提示
        String ErrorTips4 = "不可包含 _ 号";

        String SUC = "success";
    }


    interface IdString {
        String QQ_APP_ID = "1105319451";
        String WeChat_APP_ID = "wx74425f5021cc397b";
        String WeChat_APP_Secret = "4b88ce2072e86c4390cb21f0fe56f4ab";
        String WeChat_State = "wechat_sdk_diamondgo";

        String AliPayCompany = "钻购商场";
        String AliPay_APP_ID = "2017060207403210";
        String AliPayPARTNER = "2088721059692241";//支付宝 商户PID
        String AliPaySELLER = "2984487294@qq.com";//支付宝 商户收款账号
    }

    interface Integers {
        //操作成功
        int SUC = 200;
        //操作失败
        int FAIL = 300;
        //数据为空
        int NULL = 400;
        //数据异常
        int ABNORMAL = 500;

        //短信验证码重试秒数
        int CodeRetryTime = 120;
        //用户编号基底
        long BaseUserCode = 10000000;
        //中奖号码基底
        long BaseLuckyCode = 10000001;
        //验证码失效时间 --130秒
        long CodeInvalidTime = 130 * 1000;
        //加入购物车动画时长
        long CartAnimDuration = 1500;
        //输入框较长的长度限制
        int EditLengthLong = 140;
        //输入框一般的长度限制
        int EditLengthMiddle = 60;
        //输入框较短的长度限制
        int EditLengthShort = 20;
    }


    interface Code {
        //打开相册请求码
        int AlbumCode = 0x0001;
        //拍照请求码
        int CameraCode = 0x0002;
        //修改昵称请求码
        int EditAliasCode = 0x0003;
        //修改性别请求码
        int EditGenderCode = 0x0004;

        //修改手机请求码
        int UpdateMobileCode = 0x0006;
        //修改邮箱请求码
        int UpdateEmailCode = 0x0007;
        //修改详细地址请求码
        int UpdateAddressCode = 0x0008;
        //提现密码
        int WithdrawPwdCode = 0x000A;
        //编辑地址请求码
        int EditAddressCode = 0x0009;
        //添加地址请求码
        int AddAddressCode = 0x0008;

        int PermissionCode = 0x1001;
        int PermissionStorageCode = 0x1002;
        int PermissionCameraCode = 0x1003;

        //登录跳转注册请求码
        int RegisterCode = 0x2001;
        //进入登录页请求码
        int IntoLoginCode = 0x2002;
        //进入认证
        int IntoCertifyCode = 0x2003;

        int EditCode = 0x3001;
        int AddCode = 0x3002;

        //支付结果请求码
        int PaymentCode = 0x1003;
    }

    interface Cache {
        String CacheDir = "Android/data/cn.com.zlct.diamond/files";
        String SmallCacheDir = "Android/data/cn.com.zlct.diamond/sfiles";
        String TextCacheDir = Environment.getExternalStorageDirectory() + "/Android/data/cn.com.zlct.diamond/cache";
        String ApkDownDir = Environment.getExternalStorageDirectory() + "/Download";
    }
}