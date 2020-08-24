package com.zeitheron.musiclayer.internal.soundlib;

import com.zeitheron.musiclayer.api.IInput;
import com.zeitheron.musiclayer.internal.InputSLWrapper;
import com.zeitheron.musiclayer.internal.soundlib.SoundlibSoundAdapter;
import tk.zeitheron.sound.RepeatableSound;

import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class FadedRepeatableSound
		extends RepeatableSound
{
	public final SoundlibSoundAdapter adapter = new SoundlibSoundAdapter(this);

	protected boolean toFO, toFI;
	protected float maxVolume;
	public final String sound;
	public boolean disposeWhenNotHearable = false;

	public double fadeSpeed = 0.025F;

	public FadedRepeatableSound(IInput soundIn, float volume, String sound) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		super(InputSLWrapper.createSLSrc(soundIn));
		this.sound = sound;
		maxVolume = volume;
		setVolume(maxVolume);
	}

	public void update()
	{
		double volume = getVolume();

		if(toFI)
		{
			volume += fadeSpeed;

			if(volume >= maxVolume)
			{
				volume = maxVolume;
				toFI = false;
				toFO = false;
			}
		} else if(toFO)
		{
			volume -= fadeSpeed;

			if(volume <= fadeSpeed)
			{
				volume = 0F;
				dispose();
				toFO = false;
				toFI = false;
			}
		}

		setVolumeD(Math.max(Math.min(1, volume), 0));

		if(isDonePlaying())
			play();
		if(getVolume() <= 0F && !isDonePlaying() && disposeWhenNotHearable)
			dispose();
	}

	public double getVolume()
	{
		try
		{
			return Math.pow(10, getControl(Type.MASTER_GAIN).getValue() / 20);
		} catch(IllegalArgumentException iae)
		{
			return 0F;
		}
	}

	@Override
	public void setVolume(float vol)
	{
		super.setVolume(vol);
	}

	public void setVolumeD(double vol)
	{
		/** Escape illegal values */
		if(vol < 0F || vol > 1D)
			return;
		try
		{
			getControl(Type.MASTER_GAIN).setValue((float) (20F * Math.log10(vol)));
		} catch(IllegalArgumentException iae)
		{
		}
	}

	public void fadeIn()
	{
		setVolume(0.0025F);
		toFI = true;
		toFO = false;
	}

	public void fadeOut()
	{
		toFO = true;
		toFI = false;
	}
}