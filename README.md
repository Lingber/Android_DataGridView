# Android DataGridView
基于Android的表格控件，几行代码的简单配置即可使用

## 功能列表
* 支持翻页
* 支持表头配置
* 支持列排序
* 支持单选多选
* 支持编辑行间隔
* 支持配置选中样式
* 支持滑动
* 自定义表格大小

## Demo
[Android_DataGridView.apk](https://github.com/Lingber/Android_DataGridView)  

## 项目演示
> 翻页功能演示  
> 表头排序演示  
> 单选多选演示  
> 滑动演示  

## 简单使用
Step1: 在项目路径下的build.gradle文件中添加
```
  allprojects {
    repositories {
      ...
      maven { url 'https://www.jitpack.io' }
    }
  }
```
Step2: 在app路径下的build.gradle文件中添加
```
  dependencies {
          implementation 'com.github.Lingber:Android_DataGridView:Tag'
  }
```
Step3: 在布局文件中添加
```
    <com.lingber.mycontrol.datagridview.DataGridView
        android:id="@+id/datagridview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:show_header="true"
        app:row_height="50"
        app:dividerSize="2">
    </com.lingber.mycontrol.datagridview.DataGridView>
```
Step4: 添加对象
```
public class SellerBean {

    private Integer index;
    private String name;
    private Integer age;
    private String sex;
    private String department;
    private String rank;
    private Double sales;
    
    public SellerBean(Integer index, String name, Integer age, String sex, String department, String rank, Double sales) {
        this.index = index;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.department = department;
        this.rank = rank;
        this.sales = sales;
    }
}
```

Step5: 在 Activity 或者 Fragment 中添加
```
        DataGridView mDataGridView = findViewById(R.id.dg_datagridview);
        // 设置数据源
        List<SellerBean> dataSource = new ArrayList();
        dataSource.add(new SellerBean(1, "Lisa", 21, "female", "Sales", "manager", 100000D));
        dataSource.add(new SellerBean(2, "Nana", 22, "female", "Sales", "manager", 900000D));
        dataSource.add(new SellerBean(3, "Mia", 23, "female", "Sales", "manager", 800000D));
        
        // 设置列数
        mDataGridView.setColunms(7);
        // 设置表头内容
        mDataGridView.setHeaderContentByStringId(new int[]{R.string.str_index, R.string.str_name, R.string.str_age
        , R.string.str_sex, R.string.str_department, R.string.str_rank, R.string.str_sales});
        // 绑定字段
        mDataGridView.setFieldNames(new String[]{"index","name","age","sex","department","rank","sales"});
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
```

## 选配参数
* 样式配置
```
setColunms(param) // 设置列数  
setRowHeight(param) // 设置行高  
setDividerSize(param) // 设置分割线粗细  
setDividerColor(param) // 设置分割线颜色  
setRvContentColorResId(param) // 设置列表底色  
setSetRowIsSelectedBackgroundColor(param) // 设置行选中后底色  
setCellContentView(new Class[]{TextView.class, TextView.class, TextView.class}) // 设置1、2、3列表单元格包含控件类型  
reFreshLanguage(); // 刷新语言
```
* 排序示例  
```
setSortIsEnabled(new int[]{0, 2, 6} , true);  // 开启第1、3、7列的排序
```
* 表头配置示例  
```
setHeaderHeight(param)  // 设置表头高度
setHeaderBackgroundColorResId(param) // 表头背景
setHeaderDisplayable(param);  // 为true时显示表头，为false时不显示表头
setHeaderContent(new String[]{"Header1", "Header2", "Header3"}); // 设置表头内容
setHeaderContentByStringId(new int[]{R.string.str1, R.string.str2, R.string.str3}); // 设置表头内容
setFieldNames(param) // 设置各列绑定字段
setColunmWeight(new float[]{1,2,2}) // 设置每列宽度比重
```
* 翻页功能配置 
```
setFlipOverEnable(param1, param2, param3);  // param1：否启用翻页[启用翻页时不能滑动表格]；param2：每页数据条数；param3：Context
setPageNumberEnable(param) // 页码是否可见
getCurrentPageNumber(); // 获取当前页码
getPageItems(); // 获取每页数据条数
setOnSwitchPageNumberListener(new DataGridView.OnSwitchPageNumberListener() { // 翻页切换事件监听
    @Override
    public void onClick(String type) {
    }
});
```
* 设置选中模式
```
setSelectedMode(param); // 0关闭选择 1单选 2多选
```
* 设置表格数据
```
setDataSource(List<T> datasource);  // 设置数据集
getDataSource(); // 获取当前数据集
getPageDatas();  // 获取当前页面数据集
getSelectedRowsData(); // 返回选中数据
getRowData(param); // 返回指定行数据
updateARow(param); // 更新指定行数据
updateAll(); // 更新所有数据
```
* 初始化控件
```
initDataGridView(); // 完成配置后调用此方法
```
* 单元格点击监听
```
// 单元格内部View点击事件
setOnItemCellContentClickListener(new RecyclerviewAdapter.OnItemCellContentClickListener() {
    @Override
    public void onClick(View v, int row, int column) {
    }
});

// 单元格点击事件
setOnItemCellClickListener(new RecyclerviewAdapter.OnItemCellClickListener() {
    @Override
    public void onClick(View v, int row, int column) {
    }
});

getItemCellView(row, column); // 获取指定单元格View
getItemCellContentView(row, column); // 获取指定单元格内部View
```
## 交流
欢迎加入技术交流QQ群：790307019，让我们一起完善这个控件吧！

## 打赏
如果觉得该内容对您有用，并且想要请作者喝杯咖啡的话，请扫下面的微信二维码：  
![打赏码](https://github.com/Lingber/Resource/blob/main/Lingber%E6%94%B6%E6%AC%BE%E7%A0%81.jpg)
