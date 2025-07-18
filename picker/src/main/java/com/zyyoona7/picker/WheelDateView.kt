package com.zyyoona7.picker

import android.content.Context
import android.util.AttributeSet
import com.zyyoona7.wheel.WheelView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Date Picker View like iOS, but in a single wheel.
 * Displays dates in "yyyy/MM/dd" format.
 *
 * @author Gemini
 */
class WheelDateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WheelView(context, attrs, defStyleAttr) {

    private val dates = mutableListOf<String>()
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    private var startDate: Calendar = Calendar.getInstance()
    private var endDate: Calendar = Calendar.getInstance()

    init {
        // Default range: from 2000-01-01 to 10 years from now.
        val start = Calendar.getInstance().apply { set(2000, 0, 1) }
        val end = Calendar.getInstance().apply { add(Calendar.YEAR, 10) }
        setDateRange(start, end)
        setSelectedPosition(0)
    }

    fun setDateRange(start: Calendar, end: Calendar) {
        startDate = start.clone() as Calendar
        endDate = end.clone() as Calendar
        generateDates()
        super.setData(dates)
    }

    private fun generateDates() {
        dates.clear()
        val calendar = startDate.clone() as Calendar
        while (calendar.before(endDate) || calendar.equals(endDate)) {
            dates.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
    }

    /**
     * Get the selected date as a Calendar object.
     *
     * @return The selected date as a Calendar.
     */
    fun getSelectedDateCalendar(): Calendar {
        val selectedDateStr = getSelectedItem<String>()
            ?: dateFormat.format(Calendar.getInstance().time)
        val cal = Calendar.getInstance()
        // Parsing might throw an exception if the format is wrong, but it shouldn't happen here.
        dateFormat.parse(selectedDateStr)?.let {
            cal.time = it
        }
        return cal
    }

    /**
     * Set the selected date.
     *
     * @param date The date to select.
     */
    fun setSelectedDate(date: Calendar) {
        val dateStr = dateFormat.format(date.time)
        val position = dates.indexOf(dateStr)
        if (position != -1) {
            setSelectedPosition(position)
        }
    }
}
