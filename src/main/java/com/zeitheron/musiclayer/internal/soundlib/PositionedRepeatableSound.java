package com.zeitheron.musiclayer.internal.soundlib;

import java.io.IOException;
import java.util.function.Supplier;

import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.zeitheron.musiclayer.api.IInput;
import com.zeitheron.musiclayer.internal.InputSLWrapper;
import com.zeitheron.sound.RepeatableSound;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public class PositionedRepeatableSound extends RepeatableSound
{
	public final SoundlibSoundAdapter adapter = new SoundlibSoundAdapter(this);

	public Supplier<Vec3d> position;
	public float rad;

	public boolean repeat;
	
	public PositionedRepeatableSound(IInput soundIn, float rad, Vec3d position) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		this(soundIn, rad, () -> position);
	}
	
	public PositionedRepeatableSound(IInput soundIn, float rad, Supplier<Vec3d> position) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		super(InputSLWrapper.createSLSrc(soundIn));
		this.rad = rad;
		this.position = position;
	}
	
	public void update()
	{
		setVolume(calcPosMult());
	}
	
	public float calcPosMult()
	{
		if(Minecraft.getMinecraft().player == null)
			return 1;
		Vec3d player = Minecraft.getMinecraft().player.getPositionVector();
		Vec3d sound = position.get();
		double distSq = player.squareDistanceTo(sound);
		double limDSq = rad * rad;
		if(limDSq < distSq)
			return 0;
		double curDSq = limDSq - distSq;
		return (float) (curDSq / limDSq);
	}
	
	public float getVolume()
	{
		try
		{
			return (float) Math.pow(10, getControl(Type.MASTER_GAIN).getValue() / 20);
		} catch(IllegalArgumentException iae)
		{
			return 0F;
		}
	}
	
	@Override
	public void setVolume(float vol)
	{
		/** Escape illegal values */
		if(vol < 0F || vol > 1F)
			return;
		try
		{
			getControl(Type.MASTER_GAIN).setValue(20F * (float) Math.log10(vol));
		} catch(IllegalArgumentException iae)
		{
		}
	}
	
	@Override
	public void play()
	{
		super.play();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	@Override
	public boolean isDonePlaying()
	{
		return super.isDonePlaying();
	}
	
	@Override
	public void stop()
	{
		super.stop();
	}
}