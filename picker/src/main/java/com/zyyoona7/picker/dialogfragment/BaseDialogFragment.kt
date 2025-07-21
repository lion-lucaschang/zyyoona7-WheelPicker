package com.zyyoona7.picker.dialogfragment

import androidx.viewbinding.ViewBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import java.lang.reflect.ParameterizedType
import androidx.fragment.app.DialogFragment
import com.zyyoona7.picker.R

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {

    protected lateinit var binding: VB

    @Suppress("UNCHECKED_CAST")
    fun <T> getDialogListener(clazz: Class<T>): T? {
        return when {
            clazz.isInstance(activity) -> activity as T
            clazz.isInstance(context) -> context as T
            else -> null
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        dialog?.window?.attributes?.let {
            it.dimAmount = 0.6f
            it.gravity = Gravity.BOTTOM
            it.windowAnimations = R.style.DialogAnim
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }

    private var onDismissClickListener: PopupWindow.OnDismissListener? = null

    fun setOnDismissClickListener(listener: PopupWindow.OnDismissListener) {
        onDismissClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        binding = method.invoke(null, inflater, container, false) as VB
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setOnDismissListener {
            onDismissClickListener?.onDismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariables(savedInstanceState)
        initListeners(savedInstanceState)
    }

    @LayoutRes
    abstract fun initLayoutId(): Int

    abstract fun initVariables(savedInstanceState: Bundle?)

    abstract fun initListeners(savedInstanceState: Bundle?)

}