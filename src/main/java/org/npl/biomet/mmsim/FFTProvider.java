package org.npl.biomet.mmsim;

import org.micromanager.Studio;
import org.micromanager.data.*;

import java.io.IOException;
import java.util.List;

public class FFTProvider implements DataProvider {
	public FFTProvider(Datastore datastore) {
	}

	public FFTProvider(Studio studio_, Datastore datastore) {
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public Image getAnyImage() throws IOException {
		return null;
	}

	@Override
	public List<String> getAxes() {
		return null;
	}

	@Override
	public int getAxisLength(String s) {
		return 0;
	}

	@Override
	public Image getImage(Coords coords) throws IOException {
		return null;
	}

	@Override
	public List<Image> getImagesMatching(Coords coords) throws IOException {
		return null;
	}

	@Override
	public boolean isFrozen() {
		return false;
	}

	@Override
	public Coords getMaxIndices() {
		return null;
	}

	@Override
	public int getNumImages() {
		return 0;
	}

	@Override
	public SummaryMetadata getSummaryMetadata() {
		return null;
	}

	@Override
	public Iterable<Coords> getUnorderedImageCoords() {
		return null;
	}

	@Override
	public boolean hasImage(Coords coords) {
		return false;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void registerForEvents(Object o) {

	}

	@Override
	public void unregisterForEvents(Object o) {

	}
}
