package globalquake.ui.globalquake.feature;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import globalquake.core.Settings;
import globalquake.ui.globe.GlobeRenderer;
import globalquake.ui.globe.Point2D;
import globalquake.ui.globe.RenderProperties;
import globalquake.ui.globe.feature.RenderElement;
import globalquake.ui.globe.feature.RenderEntity;
import globalquake.ui.globe.feature.RenderFeature;
import org.tinylog.Logger;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FeatureCities extends RenderFeature<CityLocation> {

    private final Collection<CityLocation> cityLocations = new ArrayList<>();

    public FeatureCities() {
        super(1);
        load();
    }

    private void load() {
        int errors = 0;
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(ClassLoader.getSystemClassLoader().getResource("cities/worldcities.csv").openStream())).withSkipLines(1).build()) {
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                String cityName = fields[1];
                double lat = Double.parseDouble(fields[2]);
                double lon = Double.parseDouble(fields[3]);

                int population;

                try {
                    population = Integer.parseInt(fields[9]);
                } catch(NumberFormatException e){
                    errors++;
                    continue;
                }

                cityLocations.add(new CityLocation(cityName, lat, lon, population));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        Logger.info("%d cities have unknown population!".formatted(errors));
    }

    @Override
    public Collection<CityLocation> getElements() {
        return cityLocations;
    }

    @Override
    public void createPolygon(GlobeRenderer renderer, RenderEntity<CityLocation> entity, RenderProperties renderProperties) {
        RenderElement elementCross = entity.getRenderElement(0);

        int population = entity.getOriginal().population();

        double multiplier = Math.sqrt(population / 100000.0);
        multiplier = Math.min(3.0, multiplier);

        double size = Math.min(36, renderer.pxToDeg(3.0, renderProperties));

        renderer.createSquare(elementCross.getPolygon(),
                entity.getOriginal().lat(),
                entity.getOriginal().lon(), size, 0.0);
    }

    @Override
    public boolean needsCreatePolygon(RenderEntity<CityLocation> entity, boolean propertiesChanged) {
        return propertiesChanged;
    }

    @Override
    public boolean needsProject(RenderEntity<CityLocation> entity, boolean propertiesChanged) {
        return propertiesChanged;
    }

    @Override
    public boolean needsUpdateEntities() {
        return false;
    }

    @Override
    public void project(GlobeRenderer renderer, RenderEntity<CityLocation> entity, RenderProperties renderProperties) {
        RenderElement element = entity.getRenderElement(0);
        element.getShape().reset();
        element.shouldDraw = renderer.project3D(element.getShape(), element.getPolygon(), true, renderProperties);
    }

    @Override
    public void render(GlobeRenderer renderer, Graphics2D graphics, RenderEntity<CityLocation> entity, RenderProperties renderProperties) {
        RenderElement element = entity.getRenderElement(0);
        if (element.shouldDraw) {
            int population = entity.getOriginal().population();

            double multiplier = Math.sqrt(population / 1000000.0);
            multiplier = Math.min(3.0, multiplier);

            graphics.setColor(Color.white);
            graphics.setStroke(new BasicStroke(3f));

            if(renderProperties.scroll < 0.3 * multiplier) {
                graphics.fill(element.getShape());
            }

            if (renderProperties.scroll < 0.15 * multiplier) {
                var point3D = GlobeRenderer.createVec3D(getCenterCoords(entity));
                var centerPonint = renderer.projectPoint(point3D, renderProperties);

                String str = entity.getOriginal().name();

                graphics.setFont(new Font("Calibri", Font.PLAIN, 14));
                graphics.drawString(str, (int) centerPonint.x - graphics.getFontMetrics().stringWidth(str) / 2, (int) centerPonint.y - 8);
            }
        }
    }

    @Override
    public Point2D getCenterCoords(RenderEntity<?> entity) {
        return new Point2D(((CityLocation) (entity.getOriginal())).lat(), ((CityLocation) (entity.getOriginal())).lon());
    }
}
