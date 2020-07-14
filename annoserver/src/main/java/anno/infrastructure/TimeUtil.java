package anno.infrastructure;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtil {

    public static String getNowTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }

    public static Timestamp strToDate(String strDate) {
        if (StringUtil.isEmpty(strDate)){
            return null;
        }
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Timestamp date = new Timestamp(d.getTime());
        return date;
    }


    public static String getCreateTimestamp() {
        return new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
    }

    public static String strToStr(String strDate) {
        if (StringUtil.isEmpty(strDate)){
            return null;
        }
        String str = strDate;
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String string = null;
        try {
            string = format.format(format1.parse(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }
}
