package hu.rxd;

import java.util.concurrent.TimeUnit;

import net.filebot.web.FloodLimit;

public class FloodLimitTest {


	public static void main(String[]a) throws Exception {
		final FloodLimit fl = new FloodLimit(1, 3, TimeUnit.SECONDS);
		fl.acquirePermit();
		System.out.println("exiting main");
	}

}
