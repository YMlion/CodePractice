package com.ymlion.thread

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.xyxg.android.unittestexample.R
import kotlinx.android.synthetic.main.activity_drag_items.*

class DragItemsActivity : AppCompatActivity() {

    private var adapter: RvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_items)

        init()
    }

    private fun init() {
        val manager = GridLayoutManager(this, 4)
        rv.layoutManager = manager
        var entries = ArrayList<Entry>()
        var e = Entry(-1, 0, "我的频道")
        entries.add(e)
        e = Entry(1, 0, "要闻")
        entries.add(e)
        e = Entry(2, 1, "专栏")
        entries.add(e)
        e = Entry(3, 2, "快讯")
        entries.add(e)
        e = Entry(4, 3, "推送")
        entries.add(e)
        e = Entry(5, 4, "日历")
        entries.add(e)
        e = Entry(-1, 5, "更多频道")
        entries.add(e)
        e = Entry(6, 6, "视频")
        entries.add(e)
        e = Entry(7, 7, "专题")
        entries.add(e)
        adapter = RvAdapter(entries)
        rv.adapter = adapter
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper
                .LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

            override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                Log.d("TAG", "dx = $dX, dy = $dY, is = $isCurrentlyActive")
                val paint = Paint()
                if (isCurrentlyActive) {
                    paint.color = Color.BLUE
                } else paint.color = Color.TRANSPARENT
                c?.drawRect(viewHolder?.itemView?.left!!.toFloat(), viewHolder.itemView?.top!!
                        .toFloat(), viewHolder.itemView?.right!!.toFloat(),
                        viewHolder.itemView?.bottom!!.toFloat(), paint)
            }

            override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                if (viewHolder is RvAdapter.ViewHolder0) {
                    return 0
                }
                var i = 1
                adapter?.list?.forEachIndexed { index, entry ->
                    if (entry.id == -1 && index > 0)
                        i = index
                }
                val index = viewHolder?.adapterPosition
                Log.d("TAG", "i = $i and index = $index")
                var o = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                o = if (index in 1..i) {
                    if (index!! <= 4)
                        ItemTouchHelper.DOWN or o
                    else {
                        ItemTouchHelper.UP or o
                    }
                } else {
                    0
                }
                return ItemTouchHelper.Callback.makeMovementFlags(o, 0)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                viewHolder?.itemView?.setBackgroundColor(Color.BLACK)
            }

            override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
                super.clearView(recyclerView, viewHolder)
                viewHolder?.itemView?.setBackgroundColor(resources.getColor(R.color.black_overlay))
            }

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                val b = (viewHolder is RvAdapter.ViewHolder1) and (target is RvAdapter.ViewHolder1)
                Log.d("TAG", "this is $b")
                if (b)
                    adapter?.notifyItemMoved(viewHolder!!.adapterPosition, target!!.adapterPosition)
                return b
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
            }
        })

        helper.attachToRecyclerView(rv)

    }
}
