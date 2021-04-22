package com.common.res.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dylanc.viewbinding.base.inflateBindingWithGeneric

abstract class BaseBindingQuickAdapter<T, VB : ViewBinding>(layoutResId: Int = -1,
                                                            data: MutableList<T>? = null) :
        BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder>(layoutResId, data) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
            BaseBindingHolder(inflateBindingWithGeneric<VB>(parent))

    class BaseBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {
        constructor(itemView: View) : this(ViewBinding { itemView })

        @Suppress("UNCHECKED_CAST")
        fun <VB : ViewBinding> getViewBinding() = binding as VB
    }
}