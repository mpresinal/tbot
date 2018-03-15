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


import com.google.common.base.Objects;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public AssetSelectionAction(MarketClient client) {
        super();
        this.client = client;
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

        int candidateAssetsLimit = 10;
        int minVolumeValue = 1_000;
        
        try {
            
            logger.info("Loading assets....");
            
            List<AssetPriceChange> assets = client.loadAssetsPriceChange();
            
            logger.info("Loading assets....OK");
            logger.info("assets.size = "+ (assets != null? assets.size() : null));
            
            if(assets != null && !assets.isEmpty()) {
                
                logger.info("Applying filtering and sorting to list....");
                Iterator<AssetPriceChange> assetsIterator = assets.stream()
                    .filter(apc -> { 
                        return Objects.equal(quoteAsset, apc.getAssetPair().getQuoteAsset());
                    })
                    // Sort by priceChange in decending order
                    .sorted((asset1, asset2) -> Double.compare(asset2.getPriceChange(), asset1.getPriceChange()))
                    .limit(candidateAssetsLimit)
                    .filter(apc -> {
                        return apc.getVolume() >= minVolumeValue;
                    })
                    // Sort by volumn in acending order
                    .sorted((asset1, asset2) -> Double.compare(asset1.getVolume(), asset2.getVolume()))
                    .iterator();
                
                logger.info("Applying filtering and sorting to list....OK");                
                
                if(assetsIterator.hasNext()){
                    AssetPriceChange selectedAsset = assetsIterator.next();
                    
                    logger.info("selectedAsset = "+selectedAsset);
                    
                    getContext().put(KEY, selectedAsset.getAssetPair());
                    notifyListener();
                }
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
}
