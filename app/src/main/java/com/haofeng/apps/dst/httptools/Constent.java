package com.haofeng.apps.dst.httptools;

public class Constent {

    public static String VERSON = "1.1.1";
    public static String VER = "android1.1.1";
    public static final String URLHEAD = "http://120.25.209.72/index-app.php";
    // public static final String URLHEAD =
    // "http://192.168.96.123/car_system/backend/web/index-app.php";

    // 二期
    //public static String URLHEAD_GET = "http://app.dstcar.com/app/";
    //public static String URLHEAD_GET = "http://192.168.1.107/dstzc_new/version2.2.0/app/";
    public static String secret = "0ca12afc883db2be3269e2074df3328b";// 固定值

    // 测试环境
//    public static String URLHEAD_GET = "http://192.168.1.107/dstzc_new/version2.2.0/app/";
//    public static String URLHEAD_IMAGE = "http://192.168.1.107/dstzc_new/";

    // 正式环境
    public static String URLHEAD_GET = "http://app.dstcar.com/v2/index.php?/app/";
    public static String URLHEAD_IMAGE = "http://120.25.209.72/";

    //public static String URLHEAD_IMAGE = "http://app.dstcar.com/";
    //public static String URLHEAD_IMAGE = "http://192.168.1.107/dstzc_new/";
    //public static String URLHEAD_NEW_IMAGE = "http://192.168.1.104/dst/backend/web/";
    public static String URLHEAD_NEW_IMAGE = "http://120.25.209.72/";

    public static final String WEATHER_GET = "http://op.juhe.cn/onebox/weather/query";// 天气信息
    public static final String WEATHER_KEY = "f01eb4aebecdb94983e1c64939defffc";// 天气信息key

    public static final int PUSHHANLDER_CODE = 0Xffee;
    // 为防止混淆，从0X0001到0X00FF用来标示网络请求id 0xFF00-0xFFFF用在网络请求的参数配置上 0x4400-0x44ff
    // 用在界面延迟布局上
    // 这里的协议是原来一期的，实际目前没有用到了。保留的目的是为了配合保留那些写过但是又放弃的界面比如：车辆监控，电桩分享等界面
    public static final String ACT_CHANGEPWD = "editPwd";// 修改密码
    public static final int ID_CHANGEPWD = 0X0004;
    public static final String ACT_FORGETPWD = "forgetPwd";// 忘记密码
    public static final int ID_FORGETPWD = 0X0005;
    public static final String ACT_AUTHENTICA = "";// 身份审核
    public static final int ID_AUTHENTICA = 0X0006;
    public static final String ACT_ORDER = "addEditAppointment";// 预约
    public static final int ID_ORDER = 0X0007;
    public static final String ACT_MYORDER = "getAppointment";// 我的预约
    public static final int ID_MYORDER = 0X0011;
    public static final String ACT_DELETEORDER = "removeAppointment";// 删除预约
    public static final int ID_DELETEORDER = 0X0012;
    public static final String ACT_CARUPDATE = "addEditVehicle";// 修改，添加车辆
    public static final int ID_CARUPDATE = 0X0013;
    public static final String ACT_SHARE = "addEditShare";// 分享
    public static final int ID_SHARE = 0X0017;
    public static final String ACT_GETCAR = "getVehicle";// 获取车辆列表
    public static final int ID_GETCAR = 0X0022;
    public static final String ACT_CARDELETE = "removeVehicle";// 删除车辆
    public static final int ID_CARDELETE = 0X0023;
    public static final String ACT_IMAGEPOST = "upload";// 上传图片
    public static final int ID_IMAGEPOST = 0X0024;
    public static final String ACT_SHARELIST = "getShare";// 分享列表
    public static final int ID_SHARELIST = 0X0025;
    public static final String ACT_RECHARGELIST = "getRechargeRecord";// 充值记录列表

    public static final int ID_RECHARGELIST = 0X0027;
    public static final String ACT_PAYLIST = "vip-account_charge-count-record";// 支付记录列表
    public static final int ID_PAYLIST = 0X0028;
    public static final String ACT_SHAREREMOVE = "share_remove-share";// 删除分享
    public static final int ID_SHAREREMOVE = 0X0029;
    public static final String ACT_GETALIPAY = "recharge";// 请求支付宝配置参数
    public static final int ID_GETALIPAY = 0X0030;
    public static final String ACT_WALLETLIST = "getMoneyChangeRecord";// 钱包金额记录

    public static final int ID_WALLETLIST = 0X0032;
    public static final String ACT_WALLET = "getVipAccount";// 钱包余额
    public static final int ID_WALLET = 0X0033;
    public static final String ACT_GETYZM = "sendMSGCreate";// 注册获取验证码
    public static final int ID_GETYZM = 0X0034;
    public static final String ACT_FORGETPWDGETYZM = "sendMSGExists";// 忘记密码获取验证码

    public static final int ID_FORGETPWDGETYZM = 0X0035;
    public static final String ACT_GETLEASECUSTOM = "getComCusInfo";// 获取企业客户信息

    public static final int ID_GETLEASECUSTOM = 0X0037;
    public static final String ACT_GETLEASECARLIST = "getComCusLetingCar";// 获取企业客户车辆列表

    public static final int ID_GETLEASECARLIST = 0X0038;
    public static final String ACT_GETLEASECARINFOR = "getLetingCarRealtimeDetail";// 获取关联客户
    public static final int ID_GETLEASECARINFOR = 0X0039;
    /**
     * 下面这里是一期还要用到的协议
     */
    public static final String ACT_FUJIN = "charge-station_search-width-condition";// 获取附近电站
    public static final int ID_FUJIN = 0X0003;
    public static final String ACT_COLLECTION = "addFavorite";// 收藏
    public static final int ID_COLLECTION = 0X0008;
    public static final String ACT_STARTCHARGE = "startCharge";// 开始充电
    public static final int ID_STARTCHARGE = 0X0009;
    public static final String ACT_ENDCHARGE = "endCharge";// 充电结束
    public static final int ID_ENDCHARGE = 0X0010;
    public static final String ACT_GETCOLLECTION = "getFavorite";// 获取收藏的电桩信息
    public static final int ID_GETCOLLECTION = 0X0014;
    public static final String ACT_REMOVECOLLECTION = "removeFavorite";// 取消收藏
    public static final int ID_REMOVECOLLECTION = 0X0015;
    public static final String ACT_GETCONNECTIONTYPE = "getConfigs";// 获取电桩类型
    public static final int ID_GETCONNECTIONTYPE = 0X0016;
    public static final String ACT_GETSUGGEST = "getSuggestion";// 获取建议
    public static final int ID_GETSUGGEST = 0X0018;
    public static final String ACT_ADDSUGGEST = "addSuggestion";// 新增建议
    public static final int ID_ADDSUGGEST = 0X0019;
    public static final String ACT_GETNOTICE = "notice_get-notice";// 获取新闻资讯
    public static final int ID_GETNOTICE = 0X0020;
    public static final String ACT_CHARGELIST = "charge_get-charge-record";// 充电记录列表
    public static final int ID_CHARGELIST = 0X0026;

    public static final String ACT_GETCHARGEID = "charge_pole-info";// 获取单个电枪信息
    public static final int ID_GETCHARGEID = 0X0036;
    public static final String ACT_GETCHARGEINFOR = "charge_car-status";// 获取充电详情
    public static final int ID_GETCHARGEINFOR = 0X0040;
    public static final String ACT_GETINDEX = "tip_index";// 首页获取配置信息
    public static final int ID_GETINDEX = 0X0041;
    public static final String ACT_GETCHARGEENDINFOR = "charge_detail";// 充电结束获取信息
    public static final int ID_GETCHARGEENDINFOR = 0X0042;
    public static final String ACT_RECHARGE = "charge_pay";// 用钱包支付
    public static final int ID_RECHARGE = 0X0043;
    public static final String ACT_GETMAPCHARGEINFOR = "charge-station_get-detail";// 获取单个电站详情
    public static final int ID_GETMAPCHARGEINFOR = 0X0045;
    public static final String ACT_GETMAPSEARCHKEYWORD = "charge-station_search-keyword";// 获取地图搜索框下拉列表数据
    public static final int ID_GETMAPSEARCHKEYWORD = 0X0046;
    public static final String ACT_GETMAPSTATIONWITHKEYWORD = "charge-station_search-with-keyword";// 根据关键字查询
    public static final int ID_GETMAPSTATIONWITHKEYWORD = 0X0047;
    public static final String ACT_GETMAPCONNECTIONTYPE = "charge-station_search-condition";// 获取地图界面筛选配置
    public static final int ID_GETMAPCONNECTIONTYPE = 0X0048;
    public static final String ACT_GETSTATIONLIST = "charge-station_station-list";// 获取电站列表数据
    public static final int ID_GETSTATIONLIST = 0X0049;
    public static final String ACT_GETOPERATIONHELP = "guide_get-guide-url";// 获取操作指南
    public static final int ID_GETOPERATIONHELP = 0X0050;
    public static final String ACT_CHECKUPDATE = "version-check_check";// 获取升级信息
    public static final int ID_CHECKUPDATE = 0X0051;

    // 二期的协议请求
    // 这里是分割线----------------------------------------------------------------------------------------------------------------
    public static final String ACT_LOGIN_INDEX = "login/index";// 登录
    public static final int ID_LOGIN_INDEX = 0X0052;
    public static final String ACT_SMS_CODE_INDEX = "sms_code/index";// 短信验证码
    public static final int ID_SMS_CODE_INDEX = 0X0053;
    public static final String ACT_REGISTER_INDEX = "register/index";// 注册
    public static final int ID_REGISTER_INDEX = 0X0054;
    public static final String ACT_REGION_INDEX = "region/get_list";// 获取城市列表
    public static final int ID_REGION_INDEX = 0X0055;
    public static final String ACT_CAR_LIST = "car/get_list";// 获取车辆列表
    public static final int ID_CAR_LIST = 0X0056;
    public static final String ACT_CAR_DETAIL = "car/get_detail";// 获取车辆详情
    public static final int ID_CAR_DETAIL = 0X0057;
    public static final String ACT_CAR_DISPATCH = "car/get_dispatch_record";// 车辆出车时间段
    public static final int ID_CAR_DISPATCH = 0X0058;
    public static final String ACT_STORE_LIST = "car/get_store_list";// 门店列表
    public static final int ID_STORE_LIST = 0X0059;
    public static final String ACT_MEMBER_INFOR = "member/info";// 获取会员信息
    public static final int ID_MEMBER_INFOR = 0X0060;
    public static final String ACT_ORDER_LIST = "order/get_list";// 获取订单列表
    public static final int ID_ORDER_LIST = 0X0061;
    public static final int ID_ORDER_LIST2 = 0X0101;
    public static final int ID_ORDER_LIST3 = 0X0102;
    public static final int ID_ORDER_LIST4 = 0X0103;
    public static final String ACT_LONG_RENT = "long_rent_apply";// 企业长租
    public static final int ID_LONG_RENT = 0X0062;
    public static final String ACT_AUTH_INDEX = "auth/index";// 会员认证
    public static final int ID_AUTH_INDEX = 0X0063;
    public static final String ACT_ORDER_CHANGE_ADD = "order/change_add";// 申请换车
    public static final int ID_ORDER_CHANGE_ADD = 0X0064;
    public static final String ACT_ORDER_AGENT = "order/agent";// 他人代取
    public static final int ID_ORDER_AGENT = 0X0065;
    public static final String ACT_ORDER_RELET = "order/relet";// 续租
    public static final int ID_ORDER_RELET = 0X0066;
    public static final String ACT_ORDER_AMOUNT_CONFIRMT = "order/amount_confirm";// 订单费用确认
    public static final int ID_ORDER_AMOUNT_CONFIRMT = 0X0067;
    public static final String ACT_AUTH_ID_CARD_AUTH = "auth/id_card_auth";// 上传身份证
    public static final int ID_AUTH_ID_CARD_AUTH = 0X0068;
    public static final String ACT_AUTH_DRIVING_CARD_AUTH = "auth/driving_card_auth";// 上传驾驶证
    public static final int ID_AUTH_DRIVING_CARD_AUTH = 0X0069;
    public static final String ACT_MEMBER_MESSAGE_LIST = "member/message_list";// 获取会员消息列表
    public static final int ID_MEMBER_MESSAGE_LIST = 0X0070;
    public static final String ACT_MEMBER_OPER_MESSAGE = "member/oper_message";// 会员消息列表操作
    public static final int ID_MEMBER_OPER_MESSAGE = 0X0071;
    public static final int ID_MEMBER_OPER_MESSAGE_READ = 0X0072;// 会员消息操作阅读
    public static final String ACT_CAR_GET_DISPATCH_RECORD = "car/get_dispatch_record";// 获取车辆出车时间段
    public static final int ID_CAR_GET_DISPATCH_RECORD = 0X0073;
    public static final String ACT_ORDER_ADD = "order/add";// 3.1订单添加
    public static final int ID_ORDER_ADD = 0X0074;
    public static final String ACT_ORDER_GET_DETAIL = "order/get_detail";// 订单详情
    public static final int ID_ORDER_GET_DETAIL = 0X0075;
    public static final String ACT_MEMBER_ALTER_PWD = "member/alter_pwd";// 修改密码
    public static final int ID_MEMBER_ALTER_PWD = 0X0076;
    // public static final String ACT_MEMBER_GET_ACCOUNT_FLOW_LIST =
    // "member/get_account_flow_list";// 账户流水
    public static final String ACT_MEMBER_PAYMENTS = "member/payments";// 账户流水
    public static final int ID_MEMBER_PAYMENTS = 0X0077;
    public static final String ACT_CREATE_ORDER_PAY = "create/order_pay";// 创建订单---支付
    public static final int ID_CREATE_ORDER_PAY = 0X0078;
    public static final String ACT_CREATE_RECHARGE = "create/recharge";// 创建订单---充值
    public static final int ID_CREATE_RECHARGE = 0X0079;
    public static final String ACT_CAR_GET_STORE_LIST = "car/get_store_list";// 门店列表
    public static final int ID_CAR_GET_STORE_LIST = 0X0080;
    public static final String ACT_WEATHER_INFOR = "weather";// 天气信息
    public static final int ID_WEATHER_INFOR = 0X0081;// 获取天气信息
    public static final String ACT_CHECK_IS_PAY_SUCCESS = "check/is_pay_success";// 验证支付宝支付是否成功
    public static final int ID_CHECK_IS_PAY_SUCCESS = 0X0082;
    public static final String ACT_REGION_GET_ID = "region/get_id";// 通过城市名称获取服务端id
    public static final int ID_REGION_GET_ID = 0X0083;
    public static final String ACT_MEMBER_FORGET_PWD = "login/forget_pwd";// 忘记密码
    public static final int ID_MEMBER_FORGET_PWD = 0X0084;
    public static final String ACT_AUTH_BUSINESS_LICENSE_AUTH = "auth/business_license_auth";// 上传营业执照
    public static final int ID_AUTH_BUSINESS_LICENSE_AUTH = 0X0085;
    public static final String ACT_MEMBER_MY_WZ = "member/my_wz";// 违章列表
    public static final int ID_MEMBER_MY_WZ = 0X0086;
    public static final String ACT_CREATE_WZ_PAY = "create/wz_pay";// 违章订单创建
    public static final int ID_CREATE_WZ_PAY = 0X0087;
    public static final String ACT_ORDER_CANCEL_ORDER = "order/cancel_order";// 取消订单
    public static final int ID_ORDER_CANCEL_ORDER = 0X0088;
    public static final String ACT_ORDER_WZ_RECORD = "order/wz_record";// 违章结算中信息
    public static final int ID_ORDER_WZ_RECORD = 0X0089;
    public static final String ACT_CAR_GET_PAY_CUBE_LIST = "car/get_pay_cube_list";// 获取货箱容积
    public static final int ID_CAR_GET_PAY_CUBE_LIST = 0X0090;
    public static final String ACT_CAR_GET_CAR_BRABD = "car/get_car_brand";// 获取车辆品牌
    public static final int ID_CAR_GET_CAR_BRABD = 0X0091;
    public static final String ACT_MEMBER_UPDATE_INFOR = "member/update_info";// 用户信息编辑
    public static final int ID_MEMBER_UPDATE_INFOR = 0X0092;
    public static final String ACT_SHORT_RENT_GET_LIST = "short_rent/get_list";// 短租列表
    public static final int ID_SHORT_RENT_GET_LIST = 0X0093;
    public static final String ACT_SHORT_RENT_CREATE_ORDER = "short_rent/create_order";// 短租添加订单
    public static final int ID_SHORT_RENT_CREATE_ORDER = 0X0094;
    public static final String ACT_MEMBER_LATEST_NOTICE = "member/latest_notice";// 获取通知最新时间
    public static final int ID_MEMBER_LATEST_NOTICE = 0X0095;

    public static final String ACT_GET_CAR_LIST = "new_order/get_list";// 获取车辆列表
    public static final int ID_GET_CAR_LIST = 0X0096;

    public static final String ACT_GET_CAR_DETAIL = "new_order/get_detail";// 获取车辆详情
    public static final int ID_GET_CAR_DETAIL = 0X0097;
    public static final String ACT_ADD_ORDER = "new_order/create_order";// 添加订单
    public static final int ID_GET_ACT_ADD_ORDER = 0X0098;
    public static final String ACT_SHAO_MAO_LOGGING = "device/login";// 扫码登录
    public static final int ID_GET_SHAO_MAO_LOGGING = 0X0099;
    public static final String ACT_WEI_ZHANG_SEARCH = "search/wz";// 违章查询
    public static final int ID_ACT_WEI_ZHANG_SEARCH = 0X0120;
    public static final String ACT_WEI_ZHANG_ORDER = "create/wz_generation_pay";// 创建违章代缴订单
    public static final int ID_ACT_WEI_ZHANG_ORDER = 0X0121;
    public static final String ACT_CREATE_CAR_ASSOCIATION = "associated_car/add";// 创建车辆关联
    public static final int ID_ACT_CREATE_CAR_ASSOCIATION = 0X0122;
    public static final String ACT_MY_CAR_LIST = "associated_car/get_list";//我的车辆
    public static final int ID_ACT_MY_CAR_LIST = 0X0123;
    public static final int ID_ACT_MY_CAR_LIST2 = 0X0124;
    public static final String ACT_MY_GET_MONEY_DETAIL = "money_detailed/get_list";//明细
    public static final int ID_ACT_MY_GET_MONEY_DETAIL = 0X0125;
    public static final String ACT_YAJINCONGZHI = "create/foregift_recharge";//押金充值
    public static final int ID_ACT_YAJINCONGZHI = 0X0126;
    public static final String ACT_GET_ORDERlIST = "order/get_list";//获取订单列表
    public static final int ID_ACT_GET_ORDERlIST = 0X0127;
    public static final String ACT_YAJINTIXIAN = "deposit_extract/extract_money";//押金提现
    public static final int ID_ACT_YAJINTIXIAN = 0X0128;
    public static final String ACT_YAJINTIXIANRECORD = "deposit_extract/record";//押金提现
    public static final int ID_ACT_YAJINTIXIANRECORD = 0X0129;
    public static final String ACT_GET_ALWAYS_USE_CAR = "associated_car/common";//常用车辆
    public static final int ID_ACT_GET_ALWAYS_USE_CAR = 0X0130;
    public static final String ACT_GET_CONCEL_ASSOCIATE = "associated_car/cancel";//常用车辆
    public static final int ID_ACT_GET_CONCEL_ASSOCIATE = 0X0131;
    //V2.2.0新版接口
    public static final String ACT_NEW_GET_CAR_LIST = "car/get_list";// 获取车辆列表
    public static final int ID_GET_NEW_CAR_LIST = 0X0132;
    public static final String ACT_NEW_GET_CAR_DETAIL = "car/get_detail";// 获取车辆详情
    public static final int ID_GET_NEW_CAR_DETAIL = 0X0133;
    public static final String ACT_RENT_CONFIG = "rent/config";// 获取租赁配置
    public static final int ID_GET_ACT_RENT_CONFIG = 0X0134;
    public static final String ACT_CAR_SITE = "car/car_site";// 获取提车门店
    public static final int ID_GET_ACT_CAR_SITE = 0X0135;
    public static final String ACT_SYSTEM_TIME = "system/time";// 获取时间
    public static final int ID_GET_ACT_SYSTEM_TIME = 0X0136;
    public static final String ACT_NEW_ORDER_LIST = "order/get_list";// 获取订单列表
    public static final int ID_ACT_NEW_ORDER_LIST = 0x0137;// 获取订单列表
    public static final String ACT_NEW_ORDER_DETAIL = "order/get_detail";// 获取订单详情
    public static final int ID_ACT_NEW_ORDER_DETAIL = 0x0138;// 获取订单列表
    public static final String ACT_NEW_ADD_ORDER = "order/create_order";// 添加订单
    public static final int ID_ACT_NEW_ADD_ORDER = 0x0139;// 获取订单列表
}
