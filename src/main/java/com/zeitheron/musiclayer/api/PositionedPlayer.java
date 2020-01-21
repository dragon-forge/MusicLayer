package com.zeitheron.musiclayer.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.zeitheron.musiclayer.internal.soundlib.PositionedRepeatableSound;
import net.minecraft.util.math.Vec3d;

public class PositionedPlayer
{
	public Map<String, PositionedRepeatableSound> soundMap = new HashMap<>();
	private final Timer timer = new Timer(30);
	private Thread ticker;
	
	{
		checkThreadState();
	}
	
	public String play(Vec3d pos, float maxDist, IInput in)
	{
		return play(() -> pos, maxDist, in);
	}
	
	public String play(Supplier<Vec3d> pos, float maxDist, IInput in)
	{
		try
		{
			String id = UUID.randomUUID().toString();
			PositionedRepeatableSound prs;
			soundMap.put(id, prs = new PositionedRepeatableSound(in, maxDist, pos));
			prs.play();
			return id;
		} catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
		}
		return null;
	}
	
	private void checkThreadState()
	{
		if(ticker != null && ticker.isAlive())
			return;
		
		ticker = new Thread(() ->
		{
			while(true)
			{
				try
				{
					Thread.sleep(50L);
				} catch(InterruptedException e)
				{
				}
				timer.advanceTime();
				for(int i = 0; i < timer.ticks; ++i)
					update();
			}
		});
		
		ticker.setName("MusicLayer#PosTicker-" + Integer.toHexString(hashCode()));
		ticker.start();
	}
	
	private final List<String> toRem = new ArrayList<>();
	
	public void update()
	{
		toRem.clear();
		for(Map.Entry<String, PositionedRepeatableSound> e : soundMap.entrySet())
		{
			if(e.getValue().isDonePlaying())
			{
				if(e.getValue().repeat)
					e.getValue().play();
				else
					toRem.add(e.getKey());
			} else
				e.getValue().update();
		}
		for(String s : toRem)
			soundMap.remove(s);
	}
}