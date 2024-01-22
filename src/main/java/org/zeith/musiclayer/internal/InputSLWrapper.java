package org.zeith.musiclayer.internal;

import org.zeith.musiclayer.api.IInput;
import tk.zeitheron.sound.IInputStream;

public class InputSLWrapper
{
	public static IInputStream createSLSrc(IInput input)
	{
		return input::createInput;
	}
}