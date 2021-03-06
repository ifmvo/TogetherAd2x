package com.rumtel.ad.other

/*
 * (●ﾟωﾟ●)
 *
 * 参数 configStr : "baidu:1,gdt:4,csj:4"
 *
 * 按照 2 ：8 的比例随机返回 BAIDU or GDT or CSJ
 *
 * return AdNameType.BAIDU  || AdNameType.GDT || AdNameType.CSJ
 *
 * Created by Matthew_Chen on 2018/8/24.
 */
object AdRandomUtil {

    /**
     * configStr : "baidu:3,gdt:7,csj:7"
     * return AdNameType.BAIDU  || AdNameType.GDT || AdNameType.CSJ
     */
    fun getRandomAdName(configStr: String?): AdNameType {
        logd("广告的配置：$configStr")
        if (configStr.isNullOrEmpty()) return AdNameType.NO
        val list = mutableListOf<AdNameType>()
        //{baidu:2},{gdt:8}
        val split = configStr.split(",")
        for (itemStr in split) {
            //不能为空
            if (itemStr.isEmpty()) break
            val splitKeyValue = itemStr.split(":")
            //必须分割两份才正确
            if (splitKeyValue.size != 2) break
            //"baidu:2"
            val keyStr = splitKeyValue[0]; val valueStr = splitKeyValue[1]
            //都不能为空
            if (keyStr.isEmpty() || valueStr.isEmpty()) break
            //加到 list 里面 2 个 "baidu"
            when (keyStr) {
                AdNameType.GDT.type -> repeat(valueStr.toIntOrNull() ?: 0) { list.add(AdNameType.GDT) }
                AdNameType.CSJ.type -> repeat(valueStr.toIntOrNull() ?: 0) { list.add(AdNameType.CSJ) }
                else -> { /* 如果后台人员拼写字符串出错，忽略即可 */ }
            }
        }
        //没有匹配的
        if (list.size == 0) return AdNameType.NO
        //在list里面随机选择一个
        val adNameType = list[(0 until list.size).random()]
        logd("随机到的广告: ${adNameType.type}")
        return adNameType
    }
}


///**
// * 测试工具
// */
//fun main() {
//
//    var baidu = 0
//    var gdt = 0
//    var csj = 0
//
//    val startTime = System.currentTimeMillis()
//    repeat(3000000) {
//        when (AdRandomUtil.getRandomAdName("baidu:10,gdt:10,csj:10").type) {
//            AdNameType.BAIDU.type -> baidu++
//            AdNameType.GDT.type -> gdt++
//            AdNameType.CSJ.type -> csj++
//        }
//    }
//    val endTime = System.currentTimeMillis()
//    //main函数执行的代码不能打log，要把log删除
//    println("baidu: $baidu, gdt: $gdt, csj: $csj, 耗时: ${endTime - startTime}")
//}