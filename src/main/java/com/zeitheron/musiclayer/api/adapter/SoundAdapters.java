package com.zeitheron.musiclayer.api.adapter;

import com.zeitheron.musiclayer.internal.soundlib.SoundLibAdapter;
import com.zeitheron.musiclayer.internal.javafx.JavaFXSoundAdapter;

public class SoundAdapters
{
	public static ISoundAdapter SOUND_LIB_ADAPTER = new SoundLibAdapter();
	public static ISoundAdapter JAVAFX_ADAPTER = new JavaFXSoundAdapter();
}