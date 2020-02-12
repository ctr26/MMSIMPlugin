package org.npl.biomet.mmsim;

public class SIModeRunnable implements Runnable {
	private SIMMode simmode_;

	public SIModeRunnable(SIMMode simmode) {
		simmode_ = simmode;
	}
	@Override
	public void run() {
		System.out.println("SIModeRunnable");
		simmode_.newFrame();
	}
}
