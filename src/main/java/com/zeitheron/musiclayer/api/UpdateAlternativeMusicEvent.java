package com.zeitheron.musiclayer.api;

import net.minecraftforge.fml.common.eventhandler.Event;

public class UpdateAlternativeMusicEvent extends Event
{
	public final MusicPlayer thread;
	
	public UpdateAlternativeMusicEvent(MusicPlayer thread)
	{
		this.thread = thread;
	}
}