package org.zeith.musiclayer.api.adapter;

import org.zeith.musiclayer.api.IInput;

import java.io.InputStream;
import java.net.URL;

public interface ISoundAdapter
{
	/**
	 * Creates a sound from a stream. Note that this is not supported for some adapters like JavaFX. Better use URI version
	 */
	IAdaptedSound createSimpleStreamingSound(IInput stream);

	/**
	 * Creates an adapted sound from given url, if possible.
	 */
	IAdaptedSound createSimpleURISound(URL uri);

	/**
	 * Retrieves whether or not this adapter is cable of creating sounds from {@link InputStream} ({@link #createSimpleStreamingSound(IInput)})
	 */
	boolean canCreateFromStream();
}