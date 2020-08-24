package com.zeitheron.musiclayer.internal;

import com.zeitheron.musiclayer.api.IInput;
import tk.zeitheron.sound.IInputStream;

public class InputSLWrapper
{
	public static IInputStream createSLSrc(IInput input)
	{
		return input::createInput;
	}
}