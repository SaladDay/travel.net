package cn.saladday.travel.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

/**
 * 验证码工具类
 */
public final class CaptchaUtils {
    private CaptchaUtils(){};

    public enum SecurityCodeLevel {
        Simple,
        Medium,
        Hard
    }


    /**
     * 默认获取验证码字符【4位，中等难度，不可重复】
     * @return
     */
    public static String getSecurityCode() {
        return (String) getSecurityCode(4, SecurityCodeLevel.Medium, false);
    }

    /**
     * 自定义参数获取验证码字符
     * @param length
     * @param level
     * @param isCanRepeat
     * @return
     */
    public static String getSecurityCode(int length, SecurityCodeLevel level, boolean isCanRepeat) {
        int len = length;
        //除去容易混淆的0和o，1和l
        char[] codes = {
                '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                'j', 'm', 'n', 'p', 'q', 'r', 's', 't','u',
                'v', 'w', 'x', 'y', 'z','A','B','C','D','E',
                'F','G','H','J','K','L','M','N','P','Q','R','S',
                'T','U','V','W','X','Y','Z'};
        if(level==SecurityCodeLevel.Simple){
            codes= Arrays.copyOfRange(codes,0,9);
        }else if (level==SecurityCodeLevel.Medium){
            codes= Arrays.copyOfRange(codes,0,33);
        }
        int n=codes.length;
        //抛出运行时异常
        if (len>n&&isCanRepeat==false){
            throw new RuntimeException(
                    String.format("调用securitycode.getSecurityCode(%1$s,len,level,isCanRepeat,n)"));}
        //char[] result用来存放结果
        char[] result=new char[len];
        //判断能否出现重复的字符
        if (isCanRepeat){
            for(int i=0;i<result.length;i++){
                //索引0 and n-1
                int r=(int)(Math.random()*n);
                //将result中的第i个元素设为codes[r]存放的数值
                result[i]=codes[r];
            }
        }else {
            for (int i=0;i<result.length;i++){
                int r=(int)(Math.random()*n);
                //将result中的第i个元素设为codes[r]存放的数值
                result[i]=codes[r];
                //r个取过了因此将n-1的值给他，再将n-1去除索引即可
                codes[r]=codes[n-1];
                n--;
            }
        }
        return String.valueOf(result);
    }

    /* 以下是操作图片的*/



    private static Random r = new Random();
    //  字体
    private static String[] fontNames = { "宋体", "华文楷体", "黑体", "华文新魏", "华文隶书", "微软雅黑", "楷体_GB2312" };


    /**
     * 生成随机的颜色
     * @return
     */
    private static Color randomColor() {
        int red = r.nextInt(150);
        int green = r.nextInt(150);
        int blue = r.nextInt(150);
        return new Color(red, green, blue);
    }

    /**
     * 生成随机的字体
     * @return
     */
    private static Font randomFont() {
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];// 生成随机的字体名称
        int style = r.nextInt(4);// 生成随机的样式, 0(无样式), 1(粗体), 2(斜体), 3(粗体+斜体)
        int size = r.nextInt(5) + 24; // 生成随机字号, 24 ~ 29
        // int size = r.nextInt(5) + 15; // 生成随机字号, 20 ~ 24
        return new Font(fontName, style, size);
    }

    /**
     * 画干扰线
     * @param image
     */
    private static void drawLine(BufferedImage image, int CAPTCHA_WIDTH, int CAPTCHA_HEIGHT) {
        int num = 5;// 一共画5条
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {// 生成两个点的坐标，即4个值
            int x1 = r.nextInt(CAPTCHA_WIDTH);
            int y1 = r.nextInt(CAPTCHA_HEIGHT);
            int x2 = r.nextInt(CAPTCHA_WIDTH);
            int y2 = r.nextInt(CAPTCHA_HEIGHT);
            g2.setStroke(new BasicStroke(1.5F));
            g2.setColor(randomColor()); // 随机生成干扰线颜色
            g2.drawLine(x1, y1, x2, y2);// 画线
        }
    }

    /**
     * 根据提供的验证码String，生成对应的长80，宽30，中等难度，不允许重复的验证码图片
     * @param securityCode
     * @return
     */
    public static BufferedImage createImage(String securityCode){

        return createImage(securityCode,80,30,securityCode.length(),SecurityCodeLevel.Medium,false);
    }

    /**
     * 生成验证码图片,全部参数
     * @return
     */
    public static BufferedImage createImage(String securityCode,int CAPTCHA_WIDTH,int CAPTCHA_HEIGHT,
                                            int length,SecurityCodeLevel securityCodeLevel,Boolean isCanRepeat) {


        BufferedImage image = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        // 背景色,白色
        g2.setColor(new Color(255, 255, 255));
        g2.fillRect(0, 0, CAPTCHA_WIDTH, CAPTCHA_HEIGHT);
        //填上背景

        // 向图片中画字符  String securityCode
        for (int i = 0; i < securityCode.length(); i++) {// 循环,每次生成一个字符
            String s = securityCode.charAt(i) + "";// 随机生成一个字母
            float x = i * 1.0F * CAPTCHA_WIDTH / 4+7F; // 设置当前字符的x轴坐标
            g2.setFont(randomFont()); // 设置随机字体
            g2.setColor(randomColor()); // 设置随机颜色
            g2.drawString(s, x, CAPTCHA_HEIGHT-7); // 画图,依次将字符写入到图片的相应位置-------------------
        }
        drawLine(image,CAPTCHA_WIDTH,CAPTCHA_HEIGHT); // 添加干扰线
        return image;
    }






}
