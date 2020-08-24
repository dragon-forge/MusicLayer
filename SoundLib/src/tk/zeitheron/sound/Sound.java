package tk.zeitheron.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.gagravarr.ogg.audio.OggAudioStatistics;
import org.gagravarr.vorbis.VorbisFile;
import org.tritonus.share.sampled.TAudioFormat;

public class Sound implements ISound
{
	final SourceDataLine line;
	final AudioInputStream ais;
	final AudioFormat outFormat;
	final Object lock;
	public final Object onFinished;
	boolean isPaused;
	boolean isDisposed;
	boolean isDonePlaying;
	
	double pos;
	double duration;
	
	public Sound(final IInputStream soundIn) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		this.lock = new Object();
		this.onFinished = new Object();
		this.isPaused = true;
		this.isDisposed = false;
		this.isDonePlaying = false;
		
		File fl = File.createTempFile("musiclib", "tmp").getAbsoluteFile();
		
		try(InputStream in = soundIn.getInput();FileOutputStream fos = new FileOutputStream(fl))
		{
			byte[] buf = new byte[1024];
			int r;
			while((r = in.read(buf)) > 0)
				fos.write(buf, 0, r);
		} catch(IOException ioe)
		{
			fl.delete();
			throw ioe;
		}
		
		this.ais = AudioSystem.getAudioInputStream(new BufferedInputStream(soundIn.getInput()));
		
		if(ais.getFormat() instanceof TAudioFormat)
		{
			try(VorbisFile vorbisFile = new VorbisFile(fl))
			{
				OggAudioStatistics stats = new OggAudioStatistics(vorbisFile, vorbisFile);
				stats.calculate();
				duration = stats.getDurationSeconds();
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		} else
		{
			duration = ais.getFrameLength() * ais.getFormat().getFrameRate();
		}
		
		fl.delete();
		
		this.outFormat = SoundLib.getOutFormat(this.ais.getFormat());
		final DataLine.Info info = new DataLine.Info(SourceDataLine.class, this.outFormat);
		(this.line = (SourceDataLine) AudioSystem.getLine(info)).open(this.outFormat);
		new Thread(() ->
		{
			try
			{
				this.line.start();
				this.appendAudioData(AudioSystem.getAudioInputStream(this.outFormat, this.ais));
				this.line.drain();
				this.line.stop();
				this.line.close();
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}).start();
	}
	
	public Sound(final byte[] soundIn) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		this(new IInputStream.ByteArray(soundIn));
	}
	
	@Override
	public void setVolume(final float vol)
	{
		final FloatControl volume = this.getControl(FloatControl.Type.MASTER_GAIN);
		final float difference = volume.getMaximum() - volume.getMinimum();
		volume.setValue(volume.getMinimum() + difference * vol);
	}
	
	@Override
	public void play()
	{
		if(!this.isPaused)
		{
			return;
		}
		this.isPaused = false;
		synchronized(this.lock)
		{
			this.lock.notifyAll();
		}
	}
	
	@Override
	public void stop()
	{
		this.isPaused = true;
	}
	
	@Override
	public void dispose()
	{
		this.stop();
		this.isDisposed = true;
		synchronized(this.lock)
		{
			this.lock.notifyAll();
		}
	}
	
	@Override
	public boolean isDonePlaying()
	{
		return this.isDonePlaying;
	}
	
	@Override
	public FloatControl getControl(final FloatControl.Type type)
	{
		return (FloatControl) this.line.getControl(type);
	}
	
	public void appendAudioData(final AudioInputStream ais) throws IOException
	{
		final byte[] buffer = new byte[512];
		for(int n22 = 0; n22 != -1; n22 = ais.read(buffer, 0, buffer.length))
		{
			if(this.isDisposed)
			{
				return;
			}
			
			if(this.isPaused)
			{
				synchronized(this.lock)
				{
					try
					{
						this.lock.wait();
					} catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			if(this.isDisposed)
			{
				return;
			}
			
			this.line.write(buffer, 0, n22);
			
			this.pos = TimeUnit.MILLISECONDS.convert(line.getMicrosecondPosition(), TimeUnit.MICROSECONDS) / 1000D;
		}
		synchronized(this.onFinished)
		{
			this.onFinished.notifyAll();
		}
		this.isDonePlaying = true;
		this.dispose();
	}
	
	@Override
	public double getDurationInSeconds()
	{
		return duration;
	}
	
	@Override
	public double getPlayedSeconds()
	{
		return pos;
	}
}