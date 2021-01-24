package com.framecad.plum.view.project

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.framecad.plum.R


class ProgressIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val rect: RectF = RectF()

    private var statusProgress: Float = 0f

    private var backgroundPaint: Paint = Paint()

    private var progressPaint: Paint = Paint()

    private var strokeWidth: Float = 0f

    init {
        strokeWidth = context.resources.getDimension(R.dimen.projects_list_item_progressbar_stroke)

        backgroundPaint.isAntiAlias = true
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = strokeWidth



        progressPaint.isAntiAlias = true
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = strokeWidth
    }


    /**
     * @param progress This value should be the range of 0-100
     */
    fun setStatusProgressInt(progress: String) {
        statusProgress = progress.split("%")[0].toFloat()

        when (statusProgress) {
            in 1f..50f -> {
                progressPaint.color = ContextCompat.getColor(context, R.color.projects_progress_1_to_50)
            }
            in 51f..75f -> {
                progressPaint.color = ContextCompat.getColor(context, R.color.projects_progress_51_to_75)
            }
            in 76f..100f -> {
                progressPaint.color = ContextCompat.getColor(context, R.color.projects_progress_76_to_100)
            }
            else -> {
                progressPaint.color = ContextCompat.getColor(context, android.R.color.transparent)
            }
        }
    }


    fun setProgressBackgroundColor(color: Int) {
        backgroundPaint.color = ContextCompat.getColor(context, color)
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rect.set(0f + strokeWidth, 0f + strokeWidth, width.toFloat() - strokeWidth, height.toFloat() - strokeWidth)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc(rect, 0f, 360f, false, backgroundPaint)
        canvas?.drawArc(rect, -90f, 360 * (statusProgress / 100f), false, progressPaint)
    }


}