package org.zeith.musiclayer.net;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import org.zeith.musiclayer.MusicLayerMC;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class PacketStopMusic implements IPacket
{
	public String channel;
	
	static
	{
		IPacket.handle(PacketStopMusic.class, PacketStopMusic::new);
	}
	
	public PacketStopMusic(String channel)
	{
		this.channel = channel;
	}
	
	public PacketStopMusic()
	{
	}
	
	@Override
	public IPacket execute(Side side, PacketContext ctx)
	{
		MusicLayerMC.proxy.playOnce(channel, "");
		return IPacket.super.execute(side, ctx);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Ch", channel);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		channel = nbt.getString("Ch");
	}
}