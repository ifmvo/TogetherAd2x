package com.matthewchen.togetherad.ui

import com.matthewchen.togetherad.bean.IndexBean

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2019-11-27.
 */
object DataFactory {

    fun getIndexDatas(currentPage: Int): List<IndexBean> {
        val listData = mutableListOf<IndexBean>()
        for (index in 1..15) {
            val title = "正文内容序号：${((currentPage - 1) * 15) + index}"
            listData.add(IndexBean("http://t7.baidu.com/it/u=2336214222,3541748819&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1590481777&t=97f48ca66a3a5932399a88caa1da6b84", title, "xxxxxx"))
        }
        return listData
    }

}