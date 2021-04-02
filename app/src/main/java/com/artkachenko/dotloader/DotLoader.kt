package com.artkachenko.dotloader

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import kotlin.math.min


class DotLoader @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

    private val rect = RectF()

    private var customHeight = 26F
    private var avatarCount = 4
    private var distance = 26F
    private var pulseStrength = 0.7F
    private var delay = 100L

    private var animationSpeed = 500L

    private var isAnimating = false

    init {
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.DotLoader)

        runCatching {
            customHeight = array.getFloat(R.styleable.DotLoader_dot_size, 26F)
            avatarCount = array.getInt(R.styleable.DotLoader_dot_amount, 4)
            distance = array.getFloat(R.styleable.DotLoader_item_distance, 26F)
            pulseStrength = array.getFloat(R.styleable.DotLoader_pulse_strength, 0.7F)
            delay = array.getInt(R.styleable.DotLoader_delay, 100).toLong()
        }

        array.recycle()

        generateImages().take(avatarCount).forEach { image ->
            addViewWithOverlap(image)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val calculateDotSize = avatarCount * dp(customHeight)
        val calculateOffSetSize = (avatarCount - 1) * dp(distance)
        val desiredWidth = calculateDotSize + calculateOffSetSize + paddingLeft + paddingRight + marginRight + marginLeft
        val desiredHeight = dp(customHeight * 3) + paddingTop + paddingBottom

        val measuredWidth = measureDimension(desiredWidth, widthMeasureSpec)
        val measuredHeight = measureDimension(desiredHeight, heightMeasureSpec)

        rect.set(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())

        setMeasuredDimension(
            measuredWidth,
            measuredHeight
        )

        if (childCount == 0) return
        for (i in 0.until(childCount)) {
            val child = getChildAt(i)
            measureChild(
                child,
                MeasureSpec.makeMeasureSpec(dp(customHeight), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(dp(customHeight), MeasureSpec.EXACTLY)
            )
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (childCount == 0) return
        for (i in 0.until(childCount)) {
            val child = getChildAt(i)
            val offsetX = (dp(distance) * i)
            val dotPosition = (dp(customHeight) * i)
            val positionX = offsetX + dotPosition
            val positionY = rect.bottom.toInt() - child.measuredHeight * 2
            child.layout(
                positionX,
                positionY,
                positionX + child.measuredWidth,
                positionY + child.measuredHeight
            )
            setAnimator(child, delay * i)
        }
    }

    private fun setAnimator(view: View, delay: Long, isReversed: Boolean = false) {
        if (isAnimating) {
            val increment = if (isReversed) 1F else -1F
            val direction = -dpF(customHeight) * increment / 2
            val scaleIncrement = if (isReversed) pulseStrength  else 1F
            val animatorSet = AnimatorSet()
            val yAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, direction)
            val xScaleAnimator = ObjectAnimator.ofFloat(view, View.SCALE_X, scaleIncrement)
            val yScaleAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, scaleIncrement)
            animatorSet.playTogether(yAnimator, xScaleAnimator, yScaleAnimator)

            animatorSet.startDelay = delay
            animatorSet.doOnEnd {
                setAnimator(view, 0, !isReversed)
            }

            animatorSet.duration = animationSpeed
            animatorSet.start()
        }
    }

    private fun addViewWithOverlap(imageView: ImageView) {
        imageView.apply {
            setImageViewParams(this)
        }

        addView(imageView)
    }

    private fun setImageViewParams(imageView: ImageView) {
        imageView.apply {
            val layoutParams = ViewGroup.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            layoutParams.height = dp(customHeight)
            layoutParams.width = dp(customHeight)
            this.layoutParams = layoutParams
        }
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }

        return result
    }

    override fun setVisibility(visibility: Int) {
        isAnimating = visibility == View.VISIBLE
        super.setVisibility(visibility)
    }

    private fun dpF(pixels: Float): Float {
        return context.resources.displayMetrics.density * pixels
    }

    private fun dp(pixels: Float): Int {
        return (context.resources.displayMetrics.density * pixels).toInt()
    }

    private fun generateImageList(): List<Int> {
        return listOf(
            R.drawable.ic_circular_avatar,
            R.drawable.ic_circular_avatar_red,
            R.drawable.ic_circular_avatar_yellow,
            R.drawable.ic_circular_avatar_blue,
            R.drawable.ic_circular_avatar,
            R.drawable.ic_circular_avatar_red)
    }

    private fun generateImages() : List<ImageView> {
        val reversedDrawableList = generateImageList().reversed()
        val imageList = mutableListOf<ImageView>()
        reversedDrawableList.forEachIndexed { index, i ->
            imageList.add(ImageView(context).apply {
                setImageDrawable(ContextCompat.getDrawable(context, reversedDrawableList[index]))
            })
        }
        return imageList
    }
}