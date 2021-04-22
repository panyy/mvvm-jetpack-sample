package com.common.webframe.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.common.webframe.R

/**
 * desc :URL adapter
 * author：panyy
 * data：2018/12/5
 */
class H5UrlAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_item_h5_url, null) {

    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.tv_url, item)
    }

}

