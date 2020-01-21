package com.zeitheron.musiclayer.internal.javafx;

import com.sun.javafx.application.PlatformImpl;
import com.zeitheron.musiclayer.api.adapter.IAdaptedSound;
import com.zeitheron.musiclayer.api.adapter.ISoundAdapter;
import com.zeitheron.musiclayer.internal.DummySound;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.InputStream;
import java.net.URL;

public class JavaFXSoundAdapter
		implements ISoundAdapter
{
	static
	{
		PlatformImpl.startup(() ->
		{
		});
	}

	@Override
	public IAdaptedSound createSimpleStreamingSound(InputStream stream)
	{
		return new DummySound(this);
	}

	@Override
	public IAdaptedSound createSimpleURISound(URL uri)
	{
		try
		{
			Media media = new Media(uri.toString());
			MediaPlayer player = new MediaPlayer(media);
			return new JavaFXSound(player);
		} catch(Throwable err)
		{
			err.printStackTrace();
			return new DummySound(this);
		}
	}

	@Override
	public boolean canCreateFromStream()
	{
		return false;
	}
}