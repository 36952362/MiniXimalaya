package com.jupiter.miniximalaya.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

public class GaussianBlur {
    public static int DEFAULT_RADIUS = 10;

    /**
     * 创建毛玻璃效果
     *
     * @param iv      图片显示的控件嘛
     * @param context 上下文
     */
    public static void makeBlur(ImageView iv, Context context) {
        //先拿到控件里显示的内容
        BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //开始去转换
        Bitmap blurred = blurRenderScript(bitmap, DEFAULT_RADIUS, context);
        iv.setImageBitmap(blurred); //radius decide blur amount
    }


    private static Bitmap blurRenderScript(Bitmap smallBitmap, int radius, Context context) {
        //把原图转成ARGB_8888格式
        smallBitmap = RGB565toARGB888(smallBitmap);
        //再创建一张图，同样格式为ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(smallBitmap.getWidth(), smallBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //我们的java，效率没有C++的高，其实一般来说，做高斯模糊的话，直接提供类库，然后通过jni去调用，模糊的图片传回来即可。
        //这里借助RenderScript，它是一个高效的计算平台
        RenderScript renderScript = RenderScript.create(context);
        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        blur.setInput(blurInput);
        //参数范围(0,25)
        blur.setRadius(radius);
        blur.forEach(blurOutput);
        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        return bitmap;
    }

    private static Bitmap RGB565toARGB888(Bitmap img) {
        int numPixels = img.getWidth() * img.getHeight();
        int [] pixels = new int[numPixels];
        img.getPixels(pixels, 0 , img.getWidth(), 0 , 0 , img.getWidth(), img.getHeight()
        );

        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        result.setPixels(pixels, 0 , result.getWidth(), 0, 0, result.getWidth(), result.getHeight()
        );
        return  result;
    }
}
