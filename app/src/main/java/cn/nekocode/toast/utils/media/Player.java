package cn.nekocode.toast.utils.media;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class Player implements OnBufferingUpdateListener, OnCompletionListener, MediaPlayer.OnPreparedListener {
	public MediaPlayer mediaPlayer;

	private Timer mTimer = new Timer();

	MediaPlayerProxy proxy;
	private PlayerListener playerListener;

	private boolean USE_PROXY = true;

	public Player() {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}

		mTimer.schedule(mTimerTask, 0, 1000);

		proxy = new MediaPlayerProxy();
		proxy.init();
		proxy.start();
	}

	public void setPlayerListener(PlayerListener listener) {
		playerListener = listener;
	}

	/*******************************************************
	 * 通过定时器和Handler来更新进度条
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying()) {
				int position = mediaPlayer.getCurrentPosition();
				int duration = mediaPlayer.getDuration();

				if(playerListener != null) {
					playerListener.onProgress(duration, position);
				}
			}
		}
	};

	public void play() {
		mediaPlayer.start();
	}

	public void playUrl(String url) {

		if (USE_PROXY) {
			startProxy();
			url = proxy.getProxyURL(url);
		}

		try {
			mediaPlayer.reset();
			mediaPlayer.setLooping(true);
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepareAsync();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	/**
	 * 通过onPrepared播放 
	 */
	public void onPrepared(MediaPlayer arg0) {
		if(playerListener != null)
			playerListener.onPrepared();

		arg0.start();
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		Log.e("mediaPlayer", "onCompletion");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		if(playerListener != null)
			playerListener.onBuffering(bufferingProgress);
	}

	private void startProxy() {
		if (proxy == null) {
			proxy = new MediaPlayerProxy();
			proxy.init();
			proxy.start();
		}
	}

	public interface PlayerListener {
		void onPrepared();
		void onBuffering(int bufferingProgress);
		void onProgress(int duration, int position);
	}
}
