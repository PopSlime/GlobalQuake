package globalquake.ui.action.source;

import com.opencsv.CSVReader;
import globalquake.core.database.StationDatabaseManager;
import globalquake.core.database.StationSource;
import org.tinylog.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ImportStationSourcesAction extends AbstractAction {

    private final StationDatabaseManager databaseManager;
    private final Window parent;

    public ImportStationSourcesAction(Window parent, StationDatabaseManager databaseManager) {
        super("Import");
        this.databaseManager = databaseManager;
        this.parent = parent;

        putValue(SHORT_DESCRIPTION, "Import Seedlink Networks");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "CSV Files", "csv");
        chooser.setFileFilter(filter);
        chooser.setApproveButtonText("Ok");
        chooser.setDialogTitle("Import from CSV");
        chooser.setDragEnabled(false);

        int returnVal = chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                // Call your importFrom method with the selectedFile
                importCSV(chooser.getSelectedFile());
                JOptionPane.showMessageDialog(chooser, "CSV file imported successfully!");
            } catch (Exception e) {
                Logger.error(e);
                JOptionPane.showMessageDialog(chooser, "Import failed: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importCSV(File selectedFile) throws IOException {
        java.util.List<StationSource> loaded = new ArrayList<>();
        CSVReader reader = new CSVReader(new FileReader(selectedFile));
        reader.skip(1);
        for (String[] data : reader) {
            if (data.length > 0)
                loaded.add(createSeedlinkNetworkFromStringArray(data));
        }


        boolean delete = JOptionPane.showConfirmDialog(parent, "Keep existing station sources?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION;

        databaseManager.getStationDatabase().getDatabaseWriteLock().lock();
        try {
            if (delete) {
                databaseManager.removeAllStationSources(databaseManager.getStationDatabase().getStationSources());
            }
            databaseManager.getStationDatabase().getStationSources().addAll(loaded);
        } finally {
            databaseManager.getStationDatabase().getDatabaseWriteLock().unlock();
        }

        databaseManager.fireUpdateEvent();
    }

    public StationSource createSeedlinkNetworkFromStringArray(String[] array) {
        if (array.length != 2) {
            throw new IllegalArgumentException("Invalid array length");
        }

        String name = array[0].replace('"', ' ').trim();
        String url = array[1].replace('"', ' ').trim();

        return new StationSource(name, url);
    }

}
