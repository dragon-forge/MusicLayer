package com.zeitheron.musiclayer.api;

import com.zeitheron.musiclayer.url.MusicCacher;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Gets the music each tick, perform your own checks to override music fired on
 * the {@link MusicPlayer#BUS}
 **/
public class GetMusicEvent extends PlayerEvent
{
	public final MusicPlayer thread;
	public IInput music;
	public String name;
	public boolean restart;

	public final double currentLength, currentPlayTime, currentLeftTime;
	
	public GetMusicEvent(EntityPlayer player, IInput music, MusicPlayer thread, String name, double currentLength, double currentPlayTime, double currentLeftTime)
	{
		super(player);
		this.thread = thread;
		this.music = music;
		this.name = name;
		this.currentLength = currentLength;
		this.currentPlayTime = currentPlayTime;
		this.currentLeftTime = currentLeftTime;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public IInput getMusic()
	{
		return music;
	}
	
	public void setMusic(IInput music)
	{
		this.music = music;
	}
	
	/**
	 * Use this to set your sound. Here's how it works: <br>
	 * Call setSound(new ResourceLocation("musiclayer", "music/example_music"))
	 * <br>
	 * * This will play sound from this file:
	 * /assets/musiclayer/sounds/music/example_music.ogg <br>
	 * Note: the music file can be overrided by resource packs.
	 */
	public void setSound(ResourceLocation path)
	{
		setMusic(IInput.ofSound(path));
		setName(path.toString());
	}
	
	/**
	 * Use this to set your sound. Here's how it works: <br>
	 * Call setSound(new ResourceLocation("musiclayer",
	 * "sounds/music/example_music.ogg")) <br>
	 * * This will play sound from this file:
	 * /assets/musiclayer/sounds/music/example_music.ogg <br>
	 * Note: the music file can be overrided by resource packs.
	 */
	public void setSoundFullPath(ResourceLocation path)
	{
		setMusic(IInput.ofSoundFullPath(path));
		setName(path.toString());
	}
	
	/**
	 * Use this to set your sound. Here's how it works: <br>
	 * Call setSound("<DIRECT URL TO A OGG>") <br>
	 * This will play sound from passed url. Note: the music file will get
	 * cached on user's hard drive in order to play it in offline mode.
	 */
	public void setSound(String url)
	{
		setMusic(() -> MusicCacher.getMusicStream(url));
		setName(url + "");
	}
}