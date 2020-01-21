package com.zeitheron.musiclayer.internal.soundlib;

import com.zeitheron.musiclayer.api.adapter.IAdaptedSound;
import com.zeitheron.musiclayer.api.adapter.ISoundAdapter;
import com.zeitheron.musiclayer.internal.DummySound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SoundLibAdapter
		implements ISoundAdapter
{
	@Override
	public IAdaptedSound createSimpleStreamingSound(InputStream stream)
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
		try
		{
			return createSimpleStreamingSound(url.openStream());
		} catch(IOException e)
		{
			e.printStackTrace();
			return new DummySound(this);
		}
	}

	@Override
	public boolean canCreateFromStream()
	{
		return true;
	}
}