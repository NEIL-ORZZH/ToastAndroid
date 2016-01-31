package cn.nekocode.toast.ui.view

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import cn.nekocode.toast.App

public class NekooLayout : RelativeLayout {
    public constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
    }

    public constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    public constructor(context: Context) : super(context) {
    }

    public var heightScale: Float = 0.8f
        set(value) {
            field = value
            this.requestLayout()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = Point()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)

        var newHeightMeasureSpec = heightMeasureSpec

        if(size.x < size.y) {
            setMeasuredDimension(View.getDefaultSize(0, widthMeasureSpec), View.getDefaultSize(0, heightMeasureSpec))
            newHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((measuredWidth * heightScale).toInt(), View.MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

}
