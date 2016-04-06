package com.mymovieapp;

/**
 * Span that is clickable
 */
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class MyClickableSpan extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MyClickableSpan(boolean isUnderlined) {
            this.isUnderline = isUnderlined;
        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setUnderlineText(isUnderline);

        }

        @Override
        public void onClick(View widget) {

        }
    }
