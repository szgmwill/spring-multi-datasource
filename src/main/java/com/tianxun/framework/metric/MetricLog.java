package com.tianxun.framework.metric;

public class MetricLog {

    public static final int CHANNEL_FLIGHT = 1;
    public static final int CHANNEL_HOTEL = 2;
    public static final int CHANNEL_CARHIRE = 5;
    
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAIL = 0;
    
    public static final int HASDATA_TRUE = 1;    
    public static final int HASDATA_FALSE = 0;
    
    // api请求类型
    // 天巡b2b api调用
    public static final String TYPE_HOTEL_ENTITY = "hotel_entity";
    public static final String TYPE_AUTO_SUGGEST = "auto_suggest";
    public static final String TYPE_CREATE_DETAIL = "create_detail";
    public static final String TYPE_POLL_DETAIL = "poll_detail";
    public static final String TYPE_CREATE_LIST = "create_list";
    public static final String TYPE_POLL_LIST = "poll_list";    
    public static final String TYPE_POLL_TOTAL = "poll_total";   
    public static final String TYPE_POLL_FIRST = "poll_first";     
    public static final String TYPE_SEB_PRICE = "seb_price";
    public static final String TYPE_BROWSEQUOTES = "browsequotes";
    public static final String TYPE_BROWSEGRID = "browsegrid";
    public static final String TYPE_BROWSEROUTES = "browseroutes";     
    public static final String TYPE_B2B_DATA_SIZE = "data_size";
    public static final String TYPE_B2B_DETAIL_CHANGE = "detail_change";
    public static final String TYPE_B2B_MULTI_CITY_CREATE_SESSION = "multi_session";
    public static final String TYPE_B2B_MULTI_CITY_PULL_DATA = "multi_data";
    
    
    // F51book的requestType
    public static final String TYPE_GetAvailableFlightWithPrice = "getAvailableFlightWithPrice";
    public static final String TYPE_GetModifyAndRefundStipulates = "getModifyAndRefundStipulates";
    public static final String TYPE_CreateNewOrderRequest = "createNewOrderRequest";
    public static final String TYPE_PayOrderRequest = "payOrderRequest";
    public static final String TYPE_CallBackTicketCode = "callBackTicketCode";
    
    // eterm
    public static final String TYPE_GetPnrInfo = "getPnrInfo";
    public static final String TYPE_GetPnrPatInfo = "getPnrPatInfo";
    public static final String TYPE_PnrRequest = "pnrRequest";
    public static final String TYPE_GetTicketInfoByNo = "getTicketInfoByNo";    
    public static final String TYPE_GetTicketInfoByPNR = "getTicketInfoByPNR";
    public static final String TYPE_PnrAuth = "pnrAuth";
    public static final String TYPE_PnrAuthCancel = "pnrAuthCancel";
    public static final String TYPE_RtPnr = "rtPnr";
    public static final String TYPE_XePnr = "xePnr";
    public static final String TYPE_GetPID = "getPID";
    public static final String TYPE_ExeCommand = "exeCommand";
    public static final String TYPE_ReleasePID = "releasePID";
    public static final String TYPE_GetPataFare = "getPataFare";
    
    // ibe
    public static final String TYPE_GetAvItem = "getAvItem";
    public static final String TYPE_GetFDItem = "getFDItem";
    public static final String TYPE_GetAvailability = "getAvailability";    
    
    // ctrip flight
    public static final String TYPE_OTA_FlightSearch = "ota_FlightSearch";
    
    // 17u flight
    public static final String TYPE_FzSearch = "fzSearch";
    
    // hotel detail
    public static final String TYPE_hotel_detail = "hotel_detail";
    
    //靠谱请求类型
    public static final String kopu_Check_Price = "kopu_Check_Price";
    public static final String kopu_Create_Order = "kopu_Create_Order";
    public static final String kopu_Notify_Ticket = "kopu_Notify_Ticket";
    public static final String kopu_Query_Bag = "kopu_Query_Bag";
    public static final String kopu_Query_Order_Detail = "kopu_Query_Order_Detail";
    public static final String kopu_Verfy_Seat = "kopu_Verfy_Seat";
    public static final String kopu_query_policies = "kopu_query_policies";
    public static final String kopu_Query_limit = "kopu_Query_limit";
    public static final String kopu_TICKET_NO = "kopu_Ticket_No";

    public static final String kopu_change_order = "kopu_change_order";
    public static final String kopu_cancel_order = "kopu_cancel_order";
    public static final String domestic_tuniu_request = "domestic_tuniu_request";
    public static final String domestic_tongcheng_request = "domestic_tongcheng_request";
    public static final String domestic_ctrip_request = "domestic_ctrip_request";
    /**
     * 频道  
     * 1:flight 2:hotel 3:travel 4:ticket 5:carhire
     */
    int channel;
    
    /**
     * 1.source_site_id 2.数据供应商code
     */
    String callPartner;
    
    /**
     * 调用类型，参见TYPE_打头的常量
     */
    String requestType;
    
    /**
     * 请求的地址
     */
    String requestUrl;
    
    /**
     * 请求的参数
     */
    String requestParameters;
    
    /**
     * 返回状态 1成功 0失败
     */
    int returnState;
    
    /**
     * 请求的备注,如果是失败的请求则为返回的错误信息或异常信息
     */
    String remark;
    
    /**
     * 请求起始时间戳
     */
    long requestTime;
    
    /**
     * 请求返回时间戳
     */
    long responseTime;
    
    /**
     * 本机ip(局域网或外网ip)
     */
    String serverIp;
    
    /**
     * session ID
     */
    String sessionId;
    
    /**
     * 1有数据 0无数据
     */
    int hasData;
    
    /**
     * 请求总耗时(毫秒)
     */
    int totalRequestTime;
    
    /**
     * 内容
     */
    String content;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getCallPartner() {
        return callPartner;
    }

    public void setCallPartner(String callPartner) {
        this.callPartner = callPartner;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public int getReturnState() {
        return returnState;
    }

    public void setReturnState(int returnState) {
        this.returnState = returnState;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getHasData() {
        return hasData;
    }

    public void setHasData(int hasData) {
        this.hasData = hasData;
    }

    public int getTotalRequestTime() {
        return totalRequestTime;
    }

    public void setTotalRequestTime(int totalRequestTime) {
        this.totalRequestTime = totalRequestTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
