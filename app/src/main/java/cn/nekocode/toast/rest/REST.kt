package cn.nekocode.toast.rest

import cn.nekocode.toast.App
import cn.nekocode.toast.Config
import cn.nekocode.toast.model.Weather
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.okhttp.Cache
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import retrofit.*
import retrofit.http.*
import retrofit.GsonConverterFactory
import rx.Observable
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by nekocode on 2015/8/13 0013.
 */

public class REST {
    companion object {
        public @JvmStatic val API_HOST_URL: String = "http://www.weather.com.cn/"

        val okHttpClient: OkHttpClient
        val gson: Gson
        val api: APIs

        init {
            val cacheDir = File(App.instance.cacheDir, Config.RESPONSE_CACHE_FILE)
            okHttpClient = OkHttpClient()
            okHttpClient.setCache(Cache(cacheDir, Config.RESPONSE_CACHE_SIZE.toLong()))
            okHttpClient.setConnectTimeout(Config.HTTP_CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            okHttpClient.setWriteTimeout(Config.HTTP_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            okHttpClient.setReadTimeout(Config.HTTP_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)

            gson = GsonBuilder().setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'").create()

            val client = OkHttpClient()
            client.interceptors().add(Interceptor {
                val response = it.proceed(it.request())
                // Do anything with response here
                response
            })

            val restAdapter = Retrofit.Builder()
                    .baseUrl(REST.API_HOST_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client).build()

            api = restAdapter.create(APIs::class.java)
        }
    }

    interface APIs {
        @GET("adat/sk/{cityId}.html") //101010100
        fun getWeather(@Path("cityId") cityId: String): Observable<Weather>
    }
}
