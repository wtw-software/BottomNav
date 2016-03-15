package com.nasahapps.bottomnav;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hakeem on 3/14/16.
 */
public class BottomNavigationBar extends LinearLayout {

    public static final int INVALID_TAB_POSITION = -1;

    @ColorInt
    private int mAccentColor;
    private int mTabCount;
    private boolean mUsesDarkTheme;
    private ArrayList<Tab> mTabs = new ArrayList<>();
    private LayoutGravity mLayoutGravity = LayoutGravity.FILL;

    public BottomNavigationBar(Context context) {
        super(context);
        initView(null);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.BottomNavigationBar, 0, 0);

            try {
                mAccentColor = ta.getColor(R.styleable.BottomNavigationBar_accentColor, 0);
                if (mAccentColor == 0) {
                    TypedValue tv = new TypedValue();
                    TypedArray colorTa = getContext().obtainStyledAttributes(tv.data, new int[]{R.attr.accentColor});
                    mAccentColor = colorTa.getColor(0, 0);
                    colorTa.recycle();
                }

                mUsesDarkTheme = ta.getBoolean(R.styleable.BottomNavigationBar_darkTheme, false);
            } finally {
                ta.recycle();
            }

            if (isInEditMode()) {

            }
        }

        ViewCompat.setElevation(this, getResources().getDimensionPixelSize(R.dimen.na_bottom_nav_elevation));
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void addTab(Tab tab, boolean setSelected) {
        addTab(tab, mTabs.size(), setSelected);
    }

    public void addTab(Tab tab, int position) {
        addTab(tab, position, false);
    }

    public void addTab(Tab tab, int position, boolean setSelected) {
        checkTabCount();
        mTabCount++;
        mTabs.add(position, tab);
        if (setSelected || mTabCount == 1) {
            setTabSelected(position);
        }

        if (mTabCount <= 3) {
            LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {

        }

        requestLayout();
        invalidate();
    }

    public Tab newTab() {
        return new Tab(getContext(), true, mUsesDarkTheme);
    }

    private void checkTabCount() {
        if (mTabCount >= 5) {
            throw new RuntimeException("You can only have a max of 5 tabs! If you want more, it's " +
                    "best to use a navigation drawer.");
        }
    }

    public void setTabSelected(int position) {
        for (Tab tab : mTabs) {
            tab.setSelected(false);
        }

        mTabs.get(position).setSelected(true);
    }

    public int getSelectedTabPosition() {
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i).isSelected()) {
                return i;
            }
        }

        return INVALID_TAB_POSITION;
    }

    public Tab getTabAt(int position) {
        return mTabs.get(position);
    }

    public int getTabCount() {
        return mTabCount;
    }

    public void removeAllTabs() {
        mTabCount = 0;
        mTabs = new ArrayList<>();
    }

    public void removeTab(Tab tab) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i).equals(tab)) {
                mTabs.remove(i);
                break;
            }
        }
    }

    public void removeTabAt(int position) {
        mTabs.remove(position);
    }

    public enum LayoutGravity {
        CENTER,
        FILL
    }

    public static class Tab {

        private FrameLayout mView;
        private boolean mIsSelected, mIsFixed;

        private Tab(Context c, boolean isFixed, boolean isDarkThemed) {
            mIsFixed = isFixed;

            mView = new FrameLayout(c);
            if (isFixed) {
                mView.setMinimumWidth(c.getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_tab_min_width));
                mView.setPadding(c.getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_padding_sides),
                        c.getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_padding_top_inactive),
                        c.getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_padding_sides),
                        c.getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_padding_bottom));
            }

            int iconSize = c.getResources().getDimensionPixelSize(R.dimen.na_fixed_bottom_nav_icon_size);
            ImageView iv = new ImageView(c);
            FrameLayout.LayoutParams iconLp = new FrameLayout.LayoutParams(iconSize, iconSize);
            iconLp.gravity = Gravity.CENTER_HORIZONTAL;
            iv.setLayoutParams(iconLp);
            ViewCompat.setAlpha(iv, c.getResources().getDimension(R.dimen.na_inactive_opacity));
            mView.addView(iv);

            TextView tv = new TextView(c);
            FrameLayout.LayoutParams textLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            textLp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            tv.setLayoutParams(textLp);
            tv.setSingleLine();
            tv.setTextSize(c.getResources().getDimension(R.dimen.na_fixed_bottom_nav_text_size_inactive));
            mView.addView(tv);
        }

        @Nullable
        public Drawable getIcon() {
            return ((ImageView) mView.getChildAt(0)).getDrawable();
        }

        public void setIcon(@DrawableRes int res) {
            ((ImageView) mView.getChildAt(0)).setImageResource(res);
        }

        public void setIcon(Drawable d) {
            ((ImageView) mView.getChildAt(0)).setImageDrawable(d);
        }

        public CharSequence getText() {
            return ((TextView) mView.getChildAt(1)).getText();
        }

        public void setText(@StringRes int res) {
            ((TextView) mView.getChildAt(1)).setText(res);
        }

        public void setText(CharSequence s) {
            ((TextView) mView.getChildAt(1)).setText(s);
        }

        public Object getTag() {
            return mView.getTag();
        }

        public void setTag(Object tag) {
            mView.setTag(tag);
        }

        public boolean isSelected() {
            return mIsSelected;
        }

        public void setSelected(boolean selected) {
            mIsSelected = selected;
        }

        public boolean isFixed() {
            return mIsFixed;
        }

        public View getView() {
            return mView;
        }
    }
}
