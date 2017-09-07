package myD7Util;

import java.util.UUID;

/**
 *
 * @author xuecf
 */
public class GUID {
    public static void main(String[] args){
        System.out.println("########getGUID_36########"+getGUID_36());
    }
    public static String getGUID_36(){
        return UUID.randomUUID().toString();
    }
}
