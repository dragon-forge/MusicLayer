package org.zeith.musiclayer.internal;

import org.zeith.musiclayer.api.adapter.IAdaptedSound;
import org.zeith.musiclayer.api.adapter.ISoundAdapter;

public class DummySound
		implements IAdaptedSound
{
	final ISoundAdapter adapter;

	public DummySound(ISoundAdapter adapter)
	{
		this.adapter = adapter;
	}

	@Override
	public boolean isDummy()
	{
		return true;
	}

	@Override
	public ISoundAdapter getAdapter()
	{
		return adapter;
	}

	@Override
	public boolean setVolume(float volume)
	{
		return false;
	}

	@Override
	public float getVolume()
	{
		return 0;
	}

	@Override
	public boolean seek(double progress)
	{
		return false;
	}

	@Override
	public boolean setBalance(float balance)
	{
		return false;
	}

	@Override
	public boolean play()
	{
		return false;
	}

	@Override
	public boolean pause()
	{
		return false;
	}

	@Override
	public boolean isPaused()
	{
		return false;
	}

	@Override
	public boolean stop()
	{
		return false;
	}
}
