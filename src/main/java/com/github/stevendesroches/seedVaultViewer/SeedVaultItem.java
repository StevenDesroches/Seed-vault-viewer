package com.github.stevendesroches.seedVaultViewer;

import lombok.Getter;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.AsyncBufferedImage;

public class SeedVaultItem {
    private int id;
    private int quantity;

    @Getter
    private String name;

    private ItemComposition itemComposition;

    @Getter
    private AsyncBufferedImage image;


    public SeedVaultItem(int id, int quantity){
        this.id = id;
        this.quantity = quantity;
    }

    public SeedVaultItem(net.runelite.api.Item item){
        this(item.getId(),item.getQuantity());
    }

    public SeedVaultItem(int id, int quantity, ItemManager itemManager){
        this(id,quantity);

        this.itemComposition=itemManager.getItemComposition(this.id);
        this.image = itemManager.getImage(this.id,this.quantity,this.quantity>1);
        this.name = itemComposition.getName();
    }

    public SeedVaultItem(net.runelite.api.Item item, ItemManager itemManager){
        this(item.getId(),item.getQuantity());

        this.itemComposition=itemManager.getItemComposition(this.id);
        this.image = itemManager.getImage(this.id,this.quantity,this.quantity>1);
        this.name = itemComposition.getName();
    }

}
