package com.stenisway.wan_android.component

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.stenisway.wan_android.R
import com.stenisway.wan_android.databinding.SettingCustomWordBinding

class Setting_Custom_Word : LinearLayout {
    private var txt_big_size = resources.getDimensionPixelSize(R.dimen.default_txtsize).toFloat()
    private var txt_small_size = txt_big_size * 3 / 4
    private var txt_big_string: String? = ""
    private var txt_small_string: String? = ""
    private var txt_big_color =
        ResourcesCompat.getColor(resources, R.color.defult_txt_big_color, null)
    private var txt_small_color = resources.getColor(R.color.gray, null)
    private val binding: SettingCustomWordBinding? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Setting_Custom_Word)
        getValue(typedArray)
        initViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Setting_Custom_Word)
        getValue(typedArray)
        initViews(context)
    }

    private fun getValue(typedArray: TypedArray) {
        txt_big_string = typedArray.getString(R.styleable.Setting_Custom_Word_txt_big_string)
        txt_big_size = typedArray.getDimension(
            R.styleable.Setting_Custom_Word_txt_size,
            resources.getDimension(R.dimen.default_txtsize)
        )
        txt_big_color = typedArray.getColor(
            R.styleable.Setting_Custom_Word_txt_big_color, ResourcesCompat.getColor(
                resources, R.color.defult_txt_big_color, null
            )
        )
        txt_small_string = typedArray.getString(R.styleable.Setting_Custom_Word_txt_small_string)
        txt_small_size = txt_big_size * 3 / 4
        txt_small_color = typedArray.getColor(
            R.styleable.Setting_Custom_Word_txt_small_color, ResourcesCompat.getColor(
                resources, R.color.gray, null
            )
        )
        typedArray.recycle()
    }

    private fun initViews(context: Context) {
        val view = inflate(context, R.layout.setting_custom_word, this)
        val txtBig = view.findViewById<TextView>(R.id.txt_big)
        val txtSmall = view.findViewById<TextView>(R.id.txt_small)
        val view1 = view.findViewById<View>(R.id.cut_line)
        orientation = VERTICAL
        val txt_big_layout_param =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        txt_big_layout_param.setMargins(5, 5, 5, 5)
        txtBig.setTextSize(TypedValue.COMPLEX_UNIT_PX, txt_big_size)
        txtBig.setTextColor(txt_big_color)
        txtBig.layoutParams = txt_big_layout_param
        txtBig.text = txt_big_string
        val txt_small_layout_param =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        txt_small_layout_param.setMargins(5, 5, 5, 5)
        txtSmall.setTextSize(TypedValue.COMPLEX_UNIT_PX, txt_small_size)
        txtSmall.setTextColor(txt_small_color)
        txtSmall.layoutParams = txt_small_layout_param
        txtSmall.text = txt_small_string
        val cut_line_layout_param = LayoutParams(LayoutParams.MATCH_PARENT, 5)
        cut_line_layout_param.setMargins(0, 8, 0, 0)
        view1.layoutParams = cut_line_layout_param
    }
}