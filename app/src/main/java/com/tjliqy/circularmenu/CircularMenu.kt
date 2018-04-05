package com.tjliqy.circularmenu

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by tjliqy on 2018/4/4.
 */
class CircularMenu : View {

    var labelText: String? = null
    var labelColor = Color.rgb(0, 0, 0)
    var labelSize = 0
    var dialRadius = 300f
    var dialImg = R.mipmap.dial
    var arrowImg = R.mipmap.arrow
    var arrowWidth = 30f
    var arrowHeight = 40f
    var arrowPadding = 20f
    var frameWidth = 40f
    var frameColor = Color.rgb(0, 0, 0)
    var itemDiameter = 100f
    var itemPadding = 20f
    var labelHeight = 20f
    var itemAngle = 30f


    val itemList: ArrayList<CircularMenuItem> = ArrayList()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CircularMenu, 0, 0)
        try {
            labelColor = ta.getColor(R.styleable.CircularMenu_labelColor, 0)
            labelText = ta.getString(R.styleable.CircularMenu_labelText)
            labelSize = ta.getInt(R.styleable.CircularMenu_labelSize, 0)
        } finally {
            ta.recycle()
        }
    }


    fun addItem(circularMenuItem: CircularMenuItem): CircularMenu {
        itemList.add(circularMenuItem)
        return this
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        when (widthMode) {
            MeasureSpec.AT_MOST -> {
                widthSize = (dialRadius * 2 + frameWidth * 2 + itemPadding * 2 + itemDiameter * 2).toInt()
            }
            MeasureSpec.UNSPECIFIED -> {
                widthSize = suggestedMinimumWidth
            }
            MeasureSpec.EXACTLY -> {
            }
        }

        when (heightMode) {
            MeasureSpec.AT_MOST -> {
                heightSize = (dialRadius * 2 + frameWidth * 2 + itemPadding + itemDiameter + labelHeight).toInt()
            }
            MeasureSpec.UNSPECIFIED -> {
                heightSize = suggestedMinimumHeight
            }
            MeasureSpec.EXACTLY -> {
            }
        }

        setMeasuredDimension(widthSize, heightSize)
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        val circleCenterX = width / 2f
        val circleCenterY =  itemDiameter + itemPadding + frameWidth + dialRadius

        //绘制旋钮背景
        val frameRadius = dialRadius + frameWidth
        val paint = Paint()
        paint.color = frameColor
        canvas.drawCircle(circleCenterX, circleCenterY, frameRadius, paint)

        //绘制银色旋钮

        canvas.translate(circleCenterX, circleCenterY)

        val dialBitMap = BitmapFactory.decodeResource(context.resources, dialImg)

        val dialRadiusInt = dialRadius.toInt()

        canvas.drawBitmap(dialBitMap, Rect(0, 0, dialBitMap.width, dialBitMap.height), Rect(-dialRadiusInt, -dialRadiusInt, dialRadiusInt, dialRadiusInt), Paint())

        if (itemList.size != 0) {
            //绘制箭头

            val arrowBitMap = BitmapFactory.decodeResource(context.resources, arrowImg)

            val arrowWidthInt = arrowWidth.toInt()

            canvas.save()
            canvas.rotate(itemList[0].angle)
            canvas.drawBitmap(arrowBitMap, Rect(0, 0, arrowBitMap.width, arrowBitMap.height), Rect(-arrowWidthInt / 2, -dialRadiusInt + arrowPadding.toInt(), arrowWidthInt / 2, arrowHeight.toInt() + arrowPadding.toInt() - dialRadiusInt), Paint())

            //绘制item

            val halfItemWidth = (itemDiameter / 2).toInt()
            val itemHeight = (-dialRadius - frameWidth - itemPadding - itemDiameter).toInt()
//            val itemHeight = 0

            for (item in itemList) {
                canvas.restore()
                canvas.save()
                canvas.rotate(item.angle)
                val itemBitMap = BitmapFactory.decodeResource(context.resources, item.src)
                canvas.drawBitmap(itemBitMap, Rect(0, 0, itemBitMap.width, itemBitMap.height), Rect(-halfItemWidth, itemHeight, halfItemWidth, itemHeight + itemDiameter.toInt()), Paint())
            }

        }




        super.onDraw(canvas)
    }

    /**
     * 用来更新item的位置,记得在之后重绘
     */
    public fun refreshItemPosition() {
        val itemAreaAngle = (itemList.size - 1) * itemAngle
        if (itemAreaAngle > 180) {
            throw Exception("Item Angle is too big")
        }
        if (itemList.size == 1) {
            itemList[0].angle = 0f
        } else {
            var position = -itemAreaAngle / 2f
            for (item in itemList) {
                item.angle = position
                position += itemAngle
            }
        }

        invalidate()
    }

    private constructor(builder: Builder) : this(builder.activity) {
        itemList.addAll(builder.itemList)
        refreshItemPosition()
        invalidate()
    }


    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }

    class Builder() {
        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        lateinit var activity: Activity
        var labelText: String? = null
        var labelColor = Color.rgb(0, 0, 0)
        var labelSize = 0
        var dialRadius = 300f
        var dialImg = R.mipmap.dial
        var arrowImg = R.mipmap.arrow
        var arrowWidth = 30f
        var arrowHeight = 40f
        var arrowPadding = 20f
        var frameWidth = 40f
        var frameColor = Color.rgb(0, 0, 0)
        var itemDiameter = 0f
        var labelHeight = 50f

        val itemList: ArrayList<CircularMenuItem> = ArrayList()

        fun activity(init: Builder.() -> Activity) = apply { activity = init() }


        fun addItem(init: Builder.() -> CircularMenuItem) = apply { itemList.add(init()) }

        fun build() = CircularMenu(this)
    }

}