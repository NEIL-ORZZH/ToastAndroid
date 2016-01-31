package cn.nekocode.toast.model

import java.util.*

/**
 * Created by nekocode on 2015/8/18.
 */
public data class ToastPo(var id: String,
                          var creatorId: String,
                          var coverUrl: String,
                          var heightScale: Float,
                          var text: String,
                          var murmurs: Array<String>,
                          var commentCount: Long,
                          var toastCount: Long,
                          val createAt: Date,

                          val creator: User?)

