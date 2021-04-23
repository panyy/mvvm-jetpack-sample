package com.common.main.ui.adapter

import com.common.main.databinding.ItemArticleListLayoutBinding
import com.common.main.entity.ArticleEntity
import com.common.res.adapter.BaseBindingQuickAdapter
import com.common.res.ext.load

class ArticleAdapter : BaseBindingQuickAdapter<ArticleEntity, ItemArticleListLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: ArticleEntity) {
        holder.getViewBinding<ItemArticleListLayoutBinding>().apply {
            tvName.text = "${holder.adapterPosition + 1}.${item.title}"
            ivCover.load("https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png")
        }
    }
}