package org.zeith.musiclayer.net;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import org.zeith.musiclayer.MusicLayerMC;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

public class PacketStartMusic implements IPacket
{
	public int code = -1;
	public String url;
	public String channel;
	
	static
	{
		IPacket.handle(PacketStartMusic.class, PacketStartMusic::new);
	}
	
	public PacketStartMusic(String url, String channel)
	{
		this.url = url;
		this.channel = channel;
		code = 0;
	}
	
	public PacketStartMusic(ResourceLocation path, String channel)
	{
		this.url = path.toString();
		this.channel = channel;
		code = 1;
	}
	
	public PacketStartMusic()
	{
	}
	
	@Override
	public IPacket execute(Side side, PacketContext ctx)
	{
		if(code == 0)
			MusicLayerMC.proxy.playOnce(channel, url);
		if(code == 1)
			MusicLayerMC.proxy.playOnce(channel, new ResourceLocation(url));
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("URL", url);
		nbt.setString("Channel", channel);
		nbt.setInteger("ID", code);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		url = nbt.getString("URL");
		channel = nbt.getString("Channel");
		code = nbt.getInteger("ID");
	}
}