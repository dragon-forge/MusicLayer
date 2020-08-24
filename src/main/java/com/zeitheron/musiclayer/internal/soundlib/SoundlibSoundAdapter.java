package com.zeitheron.musiclayer.internal.soundlib;

import com.zeitheron.musiclayer.api.adapter.IAdaptedSound;
import com.zeitheron.musiclayer.api.adapter.ISoundAdapter;
import com.zeitheron.musiclayer.api.adapter.SoundAdapters;
import tk.zeitheron.sound.ISound;

import javax.sound.sampled.FloatControl;

public class SoundlibSoundAdapter
		implements IAdaptedSound
{
	public ISound sound;

	public SoundlibSoundAdapter(ISound sound)
	{
		this.sound = sound;
	}

	@Override
	public ISoundAdapter getAdapter()
	{
		return SoundAdapters.SOUND_LIB_ADAPTER;
	}

	@Override
	public boolean setVolume(float volume)
	{
		if(sound != null && volume >= 0 && volume <= 1)
		{
			sound.getControl(FloatControl.Type.MASTER_GAIN).setValue(20F * (float) Math.log10(volume));
			return true;
		}
		return false;
	}

	@Override
	public float getVolume()
	{
		if(sound == null) return 0F;
		try
		{
			return (float) Math.pow(10, sound.getControl(FloatControl.Type.MASTER_GAIN).getValue() / 20);
		} catch(IllegalArgumentException iae)
		{
			return 0F;
		}
	}

	@Override
	public boolean seek(double progress)
	{
		return false;
	}

	@Override
	public boolean setBalance(float balance)
	{
		if(sound != null)
			sound.getControl(FloatControl.Type.PAN).setValue(balance);
		return false;
	}

	@Override
	public boolean play()
	{
		if(sound != null) sound.play();
		return sound != null;
	}

	@Override
	public boolean pause()
	{
		if(sound != null) sound.stop();
		return sound != null;
	}

	@Override
	public boolean isPaused()
	{
		return sound.isDonePlaying();
	}

	@Override
	public boolean stop()
	{
		if(sound != null) sound.stop();
		return sound != null;
	}
}