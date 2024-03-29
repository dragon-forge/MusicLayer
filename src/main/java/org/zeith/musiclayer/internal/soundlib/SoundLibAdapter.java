package org.zeith.musiclayer.internal.soundlib;

import org.zeith.musiclayer.api.IInput;
import org.zeith.musiclayer.api.adapter.IAdaptedSound;
import org.zeith.musiclayer.api.adapter.ISoundAdapter;
import org.zeith.musiclayer.internal.DummySound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class SoundLibAdapter
		implements ISoundAdapter
{
	@Override
	public IAdaptedSound createSimpleStreamingSound(IInput stream)
	{
		try
		{
			return new SimpleSound(stream).adapter;
		} catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			e.printStackTrace();
			return new DummySound(this);
		}
	}

	@Override
	public IAdaptedSound createSimpleURISound(URL url)
	{
		return createSimpleStreamingSound(url::openStream);
	}

	@Override
	public boolean canCreateFromStream()
	{
		return true;
	}
}