package com.really.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneInfo {

	 private TelephonyManager telephonemanager;
	 
	 private String IMSI;
	 
	 /**
	     * 获取手机国际识别码IMEI
	     * */
	    public  PhoneInfo(Context context){
	        telephonemanager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	    }
	    
	    /**
	     * 获取手机号码 
	     * */
	    public String getNativePhoneNumber(){
	       
	       String nativephonenumber=null;
	       nativephonenumber=telephonemanager.getLine1Number();
	        
	       return nativephonenumber;        
	    }
	    
	    /**
	     * 获取手机服务商信息
	     *  
	     * */
	    public String  getProvidersName(){
	        String providerName=null;
	        try{
	            IMSI=telephonemanager.getSubscriberId();
	            //IMSI前面三位460是国家号码，其次的两位是运营商代号，00、02是中国移动，01是联通，03是电信。
	            System.out.print("IMSI是："+IMSI);
	            if(IMSI.startsWith("46000")||IMSI.startsWith("46002")){
	                providerName="中国移动";
	            }else if(IMSI.startsWith("46001")){
	                providerName="中国联通";
	            }else if(IMSI.startsWith("46003")){
	                providerName="中国电信";
	        }
	            
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return providerName;
	 
	}
}
