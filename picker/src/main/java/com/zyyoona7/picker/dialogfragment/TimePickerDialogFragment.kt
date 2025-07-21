package com.zyyoona7.picker.dialogfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.zyyoona7.picker.R
import com.zyyoona7.picker.databinding.DfHourMinutePickerBinding
import com.zyyoona7.picker.listener.OnTimeSelectedListener

class TimePickerDialogFragment : BaseDialogFragment<DfHourMinutePickerBinding>() {

    companion object {
        fun newInstance(): TimePickerDialogFragment = TimePickerDialogFragment()
    }

    private var fragmentReadyListener: (() -> Unit)? = null

    private var timeSelectedListener: OnTimeSelectedListener? = null

    val timePicker get() = binding.timePicker

    var title: String
        get() = binding.title.text as String
        set(value) {
            binding.title.text = value
        }

    fun setOnFragmentReadyListener(listener: () -> Unit) {
        this.fragmentReadyListener = listener
    }

    fun setOnTimeSelectedListener(listener: OnTimeSelectedListener) {
        this.timeSelectedListener = listener
    }

    override fun initLayoutId(): Int = R.layout.df_hour_minute_picker

    override fun initVariables(savedInstanceState: Bundle?) {
    }

    override fun initListeners(savedInstanceState: Bundle?) {

        binding.timePicker.setOnTimeSelectedListener(object :
            OnTimeSelectedListener {
            override fun onTimeSelected(
                is24Hour: Boolean,
                hour: Int,
                minute: Int,
                second: Int,
                isAm: Boolean
            ) {
                Log.d(
                    "TimePicker",
                    "Selected time: ${if (is24Hour) "24-hour" else "12-hour"} format - ${
                        String.format(
                            "%02d:%02d:%02d",
                            hour,
                            minute,
                            second
                        )
                    } ${if (!is24Hour) if (isAm) "AM" else "PM" else ""}"
                )
            }
        })

        binding.okButton.setOnClickListener {
            getDialogListener(OnTimeSelectedListener::class.java)
                ?.onTimeSelected(
                    true,
                    binding.timePicker.getSelectedHour(),
                    binding.timePicker.getSelectedMinute(),
                    0,
                    false
                )
            timeSelectedListener?.onTimeSelected(
                true,
                binding.timePicker.getSelectedHour(),
                binding.timePicker.getSelectedMinute(),
                0,
                false
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