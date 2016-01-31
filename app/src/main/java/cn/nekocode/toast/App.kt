package cn.nekocode.toast

import android.app.Application
import cn.nekocode.toast.utils.FileManager
import com.avos.avoscloud.AVOSCloud

/**
 * Created by nekocode on 2015/7/22.
 */
public class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instanceTmp = this

        FileManager.createAppRootDirs()

        AVOSCloud.initialize(this, "vkkm14q7j5l9wrw5ygti6uymv8afzeh2c3xizknnan2wk578", "iaxud2w5ii9miw33wfd7g184k4gordnlul6shpuq3edv428n")
    }

    companion object {
        private var instanceTmp: App? = null

        public val instance: App by lazy {
            instanceTmp!!
        }
    }

}
