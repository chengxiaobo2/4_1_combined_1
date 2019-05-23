
# 4_1_combined_1 2017年5月25日完成demo。2019年5月23日 整理文档
1.模拟小米桌面，拖动图标，换位置的效果。（分四次提交，朋友们可以参考哦）。

![image](https://github.com/chengxiaobo2/4_1_combined_1/blob/master/combined_1.gif)

### 思路分析
* 第一步 addChild 和 layout Children（对应github第一次提交）。
* 处理长按事件，长按以后，长按的View,透明度变0.5f，添加跟长按的view长一样的View,scale为1.1f（对应github第二次提交）。
* 移动并且更改位置，没有animation（对应github第三次提交）。
    * 1.比如当前选中了第5个条目，移动到了第2个条目。则2-4条目index+1，5的index变成2，重新layout。
    * 2.比如当前选中了第2个条目，移动到了第5个条目。则3-5条目index-1，2的index变成5，重新layout。
* 移动并且更改位置，有animation（对应github第四次提交）。
    * 添加动画
### 总结：
这个效果看着挺绚丽的，其实逻辑比较干净。和自己实现listView相比，还是简单了挺多。