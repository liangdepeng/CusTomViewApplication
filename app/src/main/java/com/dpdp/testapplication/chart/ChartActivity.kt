package com.dpdp.testapplication.chart

import android.graphics.Color
import com.dpdp.testapplication.base.BaseVBActivity
import com.dpdp.testapplication.databinding.ActivityChartTestLayoutBinding

/**
 * 自定义折线图 测试
 */
class ChartActivity : BaseVBActivity<ActivityChartTestLayoutBinding>() {

    override fun initContentView() {
        initChart1()
        initChart2()
        initChart3()
    }

    private fun initChart1() {
        // x轴显示的文案 xList 数量 要和 chartDataList size 保持一致
        val xList =
            arrayListOf("第一天", "第二天", "第三天", "第四天", "第五天", "第六天", "第七天", "第八天", "第九天", "第十天")
        val chartDataList = arrayListOf(0, 60, 20, 30, 40, 50, 60, 100, 120, 80)

        // 找个最大值  这块自由发挥 不要破坏原数据
        val tempList = ArrayList(chartDataList)
        tempList.sort()
        val max = tempList[tempList.size - 1]

        // 根据 图表数据 最大值 计算间隔大概是多少  或者自由发挥 自己给定数值标准
        val yList = arrayListOf<Int>()
        for (i in 0..5) {
            yList.add((i * max / 5f).toInt())
        }

        mViewBinding.chart1.setData(xList, yList, chartDataList, ChartConfig())
    }

    private fun initChart2() {
        // x轴显示的文案 xList 数量 要和 chartDataList size 保持一致
        val xList = arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        val chartDataList = arrayListOf(0, 50, 80, 90, 100, 50, 60, 130, 120, 125)

        // 找个最大值  这块自由发挥 不要破坏原数据
        val tempList = ArrayList(chartDataList)
        tempList.sort()
        val max = tempList[tempList.size - 1]

        // 根据 图表数据 最大值 计算间隔大概是多少  或者自由发挥 自己给定数值标准
        val yList = arrayListOf<Int>()
        for (i in 0..6) {
            yList.add((i * max / 6f).toInt())
        }

        mViewBinding.chart2.setData(xList, yList, chartDataList,
            ChartConfig().apply {
                setNeedXY(true)
                setNeedGridLine(false)
                setNeedDescription(false)
                setNeedAnim(true)
                setDuration(3000)
            })
    }

    private fun initChart3() {
        // x轴显示的文案 xList 数量 要和 chartDataList size 保持一致
        val xList =
            arrayListOf("第一天", "第二天", "第三天", "第四天", "第五天", "第六天", "第七天", "第八天", "第九天", "第十天")
        val chartDataList = arrayListOf(100, 60, 120, 30, 40, 50, 60, 100, 120, 80)

        // 找个最大值  这块自由发挥 不要破坏原数据
        val tempList = ArrayList(chartDataList)
        tempList.sort()
        val max = tempList[tempList.size - 1]

        // 根据 图表数据 最大值 计算间隔大概是多少  或者自由发挥 自己给定数值标准
        val yList = arrayListOf<Int>()
        for (i in 0..5) {
            yList.add((i * max / 5f).toInt())
        }

        mViewBinding.chart3.setData(xList, yList, chartDataList, ChartConfig().apply {
            setNeedAnim(true)
            setDuration(3000)
            setChartThemeColor(Color.GRAY)
            setLineColor(Color.RED)
            setTextColor(Color.BLUE)
            setOpenShade(true)
            setShadeColors(intArrayOf(
                Color.argb(100,Color.red(Color.RED),Color.green(Color.RED),Color.blue(Color.RED)),
                Color.argb(60,Color.red(Color.RED),Color.green(Color.RED),Color.blue(Color.RED)),
                Color.argb(0,Color.red(Color.RED),Color.green(Color.RED),Color.blue(Color.RED))
            ))
            setTextSize(22)
        })
    }

}