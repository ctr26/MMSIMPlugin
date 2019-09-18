package org.npl.biomet.mmsim;

public class SIModeRunnable implements Runnable {
	private SIMMode simmode_;

	public SIModeRunnable(SIMMode simmode) {
		simmode_ = simmode;
	}
	@Override
	public void run() {
		simmode_.newFrame();
	}
}
