package com.zeitheron.musiclayer.internal.soundlib;

import com.zeitheron.musiclayer.api.IInput;
import com.zeitheron.musiclayer.internal.InputSLWrapper;
import tk.zeitheron.sound.Sound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class SimpleSound
		extends Sound
{
	public final SoundlibSoundAdapter adapter = new SoundlibSoundAdapter(this);

	public SimpleSound(IInput soundIn) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		super(InputSLWrapper.createSLSrc(soundIn));
	}
}
