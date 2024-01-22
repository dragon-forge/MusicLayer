package org.zeith.musiclayer.api.adapter;

import org.zeith.musiclayer.internal.soundlib.SoundLibAdapter;
import org.zeith.musiclayer.internal.javafx.JavaFXSoundAdapter;

public class SoundAdapters
{
	public static ISoundAdapter SOUND_LIB_ADAPTER = new SoundLibAdapter();
	public static ISoundAdapter JAVAFX_ADAPTER = new JavaFXSoundAdapter();
}