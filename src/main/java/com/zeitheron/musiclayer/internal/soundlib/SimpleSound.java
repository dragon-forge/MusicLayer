package com.zeitheron.musiclayer.internal.soundlib;

import com.zeitheron.sound.Sound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

public class SimpleSound
		extends Sound
{
	public final SoundlibSoundAdapter adapter = new SoundlibSoundAdapter(this);

	public SimpleSound(InputStream soundIn) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		super(soundIn);
	}
}
