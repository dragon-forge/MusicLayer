package org.zeith.musiclayer.internal.test;

import org.zeith.musiclayer.api.GetMusicEvent;
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
		e.setSound(new File("P:\\$Assets\\Terraria\\1.4.4.1\\Music\\06_Jungle.wav").toURI().toURL().toString());
	}
}