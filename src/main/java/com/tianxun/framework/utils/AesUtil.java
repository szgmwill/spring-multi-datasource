package com.tianxun.framework.utils;

import java.security.Key;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

 
/**
 * AES 加解密
 *
 * @see Base64
 */
public class AesUtil {
    private final static Logger logger = Logger.getLogger(AesUtil.class);
    //-----類別常數-----
    /**
     * 預設的Initialization Vector，為16 Bits的0
     */
    private static final IvParameterSpec DEFAULT_IV = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    /**
     * 加密演算法使用AES
     */
    private static final String ALGORITHM = "AES";
    /**
     * AES使用CBC模式與PKCS5Padding
     */
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
 
    //-----物件變數-----
    /**
     * 取得AES加解密的密鑰
     */
    private Key key;
    /**
     * AES CBC模式使用的Initialization Vector
     */
    private IvParameterSpec iv;
    /**
     * Cipher 物件
     */
    private Cipher cipher;
    
    private static final String DEFAULT_KEY = "UDFNSDF#$$11D12.;L*&^%$#$%PIG092";
    
    private static final String DEFAULT_IVSTR = "DLO@#@^DSF324&*()*(KDJF5DF&*PO12";
    
    private static final String SALT = "n}#S:L$vFj6WV^5f*rG5mMQ{.^s]w.]%pq%?j6A&bDG{xG{$Rs5igmJ)dJrj5PF@sgfw%%2GpSTMRx6J8v?irf[,qp(%}n96fxDKf@p[w]!9v??M&4T]aeTLi6k7RHfH";
    //-----建構子-----
    /**
     * 建構子，使用128 Bits的AES密鑰(計算任意長度密鑰的MD5)和預設IV
     *
     * @param key 傳入任意長度的AES密鑰
     */
    public AesUtil(final String key) {
        this(key, 128);
    }
 
    /**
     * 建構子，使用128 Bits或是256 Bits的AES密鑰(計算任意長度密鑰的MD5或是SHA256)和預設IV
     *
     * @param key 傳入任意長度的AES密鑰
     * @param bit 傳入AES密鑰長度，數值可以是128、256 (Bits)
     */
    public AesUtil(final String key, final int bit) {
        this(key, bit, null);
    }
 
    /**
     * 建構子，使用128 Bits或是256 Bits的AES密鑰(計算任意長度密鑰的MD5或是SHA256)，用MD5計算IV值
     *
     * @param key 傳入任意長度的AES密鑰
     * @param bit 傳入AES密鑰長度，數值可以是128、256 (Bits)
     * @param iv 傳入任意長度的IV字串
     */
    public AesUtil(final String key, final int bit, final String iv) {
        if (bit == 256) {
            this.key = new SecretKeySpec(getHash("SHA-256", key + SALT), ALGORITHM);
        } else {
            this.key = new SecretKeySpec(getHash("MD5", key + SALT), ALGORITHM);
        }
        if (iv != null) {
            this.iv = new IvParameterSpec(getHash("MD5", iv + SALT));
        } else {
            this.iv = DEFAULT_IV;
        }
 
        init();
    }
 
    //-----物件方法-----
    /**
     * 取得字串的雜湊值
     *
     * @param algorithm 傳入雜驟演算法
     * @param text 傳入要雜湊的字串
     * @return 傳回雜湊後資料內容
     */
    private static byte[] getHash(final String algorithm, final String text) {
        try {
            return getHash(algorithm, text.getBytes("UTF-8"));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
 
    /**
     * 取得資料的雜湊值
     *
     * @param algorithm 傳入雜驟演算法
     * @param data 傳入要雜湊的資料
     * @return 傳回雜湊後資料內容
     */
    private static byte[] getHash(final String algorithm, final byte[] data) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(data);
            return digest.digest();
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
 
    /**
     * 初始化
     */
    private void init() {
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
 
    /**
     * 加密文字
     *
     * @param str 傳入要加密的文字
     * @return 傳回加密後的文字
     */
    public String encrypt(final String str) {
        try {
            return encrypt(str.getBytes("UTF-8"));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
 
    /**
     * 加密資料
     *
     * @param data 傳入要加密的資料
     * @return 傳回加密後的資料
     */
    public String encrypt(final byte[] data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            final byte[] encryptData = cipher.doFinal(data);
            return new String(Base64.encodeBase64(encryptData));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
 
    /**
     * 解密文字
     *
     * @param str 傳入要解密的文字
     * @return 傳回解密後的文字
     */
    public String decrypt(final String str) {
        try {
            if(str.length()<24){
                return str;
            }
            String rst = decrypt(Base64.decodeBase64(str));
            if(StringUtils.isNotEmpty(rst)){
                return rst;
            }else{
                return str;
            }
        } catch (final Exception ex) {
            ExceptionLogUtil.logExceptionInfo(logger, ex);
            return str;
        }        
    }
 
    /**
     * 解密文字
     *
     * @param data 傳入要解密的資料
     * @return 傳回解密後的文字
     */
    public String decrypt(final byte[] data) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            final byte[] decryptData = cipher.doFinal(data);
            return new String(decryptData, "UTF-8");
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public static String encryptStr (String content){
        if(StringUtils.isNotEmpty(content) && !content.toUpperCase().equals("NULL")){
            AesUtil aes=new AesUtil(DEFAULT_KEY,256,DEFAULT_IVSTR);
            return aes.encrypt(content);
        }else{
            return "";
        }       
    }
    
    public static String decryptStr (String content){
        AesUtil aes=new AesUtil(DEFAULT_KEY,256,DEFAULT_IVSTR);
        return aes.decrypt(content);
    }
    
    public static String encryptStr128 (String content){
        if(StringUtils.isNotEmpty(content) && !content.toUpperCase().equals("NULL")){
            AesUtil aes=new AesUtil(DEFAULT_KEY,128,DEFAULT_IVSTR);
            return aes.encrypt(content);
        }else{
            return "";
        }        
    }
    
    public static String decryptStr128 (String content){
        if(StringUtils.isNotEmpty(content)){
            AesUtil aes=new AesUtil(DEFAULT_KEY,128,DEFAULT_IVSTR);
            return aes.decrypt(content);
        }else{
            return "";
        }
    }
    
    public static void main(String[] args) {
        /*Map<String, String> mp = new HashMap<String, String>();      
        for(int startId = 11259; startId < 11315; startId++){
            SqlSession session = null;
            session = MybatisFactory.getSession();
            FlightOrderMapper orderMapper = session.getMapper(FlightOrderMapper.class);
            FlightOrderPassengerMapper flightOrderPassengerMapper = session.getMapper(FlightOrderPassengerMapper.class);
            FlightOrder fo = orderMapper.getOrderbyId(startId);
            mp.put("linkman", encryptStr(fo.getLinkman()));
            mp.put("linkman_phone", encryptStr(fo.getLinkman_phone()));
            if(StringUtil.hasText(fo.getLinkman_email())){
                mp.put("linkman_email", encryptStr(fo.getLinkman_email()));
            }            
            mp.put("flight_order_id",startId + "");
            int uo = orderMapper.updateOrderByMap(mp);
            if(uo>0){
                for(FlightOrderPassenger fop : flightOrderPassengerMapper.getOrderPassengerList(startId)){
                    fop.setFirst_name(encryptStr(fop.getFirst_name()));
                    if(StringUtil.hasText(fop.getLast_name())){
                        fop.setLast_name(encryptStr(fop.getLast_name()));
                    }
                    fop.setVoucher_type(encryptStr(fop.getVoucher_type()));
                    fop.setVoucher_code(encryptStr(fop.getVoucher_code()));
                    if(StringUtil.hasText(fop.getBirthday())){
                        fop.setBirthday(encryptStr(fop.getBirthday()));
                    }
                    int up = flightOrderPassengerMapper.updatePassenger(fop);
                    if(up<=0){
                        System.out.println("失败的id" + startId);
                    }else{
                        session.commit();
                    }
                }
            }
        }*/
        
        String content = "ssss";
        String ss = encryptStr(content);
        System.out.println("加密前：" + content);
        System.out.println("加密后：" + ss);
        System.out.println("解密后：" + decryptStr(ss));
        //String content= "4DEB19562987B563D00188D21357E78A";
        //System.out.println(parseHexStr2Byte(content));
    }
}
