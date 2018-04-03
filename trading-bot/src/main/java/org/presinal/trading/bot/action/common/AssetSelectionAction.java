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
package org.presinal.trading.bot.action.common;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import org.presinal.market.client.MarketClient;
import org.presinal.market.client.MarketClientException;
import org.presinal.market.client.types.AssetPriceChange;
import org.presinal.trading.bot.action.AbstractBotAction;

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
    
    private MarketClient client;
    
    private String quoteAsset;

    private Set<String> excludedAsset;
    
    public AssetSelectionAction(MarketClient client) {
        super();
        this.client = client;
        excludedAsset = new HashSet<>();
        setupLogger();        
    }
    
    private void setupLogger() {
        
        logger.setLevel(Level.INFO);
        
        try {            
            FileHandler fileHandler = new FileHandler(AssetSelectionAction.class.getSimpleName()+".log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error adding File Handler. "+ex.getMessage(), ex);            
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

        int cantAssetsLimit = 5;
        int minVolumeValue = 1_000;
        
        try {
            
            logger.info("Loading assets....");
            
            List<AssetPriceChange> assets = client.loadAssetsPriceChange();
            
            logger.info("Loading assets....OK");
            logger.info("assets.size = "+ (assets != null? assets.size() : null));
            
            if(assets != null && !assets.isEmpty()) {
                
                logger.info("Applying filtering and sorting to list....");                
                List<AssetPriceChange> assetList = assets.stream()
                        .filter(apc -> {
                            return apc.getAssetPair().toSymbol("").endsWith(quoteAsset);                            
                        })
                        .filter(apc -> {
                            String tmp = apc.getAssetPair().toSymbol("");
                            return !excludedAsset.contains(tmp.substring(0, tmp.indexOf(quoteAsset)));
                        })
                        // Sort by volumn in decending order
                        .sorted((asset1, asset2) -> Double.compare(asset2.getQuoteVolume(), asset1.getQuoteVolume()))
                        
                        //.sorted((asset1, asset2) -> Double.compare(asset2.getVolume(), asset1.getVolume()))
                        .limit(cantAssetsLimit)
                        // Sort by priceChange in acending order
                        .sorted((asset1, asset2) -> Double.compare(asset2.getPriceChangePercent(), asset1.getPriceChangePercent()))
                        .filter(apc -> {
                            return apc.getVolume() >= minVolumeValue;
                        })
                        .collect(Collectors.toList());
                
                logger.info("Applying filtering and sorting to list....OK");                
                
                logger.info("Assets filtered and sorted:");           
                
                logger.info("Notifying the list of assets....");
                
                String outputFormat = "Asset: %s, Price: %s, Price Change: %s, Change Rate: %s, High: %s, Volume: %s , Qute Volume: %s";
                
                getContext().put(KEY, assetList.get(0).getAssetPair());
                notifyListener();
                for(AssetPriceChange asset : assetList) {
                    
                    logger.info(String.format(outputFormat, asset.getAssetPair().toSymbol(""),
                        asset.getAskPrice(),
                        asset.getPriceChange(),
                        asset.getPriceChangePercent(),
                        asset.getHighPrice(),
                        asset.getVolume(),
                        asset.getQuoteVolume()));
                    
                    /*
                    getContext().put(KEY, asset.getAssetPair());
                    notifyListener();
                    
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AssetSelectionAction.class.getName()).log(Level.SEVERE, null, ex);
                    }*/                    
                    
                }
                
                logger.info("Notifying the list of assets....OK");
                
                /*
                Iterator<AssetPriceChange> assetsIterator = assetList.iterator();
                                
                
                if(assetsIterator.hasNext()){
                    AssetPriceChange selectedAsset = assetsIterator.next();
                    
                    logger.info("selectedAsset = "+selectedAsset);
                    
                    getContext().put(KEY, selectedAsset.getAssetPair());
                    notifyListener();
                }*/
            }

        } catch (MarketClientException ex) {
            logger.log(Level.SEVERE, "Error loading assets price changes. "+ex.getMessage(), ex);
        }
        logger.exiting(CLASS_NAME, METHOD);
    }

    @Override
    public void notifySignal() {
        final String METHOD = "notifySignal()";
        logger.entering(CLASS_NAME, METHOD);
        logger.exiting(CLASS_NAME, METHOD);        
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }
    
    public void excludeAsset(String asset){
        excludedAsset.add(asset);
    }
}
