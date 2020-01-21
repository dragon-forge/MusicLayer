package com.zeitheron.musiclayer.api.adapter;

/**
 * This class provides a simple control over the sound created using {@link ISoundAdapter}
 */
public interface IAdaptedSound
{
	/**
	 * Returns the adapter that was used to create this sound.
	 */
	ISoundAdapter getAdapter();

	/**
	 * [0;1]
	 */
	boolean setVolume(float volume);

	/**
	 * [0;1]
	 */
	float getVolume();

	/**
	 * [0;1]
	 */
	boolean seek(double progress);

	/**
	 * [-1;1]
	 */
	boolean setBalance(float balance);

	boolean play();

	boolean pause();

	boolean isPaused();

	boolean stop();

	default boolean isDummy()
	{
		return false;
	}
}