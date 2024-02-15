package globalquake.playground;

import globalquake.core.analysis.Event;
import globalquake.core.station.AbstractStation;
import gqserver.api.packets.station.InputType;

public class PlaygroundStation extends AbstractStation {

    public static final double SAMPLE_RATE = 50;
    public long lastSampleTime = -1;
    private final StationWaveformGenerator generator;
    public PlaygroundStation(String networkCode, String stationCode, String channelName, String locationCode, double lat, double lon, double alt, int id) {
        super(networkCode, stationCode, channelName, locationCode, lat, lon, alt, id, null,
                2E10);
        getAnalysis().setSampleRate(SAMPLE_RATE);
        this.generator = new StationWaveformGenerator(this, id);
    }

    public PlaygroundStation(String stationCode, double lat, double lon, double alt, int id) {
        this("", stationCode, "", "", lat, lon, alt, id);
    }

    @Override
    public void second(long time) {
        super.second(time);
        generator.second();
    }

    @Override
    public InputType getInputType() {
        return InputType.VELOCITY;
    }

    @Override
    public boolean hasData() {
        return lastSampleTime != -1;
    }

    @Override
    public boolean hasDisplayableData() {
        return hasData();
    }

    public int getNoise(long time) {
        return generator.getValue(time);
    }

    @Override
    public boolean isInEventMode() {
        Event event = getAnalysis() == null ? null : getAnalysis().getLatestEvent();
        return event != null && event.isValid() && !event.hasEnded();
    }
}
