package org.openmrs.module.OpenMRSSMSModule.api.impl;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTime;

public class TarseelGlobals {

	static Random ran = new Random();
	public static synchronized String getUniqueSmsId(int projectId)
	{
		try
		{
			Thread.sleep(1);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
        long l1 = Math.abs(ran.nextInt(9999));
		StringBuilder sb = new StringBuilder(projectId+""+Long.toString(System.currentTimeMillis()));
		sb.append(ran.nextInt(9)+""+ran.nextInt(9));
		//sb.setLength(17);
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
	    ran.setSeed(new DateTime().getMillis()+Math.abs(ran.nextInt(Integer.MAX_VALUE)));

		Set<String> s = new HashSet<String>();
		for (int i = 0; i < 100000; i++)
		{
			s.add(getUniqueSmsId(1));
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		//System.out.println(s);
		for (String string : s)
		{
			System.out.println(string);
		}
		System.out.println(s.size());

	}
}
