package cn.nekocode.toast.rest;

import cn.nekocode.toast.model.Murmur;
import com.avos.avoscloud.*

/**
 * Created by nekocode on 2015/4/22 0022.
 */
public class API {
    companion object {
        public fun murmurs(callback: (MutableMap<String, Murmur>)->Unit) {
            val query = AVQuery.getQuery<AVObject>("Murmurs");
            query.findInBackground(object: FindCallback<AVObject>() {
                override fun done(list: MutableList<AVObject>?, e: AVException?) {
                    val map: MutableMap<String, Murmur> = hashMapOf()

                    if(list != null) {
                        for(avObject in list) {
                            val murmur = Murmur(avObject.objectId,
                                    avObject.getString("name"),
                                    avObject.getAVFile<AVFile>("file").url)

                            map.put(murmur.name, murmur);
                        }
                    }

                    callback.invoke(map)
                }
            })
        }
    }
}
