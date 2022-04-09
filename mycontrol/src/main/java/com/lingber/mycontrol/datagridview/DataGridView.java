package com.lingber.mycontrol.datagridview;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lingber.mycontrol.R;
import com.lingber.mycontrol.datagridview.i18n.LangTextView;
import com.lingber.mycontrol.datagridview.i18n.Util.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author :ShuboLin
 * @CreatTime: 2020-05-14 18:40.
 * @Description:
 */

public class DataGridView<T> extends LinearLayout implements View.OnClickListener{

    private Context mContext;
    private View mView;
    /**包含组件**/
    private LinearLayout mLlHeader;
    private RecyclerView mRecyclerView;
    private ImageButton mBtnPrev;
    private ImageButton mBtnNext;
    /**RecyclerView适配器**/
    private RecyclerviewAdapter mAdapter;
    /** 表格方向**/
    private int mOrientation = LinearLayoutManager.VERTICAL;
    /**列宽**/
    private float[] colunmsWeight;
    /**表头内容**/
    private String[] headerContent;
    /**表头高度**/
    private int headerHeight = 50;
    /**表头背景**/
    private int headerBackgroundColorResId = R.color.colorBlue;
    /**是否显示表头**/
    private boolean isShowHeader = false;
    /**行高**/
    private int rowHeight;
    /**列数**/
    private int colunms;
    /**分割线粗细**/
    private int dividerSize;
    /**分割线颜色**/
    private int dividerColorResId = R.color.colorGray;
    /**列表底色**/
    private int rvContentColorResId = R.color.colorWhite;
    /**行选中后底色**/
    private int selectedBackgroundColorResId = R.color.colorCyan;
    /**选中模式 0关闭选中 1单选 2多选**/
    private int selectedMode = 0;
    /**控件类型**/
    private Class[] contentViewsClass;
    /** 各列绑定字段**/
    private String[] fieldNames;
    /**数据源**/
    private List<Object> dataSource;
    /**是否启用排序功能**/
    private Map<Integer,Integer> sortEnableMap = new HashMap<>();
    /** 圆角设置 左上 右上 左下 右下**/
    private float[] arrRadius = new float[]{14,14,14,14};
    /** 是否开启快速添加功能**/
    private boolean isFastAdd = false;
    /** 是否启用翻页**/
    private boolean flipOverEnable = false;
    /** 页码View**/
    private RelativeLayout mRvPageNumber;
    /** 当前页码View**/
    private TextView mTvCurrentPageNumber;
    /** 最大页码View**/
    private TextView mTvMaxPageNumber;
    /** 当前页码**/
    private int mPageNumber = 0;
    /** 每页数据条数**/
    private int mPageItems = 6;
    /** 当前页数据**/
    private List<Object> pageDatas = new ArrayList<>();
    /** 分页器**/
    private HashMap<Integer, List<Object>> mPager = new HashMap<>();
    private FragmentManager mFragmentManager;
    /** 是否可滑动**/
    private boolean mSlidable = false;

    public final String BTN_PREV = "prev";
    public final String BTN_NEXT = "next";

    public DataGridView(Context context) {
        super(context);
    }

    public DataGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DataGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        this.mContext = context;
        this.mView = LayoutInflater.from(mContext).inflate(R.layout.lingber_datagridview_template, this);
        mLlHeader = (LinearLayout)mView.findViewById(R.id.ll_header);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_rv1);
        mBtnPrev = (ImageButton) mView.findViewById(R.id.btn_prev);
        mBtnNext = (ImageButton) mView.findViewById(R.id.btn_next);
        mRvPageNumber = (RelativeLayout) mView.findViewById(R.id.rv_page_number);
        mTvCurrentPageNumber = (TextView) mView.findViewById(R.id.tv_current_page);
        mTvMaxPageNumber = (TextView) mView.findViewById(R.id.tv_count_page);
        // 获取控件属性
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.DataGridView);
        // 设置各包含组件
        // 是否显示表头
        setHeaderDisplayable(a.getBoolean(R.styleable.DataGridView_show_header, false));
        // 设置列数
        setColunms(a.getInt(R.styleable.DataGridView_column, 0));
        // 设置行高
        setRowHeight(a.getInt(R.styleable.DataGridView_row_height, 40));
        // 设置分割线大小
        setDividerSize(a.getInt(R.styleable.DataGridView_dividerSize, 2));
        // 设置单元格包含控件
        setCellContentView(new Class[colunms]);
        // 是否启用翻页
        setFlipOverEnable(flipOverEnable, null, null);
    }

    /**设置表头是否可见**/
    public void setHeaderDisplayable(boolean isShow){
        this.isShowHeader = isShow;
        if(isShow){
            mLlHeader.setVisibility(VISIBLE);
        }else{
            mLlHeader.setVisibility(GONE);
        }
    }

    /** 设置翻页控件是否可见、当前页面数据条数**/
    public void setFlipOverEnable(boolean enable, Integer pageItems, FragmentManager fragmentManager){
        this.flipOverEnable = enable;
        if(enable){
            mBtnPrev.setVisibility(VISIBLE);
            mBtnNext.setVisibility(VISIBLE);
            mRvPageNumber.setVisibility(VISIBLE);

            // 点击监听
            mBtnPrev.setTag(BTN_PREV);
            mBtnNext.setTag(BTN_NEXT);
            mBtnPrev.setOnClickListener(new OnFlipOverBtnOnClickListener());
            mBtnNext.setOnClickListener(new OnFlipOverBtnOnClickListener());
            mRvPageNumber.setOnClickListener(new OnPageNumberViewOnClickListener());
        }else{
            mBtnPrev.setVisibility(GONE);
            mBtnNext.setVisibility(GONE);
            mRvPageNumber.setVisibility(GONE);
        }

        if(pageItems!=null){
            this.mPageItems = pageItems;
        }

        if(fragmentManager!=null){
            this.mFragmentManager = fragmentManager;
        }
    }

    /** 设置页码是否可见**/
    public void setPageNumberEnable(boolean enable){
        if(enable){
            mRvPageNumber.setVisibility(VISIBLE);
        }else{
            mRvPageNumber.setVisibility(GONE);
        }
    }

    /** 页码点击事件**/
    private class OnPageNumberViewOnClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt("lastPageNumber", mPager.size());
            bundle.putInt("currentPageNumber", mPageNumber);
            PageNumberSelector dialog = new PageNumberSelector();
            dialog.setArguments(bundle);
            if(mFragmentManager == null){
                return;
            }
            dialog.show(mFragmentManager, dialog.getTag());

            dialog.setOnGetPageNumberListener(new PageNumberSelector.OnGetPageNumberListener() {
                @Override
                public void getPageNumber(int pageNumber) {
                    mPageNumber = pageNumber;
                    mTvCurrentPageNumber.setText(mPageNumber + "");

                    setPageChangeBtnCliable();

                    refrashPageByPageNumber();
                }
            });
        }
    }

    /** 翻页按钮点击事件**/
    private class OnFlipOverBtnOnClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            if(BTN_PREV.equals(v.getTag())){

                if(mPageNumber<=1){
                    return;
                }
                mPageNumber --;
            }else{

                if(mPageNumber >= mPager.size()){
                    return;
                }
                mPageNumber ++;
            }

            setPageChangeBtnCliable();
            refrashPageByPageNumber();

            if(mOnSwitchPageNumberListener!=null){
                mOnSwitchPageNumberListener.onClick(v.getTag().toString());
            }
        }
    }

    /** 设置切换页面按钮是否可点击**/
    private void setPageChangeBtnCliable(){
        if(mPager.size()==0&&mPageNumber== 0){
            mBtnPrev.setBackgroundResource(R.drawable.lingber_ripple_bg_btn_unavailable);
            mBtnNext.setBackgroundResource(R.drawable.lingber_ripple_bg_btn_unavailable);
            mBtnPrev.setEnabled(false);
            mBtnNext.setEnabled(false);
            return;
        }

        if(mPageNumber<=1){
            mBtnPrev.setBackgroundResource(R.drawable.lingber_ripple_bg_btn_unavailable);
            mBtnPrev.setEnabled(false);
        }else{
            mBtnPrev.setBackgroundResource(R.drawable.lingber_ripple_bg_btn);
            mBtnPrev.setEnabled(true);
        }

        if(mPageNumber >= mPager.size()){
            mBtnNext.setBackgroundResource(R.drawable.lingber_ripple_bg_btn_unavailable);
            mBtnNext.setEnabled(false);
        }else{
            mBtnNext.setBackgroundResource(R.drawable.lingber_ripple_bg_btn);
            mBtnNext.setEnabled(true);
        }

    }

    /** 根据页码刷新页面数据**/
    private void refrashPageByPageNumber(){
        if(mPager.get(mPageNumber) == null){
            return;
        }

        pageDatas.clear();
        pageDatas.addAll(mPager.get(mPageNumber));

        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
            // 设置View的复用池子大小,会不会影响性能待验证
            //mRecyclerView.setItemViewCacheSize(dataSource.size());
            mRecyclerView.setItemViewCacheSize(pageDatas.size());
            mAdapter.refreshAllRowStyle();
            updatePageNumberView();
        }
    }

    /** 设置是否可滑动**/
    public void setSlidable(boolean slidable){
        this.mSlidable = slidable;
    }


    /** 滑动控制**/
    private LinearLayoutManager setScrollEnable(Context context, final boolean enable){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                if(enable){
                    return super.canScrollVertically();
                }else{
                    return false;
                }
            }

            @Override
            public boolean canScrollHorizontally() {
                if(enable){
                    return super.canScrollVertically();
                }else{
                    return false;
                }
            }
        };
        return layoutManager;
    }

    /** 设置表格方向 暂时不可用**/
    private void setmOrientation(int orientation){
        this.mOrientation = orientation;
    }

    /**设置表头高度**/
    public void setHeaderHeight(int headerHeight){
        this.headerHeight = headerHeight;
    }

    /**设置表头背景**/
    public void setHeaderBackgroundColorResId(int headerBackgroundColorResId){
         this.headerBackgroundColorResId = headerBackgroundColorResId;
    }

    /**设置列数**/
    public void setColunms(int colunms){
        this.colunms = colunms;
    }

    /**设置行高**/
    public void setRowHeight(int rowHeight){
        this.rowHeight = rowHeight;
    }

    /**设置分割线粗细**/
    public void setDividerSize(int size){
        this.dividerSize = size;
    }

    /**设置分割线颜色**/
    public void setDividerColor(int colorResId){
        this.dividerColorResId = colorResId;
    }

    /**设置列表底色**/
    public void setRvContentColorResId(int colorResId){
        this.rvContentColorResId = colorResId;
    }

    /**设置列表单元格包含控件**/
    public void setCellContentView(Class[] contentViewsClass){
        this.contentViewsClass = contentViewsClass;
    }

    /** 设置各列绑定字段**/
    public void setFieldNames(String[] fieldNames){
        this.fieldNames = fieldNames;
    }

    /**设置行选中后底色**/
    public void setSetRowIsSelectedBackgroundColor(int colorResId){
        this.selectedBackgroundColorResId = colorResId;
    }

    /**设置选中模式 0关闭选择 1单选 2多选**/
    public void setSelectedMode(int mode){
        this.selectedMode = mode;
    }

    /**设置表头内容**/
    public void setHeaderContent(String[] headerContent){
        this.headerContent = headerContent;
    }

    /**设置表头内容**/
    public void setHeaderContentByStringId(int[] arrResId){
        String[] headercontent = new String[arrResId.length];
        for(int i=0;i<arrResId.length;i++){
            headercontent[i] = String.valueOf(arrResId[i]);
        }
        this.headerContent = headercontent;
    }

    /**设置colunm宽度**/
    public void setColunmWeight(float[] colunmsWeight){
        this.colunmsWeight = colunmsWeight;
    }

    /**开启/关闭排序功能**/
    public void setSortIsEnabled(int[] colunmNo, boolean enable){
        for(int i=0;i<colunmNo.length;i++){
            sortEnableMap.put(colunmNo[i], colunmNo[i]);
        }
    }

    /** 设置圆角**/
    public void setRadius(float[] radius){
        if(radius.length==4){
            for(int i=0;i<radius.length;i++){
                if(radius[i]>0){
                    this.arrRadius[i] = radius[i];
                }
            }
        }
    }

    /** 设置快速添加功能**/
    public void setFastAdd(boolean fastAdd){
        this.isFastAdd = fastAdd;
    }

    /**初始化**/
    public void initDataGridView(){
        if(colunmsWeight.length!=0){
            if(headerContent!=null){
                if(headerContent.length!=0){
                    setHeader(headerContent, colunmsWeight);
                }
            }
            if(flipOverEnable){
                initPager(this.dataSource);
            }

            //setRecyclerView(this.dataSource, this.colunms, this.rowHeight, this.colunmsWeight, this.dividerSize, this.dividerColorResId, this.rvContentColorResId, this.selectedBackgroundColorResId, this.selectedMode, this.contentViewsClass, this.fieldNames, this.arrRadius, this.isFastAdd, this.flipOverEnable);
            setRecyclerView(this.pageDatas, this.colunms, this.rowHeight, this.colunmsWeight, this.dividerSize, this.dividerColorResId, this.rvContentColorResId, this.selectedBackgroundColorResId, this.selectedMode, this.contentViewsClass, this.fieldNames, this.arrRadius, this.isFastAdd);
        }
    }

    /**设置数据源**/
    public void setDataSource(List<T> dataSource){
        this.dataSource = (List<Object>) dataSource;
        // 设置View的复用池子大小,会不会影响性能待验证
        //mRecyclerView.setItemViewCacheSize(dataSource.size());
        initPager((List<Object>) dataSource);
        mRecyclerView.setItemViewCacheSize(pageDatas.size());
    }


    /** 初始化分页器**/
    private void initPager(List<Object> dataSource){

        // 配置分页器
        if(flipOverEnable){

            mPager.clear();

            for(int i=0;i<dataSource.size();i++){

                int pageNumber = (i/mPageItems)+1;
                if(i%mPageItems==0){
                    List<Object> datas = new ArrayList<>();
                    datas.add(dataSource.get(i));
                    mPager.put(pageNumber, datas);
                }else{
                    mPager.get(pageNumber).add(dataSource.get(i));
                }
            }

            if(mPageNumber >= mPager.size()){
                mPageNumber = mPager.size();
            }else if(mPageNumber == 0){
                mPageNumber = 1;
            }

            // 更新样式
            setPageChangeBtnCliable();
            updatePageNumberView();
        }

        // 设置当前页面数据源
        pageDatas.clear();
        if(mPager.get(mPageNumber) != null){
            pageDatas.addAll(mPager.get(mPageNumber));
        }else{
            pageDatas.addAll(dataSource);
        }
    }

    /** 更新页码View**/
    private void updatePageNumberView(){
        mTvCurrentPageNumber.setText(String.valueOf(mPageNumber));
        mTvMaxPageNumber.setText(String.valueOf(mPager.size()));
    }

    /**获取当前数据集**/
    public List<Object> getDataSource(){
        return this.dataSource;
    }

    /**获取当前页面数据集**/
    public List<Object> getPageDatas(){
        return this.pageDatas;
    }

    /** 页码点击监听事件**/
    public interface OnSwitchPageNumberListener{
        void onClick(String type);
    }

    private OnSwitchPageNumberListener mOnSwitchPageNumberListener;

    public void setOnSwitchPageNumberListener(OnSwitchPageNumberListener onSwitchPageNumberListener){
        this.mOnSwitchPageNumberListener = onSwitchPageNumberListener;
    }

    /**返回选中Data**/
    public List<Object> getSelectedRowsData(){
        if(mAdapter!=null){
            return mAdapter.getSelectedRowsData();
        }else{
            return null;
        }
    }

    /**返回指定Row的Data**/
    public Object getRowData(int row){
        if(mAdapter!=null){
            return mAdapter.getRowData(row);
        }else{
            return null;
        }
    }

    /**更新某一行数据**/
    public void updateARow(int row){
        if(mAdapter!=null){
            initPager(dataSource);
            mAdapter.notifyItemChanged(row);
            mAdapter.refreshAllRowStyle();
        }
    }

    /**更新数据(清除所有选中行样式)**/
    public void updateAll(){
        if(mAdapter!=null){
            initPager(dataSource);
            mAdapter.notifyDataSetChanged();
            // 设置View的复用池子大小,会不会影响性能待验证
            //mRecyclerView.setItemViewCacheSize(dataSource.size());
            mRecyclerView.setItemViewCacheSize(pageDatas.size());
            mAdapter.refreshAllRowStyle();
        }
    }

    /**更新数据(清除除选中外的所有行样式)**/
    public void updateAllExceptSelected(){
        if(mAdapter!=null){
            initPager(dataSource);
            mAdapter.notifyDataSetChanged();
            // 设置View的复用池子大小,会不会影响性能待验证
            //mRecyclerView.setItemViewCacheSize(dataSource.size());
            mRecyclerView.setItemViewCacheSize(pageDatas.size());
            mAdapter.refreshUnselectedRowStyle();
        }
    }

    /**设置表头**/
    private void setHeader(String[] headerContent, float[] colunmsWeight){
        mLlHeader.removeAllViews();
        if(headerContent.length==colunmsWeight.length){
            for(int i=0;i<headerContent.length;i++){
                // 增加LinearLayout
                LinearLayout mHeaderItemLayout = new LinearLayout(getContext());
                LayoutParams headerItemLayoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, colunmsWeight[i]);
                //headerItemLayoutParams.setMargins(0, 0, this.dividerSize, 0);
                headerItemLayoutParams.setMargins(0, 0, 0, 0);
                mHeaderItemLayout.setLayoutParams(headerItemLayoutParams);
                mHeaderItemLayout.setBackgroundResource(headerBackgroundColorResId);
                mHeaderItemLayout.setOrientation(LinearLayout.HORIZONTAL);
                // 圆角设置
                if(i==0||i==headerContent.length-1){
                    // 左右两边设置圆角
                    GradientDrawable gd = new GradientDrawable();
                    if(i==0){
                        gd.setCornerRadii(new float[]{arrRadius[0], arrRadius[0],0,0,0,0,0,0});
                    }else{
                        gd.setCornerRadii(new float[]{0,0,arrRadius[0], arrRadius[0],0,0,0,0});
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        gd.setColor(getResources().getColor(headerBackgroundColorResId,null));
                    }else{
                        gd.setColor(getResources().getColor(headerBackgroundColorResId));
                    }
                    mHeaderItemLayout.setBackground(gd);
                }

                mHeaderItemLayout.setGravity(Gravity.CENTER);
                mHeaderItemLayout.setId(getResources().getIdentifier("header_item"+i, "id", getContext().getPackageName()));
                mHeaderItemLayout.setTag(i);
                mHeaderItemLayout.setOnClickListener(this);

                // 设置表头文字
/*                TextView mTextView = new TextView(getContext());
                LayoutParams tvLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                mTextView.setGravity(Gravity.CENTER_VERTICAL);
                if(headerContent[i]!=null){
                    mTextView.setText(headerContent[i]);
                }*/
                // 设置表头文字(多语言)
                LangTextView mTextView = new LangTextView(getContext());
                LayoutParams tvLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                mTextView.setGravity(Gravity.CENTER_VERTICAL);
                mTextView.setTextSize(22);
                if(headerContent[i]!=null){
                    mTextView.setTextById(Integer.parseInt(headerContent[i]));
                }

                // 设置表头图标
                ImageView mImageView = new ImageView(getContext());
                LayoutParams imLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                mImageView.setImageResource(R.drawable.lingber_ic_downward);
                mImageView.setId(getResources().getIdentifier("header_sort_icon_item"+i, "id", getContext().getPackageName()));
                // 是否开启排序功能
                if(sortEnableMap.get(i)!=null){
                    mImageView.setVisibility(VISIBLE);
                }else{
                    mImageView.setVisibility(GONE);
                }

                mHeaderItemLayout.addView(mTextView, tvLayoutParams);
                mHeaderItemLayout.addView(mImageView, imLayoutParams);
                mLlHeader.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, headerHeight));
                mLlHeader.addView(mHeaderItemLayout, headerItemLayoutParams);
            }
            // 设置圆角
            GradientDrawable gd = new GradientDrawable();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                gd.setColor(getResources().getColor(dividerColorResId,null));
            }else{
                gd.setColor(getResources().getColor(dividerColorResId));
            }
            gd.setCornerRadii(new float[]{arrRadius[0], arrRadius[0], arrRadius[1], arrRadius[1],0,0,0,0});
            mLlHeader.setBackground(gd);
        }
    }

    /**设置表格内容**/
    private void setRecyclerView(List<Object> dataSource,int colunms, int rowHeight, float[] colunmsWeight, int dividerSize, int dividerColorResId, int rvContentColorResId, int selectedBackgroundResId, int setSelectedMode, Class[] contentViewsClass, String[] fieldNames, float[] radius, boolean isFastAdd) {
        mAdapter = new RecyclerviewAdapter(getContext(), dataSource, rowHeight, colunms, colunmsWeight, dividerSize, dividerColorResId, rvContentColorResId, selectedBackgroundResId, setSelectedMode, contentViewsClass, fieldNames, radius, isFastAdd);
        LinearLayoutManager manager = setScrollEnable(getContext(), mSlidable);
        manager.setOrientation(mOrientation);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**内部实现表头点击事件**/
    @Override
    public void onClick(View v) {
        if(sortEnableMap.get(v.getTag())!=null){
            int resId = getResources().getIdentifier("header_sort_icon_item"+v.getTag(), "id", getContext().getPackageName());
            View view = mView.findViewById(resId);
            float rotation = view.getRotation();
            if(rotation == 180){
                view.animate().rotation(0);
                LingberUtil.objectSort(pageDatas, fieldNames[Integer.parseInt(v.getTag().toString())], "asce");
                //LingberUtil.objectSort(dataSource, fieldNames[Integer.parseInt(v.getTag().toString())], "asce");
                //SortUtil.arrSort(dataSource, Integer.parseInt(v.getTag().toString()),"asce");
            }else {
                view.animate().rotation(180);
                LingberUtil.objectSort(pageDatas, fieldNames[Integer.parseInt(v.getTag().toString())], "desc");
                //LingberUtil.objectSort(dataSource, fieldNames[Integer.parseInt(v.getTag().toString())], "desc");
                //SortUtil.arrSort(dataSource, Integer.parseInt(v.getTag().toString()),"desc");
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /**设置单元格控件点击事件监听**/
    public void setOnItemCellContentClickListener(RecyclerviewAdapter.OnItemCellContentClickListener onItemCellContentClickListener){
        mAdapter.setOnItemCellContentClickListener(onItemCellContentClickListener);
    }
    /**设置单元格控件点击事件监听**/
    public void setOnItemCellClickListener(RecyclerviewAdapter.OnItemCellClickListener onItemCellClickListener){
        mAdapter.setOnItemCellClickListener(onItemCellClickListener);
    }

    /** 获取指定单元格View**/
    public View getItemCellView(int row, int column){
        return mAdapter.getItemCellView(row, column);
    }
    /** 获取指定单元格控件View**/
    public View getItemCellContentView(int row, int column){
        return mAdapter.getItemCellContentView(row, column);
    }

    /** 获取当前页码**/
    public int getCurrentPageNumber(){
        return this.mPageNumber;
    }

    /** 获取每页数据条数**/
    public int getPageItems(){
        return this.mPageItems;
    }

    /**刷新语言**/
    public void reFreshLanguage(){
        ViewUtil.updateViewLanguage(this);
    }
}
