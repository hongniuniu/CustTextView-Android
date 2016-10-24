package com.faith.ctv;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 自定义可滑动的文本view
 * Created by hongbing on 16/9/22.
 */
public class PicDescView extends LinearLayout {

    private static final String TAG = PicDescView.class.getSimpleName();
    private static final int FLING_MIN_DISTANCE = 6;// 移动最小距离
//    public static final String mDefaultStr = "我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人" +
//            "的的阿大发撒发生我是中国人我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人," +
//            "我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人," +
//            "我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人,我是中国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人我是中国人的的阿大发撒发生;我是中国人,我是中" +
//            "国人的的阿大发撒发生我是中国人的的阿大发撒发生我是中国人,我是中国人的的阿大发撒发生;我是中国人";

    private ScrollView mScrollView;
    private RelativeLayout mRelLayout;
    private TextView mTitleTv;
    private TextView mDescTv;

    // 默认文字,也就是实际应该显示的文字
    private String mDefaultStr;
    // 屏幕高度
    private int mScreenH = 0;
    // 评论受限制的最大高度
    private int mViewLimitMaxH = 0;
    // 控价的最大高度
    private int mViewMaxH = 0;
    // 控件的最小高度
    private int mViewMinH = 0;
    private int mLastY = 0;
    // 是否超出限制
    private boolean mIsLimitUp, mIsLimitDown;
    private boolean mIsUp,mIsDown;
    // 是否可以全部显示
    private boolean mIsAll;
    private boolean mDescIsNull;
    // 最大化高度是否大于受限高度
    private boolean mIsMaxView;

    public PicDescView(Context context) {
        this(context, null, 0);
    }

    public PicDescView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PicDescView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScreenH = getScreenHgt();
        mViewLimitMaxH = mScreenH / 2;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG,"测试:onFinishInflate...");

        mScrollView = (ScrollView)getChildAt(0);
        mRelLayout = (RelativeLayout) mScrollView.getChildAt(0);
        mTitleTv = (TextView) mRelLayout.getChildAt(0);
        mDescTv = (TextView) mRelLayout.getChildAt(1);
        mDefaultStr = mDescTv.getText().toString();
    }

    /**
     * 获取屏幕高度
     */
    private int getScreenHgt() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * dp转px
     */
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setTextDesc(final String title) {
        setTextDesc(title,"");
    }

    /**
     * 获取标题高度
     * @return
     */
    private int getTitleHgt(){
        Layout layout = mTitleTv.getLayout();
        int desired = layout.getLineTop(mTitleTv.getLineCount());
        int padding = mTitleTv.getCompoundPaddingTop() + mTitleTv.getCompoundPaddingBottom();
        return desired + padding;
    }

    /**
     * 动态计算控件的高度
     * @param vH
     */
    private void caclViewHgt(int vH){
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = vH;
        setLayoutParams(params);
    }

    public void setTextDesc(final String title, final String desc) {
        post(new Runnable() {
            @Override
            public void run() {

                mTitleTv.setText(title);

                mDescIsNull = false;
                mDescTv.setVisibility(VISIBLE);
                // 其它控件/间距高度(titleH + paddingTop&paddingBtm)
                int otherH = getTitleHgt() + dip2px(getContext(), 16) + dip2px(getContext(),8);

                if (TextUtils.isEmpty(desc)) {
                    mDescIsNull = true;
                    mDescTv.setVisibility(GONE);
                    caclViewHgt(otherH);
                    return;
                }
                mDefaultStr = desc;
                // 需要加上desc控件距离上边的距离
                otherH += dip2px(getContext(), 7);
                TextPaint paint = mDescTv.getPaint();
                // 一个字符的长度
                float oneTxtW = paint.measureText("中国人") / 3;
                // 描述文字控件每行的实际长度
                float rowW = mDescTv.getMeasuredWidth();
                // 每行最多能显示的文字数
                int rowCount = (int) Math.ceil(rowW / oneTxtW - 1);
//                int rowCount = (int) Math.ceil(rowW / oneTxtW);

                mDescTv.setText(desc);
                mScrollView.fullScroll(ScrollView.FOCUS_UP);
                if (desc.length() <= rowCount * 4) { // 全部显示
                    mIsAll = true;
                    // 计算显示高度
                    mViewMaxH = mViewMinH = mDescTv.getLineCount() * mDescTv.getLineHeight() + otherH;
                    caclViewHgt(mViewMinH);
                } else { // 截取显示
                    mIsAll = false;
                    // 计算最小高度(固定4行H + otherH)
                    mViewMinH = mDescTv.getLineHeight() * 4 + otherH;
                    caclViewHgt(mViewMinH);

                    // 默认最多保持四行
//                    int endIndex = 0;
//                    if (mDefaultStr.length() > rowCount * 4 + 2) {
//                        endIndex = rowCount * 4 + 4;//额外补字
//                    } else {
//                        endIndex = rowCount * 4;
//                    }
                    // 计算最小化时的显示内容
//                    mLimitStr = desc.subSequence(0, endIndex).toString();
//                    mDescTv.setText(mLimitStr);
                    // 计算总高度 = 最大化时显示的行数 * 行高 + 其它控件高度 -->默认在计算出来的行数上+1,不然会导致高度计算有问题,造成一行内容显示不全的问题
                    mViewMaxH = (mDefaultStr.length() / rowCount + 1) * mDescTv.getLineHeight() + otherH;
                    if(mViewMaxH > mViewLimitMaxH){ // 最大化高度大于受限高度
                        mViewMaxH = mViewLimitMaxH;
                        mIsMaxView = true;
                    }
//                    TDNewsUtil.dbgLog("测量:最大化高度 > 受限高度 = " + mIsMaxView);
                }

//                TDNewsUtil.dbgLog("测量:控件显示高度 = " + getHeight() +
//                        "-->屏幕高度 = " + mScreenH +
//                        "-->最大化受限高度 = " + mViewLimitMaxH +
//                        "-->描述文字的行高 = " + mDescTv.getLineHeight() +
//                        "-->计算出来的控件高度 = " + mViewMinH +
//                        "-->最大化时控件高度 = " + mViewMaxH +
//                        "-->每行最多显示字符数 = " + rowCount + "-->是否全部显示 = " + mIsAll);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
////        TDNewsUtil.dbgLog("测量:onlayout" + "-->l = " + l + "-->t = " + t + "-->r = " + r + "-->b = " + b);
//        if (mIsInit) {
////            mDescTv.layout(mDescTv.getLeft(), mDescTv.getTop(), mDescTv.getRight(), mDescTv.getBottom());
////            mScrollView.layout(mScrollView.getLeft(), mScrollView.getTop(), mScrollView.getRight(), mScrollView.getBottom());
//            mRelLayout.layout(mRelLayout.getLeft(), mRelLayout.getTop(), mRelLayout.getRight(), mRelLayout.getBottom());
//            mIsInit = false;
//        } else {
////            mDescTv.layout(mDescTv.getLeft(), mDescTv.getTop(), mDescTv.getRight(), b);
////            mScrollView.layout(mScrollView.getLeft(), mScrollView.getTop(), mScrollView.getRight(), b);
//            mRelLayout.layout(mRelLayout.getLeft(), mRelLayout.getTop(), mRelLayout.getRight(), b);
//        }
//    }

    private boolean mSvIsTop;
    private int mDownY;
    private boolean isDisTouch;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if(mIsAll || mDescIsNull) return false;

        if (mIsMaxView && mIsLimitUp) { // 最最大化
            if (mScrollView.getScrollY() > 0) { // scrollview没有到顶部,先让它到顶部
                mSvIsTop = false;
                mScrollView.requestDisallowInterceptTouchEvent(false);
//                requestDisallowInterceptTouchEvent(true);

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mDownY = 0;
                    mLastY = 0;
//                    mIsUp = false;
//                    mIsDown = false;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    isDisTouch = true;

//                    mDownY = (int) event.getY();
//                    mLastY = (int) event.getY();
                }

                Log.d(TAG,"测试:被拦截");
                return super.dispatchTouchEvent(event); // 继续往下传递
            }else{ // 顶部
                mSvIsTop = true;
//                mScrollView.setEnabled(false);
                // 从拦截到释放的过程,需要重新计算按下值
                if(isDisTouch){
                    mDownY = (int) event.getY();
                    mLastY = (int) event.getY();
                    isDisTouch = false;
                }

                mScrollView.requestDisallowInterceptTouchEvent(true);
//                requestDisallowInterceptTouchEvent(false);
            }
        }

        int y = (int) event.getY();

//        switch (event.getAction() & event.getActionMasked()) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                mDownY = y;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mScrollView.requestDisallowInterceptTouchEvent(true);
                mDownY = 0;
                mLastY = 0;
                mIsLimitUp = false;
                mIsLimitDown = false;
                mIsUp = false;
                mIsDown = false;
                mSvIsTop = false;
                // scrollview是停留在顶部的,那么对应的变量需要置成true
                if(mScrollView.getScrollY() <= 0){
                    mSvIsTop = true;
                }
                // 设置字数超出省略号
//                if (getHeight() <= mViewMinH) {
//                    mDescTv.setText(mLimitStr);
//                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = y - mLastY;
                if(!mIsLimitUp){ // 没有到达最最大化,不能使用scrollview的事件
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                    if(mScrollView.getScrollY() <= 0){
                        mSvIsTop = true;
                    }
                }

                // 用作方位判断
                if (y < mDownY && Math.abs(y - mDownY) > FLING_MIN_DISTANCE) { // 向上滑动
                    if(mIsDown){ // 先向下,然后变成向上
                        mLastY = y;
                        mIsDown = false;
                        Log.d(TAG,"测试:-->先向下,然后变成向上");
                    }
                    mIsUp = true;
                } else if (y > mDownY && Math.abs(y - mDownY) > FLING_MIN_DISTANCE) {  // 向下滑动
                    if(mIsUp){ // 先向下,然后变成向上
                        Log.d(TAG,"测试:-->先向上,然后变成向下");
                        mLastY = y;
                        mIsUp = false;
                    }
                    mIsDown = true;
                }

                Log.d(TAG,"测试:-->mIsUp = " + mIsUp + "-->mIsDown = " + mIsDown  + "-->y = " + y + "-->mDownY = " + mDownY);

                if (mIsUp) { // 向上滑动

                    if (getHeight() >= mViewMaxH && mIsMaxView) {
                        // 屏蔽自己,scroolview接收
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                        return super.dispatchTouchEvent(event); // 继续往下传递
                    }

                    int upCha = getHeight() + Math.abs(dy);
                    Log.d(TAG,"测试:upCha = " + upCha + "-->mViewMaxH = " + mViewMaxH);
                    mIsLimitUp = false;
                    if (upCha >= mViewMaxH) {
                        mIsLimitUp = true;
                        caclViewHgt(mViewMaxH);
                        Log.d(TAG,"测试:超过最大高度限制了...mIsMaxView = " + mIsMaxView);

//                        if (mIsMaxView) {
//                            // 屏蔽自己,scroolview接收
//                            mScrollView.requestDisallowInterceptTouchEvent(false);
//                            return super.dispatchTouchEvent(event); // 继续往下传递
//                        }else{
////                            mScrollView.fullScroll(ScrollView.FOCUS_UP);
//                        }
                        return false;
                    }
                    if(!mIsLimitUp)
                        caclViewHgt(upCha);
//                    layout(getLeft(),getTop()+dy,getRight(),getBottom());
                } else if (mIsDown) {  // 向下滑动
                    Log.d(TAG,"测试:手势向下-->mScrollView.getScrollY() = " + mScrollView.getScrollY() + "-->mSvIsTop = " + mSvIsTop);
                    mScrollView.fullScroll(ScrollView.FOCUS_UP);
                    if (mSvIsTop) { // 下拉控件的时候,scrollview必须是在顶部,不然就是下拉scroolview的内容了
                        // 将最大限制变量置成false,防止反复上下拖动导致的bug
                        if (getHeight() < mViewMaxH) {
                            mIsLimitUp = false;
                        }

                        int downCha = getHeight() - dy;
                        if (downCha <= mViewMinH) {
                            mIsLimitDown = true;
                            caclViewHgt(mViewMinH);
//                          TDNewsUtil.dbgLog("测量:低于最小高度限制了..." + mViewMinH);
                            return false;
                        }

                        caclViewHgt(downCha);
//                        layout(getLeft(), getTop() + dy, getRight(), getBottom());
                    }
                }

                // 更新按下的点
//                mDownY = y;
                break;
        }

        return super.dispatchTouchEvent(event);
    }

}
