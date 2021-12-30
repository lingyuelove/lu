package com.luxuryadmin.common.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author monkey king
 */
public class ImageCode {

    public static final int NUM = 255;

	/**
	 * 给定范围获得随机颜色
	 * @param fc
	 * @param bc
	 * @return
	 */
    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > NUM) {
            fc = NUM;
        }
        if (bc > NUM) {
            bc = NUM;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 生成图形验证码; 4位数;
     *
     * @param imageCode 图形验证码
     * @return
     */
    public static BufferedImage createImageCode(String imageCode) {
        if (LocalUtils.isEmptyAndNull(imageCode)) {
            return null;
        }
        // 在内存中创建图象
        int width = 60, height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics g = image.getGraphics();

        // 生成随机类
        Random random = new Random();

        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);

        // 设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        // 画边框
       /* g.setColor(new Color(RGBImageFilter.IMAGEERROR));
        g.drawRect(0,0,width-1,height-1);*/

        // 200，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 200; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 将认证码显示到图象中,调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
        g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
        char[] chars = imageCode.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            g.drawString(String.valueOf(chars[i]), 13 * i + 6, 16);
        }
        // 图象生效
        g.dispose();
        // 输出图象到页面
        return image;
    }

    public static void main(String[] args) {
        System.out.println(createImageCode("123456"));
    }
}
