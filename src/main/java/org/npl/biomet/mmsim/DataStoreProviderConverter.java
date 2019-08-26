package org.npl.biomet.mmsim;

import org.micromanager.Studio;
import org.micromanager.data.*;

import java.io.IOException;
import java.util.List;

public class DataStoreProviderConverter implements DataProvider {
	private final Studio studio_;
	private final Datastore datastore_;

	public DataStoreProviderConverter(Studio studio, Datastore datastore) {
		studio_ = studio;
		datastore_ = datastore;

	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public Image getAnyImage() throws IOException {
		return datastore_.getAnyImage();
	}

	@Override
	public List<String> getAxes() {
		return datastore_.getAxes();
	}

	@Override
	public int getAxisLength(String s) {
		return datastore_.getAxisLength(s);
	}

	@Override
	public Image getImage(Coords coords) throws IOException {
		return datastore_.getImage(coords);
	}

	@Override
	public List<Image> getImagesMatching(Coords coords) throws IOException {
		return datastore_.getImagesMatching(coords);
	}

	@Override
	public boolean isFrozen() {
		return datastore_.isFrozen();
	}

	@Override
	public Coords getMaxIndices() {
		return datastore_.getMaxIndices();
	}

	@Override
	public int getNumImages() {
		return datastore_.getNumImages();
	}

	@Override
	public SummaryMetadata getSummaryMetadata() {
		return datastore_.getSummaryMetadata();
	}

	@Override
	public Iterable<Coords> getUnorderedImageCoords() {
		return datastore_.getUnorderedImageCoords();
	}

	@Override
	public boolean hasImage(Coords coords) {
		return datastore_.hasImage(coords);
	}

	@Override
	public String getName() {
		return datastore_.getName();
	}

	@Override
	public void registerForEvents(Object o) {
		datastore_.registerForEvents(o);
	}

	@Override
	public void unregisterForEvents(Object o) {
		datastore_.unregisterForEvents(o);
	}
}
