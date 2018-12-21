package can.sms.smsMatch;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 为用户提供的类接口
 * main调用这个类的对象->匹配短信类型值->根据类型值进行正则表达式编译->根据编译结果返回字符串给main
 * 实例化对象->.getIsSpecialSMS();--结果为true-->.getKeyContent();
 */
public class SMSMatch {
    private String SMSText;
    private boolean needPattern;
    private int type;
    private String keyContent;
    private String title;
    private String endTime;
    private boolean hasJudgeSMS;
    private int isTicketSMS;
    private int isDeliverySMS;
    private boolean hasHandleSMS;
    private ArrayList<String> arrayList;

    /*
     * 快递短信类型值，1-100
     */
    private static final String TYPE1 = "菜鸟驿站";
    private static final String TYPE2 = "云喇叭";
    private static final String TYPE3 = "EMS快递";
    private static final String TYPE4 = "妈妈驿站";
    private static final String TYPE5 = "顺丰速运";
    private static final String TYPE6 = "微快递";
    private static final String TYPE7 = "顺丰快递";
    private static final String TYPE8 = "丰巢";
    /*
     * 车票短信类型值，101-...
     */
    private static final String TYPE101 = "铁友网";
    private static final String TYPE102 = "携程网";
    private static final String TYPE103 = "铁路客服";
    private static final String TYPE104 = "飞猪提醒";
    private static final String TYPE105 = "去哪儿网";
    private static final String TYPE106 = "南航";
    private static final String TYPE107 = "美团点评";

    public SMSMatch(String SMSText) {
        this.SMSText = SMSText;
        hasJudgeSMS = false;
        needPattern = true;
        keyContent = null;
        title = null;
        endTime = null;
        type = 0;
        isDeliverySMS = 0;
        isTicketSMS = 0;
        hasHandleSMS = false;
        arrayList = new ArrayList<>(3);
    }

    public ArrayList<String> getArrayList() {
        setArrayList();
        return arrayList;
    }

    private void setArrayList() {
        setKeyContent();
        setEndTime();
        setTitle();
        arrayList.add(title);
        arrayList.add(keyContent);
        arrayList.add(endTime);
    }

    /**
     * 返回给后端短信的类型
     * @return
     * 	0：普通短信
     * 	1：快递短信
     * 	2：车票短信
     */
    public int getIsTicketSMS() {
        if (!hasJudgeSMS) {
            setIsSpecialSMS();
        }
        return isTicketSMS;
    }

    public int getIsDeliverySMS() {
        if (!hasJudgeSMS) {
            setIsSpecialSMS();
        }
        return isDeliverySMS;
    }

    /**
     * String形式返回应生成的备忘录内容
     * @return 备忘录内容
     */
    public String getKeyContent() {
        setKeyContent();
        return keyContent;
    }

    private void setIsSpecialSMS() {
        judgeSMSContent();
    }

    /**
     * 判断是否为特殊短信
     */
    private void judgeSMSContent() {
        hasJudgeSMS = true;
        if (needPattern) {
            choseType();
        }
        if (type == 0) {
            isDeliverySMS = 0;
            isTicketSMS = 0;
        } else if (type < 100) {
            isDeliverySMS = 1;
            isTicketSMS = 0;
        } else if (type > 100) {
            isDeliverySMS = 0;
            isTicketSMS = 1;
        }
    }

    /**
     * 为短信选择正确类型值
     */
    private void choseType() {
        needPattern = false;
        String regex = ".*?\\【(.*?)\\】.*";										//优先匹配最先出现的【】中的字符
        Matcher matcher = Pattern.compile(regex).matcher(SMSText);
        if (!matcher.find() || Pattern.matches(".*等[您你]很久.*", SMSText) ||
                Pattern.matches(".*在线选座.*", SMSText)) {
            System.out.println("【】中的字符不对");
            type = 0;
        } else {
            String typeJudge = matcher.group(1);
            //System.out.println(typeJudge);
            judgeType(typeJudge);
        }
    }

    /**
     * 为特殊短信匹配类型值
     * @param typeJudge
     */
    private void judgeType(String typeJudge) {
        //快递短信类型值
        if (typeJudge.equals(TYPE1)) {
            type = 1;
        } else if (typeJudge.equals(TYPE2)) {
            type = 2;
        } else if (typeJudge.equals(TYPE3)) {
            type = 3;
        } else if (typeJudge.equals(TYPE4)) {
            type = 4;
        } else if (typeJudge.equals(TYPE5)) {
            type = 5;
        } else if (typeJudge.equals(TYPE6)) {
            type = 6;
        } else if (typeJudge.equals(TYPE7)) {
            type = 7;
        } else if (typeJudge.equals(TYPE8)) {
            type = 8;
            //车票信息类型值
        } else if (typeJudge.equals(TYPE101)) {
            type = 101;
        } else if (typeJudge.equals(TYPE102)) {
            type = 102;
        } else if (typeJudge.equals(TYPE103)) {
            type = 103;
        } else if (typeJudge.equals(TYPE104)) {
            type = 104;
        } else if (typeJudge.equals(TYPE105)) {
            type = 105;
        } else if (typeJudge.equals(TYPE106)) {
            type = 106;
        } else if (typeJudge.equals(TYPE107)) {
            type = 107;
        } else {
            type = 0;
        }
    }

    private void setKeyContent() {
        if (hasHandleSMS == false) {
            handleSMS();
        }
    }

    private void setTitle() {
        if (hasHandleSMS == false) {
            handleSMS();
        }
    }

    private void setEndTime() {
        if (hasHandleSMS == false) {
            handleSMS();
        }
        int curMonth;
        int endMonth;
        int curYear;
        String formatYear;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String curTime = formatter.format(curDate);
        if (endTime != null) {
            Matcher yearMatch = Pattern.compile("(.*?)\\-(.*?)\\-.*").matcher(curTime);
            Matcher dateMatch = Pattern.compile("\\ *(.*?)\\-.*").matcher(endTime);
            if (yearMatch.find() && dateMatch.find()) {
                curMonth = Integer.parseInt(yearMatch.group(2));
                endMonth = Integer.parseInt(dateMatch.group(1));
                formatYear = yearMatch.group(1);
                if (curMonth > endMonth) {
                    curYear = Integer.parseInt(formatYear) + 1;
                    formatYear = String.valueOf(curYear);
                }
                endTime = formatYear + "-" + endTime;
            } else {
                System.out.println("时间处理函数的\"yyyy-\"格式错误");
            }
        }
    }

    private void handleSMS() {
        if (hasHandleSMS == false) {
            if (type <= 0) {
                keyContent = "ERROR:识别结果为普通短信!";
            } else if (type < 100) {
                DeliveryMatch deliveryMatch = new DeliveryMatch(SMSText, type);
                keyContent = deliveryMatch.getKeyContent();
                title = deliveryMatch.getTitle();
                endTime = deliveryMatch.getEndTime();
            } else if (type > 100) {
                TicketMatch ticketMatch = new TicketMatch(SMSText, type);
                keyContent = ticketMatch.getKeyContent();
                title = ticketMatch.getTitle();
                endTime = ticketMatch.getEndTime();
            }
            hasHandleSMS = true;
        }
    }

    public String getTitle() {
        // TODO Auto-generated method stub
        setTitle();
        return title;
    }

    public String getTicketEndTime() {
        setEndTime();
        return endTime;
    }
}
