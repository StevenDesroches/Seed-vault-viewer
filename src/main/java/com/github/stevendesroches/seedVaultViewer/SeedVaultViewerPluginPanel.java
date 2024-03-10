package com.github.stevendesroches.seedVaultViewer;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.AsyncBufferedImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.regex.Pattern;

@Slf4j
public class SeedVaultViewerPluginPanel extends PluginPanel {

    //private final JLabel addMarker = new JLabel(ADD_ICON);
    private final JLabel title = new JLabel();
    private final JPanel markerView = new JPanel(new GridBagLayout());

    private final PluginErrorPanel noMarkersPanel = new PluginErrorPanel();

    private SeedVaultViewer seedVaultViewerPlugin;

    private JLabel titleLabel;
    private JPanel searchPanel;
    private JPanel contentPanel;

    protected JTextField searchField;

    public SeedVaultViewerPluginPanel(SeedVaultViewer seedVaultViewerPlugin){
        this.seedVaultViewerPlugin =seedVaultViewerPlugin;

        this.setLayout(new BorderLayout());
        //this.setBorder(new EmptyBorder(5,5,5,5));

        this.titleLabel = new JLabel();
        this.searchPanel = new JPanel();
        this.contentPanel = new JPanel();

        this.titleLabel.setText("Seed Vault Viewer");


        this.searchField = new JTextField();
        this.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

        });

        this.searchPanel.setLayout(new GridLayout(2,1));
        this.searchPanel.add(titleLabel);
        this.searchPanel.add(searchField);

        //this.add(titleLabel);
        this.add(searchPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);


        //setBorder(new EmptyBorder(10, 10, 10, 10));

        //this.setBackground(Color.green);
        //this.setLayout(new FlowLayout());
        //this.setLayout(new GridLayout(1,1));
        //this.setBorder(new EmptyBorder(10, 10, 10, 10));
        //this.setLayout(new GridBagLayout());

        /*
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBorder(new EmptyBorder(1, 0, 10, 0));

        title.setText("Seed Vault Viewer");
        title.setForeground(Color.WHITE);

        northPanel.add(title, BorderLayout.WEST);
        //northPanel.add(addMarker, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        markerView.setBackground(ColorScheme.DARK_GRAY_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;

        noMarkersPanel.setContent("Screen Markers", "Highlight a region on your screen.");
        noMarkersPanel.setVisible(false);

        markerView.add(noMarkersPanel, constraints);
        constraints.gridy++;
        */



/*
        creationPanel = new ScreenMarkerCreationPanel(plugin);
        creationPanel.setVisible(false);

        markerView.add(creationPanel, constraints);
        constraints.gridy++;

        addMarker.setToolTipText("Add new screen marker");


        centerPanel.add(markerView, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);*/
    }

    public void search(String text) {
        if (text.isEmpty()) {
            for (Component component : contentPanel.getComponents()) {
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;
                    label.setVisible(true);
                }
            }
        } else {
            for (Component component : contentPanel.getComponents()) {
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;


                    if(Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(label.getText()).find()){
                        label.setVisible(true);
                    } else {
                        label.setVisible(false);
                    }
                            /*
                            if (label.getText().contains(text)) {
                                label.setVisible(true);
                            } else {
                                label.setVisible(false);
                            }
                             */
                }
            }
        }
    }

    public void addItem(net.runelite.api.Item item){


        AsyncBufferedImage image =this.seedVaultViewerPlugin.getItemManager().getImage(item.getId(),item.getQuantity(),true);
        String itemName = this.seedVaultViewerPlugin.getItemManager().getItemComposition(item.getId()).getName();
        log.debug(itemName);

        JLabel itemLabel = new JLabel();
        itemLabel.setText(itemName + ":" + String.valueOf(item.getQuantity()));
        image.addTo(itemLabel);

        this.add(itemLabel);


        repaint();
        revalidate();
    }

    public void addItem(net.runelite.api.Item[] items) {
        this.contentPanel.removeAll();
       // this.contentPanel.setLayout(new GridLayout(items.length,1));
        //this.contentPanel.setLayout(new BoxLayout(this.contentPanel,BoxLayout.Y_AXIS));

        this.contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        for (net.runelite.api.Item item : items) {
            log.debug(String.valueOf(item.getId()));
            ItemComposition itemComposition = this.seedVaultViewerPlugin.getItemManager().getItemComposition(item.getId());
            if(itemComposition.getPlaceholderTemplateId() == 14401)
                continue;
            if(itemComposition.getNote() == 799)
                continue;

            AsyncBufferedImage image = this.seedVaultViewerPlugin.getItemManager().getImage(item.getId(),item.getQuantity(),item.getQuantity() > 1);

            String itemName = itemComposition.getName();

            JLabel itemLabel = new JLabel();
            image.addTo(itemLabel);
            itemLabel.setOpaque(true);
            //itemLabel.setBorder(new EmptyBorder(3,3,3,3));
            itemLabel.setText(itemName);
            itemLabel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

            this.contentPanel.add(itemLabel,gbc);

            itemLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorScheme.DARK_GRAY_COLOR), new EmptyBorder(5,5,5,5)));
        }

        this.contentPanel.repaint();
        this.contentPanel.revalidate();
        //repaint();
        //revalidate();
    }

    public void rebuild(){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;

        markerView.removeAll();

        boolean empty = constraints.gridy == 0;
        noMarkersPanel.setVisible(empty);
        title.setVisible(!empty);

        markerView.add(noMarkersPanel, constraints);
        constraints.gridy++;

        //this.setLayout(new FlowLayout());
/*
        JLabel firstJPanel = new JLabel();
        firstJPanel.setBackground(Color.BLUE);
        firstJPanel.setBounds(0,0,250,250);
        firstJPanel.setText("test");
        firstJPanel.setIcon();

        this.add(firstJPanel);

        JPanel secondJPanel = new JPanel();
        secondJPanel.setBackground(Color.GREEN);
        secondJPanel.setBounds(250,250,250,250);
*/
        //this.add(secondJPanel);

        repaint();
        revalidate();
    }
}
