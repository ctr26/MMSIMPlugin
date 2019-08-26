package org.npl.biomet.mmsim;

import org.micromanager.Studio;
import org.micromanager.data.DataProvider;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;

import java.io.IOException;

public class FFTThread implements Runnable{
	private Image current_image;
	public FFTThread(Studio studio_, DataProvider fft_provider) throws IOException {
		Image current_image = fft_provider.getAnyImage();
	}
	@Override
	public void run() {
		//
	}
}
