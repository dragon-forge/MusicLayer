package tk.zeitheron.sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

@FunctionalInterface
public interface IInputStream
{
	InputStream getInput() throws IOException;
	
	static IInputStream wrap(Supplier<InputStream> in)
	{
		return in::get;
	}
	
	public static class ByteArray implements IInputStream
	{
		public final byte[] instance;
		
		public ByteArray(byte[] data)
		{
			this.instance = data;
		}
		
		@Override
		public InputStream getInput()
		{
			return new ByteArrayInputStream(this.instance);
		}
	}
	
	public static class WrappedInputStream implements IInputStream
	{
		public final ByteArray asByte;
		
		public WrappedInputStream(InputStream input) throws IOException
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[8192];
			int read = 0;
			while((read = input.read(buf)) > 0)
			{
				baos.write(buf, 0, read);
			}
			this.asByte = new ByteArray(baos.toByteArray());
		}
		
		public WrappedInputStream(InputStream input, int readLength) throws IOException
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[8192];
			int read = 0;
			while((read = input.read(buf, 0, Math.min(readLength, buf.length))) > 0)
			{
				readLength -= read;
				baos.write(buf, 0, read);
			}
			this.asByte = new ByteArray(baos.toByteArray());
		}
		
		@Override
		public InputStream getInput()
		{
			return this.asByte != null ? this.asByte.getInput() : null;
		}
	}
}