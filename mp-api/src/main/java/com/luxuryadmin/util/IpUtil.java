package com.luxuryadmin.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtil {

    /**
     * 获取ip地址
     * @param request
     * @return
     */
    public  static  String  getIpAddress(HttpServletRequest request)  {
        String  ip  =  request.getHeader("x-forwarded-for");
        if (ip  ==  null  ||  ip.length()  ==  0  ||  "unknown".equalsIgnoreCase(ip))  {
            ip  =  request.getHeader("Proxy-Client-IP");
        }
        if (ip  ==  null  ||  ip.length()  ==  0  ||  "unknown".equalsIgnoreCase(ip))  {
            ip  =  request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip  ==  null  ||  ip.length()  ==  0  ||  "unknown".equalsIgnoreCase(ip))  {
            ip  =  request.getRemoteAddr();
        }
        if (ip.contains(","))  {
            return  ip.split(",")[0];
        }
        else  {
            return  ip;
        }
    }

    /**
     * 获取mac地址（物理地址)
     * @param ip
     * @return
     */
    // 从windows机器上获取mac地址
    public static String getMacInWindows(final String ip) {
        String result = "";
        String[] cmd = {"cmd", "/c", "ping " + ip};
        String[] another = {"cmd", "/c", "ipconfig -all"};
        // 获取执行命令后的result
        String cmdResult = callCmd(cmd, another);
        // 从上一步的结果中获取mac地址
        result = filterMacAddress(ip, cmdResult, "-");
        return result;
    }
    // 命令执行
    public static String callCmd(String[] cmd, String[] another) {
        String result = "";
        String line = "";
        try {
            Runtime rt = Runtime.getRuntime();
            // 执行第一个命令
            Process proc = rt.exec(cmd);
            proc.waitFor();
            // 执行第二个命令
            proc = rt.exec(another);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取mac地址
    public static String filterMacAddress(final String ip, final String sourceString, final String macSeparator) {
        String result = "";
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            result = matcher.group(1);
            // 因计算机多网卡问题，截取紧靠IP后的第一个mac地址
            int num = sourceString.indexOf(ip) - sourceString.indexOf(": "+result + " ");
            if (num>0&&num<300) {
                break;
            }
        }
        return result;
    }
}
