package com.zyyoona7.picker.dialogfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.zyyoona7.picker.R
import com.zyyoona7.picker.databinding.DfDateTimePickerBinding
import com.zyyoona7.picker.DateTimePickerView
import com.zyyoona7.picker.listener.OnDateSelectedListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateTimePickerDialogFragment : BaseDialogFragment<DfDateTimePickerBinding>() {

    companion object {
        fun newInstance(): DateTimePickerDialogFragment = DateTimePickerDialogFragment()
    }

    private var fragmentReadyListener: (() -> Unit)? = null

    private var dateSelectedListener: DateTimePickerView.OnDateTimeSelectedListener? = null

    val datePicker get() = binding.datePicker

    var title: String
        get() = binding.title.text as String
        set(value) {
            binding.title.text = value
        }

    fun setOnFragmentReadyListener(listener: () -> Unit) {
        this.fragmentReadyListener = listener
    }

    fun setOnDateSelectedListener(listener: DateTimePickerView.OnDateTimeSelectedListener) {
        this.dateSelectedListener = listener
    }

    override fun initLayoutId(): Int {
        return R.layout.df_date_time_picker
    }

    override fun initVariables(savedInstanceState: Bundle?) {
    }

    override fun initListeners(savedInstanceState: Bundle?) {

        binding.datePicker.setOnDateTimeSelectedListener(object :
            DateTimePickerView.OnDateTimeSelectedListener {
            override fun onDateTimeSelected(calendar: Calendar) {
                val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
                Log.d("LATTE", dateFormat.format(calendar.time))
            }
        })

        binding.okButton.setOnClickListener {
            getDialogListener(DateTimePickerView.OnDateTimeSelectedListener::class.java)
                ?.onDateTimeSelected(binding.datePicker.getDateTime())
            dateSelectedListener?.onDateTimeSelected(binding.datePicker.getDateTime())
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