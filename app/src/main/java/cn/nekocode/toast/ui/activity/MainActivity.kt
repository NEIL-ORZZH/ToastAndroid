package cn.nekocode.toast.ui.activity

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.Toolbar
import butterknife.bindView
import cn.nekocode.toast.R
import cn.nekocode.toast.ui.activity.helper.BaseActivity
import cn.nekocode.toast.ui.fragment.BottomTabFragment
import cn.nekocode.toast.ui.fragment.ProfileFragment
import cn.nekocode.toast.ui.fragment.TimelineFragment
import com.facebook.drawee.backends.pipeline.Fresco

public class MainActivity : BaseActivity(), BottomTabFragment.TabListener {

    val toolbar: Toolbar by bindView(R.id.toolbar)

    var fragmentTimeline: TimelineFragment? = null
    var fragmentProfile: ProfileFragment? = null
    var fragmentBottomTab: BottomTabFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(this);
        setContentView(R.layout.activity_main)

        toolbar.title = "toast!"
        setSupportActionBar(toolbar)

        setupFragments()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupFragments() {
        val fragmentTransaction = fragmentManager.beginTransaction()


        fragmentTimeline = fragmentManager.findFragmentByTag("fragmentTimeline") as TimelineFragment?
        if (fragmentTimeline?.isDetached ?: true) {
            fragmentTimeline = TimelineFragment()

            fragmentTransaction.add(R.id.fragment_container, fragmentTimeline, "fragmentTimeline");
        }


        fragmentProfile = fragmentManager.findFragmentByTag("fragmentProfile") as ProfileFragment?
        if (fragmentProfile?.isDetached ?: true) {
            fragmentProfile = ProfileFragment()

            fragmentTransaction.add(R.id.fragment_container, fragmentProfile, "fragmentProfile");
        }

        fragmentBottomTab = fragmentManager.findFragmentById(R.id.fragmentBottomTab) as BottomTabFragment?
        when(fragmentBottomTab?.tabNowPosition) {
            0 -> fragmentTransaction.show(fragmentTimeline).hide(fragmentProfile)
            1 -> fragmentTransaction.show(fragmentProfile).hide(fragmentTimeline)
            null -> fragmentTransaction.show(fragmentTimeline).hide(fragmentProfile)
        }


        fragmentTransaction.commit()
    }

    override fun handler(msg: Message) {
    }

    override fun onTabChange(position: Int) {
        if(fragmentTimeline != null && fragmentProfile != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()

            when(position) {
                0 -> fragmentTransaction.show(fragmentTimeline).hide(fragmentProfile).commit()
                1 -> fragmentTransaction.show(fragmentProfile).hide(fragmentTimeline).commit()
            }
        }
    }

    override fun onTabReClick(position: Int) {
    }
}
