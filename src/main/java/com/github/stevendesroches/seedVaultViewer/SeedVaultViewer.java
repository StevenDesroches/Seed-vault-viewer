package com.github.stevendesroches.seedVaultViewer;

import com.google.gson.Gson;
import com.google.inject.Provides;
import javax.inject.Inject;

import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.VarClientStrChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

@Slf4j
@PluginDescriptor(
	name = "Seed Vault Viewer"
)
public class SeedVaultViewer extends Plugin
{
	private static final String ICON_FILE = "/panel_icon.png";

	@Inject
	private Client client;

	@Inject
	private SeedVaultViewerConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ConfigManager configManager;

	private SeedVaultViewerPluginPanel pluginPanel;
	private NavigationButton navigationButton;
	@Inject
	private Gson gson;
	private static final String CONFIG_GROUP = "seedVaultViewer";
	private static final String CONFIG_KEY = "content";

	//private ItemManager itemManager;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
		this.pluginPanel = new SeedVaultViewerPluginPanel(this);
		this.pluginPanel.rebuild();

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), ICON_FILE);

		navigationButton = NavigationButton.builder()
				.tooltip("Seed Vault Viewer")
				.icon(icon)
				.priority(5)
				.panel(pluginPanel)
				.build();

		clientToolbar.addNavigation(navigationButton);




		//this.itemManager = new ItemManager(this.client);

	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
		this.pluginPanel = null;


	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "SeedVaultViewer says " + config.greeting(), null);

			String json = configManager.getConfiguration(CONFIG_GROUP,CONFIG_KEY);
			//log.debug(json);
			if(!Strings.isNullOrEmpty(json)){
				Item[] items = gson.fromJson(json, Item[].class);
				this.pluginPanel.addItem(items);
			}
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event){
		if(event.getContainerId() != InventoryID.SEED_VAULT.getId())
			return;

		ItemContainer seedVault = event.getItemContainer();

		Item[] items = seedVault.getItems();
		String json = this.gson.toJson(items);
		//log.debug(json);
		this.pluginPanel.addItem(items);

		configManager.setConfiguration(CONFIG_GROUP,CONFIG_KEY,json);
		//for (Item item:items) {
			//log.debug("item seed vault : " + item.getId());
			//this.pluginPanel.addItem(item);
		//}


	}



	@Subscribe
	public void onVarClientStrChanged(VarClientStrChanged event){
		if(event.getIndex() != VarClientStr.INPUT_TEXT)
			this.pluginPanel.search("");
		String text = client.getVarcStrValue(VarClientStr.INPUT_TEXT);
		this.pluginPanel.search(text);
		//this.pluginPanel.searchField.setText(text);

	}



	@Provides
	SeedVaultViewerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SeedVaultViewerConfig.class);
	}

	public ItemManager getItemManager() {
		return itemManager;
	}
}
