package cn.nekocode.toast.ui.activity

import android.app.Dialog
import android.app.DialogFragment
import android.app.Fragment
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import butterknife.bindView

import cn.nekocode.toast.R
import cn.nekocode.toast.model.Murmur
import cn.nekocode.toast.rest.API
import cn.nekocode.toast.ui.activity.helper.BaseActivity
import cn.nekocode.toast.utils.FileManager
import cn.nekocode.toast.utils.showToast
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListener
import com.thin.downloadmanager.ThinDownloadManager
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import java.io.File

public class StartActivity : BaseActivity() {
    val draweeView: SimpleDraweeView by bindView(R.id.draweeView)
    var fragmentBuffer: BufferFragment? = null
    var fragmentProgressDialog: ProgressDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(this);
        setContentView(R.layout.activity_start)

        setupFragments()
        setupViews()
        nextScreen()
    }

    private fun setupViews() {
        val uri: Uri = Uri.parse("asset:///loading.gif")

        draweeView.controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)

                .setAutoPlayAnimations(true)
                .build()
    }

    private fun setupFragments() {
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentBuffer = fragmentManager.findFragmentByTag("fragmentBuffer") as BufferFragment?
        if (fragmentBuffer?.isDetached ?: true) {
            fragmentBuffer = BufferFragment()

            fragmentTransaction.add(fragmentBuffer!!, "fragmentBuffer");
        }


        fragmentProgressDialog = fragmentManager.findFragmentByTag("fragmentProgressDialog") as ProgressDialogFragment?
        if (fragmentProgressDialog?.isDetached ?: true) {
            fragmentProgressDialog = ProgressDialogFragment()
        } else {
            // 旋转屏幕后初始化progress dialog
            fragmentProgressDialog?.text = "Now buffering some data : " +
                    (fragmentBuffer!!.downloadTotalTasks - fragmentBuffer!!.downloadingTasks).toString() +
                    "/" + fragmentBuffer!!.downloadTotalTasks.toString() + "...";
        }


        // 如果已经缓存完毕的话跳转到主页面
        if (fragmentBuffer?.loadFinished ?: true) {
            nextScreen()
        }


        fragmentTransaction.commit()
    }

    public fun nextScreen() {
        fragmentProgressDialog?.progressDialog?.dismiss()

        runDelayed({
            startActivity(intentFor<MainActivity>().singleTop())
            finish()
        }, 2000)
    }

    class BufferFragment : Fragment() {
        private val downloadManager = ThinDownloadManager(3)
        public var downloadTotalTasks: Int = 0
        public var downloadingTasks: Int = 0
        public var loadFinished: Boolean = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true

            API.murmurs {
                murmurs ->
                val murmursNeedBuffer = getMurmurNotBuffered(murmurs)
                if (murmursNeedBuffer.size != 0) {
                    bufferMurmur(murmursNeedBuffer, {
                        showToast("buffer finished!")

                        loadFinished = true
                        (activity as StartActivity).nextScreen()
                    })
                } else {
                    loadFinished = true
                    (activity as StartActivity).nextScreen()
                }
            }
        }

        public fun getMurmurNotBuffered(murmurs: MutableMap<String, Murmur>): Map<String, Murmur> {
            val dirPath = FileManager.getAppRootPath() + "murmurs" + File.separator
            val dir = File(dirPath)
            if (dir.exists()) {
                for (file in dir.listFiles()) {
                    val key = file.name
                    if (murmurs.containsKey(key)) {
                        murmurs.remove(key)
                    }
                }
            }
            return murmurs
        }

        public fun bufferMurmur(murmurs: Map<String, Murmur>, callback: () -> Unit) {
            if (!FileManager.isExternalStorageMounted()) {
                Log.e("bufferMurmur", "sdcard unavailiable")
            }

            val dirPath = FileManager.getAppRootPath() + "murmurs" + File.separator
            val dir = File(dirPath)
            if(!dir.exists()) {
                dir.mkdirs()
            }

            downloadTotalTasks = murmurs.size
            downloadingTasks = downloadTotalTasks

            val parent = (activity as StartActivity)
            parent.fragmentProgressDialog?.show(fragmentManager, "fragmentProgressDialog")
            parent.fragmentProgressDialog?.progressDialog?.setMessage(
                    "Now buffering some data : " +
                            (downloadTotalTasks - downloadingTasks).toString() +
                            "/" + downloadTotalTasks.toString() + "..."
            )


            val iterator = murmurs.values.iterator()
            while (iterator.hasNext()) {
                val murmur = iterator.next()
                download(murmur.url, dirPath + murmur.name, callback)
            }
        }

        private fun download(url: String, filePath: String, callback: () -> Unit) {
            val downloadUri = Uri.parse(url)
            val destinationUri = Uri.parse(filePath)
            val downloadRequest = DownloadRequest(downloadUri).setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH).setDownloadListener(object : DownloadStatusListener {
                override fun onDownloadComplete(id: Int) {
                    downloadingTasks--

                    val parent = (activity as StartActivity)
                    parent.fragmentProgressDialog?.progressDialog?.setMessage(
                            "Now buffering some data : " +
                                    (downloadTotalTasks - downloadingTasks).toString() +
                                    "/" + downloadTotalTasks.toString() + "..."
                    )

                    if (downloadingTasks == 0) {
                        callback.invoke()
                    }
                }

                override fun onDownloadFailed(id: Int, i1: Int, s: String) {
                    downloadingTasks--
                    showToast("Lost a file!")

                    if (downloadingTasks == 0) {
                        callback.invoke()
                    }
                }

                override fun onProgress(id: Int, totalBytes: Long, progress: Int) {
                }
            })

            downloadManager.add(downloadRequest)
        }
    }

    public class ProgressDialogFragment : DialogFragment() {
        public var progressDialog: ProgressDialog? = null
        public var text: String = "Now buffering some data..."

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            progressDialog = ProgressDialog(activity, theme);
            progressDialog?.setCancelable(false)
            progressDialog?.setMessage(text)

            return progressDialog!!
        }
    }

    override fun handler(msg: Message) {
    }
}
