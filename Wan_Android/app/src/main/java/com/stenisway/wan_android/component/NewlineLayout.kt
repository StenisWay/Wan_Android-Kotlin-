package com.stenisway.wan_android.component

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup

class NewlineLayout : ViewGroup {

    private val TAG = javaClass.name

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    //測量需要的長寬高總共是多少
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - this.paddingRight - this.paddingLeft
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - this.paddingTop - this.paddingBottom
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)
        if (modeWidth == MeasureSpec.EXACTLY && modeHeight == MeasureSpec.EXACTLY) {
            measureChildren(widthMeasureSpec, heightMeasureSpec)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
//       如有需要可以擴充其他類型，這邊只有提供 (modeWidth == MeasureSpec.EXACTLY && modeHeight == MeasureSpec.AT_MOST)
            var layoutChildViewCurX = this.paddingLeft
            var totalControlHeight = 0
            for (i in 0 until childCount) {
                val childView = getChildAt(i)
                if (childView.visibility == GONE) {
                    continue
                }
                val lpMargin = childView.layoutParams as MarginLayoutParams
                childView.measure(
                    getChildMeasureSpec(
                        widthMeasureSpec,
                        this.paddingLeft + this.paddingRight,
                        lpMargin.width
                    ),
                    getChildMeasureSpec(
                        heightMeasureSpec,
                        this.paddingTop + this.paddingBottom,
                        lpMargin.height
                    )
                )
                val width = childView.measuredWidth
                val height = childView.measuredHeight
                if (totalControlHeight == 0) {
                    totalControlHeight = height + lpMargin.topMargin + lpMargin.bottomMargin
                }
                if (layoutChildViewCurX + width + lpMargin.leftMargin + lpMargin.rightMargin > sizeWidth) {
                    layoutChildViewCurX = this.paddingLeft
                    totalControlHeight += height + lpMargin.topMargin + lpMargin.bottomMargin
                }
                layoutChildViewCurX += width + lpMargin.leftMargin + lpMargin.rightMargin
            }
            val cachedTotalWith = resolveSize(sizeWidth, widthMeasureSpec)
            val cachedTotalHeight = resolveSize(totalControlHeight, modeHeight)
            setMeasuredDimension(cachedTotalWith, cachedTotalHeight)
        }
    }

    //  規定子物件的放置規則
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val viewGroupWidth = measuredWidth
        val childCount = childCount
        var layoutChildViewCurX = l //View的X座標
        var layoutChildViewCurY = 0 //View的Y坐標，若設定成t，子view會距離parent有一段很大的距離

//        繪製所有子物件
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            //子控件的宽和高
            val width = childView.measuredWidth
            val height = childView.measuredHeight
            //            取得子物件margin
            val mglp = childView.layoutParams as MarginLayoutParams

            //如果當前子物件+margin超過ViewGroup的寬度，就會自動換行
            if (layoutChildViewCurX + width + mglp.leftMargin + mglp.rightMargin > viewGroupWidth) {
                layoutChildViewCurX = l
                //如果換行，物件高度要加上之前物件的高度以及margin總量
                layoutChildViewCurY += height + mglp.topMargin + mglp.bottomMargin
            }
            Log.d(TAG + "layoutMargin", mglp.leftMargin.toString() + "")
            childView.layout(
                layoutChildViewCurX + mglp.leftMargin,
                layoutChildViewCurY + mglp.topMargin,
                layoutChildViewCurX + width + mglp.leftMargin + mglp.rightMargin,
                layoutChildViewCurY + height + mglp.topMargin + mglp.bottomMargin
            )
            Log.d(TAG + "btMargin", mglp.bottomMargin.toString() + "")
            Log.d(TAG + "topMargin", mglp.topMargin.toString() + "")
            //一個子物件繪製完後，下個子物件的位子要把前面物件的寬度加上margin算進去
            layoutChildViewCurX += width + mglp.leftMargin + mglp.rightMargin
        }
    }
}