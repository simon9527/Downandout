package com.simonmeng.demo.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simonmeng.demo.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private LinearLayout mHeaderView;
    private View beAddHeaderView;
    private int downY = -1;
    private int measureHeight;
    private View mPullDownHeaderView;
    private final int PULL_DOWN_REFRESH = 0;
    private final int RELEASE_REFRESH = 1;
    private final int REFRESHING = 2;
    private int headerCurrentHint = PULL_DOWN_REFRESH;
    private RotateAnimation downAnimation;
    private RotateAnimation upAnimation;
    private ImageView ivArrow;
    private ProgressBar mProgressBar;
    private TextView tvHeaderHint;
    private TextView tvLastUpdateTime;
    private int mListViewOnScreenY = -1; // 当前ListView的左上角在屏幕中y轴的坐标点, 默认为: -1
    private OnRefreshListener mOnRefreshListener; // 用户的回调事件
    private View mFooterView; // 加载更多脚布局
    private int mFooterViewHeight; // 脚布局的高度
    private boolean isLoadingMore = false; // 是否正在加载更多中, 默认为: false
    private boolean isEnabledPullDownRefresh = true; // 是否启用下拉刷新的功能
    private boolean isEnabledLoadMoreRefresh = true;  // 是否启用加载更多的功能

    public RefreshListView(Context context){
        super(context);
        initPullDownHeaderView();
        initLoadMoreFooterView();
    }
    public RefreshListView(Context context,AttributeSet attrs){
        super(context,attrs);
        initPullDownHeaderView();
        initLoadMoreFooterView();
    }
    /**
     * 初始化加载更多的脚布局
     */
    private void initLoadMoreFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.refreshlistview_footer, null);
        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        addFooterView(mFooterView);

        // 给当前ListView设置滚动的监听事件
        setOnScrollListener(this);
    }

    /**因为自定义的listview就是比系统的多了一个pulldownheader
     * 所以在此处初始化header布局，通过addheaderview即可添加
     * notice1：自定义的listview创建就已经添加header了，然后一会还会调用它添加一个轮播图的作为第二个header，
     *          header的顺序是，先添加的在上面，和listview的item差不多：从上至下0,1,2,3...
     * notice2:虽然布局上下有顺序，但是二者都是listview的headerview，所以position都是0，getChildAt(0)会把二者一起得到，然后再来区分，
     *        很麻烦，要想获取后添加的headerview，自定义listview暴露一个方法---customListViewAddHeader*/
    private void initPullDownHeaderView(){
        mHeaderView = (LinearLayout) View.inflate(getContext(), R.layout.refreshlistview_header, null);
        mPullDownHeaderView = mHeaderView.findViewById(R.id.ll_refreshlistview_pull_down_header);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_refreshlistview_header_arrow);
        mProgressBar = (ProgressBar) mHeaderView.findViewById(R.id.pb_refreshlistview_header);
        tvHeaderHint = (TextView) mHeaderView.findViewById(R.id.tv_refreshlistview_header_state);
        tvLastUpdateTime = (TextView) mHeaderView.findViewById(R.id.tv_refreshlistview_header_last_update_time);


        mPullDownHeaderView.measure(0,0);
        measureHeight = mPullDownHeaderView.getMeasuredHeight();
        mPullDownHeaderView.setPadding(0, -measureHeight, 0, 0);
        this.addHeaderView(mHeaderView);
        initArrowAnimation();
    }
    //为了更好获取后添加的header就不通过addheaderview，而是在自定义的refreshlistviewheader的布局，
    // 通过一个LinearLayout包裹，然后再添加，就往这个LinearLayout中添加，orientation="vertical" ，直接添加到下面,
    // 用addView即可[添加到header上]，而不是addHeaderView[这是添加到listview上]
    //内部定义一个view==beAddHeaderView，然后就可以暴露方法获取后添加的view进行所需操作了
    public void customListViewAddHeader(View beAddHeaderView){
        this.beAddHeaderView = beAddHeaderView;
        mHeaderView.addView(beAddHeaderView);
    }

    /**实现ListView的onTouchEvent()监控手势
     * return true就是自己处理掉了touchevent，但是我们不能把所有touchevent都处理掉
     * 只是listview在顶部"按住下拉"需要处理，而其他的如向下滑动还是返回给系统的ListView自己处理*/
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = (int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(downY == -1){
                    //因为只要点击到屏幕downY就是正数，我们设置的downY默认值为负数-1，就是没得到按下事件[有时动作太快了]，需要重新获取
                    downY = (int)ev.getY();
                }
                if(!isEnabledPullDownRefresh) {
                    break;
                }
                //当处于正在刷新中，就不能再对下拉操作处理
                if(headerCurrentHint == REFRESHING){
                    break;
                }
                //如果轮播图没有完全显示，也是break，不响应下拉操作。先判断beAddHeaderView != null，说明添加了header。
                if(beAddHeaderView != null) {
                    // 第0位是x轴的地址, 第1位是y轴的值--用于承装获取的ListView在屏幕左上角的坐标
                    int[] location = new int[2];
                    if(mListViewOnScreenY == -1) {
                        this.getLocationOnScreen(location);
                        mListViewOnScreenY= location[1];
                    }
                    // 取出轮播图在屏幕中y轴的值，如果轮播图在屏幕中y轴的值, 小于 当前Listview在屏幕中y轴的值，轮播图没有完全显示, 不执行下拉头的操作, 直接跳出.
                    beAddHeaderView.getLocationOnScreen(location);
                    if(location[1] < mListViewOnScreenY) {
                        break;
                    }
                }

                int moveY = (int)ev.getY();
                int spaceY = moveY-downY;
                /**spaceY>0即向下滑动，getFirstVisiblePosition() == 0即listview在顶部---return true我们自己处理
                 * paddingTop < 0就是没有完全拉出，hint应该提示“下拉刷新”，>0就是完全拉出应该提示“松开刷新”*/
                if(spaceY > 0 && getFirstVisiblePosition() == 0){
                    int paddingTop = -measureHeight + spaceY;
                    if(paddingTop < 0 && headerCurrentHint ==  RELEASE_REFRESH){
                        headerCurrentHint = PULL_DOWN_REFRESH;
                        refreshPullDownState();
                    }else if(paddingTop > 0 && headerCurrentHint ==  PULL_DOWN_REFRESH){
                        headerCurrentHint = RELEASE_REFRESH;
                        refreshPullDownState();
                    }
                    mPullDownHeaderView.setPadding(0,paddingTop,0,0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                downY = -1;

                if(headerCurrentHint == PULL_DOWN_REFRESH) {
                    // 当前是下拉刷新, 把头布局隐藏
                    mPullDownHeaderView.setPadding(0, -measureHeight, 0, 0);
                } else if(headerCurrentHint == RELEASE_REFRESH) {
                    // 当前是松开刷新, 进入正在刷新中状态
                    headerCurrentHint = REFRESHING;
                    refreshPullDownState();
                    mPullDownHeaderView.setPadding(0, 0, 0, 0);
                    if(mOnRefreshListener != null) {
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
    private void initArrowAnimation(){
        upAnimation = new RotateAnimation(
                0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);//y以自身中心旋转
                upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(
                -180, -360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }
    private void refreshPullDownState(){
        switch (headerCurrentHint) {
            case PULL_DOWN_REFRESH: // 下拉刷新
                // 箭头执行向下旋转的动画
                ivArrow.startAnimation(downAnimation);
                // 把状态修改为: 下拉刷新
                tvHeaderHint.setText("下拉刷新");
                break;
            case RELEASE_REFRESH: // 释放刷新
                // 箭头执行向下旋转的动画
                ivArrow.startAnimation(upAnimation);
                // 把状态修改为: 下拉刷新
                tvHeaderHint.setText("松开刷新");
                break;
            case REFRESHING: // 正在刷新中
                ivArrow.clearAnimation(); // 把动画清除掉
                ivArrow.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                tvHeaderHint.setText("正在刷新");
                break;
            default:
                break;
        }
    }

    /**
     * 当用户刷新数据完成时, 回调此方法, 把下拉刷新的头或者加载更多的脚给隐藏
     */
    public void OnRefreshDataFinish() {
        if(isLoadingMore) {
            // 当前是加载更多, 隐藏脚布局
            isLoadingMore = false;
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        } else {
            // 当前是下拉刷新, 隐藏头布局
            ivArrow.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            tvHeaderHint.setText("下拉刷新");
            tvLastUpdateTime.setText("上次刷新: " + getCurrentTime());
            mPullDownHeaderView.setPadding(0, -measureHeight, 0, 0);
            headerCurrentHint = PULL_DOWN_REFRESH;
        }
    }
    /**
     * 获取当前系统的时间, 格式为: 2014-11-16 16:07:12
     * @return
     */
    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date());
    }
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(!isEnabledLoadMoreRefresh) {
            return;
        }

        // 当滚动停止时, 当前ListView是在底部(屏幕上最后一个显示的条目的索引是总长度 -1)
        if(scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            // 滚动停止
            if((getLastVisiblePosition() == getCount() -1) && !isLoadingMore) {
                System.out.println("滑动到底部了");

                isLoadingMore = true;
                // 显示脚布局
                mFooterView.setPadding(0, 0, 0, 0);
                // 让ListView滑动到底部.--把footer显示出来
                setSelection(getCount());

                if(mOnRefreshListener != null) {
                    mOnRefreshListener.onLoadingMore();
                }
            }
        }



    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    /**
     * 设置是否启用下拉刷新
     * @param b
     */
    public void setEnabledPullDownRefresh(boolean b) {
        isEnabledPullDownRefresh = b;
    }

    public void setEnabledLoadMoreRefresh(boolean b) {
        isEnabledLoadMoreRefresh = b;
    }
    /**
     * @author andong
     * 自定义ListView刷新的监听事件
     */
    public interface OnRefreshListener {

        /**
         * 当下拉刷新时, 回调此方法.
         */
        public void onPullDownRefresh();

        /**
         * 当加载更多时触发此方法.
         */
        public void onLoadingMore();
    }
}
