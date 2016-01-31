package cn.nekocode.toast.utils

import android.media.MediaPlayer

import java.io.IOException
import java.util.HashMap

import cn.nekocode.toast.utils.media.Player

/**
 * Created by nekocode on 2015/4/24 0024.
 */
public class MusicManager private constructor() {
    private val murmurPlayers: MutableMap<String, MediaPlayer> = HashMap()
    private val songPlayer: Player = Player()
    var nowPlaySongUrl: String = ""

    companion object {
        private val instance: MusicManager by lazy {
            MusicManager()
        }

        public fun onDestory() {
            instance.songPlayer.stop()

            for (mediaPlayer in instance.murmurPlayers.entrySet()) {
                mediaPlayer.getValue().stop()
                mediaPlayer.getValue().release()
            }
            instance.murmurPlayers.clear()
        }
    }

    public fun setPlayerListener(listener: Player.PlayerListener) {
        songPlayer.setPlayerListener(listener)
    }

    public fun addMurmurs(names: Array<String>) {
        try {
            for (name in names) {
                if (murmurPlayers.containsKey(name)) {
                    murmurPlayers.get(name)!!.start()
                } else {
                    val murmurPath = FileManager.getAppRootPath() + "murmurs/" + name

                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.isLooping = true
                    mediaPlayer.setDataSource(murmurPath)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                        override fun onPrepared(mp: MediaPlayer) {
                            mp.start()
                        }
                    })

                    murmurPlayers.put(name, mediaPlayer)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    public fun deleteMurmurs(names: Array<String>) {
        for (name in names) {
            if (murmurPlayers.containsKey(name)) {
                murmurPlayers.get(name)!!.pause()
            }
        }
    }

    public fun playSong(url: String) {
        songPlayer.playUrl(url)
        nowPlaySongUrl = url
    }

    public fun pauseSong() {
        songPlayer.pause()
    }


}
