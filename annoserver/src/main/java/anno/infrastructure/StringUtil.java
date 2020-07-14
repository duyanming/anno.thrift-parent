package anno.infrastructure;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtil {

    public static boolean isNumeric(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d+$|-\\d+$|\\d+\\.\\d+$|-\\d+\\.\\d+$");
        return pattern.matcher(str).matches();
    }

    public static boolean isInteger(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isLong(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static int ToInt(String str) {
        try {
            int a = Integer.parseInt(str);
            return a;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Timestamp GetDateNow() {
        return new Timestamp(System.currentTimeMillis());
    }

    /*
     * 获取格式化日期如果不是日期格式则返回null yyyy-MM-dd HH:mm:ss yyyy-MM-dd
     */
    public static Date GetDate(String date) {
        SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date != null && !date.isEmpty()) {
            try {
                return sdftime.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
                    return sdfdate.parse(date);
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        return null;
    }

    /*
     * 支持long,时间，日期
     */
    public static Date tryDate(String dateStr) {
        try {
            if (StringUtil.isLong(dateStr)) {
                long datetime = Long.parseLong(dateStr);
                return new Date(datetime);
            } else {
                return GetDate(dateStr);
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public static String GetBatchNo() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static Timestamp stringToTimestamp(String dateStr) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            Date date = sdf.parse(dateStr);
            date.getTime();
            cal.setTime(date);
            return new Timestamp(cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.setTime(new Date());
        return new Timestamp(cal.getTimeInMillis());
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        int bufSize = endIndex - startIndex;
        if (bufSize <= 0) {
            return "";
        }
        bufSize *= (((array[startIndex] == null) ? 16 : array[startIndex].toString().length()) + separator.length());
        StringBuffer buf = new StringBuffer(bufSize);
        for (int i = startIndex; i < endIndex; ++i) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static boolean isNullOrWhiteSpace(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * @param obj
     * @return
     */
    public static String asString(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    /**
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        return (obj == null || obj.toString().length() == 0);
    }

    public static boolean isEmpty(Object... str) {
        boolean flag = false;
        for (Object s : str) {
            flag = (s == null || s.toString().length() == 0);
            if (flag == true) {
                return true;
            }
        }
        return flag;
    }

    /**
     *
     *
     * @see select LPAD('DFSDf',9,'w') from dual;
     * @param srcString
     * @param padded_length
     * @param paddingString
     * @return
     */
    public static String lpad(String srcString, int padded_length, String paddingString) {
        int srcLength = asString(srcString).length();
        int padStrLength = asString(paddingString).length();

        if (srcLength < padded_length && !isEmpty(paddingString)) {
            int leftLength = padded_length - srcLength;
            int cnt = leftLength / padStrLength + (leftLength % padStrLength > 0 ? 1 : 0);
            StringBuffer sb = new StringBuffer(padStrLength * cnt);
            for (int i = 0; i < cnt; i++) {
                sb.append(paddingString);
            }
            return sb.substring(0, leftLength) + srcString;
        } else if (srcLength > padded_length)
            return srcString.substring(0, padded_length);
        else
            return srcString;
    }

    /**
     *
     * @see select RPAD('DFSDf',9,'w') from dual;
     * @param srcString
     * @param padded_length
     * @param paddingString
     * @return
     */
    public static String rpad(String srcString, int padded_length, String paddingString) {
        int srcLength = asString(srcString).length();
        int padStrLength = asString(paddingString).length();

        if (srcLength < padded_length && !isEmpty(paddingString)) {
            int leftLength = padded_length - srcLength;
            int cnt = leftLength / padStrLength + (leftLength % padStrLength > 0 ? 1 : 0);
            StringBuffer sb = new StringBuffer(padStrLength * cnt);
            for (int i = 0; i < cnt; i++) {
                sb.append(paddingString);
            }
            return srcString + sb.substring(0, leftLength);
        } else if (srcLength > padded_length)
            return srcString.substring(0, padded_length);
        else
            return srcString;
    }

    /**
     *
     * @param list
     * @param joinStr
     * @return
     */
    public static String join(String[] list, String joinStr) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; list != null && i < list.length; i++) {
            if ((i + 1) == list.length) {
                s.append(list[i]);
            } else {
                s.append(list[i]).append(joinStr);
            }
        }
        return s.toString();
    }

    /**
     * firstCharLowerCase
     *
     * @param s String
     * @return String
     */
    public static String firstCharLowerCase(String s) {
        if (s == null || "".equals(s))
            return ("");
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    /**
     * firstCharUpperCase
     *
     * @param s String
     * @return String
     */
    public static String firstCharUpperCase(String s) {
        if (s == null || "".equals(s))
            return ("");
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     *
     * @param str
     * @return
     */
    public static String toBeanPatternStr(String src) {
        String dist = src.toLowerCase();
        Pattern pattern = Pattern.compile("_([a-z0-9])");
        Matcher matcher = pattern.matcher(dist);
        while (matcher.find()) {
            dist = dist.replaceFirst(matcher.group(0), matcher.group(1).toUpperCase());
        }
        return dist;
    }

    public static String toLinePatternStr(String str) {
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     *
     * @param src
     * @return
     */
    public static String toJSLineSeparateStr(String src) {
        if (src == null) {
            return "";
        }
        String dist = src;
        dist = dist.replaceAll("\r\n", "\\\\n");
        dist = dist.replaceAll("\r", "\\\\n");
        dist = dist.replaceAll("\n", "\\\\n");
        return dist;
    }

    /**
     *
     * @param input
     * @param tail
     * @param length
     * @return
     */
    public static String getShorterString(String input, String tail, int length) {
        tail = isEmpty(tail) ? tail : "";
        StringBuffer buffer = new StringBuffer(512);
        try {
            int len = input.getBytes("GBK").length;
            if (len > length) {
                int ln = 0;
                for (int i = 0; ln < length; i++) {
                    String temp = input.substring(i, i + 1);
                    if (temp.getBytes("GBK").length == 2)
                        ln += 2;
                    else
                        ln++;

                    if (ln <= length)
                        buffer.append(temp);
                }
            } else {
                return input;
            }
            buffer.append(tail);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     *
     * @return
     */
    public static String getBytesString(String input, String code) {
        try {
            byte[] b = input.getBytes(code);
            return Arrays.toString(b);
        } catch (UnsupportedEncodingException e) {
            return String.valueOf(code.hashCode());
        }
    }

    /**
     *
     * @param clob
     * @return
     */
    public static String getStringFromClob(java.sql.Clob clob) {
        String result = "";
        try {
            if (clob == null) {
                return null;
            }
            Reader reader = clob.getCharacterStream();// 锟矫碉拷锟斤拷
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            StringBuffer sb = new StringBuffer(1024);
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch (Exception ex) {

        }
        return result;
    }

    /**
     * cust_code='111,222,333' to (cust_code='111' or cust_code='222' or
     * cust_code='333')
     *
     * @param srcString
     * @param token
     * @param columnName
     * @param isVarchar
     * @return
     */
    public static String parseToSqlOr(String srcString, String token, String columnName, boolean isVarchar) {
        String srcArray[] = StringUtils.split(srcString, token);
        StringBuffer sql = new StringBuffer();
        sql.append(" ( ");
        if (srcArray != null)
            for (int i = 0; i < srcArray.length; i++) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(srcArray[i]);
                String srcArrayTrim = m.replaceAll("");
                if (isVarchar) {
                    sql.append(columnName + "='" + srcArrayTrim + "' ");
                } else {
                    sql.append(columnName + "=" + srcArrayTrim + " ");
                }
                if (i < srcArray.length - 1) {
                    sql.append(" or ");
                }
            }
        sql.append(" ) ");
        return sql.toString();
    }

    public static String getGUID() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 3; i++)
            sb.append(UUID.randomUUID());

        return sb.toString();
    }

    /**
     * @param srcString
     * @param columnName
     * @return
     */
    public static String parseToSqlOr(String srcString, String columnName) {
        return parseToSqlOr(srcString, ",", columnName, true);
    }

    public static String ifnull(Object obj) {
        if (obj == null)
            return null;
        else
            return obj.toString();
    }

    public static String ifnull(Object obj, String str) {
        if (obj == null)
            return str;
        else
            return obj.toString();
    }

    public static String ifnullOrBlank(Object obj, String str) {
        if (obj == null || "".equals(obj))
            return str;
        else
            return obj.toString();
    }

    public static boolean hasChinese(String value) {

        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher match = pattern.matcher(value);

        return match.find();

    }

    public static void main(String[] args) {

    }

}

