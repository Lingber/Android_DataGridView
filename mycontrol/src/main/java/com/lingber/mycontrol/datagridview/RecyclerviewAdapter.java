package com.lingber.mycontrol.datagridview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lingber.mycontrol.R;
import com.lingber.mycontrol.datagridview.i18n.LangTextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author :ShuboLin
 * @CreatTime: 2020-05-07 13:55.
 * @Description: RecyclerView适配器
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.RvViewHolder>{
    private Context context;
    private List<Object> dataSource;
    private View inflater;
    private LinearLayout mRecyclerView;
    private List<RvViewHolder>holders = new ArrayList<>();
    private int height;
    private int colunms;
    private float[] colunmsWeight;
    private int dividerSize;
    private int dividerColorResId;
    private int rvContentColorResId;
    private int selectedBackgroundResId;
    private Map<Integer, Boolean> selectedMap = new HashMap<>();
    private int selectedMode;
    /**单选模式**/
    private final int RADIO_MODE = 1;
    /**多选模式**/
    private final int MULTI_MODE = 2;
    /**设置单元格包含控件**/
    private Class<View>[] contentViewsClass;
    /**各单元格对应的字段名**/
    private String[] fieldNames;
    /** 圆角**/
    float[] radius;
    /** 是否开启快速添加功能**/
    private boolean isFastAdd = false;

    /**构造函数**/
    public RecyclerviewAdapter(Context context, List<Object> dataSource, int height, int colunms, float[] colunmsWeight, int dividerSize, int dividerColorResId, int rvContentColorResId, int selectedBackgroundResId, int selectedMode, Class[] contentViewsClass, String[] fieldNames, float[] radius, boolean isFastAdd) {
        this.context = context;
        this.dataSource = dataSource;
        this.height = height;
        this.colunms = colunms;
        this.colunmsWeight = colunmsWeight;
        this.dividerSize = dividerSize;
        this.dividerColorResId = dividerColorResId;
        this.rvContentColorResId = rvContentColorResId;
        this.selectedBackgroundResId = selectedBackgroundResId;
        this.selectedMode = selectedMode;
        this.contentViewsClass = contentViewsClass;
        this.fieldNames = fieldNames;
        this.radius = radius;
        this.isFastAdd = isFastAdd;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context).inflate(R.layout.lingber_recyclerview, parent, false);
        this.mRecyclerView = (LinearLayout) inflater.findViewById(R.id.ll_recyclerview);
        // 设置分割线颜色
        mRecyclerView.setBackgroundResource(dividerColorResId);

        // 设置单行高度
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) inflater.getLayoutParams();
        params.height = height;
        inflater.setLayoutParams(params);

        //手动添加
        for(int i=0;i<colunms;i++){
            // item
            LinearLayout mItemLayout = new LinearLayout(inflater.getContext());
            LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, colunmsWeight[i]);
            itemLayoutParams.setMargins(0, 0, 0, this.dividerSize);
            mItemLayout.setLayoutParams(itemLayoutParams);
            mItemLayout.setBackgroundResource(rvContentColorResId);
            mItemLayout.setGravity(Gravity.CENTER);
            mItemLayout.setId(inflater.getResources().getIdentifier("item_cell"+i, "id", inflater.getContext().getPackageName()));

            // 设置item内容
            TextView mContentView = getViewByClass(this.contentViewsClass[i]);
            // 检查是否设置快速添加样式
/*            if(isFastAdd&&i==0){
                mContentView.setBackgroundResource(R.drawable.lingber_ic_fast_add);
            }*/

            LinearLayout.LayoutParams etLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mContentView.setGravity(Gravity.CENTER);
            mContentView.setTextSize(20);
            //mContentView.setBackground(null);
            mContentView.setId(inflater.getResources().getIdentifier("item_cell_content"+i, "id", inflater.getContext().getPackageName()));

            mItemLayout.addView(mContentView, etLayoutParams);
            mRecyclerView.addView(mItemLayout, itemLayoutParams);
        }

        // 管理Item
        RvViewHolder rvViewHolder = new RvViewHolder(inflater);
        return rvViewHolder;
    }



    /**根据控件类型返回控件实体
     * 目前只支持四种:
     * TextView EditView CheckBox Button
     * **/
    private TextView getViewByClass(Class mClass){
        if(mClass!=null){
            if (EditText.class.equals(mClass)){
                return new EditText(context);
            }else if(CheckBox.class.equals(mClass)){
                CheckBox cbItem = new CheckBox(context);
                // 自定义CheckBox样式
                cbItem.setBackgroundResource(R.drawable.lingber_checkbox_style);
                cbItem.setButtonDrawable(null);
                return cbItem;
            }else if(Button.class.equals(mClass)){
                return new Button(context);
            }else if(LangTextView.class.equals(mClass)){
                // 自定义多语言Textview
                return new LangTextView(context);
            }else{
                // TextView 为基本控件类型
                return new TextView(context);
            }
        }else{
            // TextView 为基本控件类型
            return new TextView(context);
        }
    }

    @Override
    public void onViewRecycled(RvViewHolder holder) {
        super.onViewRecycled(holder);
        // 重要！删除旧的holder，否则部分子项不可点击。
        holders.remove(holder);
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        // 添加holder
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                holders.add(holder);
        // 将数据和控件绑定
        for(int i=0;i<colunms;i++){
            String contentValue = LingberUtil.getFieldValueByName(fieldNames[i] ,dataSource.get(position));
            if(CheckBox.class.equals(this.contentViewsClass[i])){
                CheckBox checkBox = (CheckBox) holder.arrContentView[i];
                if(contentValue!=null){
                    checkBox.setChecked(Boolean.valueOf(contentValue));
                }else{
                    checkBox.setChecked(false);
                }
            }else{
                if(contentValue!=null){
                    holder.arrContentView[i].setText(contentValue);
/*                    // 设置已添加样式
                    if(isFastAdd&&i==0){
                        holder.arrContentView[i].setBackgroundResource(R.drawable.lingber_ic_fast_added);
                    }*/
                }else{
/*                    // 设置快速添加样式
                    if(isFastAdd&&i==0){
                        holder.arrContentView[i].setBackgroundResource(R.drawable.lingber_ic_fast_add);
                    }*/
                    holder.arrContentView[i].setText("");
                }
            }
            //holder.arrContentView[i].setText(dataSource.get(position)[i]);
            holder.arrLinearLayout[i].setTag(position);
            holder.arrContentView[i].setTag(position);
            holder.arrLinearLayout[i].setOnClickListener(new ItemCellClickListener(position, i, this.mOnItemCellClickListener));
            holder.arrContentView[i].setOnClickListener(new ItemCellContentClickListener(position, i, this.mOnItemCellContentClickListener));

            // 设置Item圆角
            if((i==0||i==(colunms-1))&&(position==dataSource.size()-1)){
                int resId = rvContentColorResId;
                Boolean rowIsSelected = selectedMap.get(position);
                if(rowIsSelected!=null){
                    if(rowIsSelected){
                        resId = this.selectedBackgroundResId;
                    }
                }

                GradientDrawable gd = new GradientDrawable();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    gd.setColor(context.getResources().getColor(resId,null));
                }else{
                    gd.setColor(context.getResources().getColor(resId));
                }
                if(i==0){
                    gd.setCornerRadii(new float[]{0,0,0,0,0,0,radius[3],radius[3]});
                }else{
                    gd.setCornerRadii(new float[]{0,0,0,0,radius[2],radius[2],0,0});
                }
                holder.arrLinearLayout[i].setBackground(gd);
            }
        }
        if(position==dataSource.size()-1){
            // 设置本条View圆角
            GradientDrawable gd = new GradientDrawable();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                gd.setColor(context.getResources().getColor(dividerColorResId,null));
            }else{
                gd.setColor(context.getResources().getColor(dividerColorResId));
            }
            gd.setCornerRadii(new float[]{0,0,0,0,radius[3], radius[3], radius[2], radius[2]});
            mRecyclerView.setBackground(gd);
        }
        // 设置初始选中样式-非选中
        this.selectedMap.put(position, false);
    }

    @Override
    public int getItemCount() {
        // 返回Item总条数
        return dataSource.size();
    }

    /**返回当前选中Data**/
    public List<Object> getSelectedRowsData(){
        List<Object> datas = new ArrayList<>();
        for (Integer key :selectedMap.keySet()) {
            if(selectedMap.get(key)!=null){
                if(selectedMap.get(key)){
                    if(key<dataSource.size()){
                        datas.add(dataSource.get(key));
                    }
                }
            }
        }
        return datas;
    }

    /**返回指定Row的Data**/
    public Object getRowData(int row){
        Object data = new Object();
        if(row<dataSource.size()){
            data = dataSource.get(row);
        }
        return data;
    }

    /** 多选模式**/
    private void multiselectMode(int position){
        Boolean rowIsSelected = selectedMap.get(position);
        if(rowIsSelected!=null){
            RvViewHolder holder = null;
            for(int i=0;i<holders.size();i++){
                 if(holders.get(i).getLayoutPosition()==position){
                     holder = holders.get(i);
                 }
            }
            int resId;
            if(rowIsSelected){
                resId = this.rvContentColorResId;
                selectedMap.put(position, false);
            }else{
                resId = this.selectedBackgroundResId;
                selectedMap.put(position, true);
            }
            if(holder!=null){
                for(int i=0;i<holder.arrLinearLayout.length;i++){
                    holder.arrLinearLayout[i].setBackgroundResource(resId);
                }
            }
        }
    }

    /** 单选模式**/
    private void radioMode(int position){
        for(int i=0;i<holders.size();i++){
            RvViewHolder holder = holders.get(i);
            int resId;
            int currentPosition = holder.getLayoutPosition();
            Boolean rowIsSelected = selectedMap.get(currentPosition);
            if(rowIsSelected!=null){
                if(currentPosition==position){
                    // 当前选中行
                    if(rowIsSelected){
                        resId = this.rvContentColorResId;
                        selectedMap.put(position, false);
                    }else{
                        resId = this.selectedBackgroundResId;
                        selectedMap.put(position, true);
                    }
                    if(holder!=null){
                        for(int j=0;j<holder.arrLinearLayout.length;j++){
                            holder.arrLinearLayout[j].setBackgroundResource(resId);
                        }
                    }
                }else{
                    // 非当前行，清除样式
                    if(selectedMap.get(currentPosition)!=null){
                        if(selectedMap.get(currentPosition)){
                            selectedMap.put(currentPosition, false);
                            resId = this.rvContentColorResId;
                            if(holder!=null){
                                for(int j=0;j<holder.arrLinearLayout.length;j++){
                                    holder.arrLinearLayout[j].setBackgroundResource(resId);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @author ShuboLin
     * @CreatTime 2021-02-03 9:03
     * @Description 刷新非选中行样式
     */
    public void refreshUnselectedRowStyle(){
        for(int i=0;i<holders.size();i++){
            RvViewHolder holder = holders.get(i);
            int resId;
            int currentPosition = holder.getLayoutPosition();
            Boolean rowIsSelected = selectedMap.get(currentPosition);
            if(rowIsSelected!=null){
                if(!selectedMap.get(currentPosition)){
                    selectedMap.put(currentPosition, false);
                    resId = this.rvContentColorResId;
                    if(holder!=null){
                        for(int j=0;j<holder.arrLinearLayout.length;j++){
                            holder.arrLinearLayout[j].setBackgroundResource(resId);
                        }
                    }
                }
            }
        }
    }

    /**
     * @author ShuboLin
     * @CreatTime 2021-02-03 9:03
     * @Description 刷新所有行样式(全部置为非选中)
     */
    public void refreshAllRowStyle(){
        for(int i=0;i<holders.size();i++){
            RvViewHolder holder = holders.get(i);
            int resId;
            int currentPosition = holder.getLayoutPosition();
            Boolean rowIsSelected = selectedMap.get(currentPosition);
            if(rowIsSelected!=null){
                //if(!selectedMap.get(currentPosition)){
                    selectedMap.put(currentPosition, false);
                    resId = this.rvContentColorResId;
                    if(holder!=null){
                        for(int j=0;j<holder.arrLinearLayout.length;j++){
                            holder.arrLinearLayout[j].setBackgroundResource(resId);
                        }
                    }
                //}
            }
        }
    }

    /**内部类 绑定各控件**/
    class RvViewHolder extends RecyclerView.ViewHolder{
        LinearLayout[] arrLinearLayout = new LinearLayout[colunms];
        TextView[] arrContentView = new TextView[colunms];
        public RvViewHolder(View itemView) {
            super(itemView);
            for(int i=0;i<colunms;i++){
                arrLinearLayout[i] = (LinearLayout) inflater.findViewById(inflater.getResources().getIdentifier("item_cell"+i, "id", inflater.getContext().getPackageName()));
                arrContentView[i] = (TextView) inflater.findViewById(inflater.getResources().getIdentifier("item_cell_content"+i, "id", inflater.getContext().getPackageName()));
            }
        }
    }

    /**
     * @author ShuboLin
     * @CreatTime 2020-05-20 14:04
     * @Description 获取指定单元格View
     */
    public View getItemCellView(int row, int column){
        View view = null;
        if(holders!=null){
            RvViewHolder holder = null;
            for(int i=0;i<holders.size();i++){
                if(holders.get(i).getLayoutPosition()==row){
                    holder = holders.get(i);
                }
            }
            if(holder!=null){
                if(holder.arrLinearLayout.length>column){
                    view = holder.arrLinearLayout[column];
                }
            }
        }
        return view;
    }

    /**
     * @author ShuboLin
     * @CreatTime 2020-05-20 14:04
     * @Description 获取指定单元格控件View
     */
    public View getItemCellContentView(int row, int column){
        View view = null;
        if(holders!=null){
            RvViewHolder holder = null;
            for(int i=0;i<holders.size();i++){
                if(holders.get(i).getLayoutPosition()==row){
                    holder = holders.get(i);
                }
            }
            if(holder!=null){
                if(holder.arrContentView.length>column){
                    view = holder.arrContentView[column];
                }
            }
        }
        return view;
    }

    /**
     * @author ShuboLin
     * @CreatTime 2020-05-20 8:48
     * @Description 监听单元格控件点击事件
     */
    public interface OnItemCellContentClickListener{
        void onClick(View v, int row, int column);
    }

    private OnItemCellContentClickListener mOnItemCellContentClickListener;

    public void setOnItemCellContentClickListener(OnItemCellContentClickListener onItemCellContentClickListener){
        this.mOnItemCellContentClickListener = onItemCellContentClickListener;
    }

    class ItemCellContentClickListener implements View.OnClickListener{
        int column = 0;
        int row = 0;
        OnItemCellContentClickListener mOnItemCellContentClickListener;

        public ItemCellContentClickListener(int row, int column, OnItemCellContentClickListener onItemCellContentClickListener) {
            this.column = column;
            this.row = row;
            this.mOnItemCellContentClickListener = onItemCellContentClickListener;
        }

        @Override
        public void onClick(View v) {
            // 更新选中样式
            updateSelectedStyle(v);

            if(mOnItemCellContentClickListener!=null){
                mOnItemCellContentClickListener.onClick(v, row, column);
            }
        }
    }

    /**
     * @author ShuboLin
     * @CreatTime 2020-05-20 8:48
     * @Description 监听单元格点击事件
     */
    public interface OnItemCellClickListener{
        void onClick(View v, int row, int column);
    }

    private OnItemCellClickListener mOnItemCellClickListener;

    public void setOnItemCellClickListener(OnItemCellClickListener onItemCellClickListener){
        this.mOnItemCellClickListener = onItemCellClickListener;
    }

    class ItemCellClickListener implements View.OnClickListener{
        int column = 0;
        int row = 0;
        OnItemCellClickListener onItemCellClickListener;

        public ItemCellClickListener(int row, int column, OnItemCellClickListener onItemCellClickListener) {
            this.column = column;
            this.row = row;
            this.onItemCellClickListener = onItemCellClickListener;
        }

        @Override
        public void onClick(View v) {
            // 更新选中样式
            updateSelectedStyle(v);

            if(onItemCellClickListener!=null){
                onItemCellClickListener.onClick(v, row, column);
            }
        }
    }

    /** 点击事件触发更新选中样式**/
    private void updateSelectedStyle(View v){
        int position = (int) v.getTag();
        if(position>=holders.size()){
            return;
        }
        RvViewHolder holder = holders.get(position);
        if(holder!=null) {
            // 选择模式
            switch (selectedMode) {
                case RADIO_MODE:
                    radioMode(position);
                    break;
                case MULTI_MODE:
                    multiselectMode(position);
                    break;
                default:
                    break;
            }
        }
    }

    /** 解决数据错乱问题**/
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
