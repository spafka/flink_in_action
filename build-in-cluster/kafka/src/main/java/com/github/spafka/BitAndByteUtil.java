package com.github.spafka;

public class BitAndByteUtil {
    private static final char[] bcdLookup = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public BitAndByteUtil() {
    }

    public short getShort(byte[] b, int index) {
        return (short)(b[index + 0] << 8 | b[index + 1] & 255);
    }

    public int getInt(byte[] bb, int index) {
        return (bb[index + 3] & 255) << 24 | (bb[index + 2] & 255) << 16 | (bb[index + 1] & 255) << 8 | (bb[index + 0] & 255) << 0;
    }

    public int byteToShort(byte[] bytes, int offset) {
        int high = bytes[offset];
        int low = bytes[offset + 1];
        return high << 8 & '\uff00' | low & 255;
    }

    public byte[] hexStringToBytes(String hexString) {
        if (hexString != null && !hexString.equals("")) {
            hexString = hexString.toUpperCase().replace(" ", "");
            int length = hexString.length() / 2;
            char[] hexChars = hexString.toCharArray();
            byte[] d = new byte[length];

            for(int i = 0; i < length; ++i) {
                int pos = i * 2;
                d[i] = (byte)(this.charToByte(hexChars[pos]) << 4 | this.charToByte(hexChars[pos + 1]));
            }

            return d;
        } else {
            return null;
        }
    }

    private byte charToByte(char c) {
        return (byte)"0123456789ABCDEF".indexOf(c);
    }

    public String byteToBit(byte b) {
        return "" + (byte)(b >> 7 & 1) + (byte)(b >> 6 & 1) + (byte)(b >> 5 & 1) + (byte)(b >> 4 & 1) + (byte)(b >> 3 & 1) + (byte)(b >> 2 & 1) + (byte)(b >> 1 & 1) + (byte)(b >> 0 & 1);
    }

    public byte BitToByte(String bitString) {
        if (null == bitString) {
            return 0;
        } else {
            int len = bitString.length();
            StringBuffer sb = new StringBuffer();
            int i;
            if (len < 8 && len > 4) {
                for(i = 0; i < 8 - len; ++i) {
                    sb.append("0");
                }

                sb.append(bitString);
                bitString = sb.toString();
            } else if (len < 4) {
                for(i = 0; i < 4 - len; ++i) {
                    sb.append("0");
                }

                sb.append(bitString);
                bitString = sb.toString();
            } else {
                sb.append(bitString);
                bitString = sb.toString();
            }

            int re;
            if (len == 8) {
                if (bitString.charAt(0) == '0') {
                    re = Integer.parseInt(bitString, 2);
                } else {
                    re = Integer.parseInt(bitString, 2) - 256;
                }
            } else {
                re = Integer.parseInt(bitString, 2);
            }

            return (byte)re;
        }
    }

    public int BitToInt(String bitString) {
        if (null == bitString) {
            return 0;
        } else {
            int len = bitString.length();
            int re;
            if (len == 8) {
                if (bitString.charAt(0) == '0') {
                    re = Integer.parseInt(bitString, 2);
                } else {
                    re = Integer.parseInt(bitString, 2) & 255;
                }
            } else {
                try {
                    re = Integer.parseInt(bitString, 2);
                } catch (Exception var5) {
                    return 1;
                }
            }

            return re;
        }
    }

    public long bitToLong(String bitString) {
        long re = 0L;

        try {
            if (bitString.length() < 64) {
                re = Long.parseLong(bitString, 2);
                return re;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return 0L;
    }

    public int signedBinaryToInt(String bitString) {
        if (null == bitString) {
            return 0;
        } else {
            int re;
            if (bitString.charAt(0) == '0') {
                re = Integer.parseInt(bitString, 2);
            } else {
                re = Integer.parseInt(bitString.substring(1), 2) * -1;
            }

            return re;
        }
    }



    public int byteArray2int(byte[] bytes) {
        byte[] a = new byte[4];
        int i = a.length - 1;

        for(int j = bytes.length - 1; i >= 0; --j) {
            if (j >= 0) {
                a[i] = bytes[j];
            } else {
                a[i] = 0;
            }

            --i;
        }

        int v0 = (a[0] & 255) << 24;
        int v1 = (a[1] & 255) << 16;
        int v2 = (a[2] & 255) << 8;
        int v3 = a[3] & 255;
        return v0 + v1 + v2 + v3;
    }

    public long byteArray2long(byte[] bytes) {
        long l = 0L;
        l = 255L & (long)bytes[0] | 65280L & (long)bytes[1] << 8 | 16711680L & (long)bytes[2] << 16 | 4278190080L & (long)bytes[3] << 24 | 1095216660480L & (long)bytes[4] << 32 | 280375465082880L & (long)bytes[5] << 40 | 71776119061217280L & (long)bytes[6] << 48 | -72057594037927936L & (long)bytes[7] << 56;
        return l;
    }

    public byte[] int2Byte(int intValue) {
        byte[] b = new byte[4];

        for(int i = 0; i < 4; ++i) {
            b[i] = (byte)(intValue >> 8 * (3 - i) & 255);
        }

        return b;
    }

    public static byte[] long2Bytes(long longValue) {
        byte[] byteNum = new byte[8];

        for(int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte)((int)(longValue >> offset & 255L));
        }

        return byteNum;
    }

    public String convertHexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        for(int i = 0; i < hex.length() - 1; i += 2) {
            String output = hex.substring(i, i + 2);
            int decimal = Integer.parseInt(output, 16);
            sb.append((char)decimal);
            temp.append(decimal);
        }

        return sb.toString();
    }

    public String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(bytes[i] & 255);
            if (hex.length() == 1) {
                sb.append("0");
            }

            sb.append(hex.toUpperCase() + " ");
        }

        return sb.toString();
    }

    public final String bytesToHexStr(byte[] bcd) {
        StringBuffer s = new StringBuffer(bcd.length * 2);

        for(int i = 0; i < bcd.length; ++i) {
            s.append(bcdLookup[bcd[i] >>> 4 & 15]);
            s.append(bcdLookup[bcd[i] & 15] + " ");
        }

        return s.toString();
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(bytes[i] & 255);
            if (hex.length() == 1) {
                sb.append("0");
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    public int hex2int(String str) {
        int a = 0;
        if (null != str && str.length() > 0) {
            a = Integer.parseInt(str, 16);
        }

        return a;
    }

    public long hex2long(String str) {
        long a = 0L;
        if (null != str && str.length() > 0) {
            a = Long.parseLong(str, 16);
        }

        return a;
    }



    private static String getMinusNum(int binaryLength) {
        String minNum = "0";

        for(int i = 0; i < binaryLength - 1; ++i) {
            minNum = minNum + "1";
        }

        return minNum;
    }

    private static String binaryAdd(String bnum1, String bnum2, boolean overflow) {
        boolean flag = false;
        char[] bb1 = bnum1.toCharArray();
        char[] bb2 = bnum2.toCharArray();
        int maxLength = bb1.length >= bb2.length ? bb1.length : bb2.length;
        StringBuffer result = new StringBuffer();

        for(int i = 0; i < maxLength; ++i) {
            if (i > bb1.length - 1 || i > bb2.length - 1) {
                System.out.println("字符串长度不一报警！");
                throw new AbstractMethodError("对与长度不相等的二进制数相加，还有待实现！");
            }

            int temp1;
            int temp2;
            if (flag) {
                temp1 = Integer.parseInt(bb1[bb1.length - i - 1] + "");
                temp2 = Integer.parseInt(bb2[bb2.length - i - 1] + "");
                flag = temp1 + temp2 + 1 > 1;
                if (flag) {
                    result.append((char)(temp1 + temp2 + 1 == 2 ? '0' : '1'));
                } else {
                    result.append('1');
                }
            } else {
                temp1 = Integer.parseInt(bb1[bb1.length - i - 1] + "");
                temp2 = Integer.parseInt(bb2[bb2.length - i - 1] + "");
                flag = temp1 + temp2 > 1;
                if (flag) {
                    result.append('0');
                } else {
                    int temp3 = temp1 + temp2;
                    result.append((char)(temp3 == 0 ? '0' : '1'));
                }
            }
        }

        if (overflow && flag && bb1.length == bb2.length) {
            result.append('1');
        }

        return result.reverse().toString();
    }

    private static String getOpposit(String binary) {
        char[] binArray = binary.toCharArray();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < binary.length(); ++i) {
            if (binArray[i] == '0') {
                sb.append(1);
            } else {
                sb.append(0);
            }
        }

        return sb.toString();
    }

    public void test() {
    }
}
