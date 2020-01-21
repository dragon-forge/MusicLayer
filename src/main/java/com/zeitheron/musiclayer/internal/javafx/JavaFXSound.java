package com.zeitheron.musiclayer.internal.javafx;

import com.zeitheron.musiclayer.api.adapter.IAdaptedSound;
import com.zeitheron.musiclayer.api.adapter.ISoundAdapter;
import com.zeitheron.musiclayer.api.adapter.SoundAdapters;
import javafx.scene.media.MediaPlayer;

public class JavaFXSound
		implements IAdaptedSound
{
	public final MediaPlayer player;

	public JavaFXSound(MediaPlayer player)
	{
		this.player = player;
	}

	@Override
	public ISoundAdapter getAdapter()
	{
		return SoundAdapters.JAVAFX_ADAPTER;
	}

	@Override
	public boolean setVolume(float volume)
	{
		if(player != null)
		{
			player.setVolume(volume);
			return true;
		}
		return false;
	}

	@Override
	public float getVolume()
	{
		if(player != null) return (float) player.getVolume();
		return 0;
	}

	@Override
	public boolean seek(double progress)
	{
		if(player != null)
		{
			player.seek(player.getTotalDuration().multiply(progress));
			return true;
		}
		return false;
	}

	@Override
	public boolean setBalance(float balance)
	{
		if(player != null)
		{
			player.setBalance(balance);
			return true;
		}
		return false;
	}

	@Override
	public boolean play()
	{
		if(player != null)
		{
			player.play();
			return true;
		}
		return false;
	}

	@Override
	public boolean pause()
	{
		if(player != null)
		{
			player.pause();
			return true;
		}
		return false;
	}

	@Override
	public boolean isPaused()
	{
		if(player != null)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean stop()
	{
		if(player != null)
		{
			player.stop();
			return true;
		}
		return false;
	}
}