package com.csguys.viewmoretextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;


/**
 * Created by Alex
 * 13 June 2020
 */
public class ViewMoreTextView extends AppCompatTextView {

    private static final String TAG = "ViewMoreTextView";
    private static final String mEllipsis = "\u2026";
    private static final String DEFAULT_HIGHLIGHT_COLOR = "#7a08fa";

    private int readMoreTextColor;
    private boolean showUnderLine;
    private CharSequence textExpand;
    private CharSequence textCollapse;
    private int maxChar;

    public ViewMoreTextView(Context context) {
        this(context, null);
    }

    public ViewMoreTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public ViewMoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ViewMoreTextView
                , defStyleAttr, 0);
        showUnderLine = array.getBoolean(R.styleable.ViewMoreTextView_tv_click_underline, false);
        maxChar = array.getInt(R.styleable.ViewMoreTextView_tv_max_char, Integer.MAX_VALUE);
        if (maxChar <= 0){
            maxChar = Integer.MAX_VALUE;
        }
        textExpand = array.getText(R.styleable.ViewMoreTextView_tv_expand_text);
        if (TextUtils.isEmpty(textExpand)) {
            textExpand = "view more";
        }
        textCollapse = array.getText(R.styleable.ViewMoreTextView_tv_collapse_text);
        if (TextUtils.isEmpty(textCollapse)) {
            textCollapse = "view less";
        }
        textCollapse = " "+textCollapse;
        readMoreTextColor = array.getColor(R.styleable.ViewMoreTextView_tv_click_color
                , Color.parseColor(DEFAULT_HIGHLIGHT_COLOR));
        array.recycle();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        StateSaver stateSaver = new StateSaver(superState);
        stateSaver.textExpand = this.textExpand;
        stateSaver.textCollapse = this.textCollapse;
        stateSaver.maxChar = this.maxChar;
        stateSaver.readMoreTextColor = this.readMoreTextColor;
        stateSaver.showUnderLine = this.showUnderLine;
        return stateSaver;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        StateSaver stateSaver = (StateSaver) state;
        super.onRestoreInstanceState(stateSaver.getSuperState());
        this.textExpand = stateSaver.textExpand;
        this.textCollapse = stateSaver.textCollapse;
        this.maxChar = stateSaver.maxChar;
        this.showUnderLine = stateSaver.showUnderLine;
        this.readMoreTextColor = stateSaver.readMoreTextColor;
        requestLayout();
        invalidate();
    }

    /**
     * Set text using string resource id
     * @param resId string res id
     */
    public void setCharText(@StringRes int resId){
        setCharText(getContext().getResources().getText(resId));
    }

    /**
     * Set text CharSequence
     * @param text text string
     */
    public void setCharText(final CharSequence text){
        setTag(text);
        if (text.length() > maxChar){
            showText(true, text);
        }else {
            setText(text);
        }
    }

    /**
     * This method calculate text boundary and trim text if required
     * @param isCollapse true to shrink false to expans
     * @param builder1 CharSequence
     */
    private void showText(boolean isCollapse, CharSequence builder1) {
        SpannableStringBuilder builder = new SpannableStringBuilder(builder1);
        String ellipseString = isCollapse ? mEllipsis + textExpand : textCollapse+"";
        /*
         replace is performed instead of creating subSequence
         because subSequence will loss span if passed with CharSequence
         */
        builder.replace(isCollapse ? maxChar : builder.length() , builder.length(), "");
        builder.append(ellipseString);
        setMovementMethod(LinkMovementMethod.getInstance());
        final CharSequence span = (CharSequence) getTag();
        int start = isCollapse ? builder.length() - ellipseString.length() : span.length();
        int end = isCollapse ? builder.length() - ellipseString.length() + ellipseString.length()
                : span.length()+ ellipseString.length();
        builder.setSpan(new ReadMoreSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (getText().length() < span.toString().length()){
                    showText(false, span);
                }else {
                    showText(true, span);
                }
            }
        },start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        setText(builder);
    }

    /**
     * Click span for expand & collapse text
     */
    private abstract class ReadMoreSpan extends ClickableSpan {
        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setUnderlineText(showUnderLine);
            ds.setColor(readMoreTextColor);
        }
    }

    /**
     * Class to save custom states of view
     */
    private static class StateSaver extends BaseSavedState {

        int readMoreTextColor;
        boolean showUnderLine;
        CharSequence textExpand;
        CharSequence textCollapse;
        private int maxChar;

        StateSaver(Parcelable superState) {
            super(superState);
        }

        private StateSaver(Parcel in) {
            super(in);
            this.readMoreTextColor = in.readInt();
            this.showUnderLine = in.readByte() != 0;
            this.textExpand = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.textCollapse = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.maxChar = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.readMoreTextColor);
            dest.writeByte(this.showUnderLine ? (byte) 1 : (byte) 0);
            TextUtils.writeToParcel(this.textExpand, dest, flags);
            TextUtils.writeToParcel(this.textCollapse, dest, flags);
            dest.writeInt(this.maxChar);
        }


        public static final Parcelable.Creator<StateSaver> CREATOR = new Parcelable.Creator<StateSaver>() {
            @Override
            public StateSaver createFromParcel(Parcel source) {
                return new StateSaver(source);
            }

            @Override
            public StateSaver[] newArray(int size) {
                return new StateSaver[size];
            }
        };

    }

}

