package com.github.stevendesroches.seedVaultViewer;

import lombok.Getter;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SeedVaultItemPanel extends JPanel {

    @Getter
    private SeedVaultItem seedVaultItem;

    private JLabel label;

    public SeedVaultItemPanel(SeedVaultItem seedVaultItem){
        this.seedVaultItem = seedVaultItem;
        this.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.setBorder(BorderFactory.createLineBorder(ColorScheme.DARK_GRAY_COLOR,1));
        this.setLayout(new BorderLayout());

        this.label = new JLabel();
        this.label.setText(this.seedVaultItem.getName());
        this.seedVaultItem.getImage().addTo(this.label);

        this.add(this.label, BorderLayout.WEST);
    }

}
