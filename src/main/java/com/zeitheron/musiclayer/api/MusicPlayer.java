package com.zeitheron.musiclayer.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.zeitheron.hammercore.utils.base.EvtBus;
import com.zeitheron.musiclayer.MusicLayerMC;

import com.zeitheron.musiclayer.internal.soundlib.FadedRepeatableSound;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MusicPlayer
{
	private final List<FadedRepeatableSound> pendingRemove = new ArrayList<>();
	public final Set<FadedRepeatableSound> allTickedSounds = new HashSet<>();
	public final EventBus BUS = MusicLayer.createBus();
	private final Timer timer;
	private Thread ticker;
	public FadedRepeatableSound prevSound;
	public final String name;
	
	public double fadingSpeed = 0.00125F;
	public float initialVolume = 0.5F;
	
	public SoundCategory listenCategory = SoundCategory.AMBIENT;
	
	public MusicPlayer(String name)
	{
		this(name, 30);
	}
	
	public MusicPlayer(String name, int ticksPerSecond)
	{
		this.name = name;
		this.timer = new Timer(ticksPerSecond);
		MinecraftForge.EVENT_BUS.register(this);
		checkThreadState();
		MusicLayerMC.proxy.assign(this);
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
					Thread.sleep(10L);
				} catch(InterruptedException e)
				{
				}
				timer.advanceTime();
				for(int i = 0; i < timer.ticks; ++i)
					update();
			}
		});
		
		ticker.setName("MusicLayer#" + name);
		ticker.start();
	}
	
	@SubscribeEvent
	public void update(ClientTickEvent e)
	{
		if(e.phase == Phase.START)
			checkThreadState();
	}
	
	@SideOnly(Side.CLIENT)
	public void update()
	{
		for(FadedRepeatableSound snd : allTickedSounds)
		{
			snd.fadeSpeed = fadingSpeed;
			if(snd == null || snd.getVolume() <= 0F || snd.isDonePlaying())
			{
				pendingRemove.add(snd);
				snd.dispose();
			} else
				snd.update();
		}
		
		if(!pendingRemove.isEmpty())
		{
			allTickedSounds.removeAll(pendingRemove);
			pendingRemove.clear();
		}
		
		try
		{
			if(prevSound != null)
			{
				allTickedSounds.add(prevSound);
				prevSound.setVolumeD(Minecraft.getMinecraft().gameSettings.getSoundLevel(listenCategory));
				prevSound.disposeWhenNotHearable = false;
				
				if(prevSound.getVolume() == 0F)
				{
					// Called when sound is done playing
					EvtBus.postSafe(BUS, new UpdateAlternativeMusicEvent(this));
					
					prevSound.dispose();
					prevSound = null;
				}
				
				if(prevSound.isDonePlaying())
				{
					// Called when sound is done playing
					EvtBus.postSafe(BUS, new UpdateAlternativeMusicEvent(this));
					
					prevSound.dispose();
					allTickedSounds.remove(prevSound);
					prevSound = null;
				}
			}
		} catch(NullPointerException npe)
		{
		}
		
		try
		{
			GetMusicEvent gme = new GetMusicEvent(Minecraft.getMinecraft().player, null, this, "Nothing");
			getDefMusic(gme);
			BUS.post(gme);
			
			if(gme.getMusic() != null)
			{
				if(prevSound == null)
				{
					prevSound = new FadedRepeatableSound(gme.getMusic(), initialVolume, gme.getName() + "");
					prevSound.fadeSpeed = fadingSpeed;
					prevSound.play();
					prevSound.fadeIn();
				} else if(!prevSound.sound.toString().equals(gme.getName()))
				{
					/** Update possible alternative music */
					EvtBus.postSafe(BUS, new UpdateAlternativeMusicEvent(this));
					
					gme = new GetMusicEvent(Minecraft.getMinecraft().player, null, this, "Nothing");
					getDefMusic(gme);
					BUS.post(gme);
					
					String soundName = gme.getName();
					
					prevSound.fadeOut();
					prevSound = new FadedRepeatableSound(gme.getMusic(), initialVolume, soundName);
					prevSound.fadeSpeed = fadingSpeed;
					prevSound.fadeIn();
					prevSound.play();
				}
			} else if(prevSound != null)
			{
				prevSound.fadeOut();
				prevSound = null;
			}
			
		} catch(Throwable err)
		{
		}
	}
	
	@SubscribeEvent
	public void soundSystemUnload(SoundLoadEvent e)
	{
		if(prevSound != null)
			prevSound.fadeOut();
		prevSound = null;

		EvtBus.postSafe(BUS, new UpdateAlternativeMusicEvent(this));
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void login(PlayerLoggedInEvent event)
	{
		if(event.player instanceof EntityPlayerMP)
			return;
		if(prevSound != null)
			prevSound.fadeOut();
		prevSound = null;
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void logout(PlayerLoggedOutEvent event)
	{
		if(event.player instanceof EntityPlayerMP)
			return;
		if(prevSound != null)
		{
			prevSound.fadeOut();
			allTickedSounds.add(prevSound);
		}
		prevSound = null;
	}
	
	private void getDefMusic(GetMusicEvent gme)
	{
		gme.setName(null);
		gme.setMusic(null);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj == this || (obj instanceof MusicPlayer && Objects.equals(((MusicPlayer) obj).name, name));
	}
}