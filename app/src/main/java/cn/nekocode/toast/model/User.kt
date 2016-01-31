package cn.nekocode.toast.model

/**
 * Created by nekocode on 2015/8/20.
 */
public data class User(var id: String,
                       var nickName: String,
                       var avatarUrl: String,
                       var toastCount: Long,
                       var rank: Long,
                       var favesPeople: MutableList<User>?,
                       var favedMePeople: MutableList<User>?,

                       var realation: Int)      //self:-1    stranger:0    my faves:1    fave'd me:2