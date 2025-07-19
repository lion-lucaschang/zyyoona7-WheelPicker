package com.zyyoona7.picker.dialogfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.zyyoona7.picker.R
import com.zyyoona7.picker.databinding.DfDatePickerBinding
import com.zyyoona7.picker.listener.OnDateSelectedListener
import java.util.Calendar
import java.util.Date

class DatePickerDialogFragment : BaseDialogFragment<DfDatePickerBinding>() {

    companion object {
        fun newInstance(): DatePickerDialogFragment = DatePickerDialogFragment()
    }

    private var fragmentReadyListener: (() -> Unit)? = null

    private var dateSelectedListener: OnDateSelectedListener? = null

    val datePicker get() = binding.datePicker

    var title: String
        get() = binding.title.text as String
        set(value) {
            binding.title.text = value
        }

    fun setOnFragmentReadyListener(listener: () -> Unit) {
        this.fragmentReadyListener = listener
    }

    fun setOnDateSelectedListener(listener: OnDateSelectedListener) {
        this.dateSelectedListener = listener
    }

    override fun initLayoutId(): Int {
        return R.layout.df_date_picker
    }

    override fun initVariables(savedInstanceState: Bundle?) {
    }

    override fun initListeners(savedInstanceState: Bundle?) {

        binding.datePicker.setOnDateSelectedListener(object : OnDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, day: Int, date: Date) {
                Log.d("DatePickerDF", "selectedDate:$year-$month-$day")
            }
        })

        binding.okButton.setOnClickListener {
            getDialogListener(OnDateSelectedListener::class.java)
                ?.onDateSelected(
                    binding.datePicker.getSelectedYear(),
                    binding.datePicker.getSelectedMonth(),
                    binding.datePicker.getSelectedDay(),
                    binding.datePicker.getSelectedDate()
                )
            dateSelectedListener?.onDateSelected(
                binding.datePicker.getSelectedYear(),
                binding.datePicker.getSelectedMonth(),
                binding.datePicker.getSelectedDay(),
                binding.datePicker.getSelectedDate()
            )
            dismissAllowingStateLoss()
        }

        binding.tvCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentReadyListener?.invoke()
    }
}