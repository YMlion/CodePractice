package com.ymlion.thread

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xyxg.android.unittestexample.R

/**
 *
 * @author Yao Limin
 * @date 2019/6/30
 */
class RvAdapter(var list: ArrayList<Entry>) : RecyclerView.Adapter<RvAdapter.ViewHolderBase>() {

    var moreIndex = 0

    init {
        for (i in list.indices) {
            if (i > 0 && list[i].id == -1) moreIndex = i
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView?.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == 0) 4 else 1
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].id) {
            -1 -> 0
            else -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolderBase {
        return if (viewType == 0) {
            ViewHolder0(LayoutInflater.from(parent?.context).inflate(R.layout.item_title,
                    parent, false))
        } else
            ViewHolder1(LayoutInflater.from(parent?.context).inflate(R.layout.item_main,
                    parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolderBase, position: Int) {
        val e = list[position]
        if (holder is ViewHolder0) {
            if (position != 0) {
                holder.tip.visibility = View.GONE
            } else {
                holder.tip.visibility = View.VISIBLE
            }
            holder.title.text = e.name
        } else if (holder is ViewHolder1) {
            holder.text.text = e.name
            holder.itemView.setOnClickListener {
                if (position > moreIndex) {
                    list.add(moreIndex, list[position])
                    list.removeAt(position + 1)
                    moreIndex++
                    notifyDataSetChanged()
                } else {
                    if (list[position].id > 1) {
                        list.add(moreIndex + 1, list[position])
                        list.removeAt(position)
                        moreIndex--
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    abstract class ViewHolderBase(itemView: View) : RecyclerView.ViewHolder(itemView)
    class ViewHolder0(itemView: View) : ViewHolderBase(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var tip: TextView = itemView.findViewById(R.id.tip)

    }

    class ViewHolder1(itemView: View) : ViewHolderBase(itemView) {
        var text: TextView = itemView.findViewById(R.id.channel_name)
    }
}