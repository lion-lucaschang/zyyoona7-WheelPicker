package com.zyyoona7.picker

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.zyyoona7.picker.ex.WheelHourView
import com.zyyoona7.picker.ex.WheelMinuteView
import com.zyyoona7.wheel.WheelView
import com.zyyoona7.wheel.adapter.ArrayWheelAdapter
import com.zyyoona7.wheel.listener.OnItemSelectedListener
import java.util.Calendar

class DateTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), OnItemSelectedListener {

    private var widthWeightMode = false
    private var dateWeight: Float = 1f
    private var hourWeight: Float = 1f
    private var minuteWeight: Float = 1f

    private val wheelDateView: WheelDateView
    private val wheelHourView: WheelHourView
    private val wheelMinuteView: WheelMinuteView

    private var onDateTimeSelectedListener: OnDateTimeSelectedListener? = null

    init {
        orientation = HORIZONTAL

        wheelDateView = WheelDateView(context)
        wheelHourView = WheelHourView(context)
        wheelMinuteView = WheelMinuteView(context)

        wheelDateView.setOnItemSelectedListener(this)
        wheelHourView.setOnItemSelectedListener(this)
        wheelMinuteView.setOnItemSelectedListener(this)

        attrs?.let {
            initAttrs(context, it)
        }

        addViews(wheelDateView, wheelHourView, wheelMinuteView)

        // Initialize with current date and time
        val calendar = Calendar.getInstance()
        wheelDateView.setSelectedDate(calendar)
        wheelHourView.setSelectedHour(calendar.get(Calendar.HOUR_OF_DAY))
        wheelMinuteView.setSelectedMinute(calendar.get(Calendar.MINUTE))
    }

    private fun initAttrs(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DatePickerView)
        widthWeightMode =
            typedArray.getBoolean(R.styleable.DatePickerView_dpv_widthWeightMode, false)
        dateWeight = typedArray.getFloat(R.styleable.DatePickerView_dpv_yearWeight, 1f)
        hourWeight = typedArray.getFloat(R.styleable.DatePickerView_dpv_monthWeight, 1f)
        minuteWeight = typedArray.getFloat(R.styleable.DatePickerView_dpv_dayWeight, 1f)

        typedArray.getDimensionPixelSize(
            R.styleable.DatePickerView_dpv_lineSpacing,
            WheelView.DEFAULT_LINE_SPACING
        ).also {
            wheelDateView.lineSpacing = it
            wheelHourView.lineSpacing = it
            wheelMinuteView.lineSpacing = it
        }

        typedArray.getBoolean(R.styleable.DatePickerView_dpv_cyclic, true)
            .also {
                wheelDateView.isCyclic = it
                wheelHourView.isCyclic = it
                wheelMinuteView.isCyclic = it
            }

        typedArray.getDimensionPixelSize(
            R.styleable.DatePickerView_dpv_textSize,
            WheelView.DEFAULT_TEXT_SIZE
        ).also {
            wheelDateView.textSize = it
            wheelHourView.textSize = it
            wheelMinuteView.textSize = it
        }

        WheelView.convertTextAlign(
            typedArray.getInt(
                R.styleable.DatePickerView_dpv_textAlign,
                WheelView.TEXT_ALIGN_CENTER
            )
        ).also {
            wheelDateView.textAlign = it
            wheelHourView.textAlign = it
            wheelMinuteView.textAlign = it
        }

        typedArray.getDimensionPixelSize(
            R.styleable.DatePickerView_dpv_textPadding,
            WheelView.DEFAULT_TEXT_PADDING
        ).also {
            wheelDateView.textPaddingLeft = it
            wheelDateView.textPaddingRight = it
            wheelHourView.textPaddingLeft = it
            wheelHourView.textPaddingRight = it
            wheelMinuteView.textPaddingLeft = it
            wheelMinuteView.textPaddingRight = it
        }

        typedArray.getColor(
            R.styleable.DatePickerView_dpv_normalTextColor,
            WheelView.DEFAULT_NORMAL_TEXT_COLOR
        )
            .also {
                wheelDateView.normalTextColor = it
                wheelHourView.normalTextColor = it
                wheelMinuteView.normalTextColor = it
            }

        typedArray.getColor(
            R.styleable.DatePickerView_dpv_selectedTextColor,
            WheelView.DEFAULT_SELECTED_TEXT_COLOR
        ).also {
            wheelDateView.selectedTextColor = it
            wheelHourView.selectedTextColor = it
            wheelMinuteView.selectedTextColor = it
        }

        typedArray.getBoolean(R.styleable.DatePickerView_dpv_showCurtain, false)
            .also {
                wheelDateView.isShowCurtain = it
                wheelHourView.isShowCurtain = it
                wheelMinuteView.isShowCurtain = it
            }

        typedArray.getColor(R.styleable.DatePickerView_dpv_curtainColor, Color.TRANSPARENT)
            .also {
                wheelDateView.curtainColor = it
                wheelHourView.curtainColor = it
                wheelMinuteView.curtainColor = it
            }

        typedArray.recycle()
    }


    private fun addViews(
        wheelDateView: WheelDateView, wheelHourView: WheelHourView,
        wheelMinuteView: WheelMinuteView
    ) {
        orientation = HORIZONTAL
        val width = if (widthWeightMode) 0 else LayoutParams.WRAP_CONTENT
        val dateLp = LayoutParams(width, LayoutParams.WRAP_CONTENT)
        val hourLp = LayoutParams(width, LayoutParams.WRAP_CONTENT)
        val minuteLp = LayoutParams(width, LayoutParams.WRAP_CONTENT)
        dateLp.gravity = Gravity.CENTER_VERTICAL
        hourLp.gravity = Gravity.CENTER_VERTICAL
        minuteLp.gravity = Gravity.CENTER_VERTICAL
        if (widthWeightMode) {
            dateLp.weight = dateWeight
            hourLp.weight = hourWeight
            minuteLp.weight = minuteWeight
        }
        addView(wheelDateView, dateLp)
        addView(wheelHourView, hourLp)
        addView(wheelMinuteView, minuteLp)
    }

    fun setOnDateTimeSelectedListener(listener: OnDateTimeSelectedListener) {
        onDateTimeSelectedListener = listener
    }

    fun getDateTime(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = wheelDateView.getSelectedDateCalendar().time
        calendar.set(Calendar.HOUR_OF_DAY, wheelHourView.getSelectedItem<Int>() ?: -1)
        calendar.set(Calendar.MINUTE, wheelMinuteView.getSelectedItem<Int>() ?: -1)
        return calendar
    }

    override fun onItemSelected(
        wheelView: WheelView,
        adapter: ArrayWheelAdapter<*>,
        position: Int
    ) {
        onDateTimeSelectedListener?.onDateTimeSelected(getDateTime())
    }

    fun setSelectedDate(calendar: Calendar) {
        wheelDateView.setSelectedDate(calendar)
        wheelHourView.setSelectedHour(calendar.get(Calendar.HOUR_OF_DAY))
        wheelMinuteView.setSelectedMinute(calendar.get(Calendar.MINUTE))
    }

    fun setDateRange(startCalendar: Calendar, endCalendar: Calendar) {
        wheelDateView.setDateRange(startCalendar, endCalendar)
    }

    interface OnDateTimeSelectedListener {
        fun onDateTimeSelected(calendar: Calendar)
    }
}
