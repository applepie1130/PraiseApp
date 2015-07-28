package com.praiseapp.comm;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.springframework.util.StringUtils;
public class StringUtil {
	public static String toCamelCase(String value ) {
        String[] strings = StringUtils.split(value.toLowerCase(), "_");
        for (int i = 1; i < strings.length; i++){
            strings[i] = StringUtils.capitalize(strings[i]);
        }
//        return StringUtils.join(strings);
        return "";
    }
    
    public static String getUniqueString() { 
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX).toUpperCase();
    }
    
    public static Boolean isEmpty(String value) {
    	// Debug
//    	System.out.println( "value == null ? : " +  value == null );
//    	System.out.println( "value == '' ? : " +  value == "" );
//    	System.out.println( "value.isEmpty() ? : " +  value.isEmpty() );
//    	System.out.println( "''.equals(value) ? : " +  "".equals(value) );
    	
    	if ( value == null || value == "" || value.isEmpty() || "".equals(value) ) {
    		return true;
    	}
    	
    	return false;
    }
}