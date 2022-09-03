package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import com.github.clockworkclyde.basedeliverymvvm.R

class RoundedFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var path: Path? = null
    private var cornerTopLeftRadius: Int = 0
    private var cornerTopRightRadius: Int = 0
    private var cornerBottomLeftRadius: Int = 0
    private var cornerBottomRightRadius: Int = 0

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.RoundedFrameLayout, 0, 0)
        val cornerRadius =
            a.getDimensionPixelSize(R.styleable.RoundedFrameLayout_rfl_cornerRadius, 0)
        cornerTopLeftRadius =
            a.getDimensionPixelSize(
                R.styleable.RoundedFrameLayout_rfl_cornerTopLeftRadius,
                cornerRadius
            )
        cornerTopRightRadius =
            a.getDimensionPixelSize(
                R.styleable.RoundedFrameLayout_rfl_cornerTopRightRadius,
                cornerRadius
            )
        cornerBottomLeftRadius =
            a.getDimensionPixelSize(
                R.styleable.RoundedFrameLayout_rfl_cornerBottomLeftRadius,
                cornerRadius
            )
        cornerBottomRightRadius =
            a.getDimensionPixelSize(
                R.styleable.RoundedFrameLayout_rfl_cornerBottomRightRadius,
                cornerRadius
            )
        a.recycle()
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        path?.let { canvas.clipPath(it) }
        super.draw(canvas)
        canvas.restore()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val r = RectF(0f, 0f, w.toFloat(), h.toFloat())
        path = Path().apply {
            addRoundRect(
                r, floatArrayOf(
                    cornerTopLeftRadius.toFloat(),
                    cornerTopLeftRadius.toFloat(),
                    cornerTopRightRadius.toFloat(),
                    cornerTopRightRadius.toFloat(),
                    cornerBottomRightRadius.toFloat(),
                    cornerBottomRightRadius.toFloat(),
                    cornerBottomLeftRadius.toFloat(),
                    cornerBottomLeftRadius.toFloat()
                ), Path.Direction.CW
            )
            close()
        }
    }

    //public fun changeSomeRadiusOrLikeThat() {
    // *change this.parameter*
    // invalidate() to redraw() again
    // }
}