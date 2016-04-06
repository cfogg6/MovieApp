package com.mymovieapp;

/**
 * Created by Corey on 4/4/16.
 */
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class MyClickableSpan extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MyClickableSpan(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setUnderlineText(isUnderline);

        }

        @Override
        public void onClick(View widget) {

        }
    }
