package com.fsck.k9.ui.messagedetails

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.fsck.k9.ui.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

internal class FolderNameItem(
    val displayName: String,
    @DrawableRes val iconResourceId: Int
) : AbstractItem<FolderNameItem.ViewHolder>() {
    override val type: Int = R.id.message_details_folder_name
    override val layoutRes = R.layout.message_details_folder_name_item

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<FolderNameItem>(view) {
        private val folderIcon: ImageView = view.findViewById(R.id.folder_icon)
        private val folderName = view.findViewById<TextView>(R.id.folder_name)

        override fun bindView(item: FolderNameItem, payloads: List<Any>) {
            folderName.text = item.displayName
            folderIcon.setImageResource(item.iconResourceId)
        }

        override fun unbindView(item: FolderNameItem) {
            folderName.text = null
            folderIcon.setImageDrawable(null)
        }
    }
}
