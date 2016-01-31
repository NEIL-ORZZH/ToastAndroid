package cn.nekocode.toast.ui.activity.helper

import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import cn.nekocode.toast.R

import java.lang.ref.WeakReference
import kotlin.properties.Delegates

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        private val handlers = arrayListOf<MyHandler>()

        fun addHandler(handler: MyHandler) {
            handlers.add(handler)
        }

        fun deleteHandler(handler: MyHandler) {
            handlers.remove(handler)
        }

        fun removeAll() {
            handlers.clear()
        }

        fun broadcast(message: Message) {
            for (handler in handlers) {
                val msg = Message()
                msg.copyFrom(message)
                handler.sendMessage(msg)
            }
        }

        class MyHandler : Handler {
            private val mOuter: WeakReference<BaseActivity>

            constructor(activity: BaseActivity) {
                mOuter = WeakReference(activity)
            }

            override fun handleMessage(msg: Message) {
                if(mOuter.get() == null) {
                    BaseActivity.deleteHandler(this)
                    return
                } else {

                    if (msg.what == -101 && msg.arg1 == -102 && msg.arg2 == -103) {
                        (msg.obj as (()->Unit)).invoke()
                        return
                    }

                    mOuter.get().handler(msg)
                }
            }
        }
    }

    protected val handler: MyHandler by lazy {
        MyHandler(this)
    }

    fun <T: Fragment> initFragment(tag: String, fragmentClass: Class<T>): T? {
        val fragmentTransaction = fragmentManager.beginTransaction()

        var fragment = fragmentManager.findFragmentByTag(tag) as T?
        if (fragment?.isDetached ?: true) {
            fragment = fragmentClass.newInstance()

            fragmentTransaction.add(R.id.fragment_container, fragment, tag);
        }

        return fragment
    }

    public fun sendMsg(message: Message) {
        val msg = Message()
        msg.copyFrom(message)
        handler.sendMessage(msg)
    }

    public fun sendMsgDelayed(message: Message, delayMillis: Int) {
        val msg = Message()
        msg.copyFrom(message)
        handler.sendMessageDelayed(msg, delayMillis.toLong())
    }

    public fun runDelayed(runnable: ()->Unit, delayMillis: Int) {
        val msg = Message()
        msg.what = -101
        msg.arg1 = -102
        msg.arg2 = -103
        msg.obj = runnable

        handler.sendMessageDelayed(msg, delayMillis.toLong())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addHandler(handler)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> this.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun finish() {
        handler.removeMessages(-101)
        deleteHandler(handler)
        super.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    public abstract fun handler(msg: Message)
}
