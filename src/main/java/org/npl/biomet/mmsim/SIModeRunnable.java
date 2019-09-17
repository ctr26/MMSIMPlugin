package org.npl.biomet.mmsim;

import org.micromanager.Studio;

public class SIModeRunnable implements Runnable {
	private simRunnable runnable_;

	public SIModeRunnable(simRunnable runnable) {
		runnable_ = runnable;
	}
	@Override
	public void run() {
		runnable_.run();

	}
}
