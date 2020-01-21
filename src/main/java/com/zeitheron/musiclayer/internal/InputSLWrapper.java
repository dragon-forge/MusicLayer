package com.zeitheron.musiclayer.internal;

import java.io.IOException;
import java.io.InputStream;

import com.zeitheron.musiclayer.api.IInput;
import com.zeitheron.sound.IInputStream;

public class InputSLWrapper
{
	public static IInputStream createSLSrc(IInput input)
	{
		InputStream in = null;
		try
		{
			in = input.createInput();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		final InputStream $ = in;
		return () -> $;
	}
}