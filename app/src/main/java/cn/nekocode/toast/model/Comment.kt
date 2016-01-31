package cn.nekocode.toast.model

import java.util.*

/**
 * Created by nekocode on 2015/8/22.
 */
public data class Comment(var id: String,
                          var creatorId: String,
                          var text: String,
                          var createAt: Date,
                          var toastCount: Long,

                          var creator: User)