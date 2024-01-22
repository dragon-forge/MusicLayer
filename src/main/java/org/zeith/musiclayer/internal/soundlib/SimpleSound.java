package org.zeith.musiclayer.internal.soundlib;

import org.zeith.musiclayer.api.IInput;
import org.zeith.musiclayer.internal.InputSLWrapper;
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
