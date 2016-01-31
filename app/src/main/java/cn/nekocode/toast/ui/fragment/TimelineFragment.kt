package cn.nekocode.toast.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView

import cn.nekocode.toast.R
import cn.nekocode.toast.model.ToastPo
import cn.nekocode.toast.ui.adapter.NekooAdapter
import org.jetbrains.anko.act
import java.util.*

public class TimelineFragment : Fragment() {
    val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    val list: MutableList<ToastPo> = arrayListOf()
    val adapter: NekooAdapter = NekooAdapter(list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        list.add(ToastPo("id", "creatorId",
                "http://mmbiz.qpic.cn/mmbiz/3HhhAH0LrcSia7QTOHLXS8mZWESsL4C4vk1o" +
                        "dbEia21GDrEceo97qX4rEW8XHeWMAsb7Qx7PGSnBKgYuuqOe3SQQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1",
                0.7f, "text", emptyArray(), 0, 0, Date(), null))

        list.add(ToastPo("id", "creatorId",
                "http://mmbiz.qpic.cn/mmbiz/3HhhAH0LrcSia7QTOHLXS8mZWESsL4C4vHibGloU4LjnaZanNlGm5KGLbnF8ezdpZIlKYYVqpdGF" +
                        "aojGO3AhJYAg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1",
                1.3f, "text", emptyArray(), 0, 0, Date(), null))

        list.add(ToastPo("id", "creatorId",
                "http://mmbiz.qpic.cn/mmbiz/3HhhAH0LrcSia7QTOHLXS8mZWESsL4C4vmR6K4M0liaYuZ8Vczl7LialoUhFBWibMRQV2X7YGbAdR" +
                        "Bqe0LjWdr5lmQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1",
                0.7f, "text", emptyArray(), 0, 0, Date(), null))

        list.add(ToastPo("id", "creatorId",
                "http://pic18.nipic.com/20120205/5344959_123117258123_2.jpg",
                0.8f, "text", emptyArray(), 0, 0, Date(), null))

        list.add(ToastPo("id", "creatorId",
                "http://img10.3lian.com/sc6/show/15/06/20110905152307103.jpg",
                1.0f, "text", emptyArray(), 0, 0, Date(), null))

        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(act);
        recyclerView.adapter = adapter
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
    }

    override fun onDetach() {
        super.onDetach()
    }
}
