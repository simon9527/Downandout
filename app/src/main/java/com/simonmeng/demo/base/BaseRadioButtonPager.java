package com.simonmeng.demo.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.simonmeng.demo.R;

/**
 * News界面底部radio button对应的pager，布局类似，抽取成基类
 */

public class BaseRadioButtonPager {

    //调用该基类布局，会返回一个rootview，答题就是基类中的上面一个RelativeLayout显示标题，下面一个空白FrameLayout，供调用者修改
    private View rootView;
    public  Context mContext;
    public TextView tv_base_radio_button_pager_header;
    public FrameLayout frameLayoutContent;
    public ImageButton ib_title_menu;

    public BaseRadioButtonPager(Context context){
        this.mContext = context;
        rootView = initView();
    }
    /**
    *initialize the layout,find out each view,so that the subclass can reuse it ,and change the view.
     * notice:the views that find by id should be Fields and public,,so that subclass can use it*/
    private View initView(){
        View view = View.inflate(mContext, R.layout.base_radiobutton_viewpager, null);
        frameLayoutContent = (FrameLayout) view.findViewById(R.id.fl_base_radiobutton_pager_content);
        tv_base_radio_button_pager_header = (TextView)view.findViewById(R.id.tv_base_radio_button_pager_header);
        ib_title_menu = (ImageButton) view.findViewById(R.id.ib_title_menu);

        return view;
    }
    //提供一个get方法，把初始化后的rootView给调用者
    public View getRootView(){
        return rootView;
    }
    //provide a abstract method,subclass overwrite it,and fill itself data.
    public void initData(){

    }
}
