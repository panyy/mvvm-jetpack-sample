package com.common.main.ui.adapter

import com.common.main.databinding.ItemArticleListLayoutBinding
import com.common.main.entity.ArticleEntity
import com.common.res.adapter.BaseBindingQuickAdapter
import com.common.res.ext.load

class ArticleAdapter : BaseBindingQuickAdapter<ArticleEntity, ItemArticleListLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: ArticleEntity) {
        holder.getViewBinding<ItemArticleListLayoutBinding>().apply {
            tvName.text = "${holder.adapterPosition + 1}.${item.title}"
            ivCover.load("https://wanandroid.com/blogimgs/8a0131ac-05b7-4b6c-a8d0-f438678834ba.png")
        }
    }

}