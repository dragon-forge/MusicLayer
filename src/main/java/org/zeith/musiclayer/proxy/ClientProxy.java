package org.zeith.musiclayer.proxy;

import com.zeitheron.hammercore.utils.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.zeith.musiclayer.api.*;
import org.zeith.musiclayer.internal.test.TestDefaultPlayer;

import java.lang.reflect.Field;
import java.util.*;

public class ClientProxy
		extends CommonProxy
{
	private static final Map<String, MusicPlayer> PLAYERS = new HashMap<>();
	private static final Map<String, Object> PLAYING = new HashMap<>();

	@Override
	public void preInit()
	{
		MusicLayer.defaultPlayer = new MusicPlayer("MusicLayer");

		if("@VERSION@".contains("@")) MusicLayer.defaultPlayer.BUS.register(new TestDefaultPlayer());

		MusicLayer.defaultPosPlayer = new PositionedPlayer();

		try
		{
			GameSettings i = Minecraft.getMinecraft().gameSettings;
			Field f = ReflectionUtil.getField(GameSettings.class, Map.class);
			Map fmap = Map.class.cast(f.get(i));
			if(fmap instanceof EnumMap)
				ReflectionUtil.setFinalField(f, i, new HashMap<>(fmap));
		} catch(ReflectiveOperationException e1)
		{
			e1.printStackTrace();
		}
	}

	@Override
	public void assign(Object player)
	{
		MusicPlayer mp = (MusicPlayer) player;
		PLAYERS.put(mp.name, mp);
		mp.BUS.register(this);
	}

	@Override
	public void playOnce(String channel, String url)
	{
		if(url == null || url.isEmpty())
			PLAYING.remove(channel);
		else
			PLAYING.put(channel, url);
	}

	@Override
	public void playOnce(String channel, ResourceLocation path)
	{
		if(path == null)
			PLAYING.remove(channel);
		else
			PLAYING.put(channel, path);
	}

	@SubscribeEvent
	public void getMusic(GetMusicEvent gme)
	{
		Object ob = PLAYING.get(gme.thread.name);
		if(ob != null)
		{
			if(ob instanceof ResourceLocation)
				gme.setSound((ResourceLocation) ob);
			else if(ob instanceof String)
				gme.setSound(ob.toString());
		}
	}

	@SubscribeEvent
	public void updAlts(UpdateAlternativeMusicEvent e)
	{
		PLAYING.remove(e.thread.name);
	}
}