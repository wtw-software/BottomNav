package com.nasahapps.bottomnav;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
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
    private static final long ANIM_DURATION = 250;
    private static final Interpolator ANIM_INTERPOLATOR = new FastOutSlowInInterpolator();

    @ColorInt
    private int mAccentColor;
    private int mTabCount, mCurrentTab;
    private boolean mUsesDarkTheme;
    private ArrayList<Tab> mTabs = new ArrayList<>();
    private LayoutGravity mLayoutGravity = LayoutGravity.FILL;
    private OnTabSelectedListener mOnTabSelectedListener;

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
                mUsesDarkTheme = ta.getBoolean(R.styleable.BottomNavigationBar_darkTheme, false);

                mAccentColor = mUsesDarkTheme ? Color.WHITE
                        : ta.getColor(R.styleable.BottomNavigationBar_accentColor, 0);
                if (mAccentColor == 0) {
                    TypedValue tv = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.colorAccent, tv, true);
                    mAccentColor = tv.data;
                }
            } finally {
                ta.recycle();
            }

            if (isInEditMode()) {
                addTab(newTab().setText("Recents").setIcon(R.drawable.ic_na_test_history));
                addTab(newTab().setText("Favorites").setIcon(R.drawable.ic_na_test_favorite));
                addTab(newTab().setText("Nearby").setIcon(R.drawable.ic_na_test_location));
            }
        }

        ViewCompat.setElevation(this, getResources().getDimensionPixelSize(R.dimen.na_bottom_nav_elevation));
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void addTab(Tab tab) {
        addTab(tab, mTabs.size());
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
            addView(tab.getView(), new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        } else {

        }
    }

    public Tab newTab() {
        return new Tab(getContext(), true);
    }

    private void checkTabCount() {
        if (mTabCount >= 5) {
            throw new RuntimeException("You can only have a max of 5 tabs! If you want more, it's " +
                    "best to use a navigation drawer.");
        }
    }

    public void setTabSelected(int position) {
        if (getSelectedTabPosition() != position) {
            for (Tab tab : mTabs) {
                tab.setSelected(false);
            }

            mCurrentTab = position;
            mTabs.get(position).setSelected(true);
        }
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
        removeAllViews();
    }

    public void removeTab(Tab tab) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i).equals(tab)) {
                removeView(mTabs.get(i).getView());
                mTabs.remove(i);
                break;
            }
        }
    }

    public void removeTabAt(int position) {
        removeView(mTabs.get(position).getView());
        mTabs.remove(position);
    }

    public void setUsesDarkTheme(boolean usesDarkTheme) {
        mUsesDarkTheme = usesDarkTheme;
        for (Tab tab : mTabs) {
            tab.adjustForDarkTheme();
        }
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        mOnTabSelectedListener = listener;
    }

    public enum LayoutGravity {
        CENTER,
        FILL
    }

    public interface OnTabSelectedListener {
        void onTabSelected(Tab tab, int position);

        void onTabReselected(Tab tab, int position);

        void onTabUnselected(Tab tab, int position);
    }

    public class Tab {

        private FrameLayout mView;
        private boolean mIsSelected, mIsFixed;
        private Drawable mOriginalDrawable;
        private int mOriginalDrawableResource;

        private Tab(Context c, boolean isFixed) {
            mIsFixed = isFixed;

            mView = new FrameLayout(c);
            TypedValue typedValue = new TypedValue();
            c.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, typedValue, true);
            mView.setBackgroundResource(typedValue.resourceId);
            mView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (((ViewGroup) v.getParent())).indexOfChild(v);
                    if (mOnTabSelectedListener != null) {
                        if (mIsSelected) {
                            // This tab has been reselected
                            mOnTabSelectedListener.onTabReselected(Tab.this, position);
                        } else {
                            // New tab selected
                            mOnTabSelectedListener.onTabSelected(Tab.this, position);
                            mOnTabSelectedListener.onTabUnselected(mTabs.get(mCurrentTab), mCurrentTab);
                        }
                    }

                    setTabSelected(position);
                }
            });

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
            iv.setAlpha(Utils.getFloatResource(c, mUsesDarkTheme ? R.dimen.na_inactive_opacity_dark : R.dimen.na_inactive_opacity));
            mView.addView(iv);

            TextView tv = new TextView(c);
            FrameLayout.LayoutParams textLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            textLp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            tv.setLayoutParams(textLp);
            tv.setSingleLine();
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    c.getResources().getDimension(R.dimen.na_fixed_bottom_nav_text_size_inactive));
            tv.setTextColor(ContextCompat.getColor(c, mUsesDarkTheme ? R.color.inactive_tint_dark : R.color.inactive_tint));
            mView.addView(tv);
        }

        public boolean isFixed() {
            return mIsFixed;
        }

        public Tab setFixed(boolean fixed) {
            mIsFixed = fixed;
            return this;
        }

        public boolean isSelected() {
            return mIsSelected;
        }

        public Tab setSelected(boolean selected) {
            boolean wasPreviouslySelected = mIsSelected;
            mIsSelected = selected;

            if (mIsFixed) {
                // Animate changes in paddingTop and textSize, as well as tint color
                ValueAnimator paddingTopAnim = ValueAnimator.ofInt(mView.getPaddingTop(),
                        mView.getResources().getDimensionPixelSize(selected ? R.dimen.na_fixed_bottom_nav_padding_top_active
                                : R.dimen.na_fixed_bottom_nav_padding_top_inactive));
                paddingTopAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mView.setPadding(mView.getPaddingLeft(), (int) animation.getAnimatedValue(),
                                mView.getPaddingRight(), mView.getPaddingBottom());
                    }
                });
                setupAnimator(paddingTopAnim).start();

                ValueAnimator textSizeAnim = ValueAnimator.ofFloat(((TextView) mView.getChildAt(1)).getTextSize(),
                        mView.getResources().getDimension(selected ? R.dimen.na_fixed_bottom_nav_text_size_active
                                : R.dimen.na_fixed_bottom_nav_text_size_inactive));
                textSizeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ((TextView) mView.getChildAt(1)).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                (float) animation.getAnimatedValue());
                    }
                });
                setupAnimator(textSizeAnim).start();

                ValueAnimator imageAlphaAnim = ValueAnimator.ofFloat(mView.getChildAt(0).getAlpha(),
                        selected ? 1f : Utils.getFloatResource(mView.getContext(),
                                mUsesDarkTheme ? R.dimen.na_inactive_opacity_dark : R.dimen.na_inactive_opacity));
                imageAlphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mView.getChildAt(0).setAlpha((float) animation.getAnimatedValue());
                    }
                });
                setupAnimator(imageAlphaAnim).start();

                ValueAnimator textColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(),
                        ((TextView) mView.getChildAt(1)).getCurrentTextColor(), selected ? mAccentColor
                                : ContextCompat.getColor(mView.getContext(),
                                mUsesDarkTheme ? R.color.inactive_tint_dark : R.color.inactive_tint));
                textColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ((TextView) mView.getChildAt(1)).setTextColor((int) animation.getAnimatedValue());
                    }
                });
                setupAnimator(textColorAnim).start();

                // We'll only animate image tint if the selection state of this tab has changed
                if (wasPreviouslySelected != selected) {
                    int originalColor = mUsesDarkTheme ? Color.WHITE : Color.BLACK;
                    ValueAnimator imageTintAnim = ValueAnimator.ofObject(new ArgbEvaluator(),
                            selected ? originalColor : mAccentColor,
                            selected ? mAccentColor : originalColor);
                    imageTintAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Drawable wrappedDrawable = null;
                            if (mOriginalDrawable != null) {
                                wrappedDrawable = DrawableCompat.wrap(mOriginalDrawable);
                            } else if (mOriginalDrawableResource != 0) {
                                wrappedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(mView.getContext(),
                                        mOriginalDrawableResource));
                            }

                            if (wrappedDrawable != null) {
                                wrappedDrawable = DrawableCompat.wrap(wrappedDrawable);
                                DrawableCompat.setTint(wrappedDrawable, (int) animation.getAnimatedValue());
                                ((ImageView) mView.getChildAt(0)).setImageDrawable(wrappedDrawable);
                            }
                        }
                    });
                    setupAnimator(imageTintAnim).start();
                }
            } else {

            }

            return this;
        }

        private Animator setupAnimator(Animator anim) {
            anim.setDuration(ANIM_DURATION);
            anim.setInterpolator(ANIM_INTERPOLATOR);
            return anim;
        }

        public void adjustForDarkTheme() {
            if (mOriginalDrawable != null) {
                setIcon(mOriginalDrawable);
            } else if (mOriginalDrawableResource != 0) {
                setIcon(mOriginalDrawableResource);
            }

            float alpha;
            if (mIsSelected) {
                alpha = 1f;
            } else {
                alpha = Utils.getFloatResource(mView.getContext(),
                        mUsesDarkTheme ? R.dimen.na_inactive_opacity_dark : R.dimen.na_inactive_opacity);
            }
            mView.getChildAt(0).setAlpha(alpha);

            int textColor;
            if (mUsesDarkTheme) {
                textColor = mIsSelected ? Color.WHITE : ContextCompat.getColor(mView.getContext(), R.color.inactive_tint_dark);
            } else {
                textColor = mIsSelected ? mAccentColor : ContextCompat.getColor(mView.getContext(), R.color.inactive_tint);
            }
            // noinspection ResourceType
            ((TextView) mView.getChildAt(1)).setTextColor(textColor);
        }

        @Nullable
        public Drawable getIcon() {
            return ((ImageView) mView.getChildAt(0)).getDrawable();
        }

        public Tab setIcon(@DrawableRes int res) {
            mOriginalDrawableResource = res;
            mOriginalDrawable = ContextCompat.getDrawable(mView.getContext(), res);
            ((ImageView) mView.getChildAt(0))
                    .setImageDrawable(tintDrawableForTheme(mOriginalDrawable));
            return this;
        }

        public Tab setIcon(Drawable d) {
            mOriginalDrawable = d;
            ((ImageView) mView.getChildAt(0)).setImageDrawable(tintDrawableForTheme(mOriginalDrawable));
            return this;
        }

        private Drawable tintDrawableForTheme(Drawable d) {
            Drawable wrappedDrawable = DrawableCompat.wrap(d);
            DrawableCompat.setTint(wrappedDrawable, mUsesDarkTheme ? Color.WHITE : Color.BLACK);
            return wrappedDrawable;
        }

        public CharSequence getText() {
            return ((TextView) mView.getChildAt(1)).getText();
        }

        public Tab setText(@StringRes int res) {
            ((TextView) mView.getChildAt(1)).setText(res);
            return this;
        }

        public Tab setText(CharSequence s) {
            ((TextView) mView.getChildAt(1)).setText(s);
            return this;
        }

        public Object getTag() {
            return mView.getTag();
        }

        public Tab setTag(Object tag) {
            mView.setTag(tag);
            return this;
        }

        public View getView() {
            return mView;
        }
    }
}
