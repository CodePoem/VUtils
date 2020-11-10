package com.vdreamers.vutilsandroid;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符序列工具类
 * <p>
 * date 2019/10/11 17:22:45
 *
 * @author <a href="mailto:codepoetdream@gmail.com">Mr.D</a>
 */
public class CharSequenceUtils {

    /**
     * 字符串匹配关键词部分变色
     *
     * @param color   变化后的颜色
     * @param text    字符串
     * @param keyWord 关键词
     * @return 变色完后的字符序列
     */
    public static CharSequence matcherKeyWordChangeColor(int color, CharSequence text, String keyWord) {
        Pattern pattern = Pattern.compile(keyWord);
        Matcher matcher = pattern.matcher(text);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            // 设置变色
            spannableStringBuilder.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStringBuilder;
    }

    /**
     * 字符串匹配关键词部分变色并设置点击事件
     * TextView 需要调用setMovementMethod(LinkMovementMethod.getInstance()) 才能激活点击事件
     *
     * @param text            字符串
     * @param keyWord         关键词
     * @param color           变化后的颜色
     * @param underline       是否需要下划线
     * @param onClickListener 点击事件
     * @return 变色完后的字符序列
     */
    public static CharSequence matcherKeyWordChangeColorWithClick(CharSequence text,
                                                                  String keyWord,
                                                                  int color, boolean underline,
                                                                  View.OnClickListener onClickListener) {

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (onClickListener != null) {
                    onClickListener.onClick(widget);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(color);
                ds.setUnderlineText(underline);
            }
        };

        Pattern pattern = Pattern.compile(keyWord);
        Matcher matcher = pattern.matcher(text);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            // 设置点击事件
            spannableStringBuilder.setSpan(clickableSpan, start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStringBuilder;
    }
}
