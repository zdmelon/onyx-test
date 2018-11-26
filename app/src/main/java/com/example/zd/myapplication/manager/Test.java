package com.example.zd.myapplication.manager;

import java.util.Date;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Test {

    public static void main(String args[]) {

        String sessionId = "5bd6d43f452b603a7f2f4ffc";
        String refText = "hello,world";

        String signature = getInitSignature(TcConstant.ACTION_INIT, sessionId, refText, 1542269978, 2671);
        System.out.print(signature);
    }

    public static String getInitSignature(String action, String sessionId, String refText, int timestamp, int nonce){
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("Action", action);
        params.put("EvalMode", TcConstant.EVALMODE);
        params.put("Nonce", nonce);
        params.put("RefText", refText);
        params.put("Region", TcConstant.REGION);
        params.put("ScoreCoeff", TcConstant.SCORECOEFF);
        params.put("SecretId", TcConstant.SECRET_ID);
        params.put("SessionId", sessionId);
        params.put("Timestamp", timestamp);
        params.put("Version", TcConstant.VERSION);
        params.put("WorkMode", TcConstant.WORKMODE);

        String str2sign = getStringToSign("GET", TcConstant.END_POINT, params);
        System.out.print(str2sign);
        String signature = null;
        try {
            signature = sign(str2sign, TcConstant.SECRET_KEY, "HmacSHA1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Signature=" + signature);
        return signature;
    }

    public static String getTransmitSignature(String action, String sessionId, String userVoiceData, int timestamp, int nonce){
        // TreeMap可以自动排序
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("Nonce", nonce);
        // 实际调用时应当使用系统当前时间
        params.put("Timestamp", timestamp);
        params.put("Region", TcConstant.REGION);
        params.put("SecretId", TcConstant.SECRET_ID);
        params.put("Action", action);
        params.put("Version", TcConstant.VERSION);
        params.put("SessionId", sessionId);
        params.put("UserVoiceData", userVoiceData);
        params.put("SeqId", TcConstant.SEQID);
        params.put("IsEnd", TcConstant.ISEND);
        params.put("VoiceFileType", TcConstant.VOICEFILETYPE);
        params.put("VoiceEncodeType", TcConstant.VOICEENCODETYPE);

        String str2sign = getStringToSign("GET", TcConstant.END_POINT, params);
        String signature = null;
        try {
            signature = sign(str2sign, TcConstant.SECRET_KEY, "HmacSHA1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Signature=" + signature);
        return signature;
    }


    public static String sign(String s, String key, String method) throws Exception {
        Mac mac = Mac.getInstance(method);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(s.getBytes("UTF-8"));
//        return DatatypeConverter.printBase64Binary(hash);
        return printBase64Binary(hash);
    }

    public static String getStringToSign(String method, String endpoint, TreeMap<String, Object> params) {
        StringBuilder s2s = new StringBuilder();
        s2s.append(method).append(endpoint).append("/?");
        // 签名时要求对参数进行字典排序，此处用TreeMap保证顺序
        for (String k : params.keySet()) {
            s2s.append(k).append("=").append(params.get(k).toString()).append("&");
        }
        return s2s.toString().substring(0, s2s.length() - 1);
    }


    public static String printBase64Binary(byte[] input) {
        return printBase64Binary(input, 0, input.length);
    }

    public static String printBase64Binary(byte[] input, int offset, int len) {
        char[] buf = new char[((len + 2) / 3) * 4];
        int ptr = printBase64Binary(input, offset, len, buf, 0);
        assert ptr == buf.length;
        return new String(buf);
    }

    /**
     * Encodes a byte array into a char array by doing base64 encoding.
     *
     * The caller must supply a big enough buffer.
     *
     * @return
     *      the value of {@code ptr+((len+2)/3)*4}, which is the new offset
     *      in the output buffer where the further bytes should be placed.
     */
    public static int printBase64Binary(byte[] input, int offset, int len, char[] buf, int ptr) {
        // encode elements until only 1 or 2 elements are left to encode
        int remaining = len;
        int i;
        for (i = offset;remaining >= 3; remaining -= 3, i += 3) {
            buf[ptr++] = encode(input[i] >> 2);
            buf[ptr++] = encode(
                    ((input[i] & 0x3) << 4)
                            | ((input[i + 1] >> 4) & 0xF));
            buf[ptr++] = encode(
                    ((input[i + 1] & 0xF) << 2)
                            | ((input[i + 2] >> 6) & 0x3));
            buf[ptr++] = encode(input[i + 2] & 0x3F);
        }
        // encode when exactly 1 element (left) to encode
        if (remaining == 1) {
            buf[ptr++] = encode(input[i] >> 2);
            buf[ptr++] = encode(((input[i]) & 0x3) << 4);
            buf[ptr++] = '=';
            buf[ptr++] = '=';
        }
        // encode when exactly 2 elements (left) to encode
        if (remaining == 2) {
            buf[ptr++] = encode(input[i] >> 2);
            buf[ptr++] = encode(((input[i] & 0x3) << 4)
                    | ((input[i + 1] >> 4) & 0xF));
            buf[ptr++] = encode((input[i + 1] & 0xF) << 2);
            buf[ptr++] = '=';
        }
        return ptr;
    }

    private static final char[] encodeMap = initEncodeMap();

    private static char[] initEncodeMap() {
        char[] map = new char[64];
        int i;
        for (i = 0; i < 26; i++) {
            map[i] = (char) ('A' + i);
        }
        for (i = 26; i < 52; i++) {
            map[i] = (char) ('a' + (i - 26));
        }
        for (i = 52; i < 62; i++) {
            map[i] = (char) ('0' + (i - 52));
        }
        map[62] = '+';
        map[63] = '/';

        return map;
    }

    public static char encode(int i) {
        return encodeMap[i & 0x3F];
    }

}
