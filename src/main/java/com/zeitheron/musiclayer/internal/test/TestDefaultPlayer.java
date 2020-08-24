package com.zeitheron.musiclayer.internal.test;

import com.zeitheron.musiclayer.api.GetMusicEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.net.MalformedURLException;

public class TestDefaultPlayer
{
	@SubscribeEvent
	public void music(GetMusicEvent e) throws MalformedURLException
	{
		if(e.currentLeftTime < 5)
			e.restart = true;
		e.setSound(new File("C:\\Users\\Zeitheron\\Downloads\\eerie.ogg").toURI().toURL().toString());
	}
}