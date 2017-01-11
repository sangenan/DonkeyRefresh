package com.example.sangenan.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Hankkin on 16/4/10.
 */
public class DonkeyRefreshListView extends ListView implements AbsListView.OnScrollListener{
    private static final int DONE = 0;      //刷新完毕状态
    private static final int PULL_TO_REFRESH = 1;   //下拉刷新状态
    private static final int RELEASE_TO_REFRESH = 2;    //释放状态
    private static final int REFRESHING = 3;    //正在刷新状态
    private static final int RATIO = 2;
    private RelativeLayout headView;    //下拉刷新头
    private int headViewHeight; //头高度
    private float startY;   //开始Y坐标
    private float offsetY;  //Y轴偏移量
    private OnDonkeyRefreshListener mOnRefreshListener;  //刷新接口
    private int state;  //状态值
    private int mFirstVisibleItem;  //第一项可见item索引
    private boolean isRecord;   //是否记录
    private boolean isEnd;  //是否结束
    private boolean isRefreable;    //是否刷新

    private drawDog donkey;
    private drawBall ball;
    private  drawPlanet planet;
    private static final int startRotate =300;
    private static final int endRotate = 360;
    public DonkeyRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public DonkeyRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DonkeyRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface OnDonkeyRefreshListener{
        void onRefresh();
    }

    /**
     * 回调接口，想实现下拉刷新的listview实现此接口
     * @param onRefreshListener
     */
    public void setOnDonkeyRefreshListener(OnDonkeyRefreshListener onRefreshListener){
        mOnRefreshListener = onRefreshListener;
        isRefreable = true;
    }

    /**
     * 刷新完毕，从主线程发送过来，并且改变headerView的状态和文字动画信息
     */
    public void setOnRefreshComplete(){
        //一定要将isEnd设置为true，以便于下次的下拉刷新
        isEnd = true;
        state = DONE;

        changeHeaderByState(state);
    }

    private void init(Context context) {
        //关闭view的OverScroll
        setOverScrollMode(OVER_SCROLL_NEVER);
        setOnScrollListener(this);
        //加载头布局
        headView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.donkey_content,this,false);
        //测量头布局
        measureView(headView);
        //给ListView添加头布局
        addHeaderView(headView);
        //设置头文件隐藏在ListView的第一项
        headViewHeight = headView.getMeasuredHeight();
        headView.setPadding(0, -headViewHeight, 0, 0);

        donkey = (drawDog) headView.findViewById(R.id.donkey);
        ball = (drawBall) headView.findViewById(R.id.ball);
        planet = (drawPlanet) headView.findViewById(R.id.planet);
        state = DONE;
        isEnd = true;
        isRefreable = false;


    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnd) {//如果现在时结束的状态，即刷新完毕了，可以再次刷新了，在onRefreshComplete中设置
            if (isRefreable) {//如果现在是可刷新状态   在setOnMeiTuanListener中设置为true
                switch (ev.getAction()){
                    //用户按下
                    case MotionEvent.ACTION_DOWN:
                        //如果当前是在listview顶部并且没有记录y坐标
                        if (mFirstVisibleItem == 0 && !isRecord) {
                            //将isRecord置为true，说明现在已记录y坐标
                            isRecord = true;
                            //将当前y坐标赋值给startY起始y坐标
                            startY = ev.getY();

                            ball.startBallAnim(1000);
                            planet.startTranslatePlanet(1000);
                        }
                        break;
                    //用户滑动
                    case MotionEvent.ACTION_MOVE:
                        //再次得到y坐标，用来和startY相减来计算offsetY位移值
                        float tempY = ev.getY();
                        //再起判断一下是否为listview顶部并且没有记录y坐标
                        if (mFirstVisibleItem == 0 && !isRecord) {
                            isRecord = true;
                            startY = tempY;
                        }
                        //如果当前状态不是正在刷新的状态，并且已经记录了y坐标
                        if (state!=REFRESHING && isRecord ) {
                            //计算y的偏移量
                            offsetY = tempY - startY;
                            //计算当前滑动的高度
                            float currentHeight = (-headViewHeight+offsetY/RATIO);
                            //用当前滑动的高度和头部headerView的总高度进行比 计算出当前滑动的百分比 0到1
                            float currentProgress = 1+currentHeight/headViewHeight;
                            //如果当前百分比大于1了，将其设置为1，目的是让第一个状态的椭圆不再继续变大
                            if (currentProgress>=1) {
                                currentProgress = 1;
                                donkey.changeDonkey();
                            }

                            int margeRotate = (int) ((endRotate -startRotate) * currentProgress +startRotate);
                            donkey.setDonkeyRotate(margeRotate , -offsetY /5 );
                            ball.UpBall(-offsetY / 5);

                            //如果当前的状态是放开刷新，并且已经记录y坐标
                            if (state == RELEASE_TO_REFRESH && isRecord) {

                                setSelection(0);
                                //如果当前滑动的距离小于headerView的总高度
                                if (-headViewHeight+offsetY/RATIO<0) {
                                    //将状态置为下拉刷新状态
                                    state = PULL_TO_REFRESH;
                                    //根据状态改变headerView，主要是更新动画和文字等信息
                                    changeHeaderByState(state);
                                    //如果当前y的位移值小于0，即为headerView隐藏了
                                }else if (offsetY<=0) {
                                    //将状态变为done
                                    state = DONE;
                                    //根据状态改变headerView，主要是更新动画和文字等信息
                                    changeHeaderByState(state);
                                }
                            }
                            //如果当前状态为下拉刷新并且已经记录y坐标
                            if (state == PULL_TO_REFRESH && isRecord) {
                                setSelection(0);
                                //如果下拉距离大于等于headerView的总高度
                                if (-headViewHeight+offsetY/RATIO>=0) {
                                    //将状态变为放开刷新
                                    state = RELEASE_TO_REFRESH;
                                    //根据状态改变headerView，主要是更新动画和文字等信息
                                    changeHeaderByState(state);
                                    //如果当前y的位移值小于0，即为headerView隐藏了
                                }else if (offsetY<=0) {
                                    //将状态变为done
                                    state = DONE;
                                    //根据状态改变headerView，主要是更新动画和文字等信息
                                    changeHeaderByState(state);
                                }
                            }
                            //如果当前状态为done并且已经记录y坐标
                            if (state == DONE && isRecord) {
                                //如果位移值大于0
                                if (offsetY>=0) {
                                    //将状态改为下拉刷新状态
                                    state = PULL_TO_REFRESH;
                                    changeHeaderByState(state);
                                }
                            }
                            //如果为下拉刷新状态
                            if (state == PULL_TO_REFRESH) {
                                //则改变headerView的padding来实现下拉的效果
                                headView.setPadding(0,(int)(-headViewHeight+offsetY/RATIO) ,0,0);
                            }
                            //如果为放开刷新状态
                            if (state == RELEASE_TO_REFRESH) {
                                //改变headerView的padding值
                                headView.setPadding(0, 0, 0, 0);
                            }
                        }
                        break;
                    //当用户手指抬起时
                    case MotionEvent.ACTION_UP:
                        //如果当前状态为下拉刷新状态
                        if (state == PULL_TO_REFRESH) {
                            //平滑的隐藏headerView
                            this.smoothScrollBy((int)(-headViewHeight+offsetY/RATIO)+headViewHeight, 500);
                            //根据状态改变headerView
                            changeHeaderByState(state);
                        }
                        //如果当前状态为放开刷新
                        if (state == RELEASE_TO_REFRESH) {
                            //平滑的滑到正好显示headerView
                           // this.smoothScrollBy((int)(-headViewHeight+offsetY/RATIO), 500);
                            //将当前状态设置为正在刷新
                            state = REFRESHING;
                            //回调接口的onRefresh方法
                            mOnRefreshListener.onRefresh();
                            //根据状态改变headerView
                            changeHeaderByState(state);
                        }
                        //这一套手势执行完，一定别忘了将记录y坐标的isRecord改为false，以便于下一次手势的执行
                        isRecord = false;
                        break;
                }

            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据状态改变headerView的动画和文字显示
     * @param state
     */
    private void changeHeaderByState(int state){
        switch (state) {
            case DONE://如果的隐藏的状态
                //设置headerView的padding为隐藏
                headView.setPadding(0, -headViewHeight, 0, 0);
                break;
            case RELEASE_TO_REFRESH://当前状态为放开刷新
                break;
            case PULL_TO_REFRESH://当前状态为下拉刷新
                break;
            case REFRESHING://当前状态为正在刷新
                donkey.launchDonkey();
                ball.accelerateBall(500);
                break;
            default:
                break;
        }
    }

    /**
     * 测量View
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

}
