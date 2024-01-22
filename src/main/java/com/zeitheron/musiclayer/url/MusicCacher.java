package com.zeitheron.musiclayer.url;

import com.zeitheron.hammercore.lib.zlib.io.IOUtils;
import com.zeitheron.hammercore.lib.zlib.tuple.TwoTuple;
import com.zeitheron.hammercore.lib.zlib.utils.MD5;
import com.zeitheron.musiclayer.MusicLayerMC;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

public class MusicCacher
{
	private static final Map<String, Thread> md5Threads = new HashMap<>();
	private static final Map<String, Thread> parallelThreads = new HashMap<>();
	private static final List<String> delUrls = new ArrayList<>();

	public static InputStream getMusicStream(final String url)
	{
		return getFileStream(url, "music");
	}

	/**
	 * May be used to load archives and cache them.
	 */
	public static ZipInputStream getZipStream(final String url)
	{
		return new ZipInputStream(getFileStream(url, "archives"));
	}

	private static InputStream getFileStream(final String url, String subfolder)
	{
		File f = MusicLayerMC.mcPath.toPath().resolve("asm").resolve("MusicLayer").resolve(subfolder).toFile();
		if(!f.isDirectory())
			f.mkdirs();

		f = new File(f, MD5.encrypt(url));
		final File ff = f;

		if(f.isFile())
		{
			if(delUrls.contains(url))
				f.delete();
			else if(!md5Threads.containsKey(url))
			{
				Thread t = new Thread(() ->
				{
					String md5 = MD5.getMD5Checksum(ff.getAbsolutePath());
					TwoTuple<InputStream, Boolean> b = IOUtils.getInput(url);

					/* Verify only if file is being loaded from online */
					if(b.get2() != null && b.get2())
					{
						InputStream in = b.get1();
						String live = getMD5Checksum(in);

						if(!md5.equals(live))
							delUrls.add(url);
					}

					md5Threads.remove(url);
				});

				t.setName("MusicLayer#MusicMD5Thread#" + md5Threads.size());
				t.start();

				md5Threads.put(url, t);
			}
		}

		if(!f.isFile() && !parallelThreads.containsKey(url))
		{
			Thread t = new Thread(() ->
			{
				InputStream inp = IOUtils.getInput(url).get1();

				try(FileOutputStream fos = new FileOutputStream(ff); InputStream in = inp)
				{
					IOUtils.pipeData(in, fos);
				} catch(IOException er)
				{
				}

				parallelThreads.remove(url);
			});

			t.setName("MusicLayer#MusicDwnThread#" + parallelThreads.size());
			t.start();

			parallelThreads.put(url, t);
		}

		if(f.isFile())
			try
			{
				return new FileInputStream(f);
			} catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}

		return IOUtils.getInput(url).get1();
	}

	public static String getMD5Checksum(InputStream input)
	{
		byte[] b = null;
		try
		{
			b = createChecksum(input);
		} catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
		BigInteger bigInt = new BigInteger(1, b);
		String md5Hex = bigInt.toString(16);
		while(md5Hex.length() < 32)
			md5Hex = "0" + md5Hex;
		return md5Hex;
	}

	public static byte[] createChecksum(InputStream input) throws Exception
	{
		int numRead;
		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		do
		{
			if((numRead = input.read(buffer)) <= 0)
				continue;
			complete.update(buffer, 0, numRead);
		} while(numRead != -1);
		input.close();
		return complete.digest();
	}
}