package com.zeitheron.musiclayer.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.eventhandler.EventBus;

public class MusicLayer
{
	public static final Logger LOG = LogManager.getLogger("MusicLayer");
	
	/** This is the default player that is playing music */
	public static MusicPlayer defaultPlayer;
	
	/** This is the default positioned player that is playing positioned sounds */
	public static PositionedPlayer defaultPosPlayer;
	
	/** Now use {@link MusicPlayer#BUS} instead. This event bus is no longer getting events through it. */
	@Deprecated
	public static final EventBus MUSIC_BUS = createBus();
	
	public static EventBus createBus()
	{
		return new EventBus((bus, event, listeners, index, throwable) ->
		{
			LOG.error("Eror: Failed to dispatch event " + event.getClass() + " " + (event.toString().replaceAll(event.getClass().getName(), event.getClass().getSimpleName())) + "!", throwable);
			LOG.error("Thought this is going to get ignored, please report this to the mod author!");
		});
	}
}