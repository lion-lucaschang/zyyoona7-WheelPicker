package com.zyyoona7.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupWindow
import android.widget.Toast
import com.zyyoona7.demo.activity.BaseActivity
import com.zyyoona7.demo.databinding.ActivityDetailsBinding
import com.zyyoona7.picker.dialogfragment.DatePickerDialogFragment
import com.zyyoona7.picker.dialogfragment.DateTimePickerDialogFragment
import com.zyyoona7.demo.dialogfragment.LinkagePickerDialogFragment
import com.zyyoona7.demo.entities.City
import com.zyyoona7.picker.DateTimePickerView
import com.zyyoona7.picker.dialogfragment.TimePickerDialogFragment
import com.zyyoona7.picker.listener.OnDateSelectedListener
import com.zyyoona7.picker.listener.OnLinkageSelectedListener
import com.zyyoona7.picker.listener.OnTimeSelectedListener
import com.zyyoona7.wheel.WheelView
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : BaseActivity<ActivityDetailsBinding>(), OnDateSelectedListener,
    OnLinkageSelectedListener, OnTimeSelectedListener {

    private var currentYear = -1
    private var currentMonth = -1
    private var currentDay = -1


    private var p: String = ""
    private var c: String = ""
    private var a: String = ""

    private var is24Hour: Boolean = false
    private var isAm: Boolean = false
    private var hour: Int = -1
    private var minute: Int = -1
    private var second: Int = -1

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, DetailsActivity::class.java))
        }
    }

    override fun initLayoutId(): Int {
        return R.layout.activity_details
    }

    override fun initVariables(savedInstanceState: Bundle?) {
    }

    override fun initListeners(savedInstanceState: Bundle?) {
        binding.btnTimePicker.setOnClickListener {
            TimePickerActivity.start(this)
        }

        binding.btnDatePicker.setOnClickListener {
            DatePickerActivity.start(this)
        }

        binding.btnLinkagePicker.setOnClickListener {
            DateTimePickerDialogFragment.newInstance().apply {
                setOnFragmentReadyListener {
                    this.title = "請選擇出發日期與時間"
                    val startCalendar = Calendar.getInstance().apply { set(2000, 0, 1) }
                    val endCalendar = Calendar.getInstance().apply { set(2030, 11, 30) }

                    this.datePicker.setDateRange(startCalendar, endCalendar)

                    this.datePicker.postDelayed({
                        this.datePicker.setSelectedDate(
                            Calendar.getInstance().apply { set(2025, 4, 5, 1, 1) }
                        )
                    }, 200)
                }
                setOnDateSelectedListener(object : DateTimePickerView.OnDateTimeSelectedListener {
                    override fun onDateTimeSelected(calendar: Calendar) {
                        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
                        Log.d("LATTE", dateFormat.format(calendar.time))
                    }
                })
                show(supportFragmentManager, "DatePicker")
                setOnDismissClickListener(object : PopupWindow.OnDismissListener {
                    override fun onDismiss() {
                        Toast.makeText(
                            this@DetailsActivity,
                            "onDismiss",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }

        binding.btnDatePickerDf.setOnClickListener {
            val datePickerFragment = DatePickerDialogFragment.newInstance()
            datePickerFragment.setOnFragmentReadyListener {
                datePickerFragment.title = "請選擇出發日期"
                val startCalendar = Calendar.getInstance().apply { set(2024, 4, 15) }
                val endCalendar = Calendar.getInstance().apply { set(2025, 10, 15) }

                datePickerFragment.datePicker.setYearRange(
                    startCalendar.get(Calendar.YEAR),
                    endCalendar.get(Calendar.YEAR)
                )
                datePickerFragment.datePicker.setDateRange(
                    startCalendar,
                    endCalendar,
                    WheelView.OverRangeMode.CANT_SCROLL
                )

                datePickerFragment.datePicker.post {
                    datePickerFragment.datePicker.setSelectedDate(
                        Calendar.getInstance().apply { set(2024, 4, 15) }
                    )
                }
            }
            datePickerFragment.show(supportFragmentManager, "DatePicker")
            datePickerFragment.setOnDateSelectedListener(object : OnDateSelectedListener {
                override fun onDateSelected(
                    year: Int,
                    month: Int,
                    day: Int,
                    date: Date
                ) {
                    Log.d("DatePickerAct", "selectedDate:$year-$month-$day")
                }

            })
            datePickerFragment.setOnDismissClickListener(object : PopupWindow.OnDismissListener {
                override fun onDismiss() {
                    Toast.makeText(
                        this@DetailsActivity,
                        "onDismiss",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        binding.btnLinkagePickerDf.setOnClickListener {
            LinkagePickerDialogFragment.newInstance(p, c, a)
                .show(supportFragmentManager, "LinkagePicker")
        }

        binding.btnTimePickerDf.setOnClickListener {
            TimePickerDialogFragment.newInstance()
                .apply {
                    setOnFragmentReadyListener {
                        this.timePicker.postDelayed({
                            this.timePicker.setTime(
                                Calendar.getInstance().apply { set(2025, 4, 5, 13, 13) },
                                true
                            )
                        }, 200)
                    }
                    setOnDismissClickListener(object : PopupWindow.OnDismissListener {
                        override fun onDismiss() {
                            Toast.makeText(
                                this@DetailsActivity,
                                "onDismiss",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
                .show(supportFragmentManager, "TimePicker")
        }
    }

    override fun onDateSelected(year: Int, month: Int, day: Int, date: Date) {
        currentYear = year
        currentMonth = month
        currentDay = day
        binding.btnDatePickerDf.text =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }

    override fun onTimeSelected(
        is24Hour: Boolean,
        hour: Int,
        minute: Int,
        second: Int,
        isAm: Boolean
    ) {
        this.is24Hour = is24Hour
        this.isAm = isAm
        this.hour = hour
        this.minute = minute
        this.second = second
        val amPm = if (!is24Hour) {
            if (isAm) "上午" else "下午"
        } else {
            ""
        }
        val time =
            String.format(if (is24Hour) "%02d:%02d:%02d" else "%d:%02d:%02d", hour, minute, second)
        binding.btnTimePickerDf.text = "$amPm $time"
    }

    override fun onLinkageSelected(firstWv: WheelView, secondWv: WheelView?, thirdWv: WheelView?) {
        p = firstWv.getSelectedItem<City>()?.name ?: ""
        c = secondWv?.getSelectedItem<City>()?.name ?: ""
        a = thirdWv?.getSelectedItem<City>()?.name ?: ""

        binding.btnLinkagePickerDf.text = "$p,$c,$a"
    }
}
