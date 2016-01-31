package cn.nekocode.toast.utils

import android.content.Context
import android.widget.Toast
import cn.nekocode.toast.App
import rx.Observable

/**
 * Created by nekocode on 2015/8/13.
 */

public fun <T> rx.Observable<T>.ui(): Observable<T> {
    return observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
}

public fun showToast(any: Any) {
    when(any) {
        is Int ->
            Toast.makeText(App.instance, any, Toast.LENGTH_SHORT).show()
        is String ->
            Toast.makeText(App.instance, any, Toast.LENGTH_SHORT).show()
    }
}

public fun Context.col(id: Int): Int {
    return resources.getColor(id)
}