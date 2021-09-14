package com.lingber.android_datagridview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lingber.mycontrol.datagridview.DataGridView;
import com.lingber.mycontrol.datagridview.RecyclerviewAdapter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DataGridView mDataGridView;
    private ArrayList dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDataGridView = findViewById(R.id.dg_datagridview);
        setDataSource();
        initTable();
    }

    private void initTable(){
        // 设置列数
        mDataGridView.setColunms(7);
        // 设置表头
        mDataGridView.setHeaderContentByStringId(new int[]{R.string.str_index, R.string.str_name, R.string.str_age
        , R.string.str_sex, R.string.str_department, R.string.str_rank, R.string.str_sales});
        // 绑定字段
        mDataGridView.setFieldNames(new String[]{"index","name","age","sex","department","rank","sales"});
        // 开启排序
        mDataGridView.setSortIsEnabled(new int[]{0,2,6}, true);
        // 每个column占比
        mDataGridView.setColunmWeight(new float[]{1,2,1,1,3,1,3});
        // 每个单元格包含控件
        mDataGridView.setCellContentView(new Class[]{TextView.class, TextView.class, TextView.class, TextView.class, TextView.class, TextView.class, TextView.class});
        // 设置数据源
        mDataGridView.setDataSource(dataSource);
        // 单行选中模式
        mDataGridView.setSelectedMode(1);
        // 启用翻页
        mDataGridView.setFlipOverEnable(true, 9, getFragmentManager());
        // 初始化表格
        mDataGridView.initDataGridView();
        // 设置点击翻页事件监听
        mDataGridView.setOnSwitchPageNumberListener(new DataGridView.OnSwitchPageNumberListener() {
            @Override
            public void onClick(String type) {
            }
        });

        // 表格内部设置单击选中事件
        mDataGridView.setOnItemCellContentClickListener(new RecyclerviewAdapter.OnItemCellContentClickListener() {
            @Override
            public void onClick(View v, int row, int column) {
            }
        });
        mDataGridView.setOnItemCellClickListener(new RecyclerviewAdapter.OnItemCellClickListener() {
            @Override
            public void onClick(View v, int row, int column) {
            }
        });
    }

    private void setDataSource(){
        dataSource = new ArrayList();
        dataSource.add(new SellerBean(1, "Lisa", 21, "female", "Sales", "manager", 100000D));
        dataSource.add(new SellerBean(2, "Nana", 22, "female", "Sales", "manager", 900000D));
        dataSource.add(new SellerBean(3, "Mia", 23, "female", "Sales", "manager", 800000D));
        dataSource.add(new SellerBean(4, "Bob", 23, "man", "Sales", "manager", 700000D));
        dataSource.add(new SellerBean(5, "Jack", 25, "man", "Sales", "manager", 600000D));
        dataSource.add(new SellerBean(6, "Luis", 22, "man", "Sales", "manager", 500000D));
        dataSource.add(new SellerBean(7, "Getang", 27, "man", "Sales", "manager", 400000D));
        dataSource.add(new SellerBean(8, "Charlie", 21, "man", "Sales", "manager", 300000D));
        dataSource.add(new SellerBean(9, "Houston", 29, "man", "Sales", "manager", 200000D));
        dataSource.add(new SellerBean(10, "Lulu", 20, "female", "Sales", "manager", 100000D));
    }
}
