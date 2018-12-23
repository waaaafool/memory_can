package can.sms.smsMatch;

/**
 * finished
 *
 * DeliveryMatch类用于识别快递短信
 * 先调用getIsDelivery()返回是否快递短信(通过匹配“快递”和“取货码”两个关键字判断)
 * 在通过公有函数接口返回需求关键字信息(公司名称，取件时间，取货所需，取件地址)
 *
 * 通用正则表达式
 * 	匹配至】后第一个字符：[^\\】*]\\】
 * 	匹配xx快递的xx(可以没有快递两字)：([^快递]+)[快递]{0,2}
 * 	匹配一个中文字符：[\u4e00-\u9fcc]
 * 	匹配逗号(,和，)：[\\,\\，]
 * 	匹配数字(0-9):[0-9]
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeliveryMatch {
    private String SMSText;
    private int deliveryType;

    private String companyName;
    private String deliveryTime;
    private String deliveryLocation;
    private String deliveryNeed;
    private String title;
    private boolean hasHandleSMS;

    DeliveryMatch(String SMSText, int deliveryType) {
        this.SMSText = SMSText;
        this.deliveryType = deliveryType;
        companyName = null;
        deliveryTime = null;
        deliveryLocation = null;
        deliveryNeed = null;
        title = null;
        hasHandleSMS = false;
    }

    public String getKeyContent() {
        setKeyContent();
        return "快递名称：" + companyName + "\n" + "取货时间：" + deliveryTime + "\n"
                + "取货地点：" + deliveryLocation + "\n" + "取货所需：" + deliveryNeed + "\n";
    }

    private void setKeyContent() {
        if (hasHandleSMS == false) {
            matchContent();
            if (deliveryNeed == null) {
                deliveryNeed = "本条短信或收件人手机尾号";
            }
            if (deliveryTime == null) {
                deliveryTime = "工作时间内";
            }
            hasHandleSMS = true;
        }
    }

    private void matchContent() {
        switch (deliveryType) {
            case 1:
                matchType1();		//菜鸟驿站
                break;
            case 2:
                matchType2();		//云喇叭
                break;
            case 3:
                matchType3();		//ems快递
                break;
            case 4:
                matchType4();		//妈妈驿站
                break;
            case 5:
                matchType5();		//顺丰速运
                break;
            case 6:
                matchType6();		//微快递
                break;
            case 7:
                matchType7();		//顺丰快递
                break;
            case 8:
                matchType8();		//丰巢
                break;
            default:
                System.out.println("ERROOR:需要新增快递模板!");
        }
    }

    /**
     * 匹配【菜鸟驿站】
     */
    private void matchType1() {
        String regex = "[^\\】*]\\】[您的]{0,2}([^快递]+)[快递]{0,2}包裹"
                + "到([^\\,\\，]+)" + "[\\,\\，]请(.*)凭(.*)取.*";
        Pattern p = Pattern.compile(regex);
        //System.out.println(p.pattern());
        Matcher m = p.matcher(SMSText);
        if (m.find()) {
            // System.out.println(m.group(0));
            companyName = m.group(1) + "快递";
            deliveryTime = m.group(3);
            deliveryLocation = m.group(2);
            deliveryNeed = "取货码--" + m.group(4);
        } else {
            System.out.println("【菜鸟驿站】匹配失败");
        }
    }

    /**
     * 匹配【云喇叭】
     */
    private void matchType2() {
        String regex;
        if (!Pattern.matches(".*苏宁.*", SMSText)) {
            regex = "[^\\】*]\\】编号([0-9]*)([^快递]+)[快递]"
                    + "{0,2}请你([^\u4e00-\u9fcc]+)(.*)领取.*";
            Pattern p = Pattern.compile(regex);
            //System.out.println(p.pattern());
            Matcher m = p.matcher(SMSText);
            if (m.find()) {
                //System.out.println(m.group(0));
                companyName = m.group(2) + "快递";
                deliveryTime = m.group(3);
                deliveryLocation = m.group(4);
                deliveryNeed = "取货码--" + m.group(1);
            } else {
                System.out.println("【云喇叭】匹配失败");
            }
        } else {
            regex = ".*编号([0-9a-zA-Z]+).*在([^\\,\\，带]+).*到(.*)取.*";
            Pattern p = Pattern.compile(regex);
            //System.out.println(p.pattern());
            Matcher m = p.matcher(SMSText);
            if (m.find()) {
                //System.out.println(m.group(0));
                companyName = "苏宁易购";
                deliveryTime = m.group(2);
                deliveryLocation = m.group(3);
                deliveryNeed = "取货码--" + m.group(1);
            } else {
                System.out.println("【云喇叭】匹配失败");
            }
        }
    }

    /**
     * 匹配【EMS快递】
     */
    private void matchType3() {
        String regex = "[^\\】*]\\】([^\\,\\，]*)[\\,\\，](.*)到(.*)领.*按(.*)领.*";
        Pattern p = Pattern.compile(regex);
        //System.out.println(p.pattern());
        Matcher m = p.matcher(SMSText);
        if (m.find()) {
            //System.out.println(m.group(0));
            companyName = m.group(1) + "快递";
            deliveryTime = m.group(2);
            deliveryLocation = m.group(3);
            deliveryNeed = "取货码--" + m.group(4);
        } else {
            System.out.println("【EMS快递】匹配失败");
        }
    }

    /**
     * 匹配【妈妈驿站】
     */
    private void matchType4() {
        String regex = "[^\\】*]\\】取货码([0-9]*)[\\,\\，您有]{0,3}([^快递]+)"
                + "快递[包裹]{0,2}[\\，\\,已到]{0,3}([0-9\u4e00-\u9fcc]+).*时间"
                + "([0-9\\:\\：\\-\\—]+).*";
        Pattern p = Pattern.compile(regex);
        //System.out.println(p.pattern());
        Matcher m = p.matcher(SMSText);
        if (m.find()) {
            //System.out.println(m.group(0));
            companyName = m.group(2) + "快递";
            deliveryTime = m.group(4);
            deliveryLocation = m.group(3);
            deliveryNeed = "取货码--" + m.group(1);
        } else {
            System.out.println("【妈妈驿站】匹配失败");
        }
    }

    /**
     * 匹配【顺丰速运】
     */
    private void matchType5() {
        String regex = ".*放置于([^\\，||\\,]+).*请持(.*)前往.*";
        Pattern p = Pattern.compile(regex);
        //System.out.println(p.pattern());
        Matcher m = p.matcher(SMSText);
        if (m.find()) {
            //System.out.println(m.group(0));
            companyName = "顺丰速运";
            deliveryLocation = m.group(1);
            deliveryNeed = m.group(2);
        } else {
            System.out.println("【顺丰速运】匹配失败");
        }
    }

    /**
     * 匹配【微快递】
     */
    private void matchType6() {
        //String regex = ".*\\｛([^\\｝]+).*编码([0-9]+).*到([^\\,||\\，]*).*请(.*)及时.*";
        String regex = "[^\\】*]\\】[^\u4e00-\u9fcc]*([\u4e00-\u9fcc]+)[^\u4e00-\u9fcc]*.*包裹[编码]*([0-9]+).*到([^\\,||\\，]*).*请(.*)及时.*";
        Pattern p = Pattern.compile(regex);
        //System.out.println(p.pattern());
        Matcher m = p.matcher(SMSText);
        if (m.find()) {
            //System.out.println(m.group(0));
            companyName = m.group(1);
            deliveryTime = m.group(4);
            deliveryLocation = m.group(3);
            deliveryNeed = m.group(2);
        } else {
            System.out.println("【微快递】匹配失败");
        }
    }

    /**
     * 匹配【顺丰快递】
     */
    private void matchType7() {
        String regex = ".*请(.*)到(.*)领取.*";
        Pattern p = Pattern.compile(regex);
        //System.out.println(p.pattern());
        Matcher m = p.matcher(SMSText);
        if (m.find()) {
            //System.out.println(m.group(0));
            companyName = "顺丰快递";
            deliveryTime = m.group(1);
            deliveryLocation = m.group(2);
        } else {
            System.out.println("【顺丰速运】匹配失败");
        }
    }

    /**
     * 匹配【丰巢】
     */
    private void matchType8() {
        String regex = ".*取件码\\『{0,1}([0-9]+)\\』{0,1}至(.*)取(.*)的.*";
        Pattern p = Pattern.compile(regex);
        //System.out.println(p.pattern());
        Matcher m = p.matcher(SMSText);
        if (m.find()) {
            //System.out.println(m.group(0));
            companyName = m.group(3);
            //deliveryTime = m.group(4);
            deliveryLocation = m.group(2);
            deliveryNeed = "取货码--" + m.group(1);
        } else {
            System.out.println("【微快递】匹配失败");
        }
    }

    public String getTitle() {
        // TODO Auto-generated method stub
        setKeyContent();
        title = companyName + "：" + deliveryNeed;
        return title;
    }
	
	public String getEndTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());
        System.out.println("++++++++++++++++++++++++++" + time);
        time = TimeHandle.handleTime(time);
        System.out.println("--------------------------" + time);
		return time;
	}
}