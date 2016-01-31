package cn.nekocode.toast.ui.fragment

import android.app.Activity
import android.app.Fragment
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import cn.nekocode.toast.R
import cn.nekocode.toast.ui.view.BadgeView

public class BottomTabFragment : Fragment {
    public var tabNowPosition: Int = 0
    public var badges: MutableList<BadgeView> = linkedListOf()
    private var tabs: MutableList<ViewGroup> = arrayListOf()
    private var tabListener: TabListener? = null

    constructor() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_tab, container, false) as ViewGroup
        badges = arrayListOf()
        tabs = arrayListOf()
        setupTabs(view)

        return view
    }

    private fun setupTabs(parentView: ViewGroup) {
        val count = parentView.childCount

        for (i in 0..count - 1) {
            tabs.add(parentView.getChildAt(i) as ViewGroup)
        }

        for (i in tabs.indices) {
            val rl = tabs[i] as RelativeLayout

            val tab = rl.getChildAt(0)
            badges.add(rl.getChildAt(1) as BadgeView)
            badges[i].visibility = View.INVISIBLE

            if (tab is LinearLayout) {
                if (tab.childCount != 2)
                    break
                rl.isClickable = true
                rl.tag = i

                rl.setOnTouchListener(object : View.OnTouchListener {
                    var down: Boolean = false
                    var rect: Rect = Rect()

                    override fun onTouch(v: View, event: MotionEvent): Boolean {
                        when (event.action) {

                            MotionEvent.ACTION_DOWN -> {
                                if (rl.tag as Int !== tabNowPosition) {
                                    rect = Rect(v.left, v.top, v.right, v.bottom);
                                    down = true
                                }
                            }
                            MotionEvent.ACTION_MOVE -> {
                                if(down) {
                                    if (!rect.contains((v.left + event.x).toInt(), (v.top + event.y).toInt())) {
                                        down = false
                                    }
                                }
                            }
                            MotionEvent.ACTION_CANCEL -> {
                                down = false
                            }
                            MotionEvent.ACTION_OUTSIDE -> {
                                down = false
                            }

                            MotionEvent.ACTION_UP -> {
                                if(down) {
                                    val pos = rl.tag as Int
                                    if (pos == tabNowPosition) {
                                        tabListener?.onTabReClick(tabNowPosition)
                                    }

                                    setNowSelected(pos)
                                }
                            }
                        }
                        return true
                    }
                })
            }
        }

        val tab = tabs[tabNowPosition].getChildAt(0) as LinearLayout
        tabs[tabNowPosition].isSelected = true
        tab.getChildAt(0).isSelected = true
        tab.getChildAt(1).isSelected = true
    }

    fun setNowSelected(position: Int) {
        if (position != tabNowPosition) {
            val tab = tabs[position].getChildAt(0) as LinearLayout
            tabs[position].isSelected = true
            tab.getChildAt(0).isSelected = true
            tab.getChildAt(1).isSelected = true

            val tabUnselect = tabs[tabNowPosition].getChildAt(0) as LinearLayout
            tabs[tabNowPosition].isSelected = false
            tabUnselect.getChildAt(0).isSelected = false
            tabUnselect.getChildAt(1).isSelected = false

            tabNowPosition = position

            tabListener?.onTabChange(tabNowPosition)
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if(activity is TabListener) {
            tabListener = activity
        } else {
            throw ClassCastException(activity.toString() + " must implement TabListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        tabListener = null
    }

    interface TabListener {
        fun onTabChange(position: Int) {
        }

        fun onTabReClick(position: Int) {
        }
    }
}
