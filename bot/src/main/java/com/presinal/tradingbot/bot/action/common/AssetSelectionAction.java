/*
 * The MIT License
 *
 * Copyright 2018 Miguel Presinal.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.presinal.tradingbot.bot.action.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import com.presinal.tradingbot.market.client.MarketClient;
import com.presinal.tradingbot.market.client.MarketClientException;
import com.presinal.tradingbot.market.client.types.AssetPair;
import com.presinal.tradingbot.market.client.types.AssetPriceChange;
import com.presinal.tradingbot.bot.action.AbstractBotAction;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class AssetSelectionAction extends AbstractBotAction {

    private static final String CLASS_NAME = AssetSelectionAction.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);
    public static final String KEY = CLASS_NAME;
    public static final String DEFAULT_QUOTEASSET = "BTC";
    private static final String ASSET_SYMBOL_SEPARATOR = "";

    private MarketClient client;

    private String quoteAsset;
    private Set<String> excludedAssets;
    private int maxAssetsToSelect = 4;
    private int minAssetVolume = 1_000;

    private Set<String> currentSelectedAssets;
    private boolean signalRecieved = false;

    public AssetSelectionAction(MarketClient client) {
        super();
        this.client = client;
        currentSelectedAssets = new HashSet<>();
        setupLogger();
    }

    private void setupLogger() {

        logger.setLevel(Level.INFO);

        try {
            FileHandler fileHandler = new FileHandler(AssetSelectionAction.class.getSimpleName() + ".log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error adding File Handler. " + ex.getMessage(), ex);
        }
    }

    @Override
    public String getContextKey() {
        return KEY;
    }

    @Override
    public void run() {
        final String METHOD = "run()";
        logger.entering(CLASS_NAME, METHOD);
        logger.info("Executing task");
        
        List<AssetPriceChange> assetList;
        
        while (!isActionEnded()) {

            try {

                logger.info("Loading assets....");

                List<AssetPriceChange> assets = client.loadAssetsPriceChange();

                logger.info("Loading assets....OK");
                logger.info("assets.size = " + (assets != null ? assets.size() : null));

                if (assets != null && !assets.isEmpty()) {                    
                    
                    notifyAssets(selectAssets(assets));
                    
                    // locking the object
                    synchronized (this) {

                        logger.info("Waiting for signal to select the next assets...");

                        while (!signalRecieved) {
                            try {
                                wait();
                            } catch (InterruptedException ex) {
                                logger.log(Level.SEVERE, "InterruptedException. " + ex.getMessage(), ex);
                            }
                        }

                        signalRecieved = false;
                        
                        logger.info("signal's received!!!");
                        Object signalData = getContext().get(getSignalDataProducerKey());
                        logger.info("signalData = "+signalData);
                                                
                        if(signalData != null && signalData instanceof AssetPair ) {
                            logger.info("Removing asset from selection list...");
                            currentSelectedAssets.remove( ((AssetPair)signalData).toSymbol(ASSET_SYMBOL_SEPARATOR));
                            logger.info("Removing asset from selection list...OK");
                        }
                        
                        
                    }

                }

            } catch (MarketClientException ex) {
                logger.log(Level.SEVERE, "Error loading assets price changes. " + ex.getMessage(), ex);
            }

        }// end while
        logger.exiting(CLASS_NAME, METHOD);
    }

    private List<AssetPriceChange> selectAssets(List<AssetPriceChange> assets) {
        logger.info("Applying filtering and sorting to list...");
        List<AssetPriceChange> list = assets.stream()
                .filter(apc -> {
                    return apc.getAssetPair().toSymbol(ASSET_SYMBOL_SEPARATOR).endsWith(quoteAsset);
                })
                .filter(apc -> {
                    String tmp = apc.getAssetPair().toSymbol(ASSET_SYMBOL_SEPARATOR);
                    String asset = tmp.substring(0, tmp.indexOf(quoteAsset));

                    /*
                     * return true only if currentSelectedAssets does not contains tmp 
                     * and excludedAssets does not contains tmp asset
                     */
                    return !currentSelectedAssets.contains(tmp) && !excludedAssets.contains(asset);
                })
                // Sort by volumn in decending order
                .sorted((asset1, asset2) -> Double.compare(asset2.getQuoteVolume(), asset1.getQuoteVolume()))
                
                .limit(Math.abs(maxAssetsToSelect-currentSelectedAssets.size()))
                // Sort by priceChange in acending order
                .sorted((asset1, asset2) -> Double.compare(asset2.getPriceChangePercent(), asset1.getPriceChangePercent()))
                /*.filter(apc -> {
                 return apc.getVolume() >= minVolumeValue;
                 })*/
                .collect(Collectors.toList());
        logger.info("Applying filtering and sorting to list...OK");
        return list;
    }

    private void notifyAssets(List<AssetPriceChange> assets) {
        logger.info("Notifying the list of assets...");
        
        String outputFormat = "Asset: %s, Price: %s, Price Change: %s, Change Rate: %s, High: %s, Volume: %s , Qute Volume: %s";
        long sleepTime = 5 * 1000;
        for (AssetPriceChange asset : assets) {

            logger.info(String.format(outputFormat, asset.getAssetPair().toSymbol(ASSET_SYMBOL_SEPARATOR),
                    asset.getAskPrice(),
                    asset.getPriceChange(),
                    asset.getPriceChangePercent(),
                    asset.getHighPrice(),
                    asset.getVolume(),
                    asset.getQuoteVolume()));

            currentSelectedAssets.add(asset.getAssetPair().toSymbol(ASSET_SYMBOL_SEPARATOR));

            getContext().put(KEY, asset.getAssetPair());
            notifyListener();

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(AssetSelectionAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        logger.info("Notifying the list of assets...OK");
    }

    @Override
    public void notifySignal() {
        final String METHOD = "notifySignal()";
        logger.entering(CLASS_NAME, METHOD);
        logger.info("Signal notificacion received!!!");
        synchronized (this) {
            notifyAll();
            signalRecieved = true;
        }
        logger.exiting(CLASS_NAME, METHOD);
    }

    public void excludeAsset(String asset) {
        getExcludedAssets().add(asset);
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public Set<String> getExcludedAssets() {

        if (excludedAssets == null) {
            excludedAssets = new HashSet<>();
        }

        return excludedAssets;
    }

    public void setExcludedAssets(Set<String> excludedAssets) {
        this.excludedAssets = excludedAssets;
    }

    public int getMaxAssetsToSelect() {
        return maxAssetsToSelect;
    }

    public void setMaxAssetsToSelect(int maxAssetsToSelect) {
        this.maxAssetsToSelect = maxAssetsToSelect;
    }

    public int getMinAssetVolume() {
        return minAssetVolume;
    }

    public void setMinAssetVolume(int minAssetVolume) {
        this.minAssetVolume = minAssetVolume;
    }

    public MarketClient getClient() {
        return client;
    }
}
