package cn.nekocode.toast.ui.adapter

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import cn.nekocode.toast.R
import cn.nekocode.toast.model.ToastPo
import cn.nekocode.toast.ui.view.NekooLayout
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onLongClick

/**
 * Created by nekocode on 2015/8/18.
 */
class NekooAdapter(private val list: List<ToastPo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object Type {
        public @JvmStatic val TYPE_ITEM: Int = 0;
    }

    var onNekooItemClickListener: ((ToastPo) -> Unit)? = null
    var onNekooItemLongClickListener: ((ToastPo) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        when (viewType) {
            Type.TYPE_ITEM -> {
                val v = parent!!.context.layoutInflater.inflate(R.layout.item_nekoo, parent, false)
                return ItemViewHolder(v);
            }

        }

        throw UnsupportedOperationException()
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = when(position) {
        else -> Type.TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ItemViewHolder -> {
                holder.setData(list[position]);
            }
        }
    }

    private inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView: SimpleDraweeView by bindView(R.id.imageView)

        fun setData(nekoo : ToastPo) {
            (itemView as NekooLayout?)?.heightScale = nekoo.heightScale
            itemView?.onClick { onNekooItemClickListener?.invoke(nekoo) }
            itemView?.onLongClick { onNekooItemLongClickListener?.invoke(nekoo) ?: false }

            imageView.setImageURI(Uri.parse(
                    nekoo.coverUrl))
        }
    }
}