/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.adafruithtu21df.handler;

import static org.openhab.binding.adafruithtu21df.adafruitHTU21DFBindingConstants.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.adafruithtu21df.internal.config.HTU21DFThingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link adafruitHTU21DFHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Avina - Initial contribution
 */
public class adafruitHTU21DFHandler extends BaseThingHandler {

    // Define Hummidity and Temperature sensor object
    HTU21DDevice htu;
    private float temperature;
    private float humidity;
    private Logger logger = LoggerFactory.getLogger(adafruitHTU21DFHandler.class);
    protected HTU21DFThingConfig thingConfig;
    private BigDecimal refresh;

    public adafruitHTU21DFHandler(Thing thing) {
        super(thing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        String thingUid = getThing().getUID().toString();
        thingConfig = getConfigAs(HTU21DFThingConfig.class);
        thingConfig.setThingUid(thingUid);
        boolean validConfig = true;

        if (StringUtils.trimToNull(thingConfig.getLocation()) == null) {
            logger.error("HTU21DF parameter location is mandatory and must be configured, disabling thing '{}'",
                    thingUid);
            validConfig = false;
        } else {
            thingConfig.getLocation();
        }

        if (thingConfig.getInterval() == null || thingConfig.getInterval() < 1 || thingConfig.getInterval() > 86400) {
            logger.error("Astro parameter interval must be in the range of 1-86400, disabling thing '{}'", thingUid);
            validConfig = false;
        }

        // Initialize Hummidity and Temperature sensor
        try {
            htu = new HTU21DDevice();
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }

        // Received 999, means error from device
        if (htu.readTemperature() == 999) {
            validConfig = false;
        }

        if (validConfig) {
            logger.debug(thingConfig.toString());

            logger.debug("Initializing Adafruit HTU21DF handler.");
            super.initialize();

            Configuration config = getThing().getConfiguration();

            try {
                refresh = (BigDecimal) config.get("interval");
            } catch (Exception e) {
                logger.debug("Cannot set refresh parameter.", e);
            }

            if (refresh == null) {
                // let's go for the default
                refresh = new BigDecimal(60);
            }

            startAutomaticRefresh();
            updateStatus(ThingStatus.ONLINE);

        } else {
            updateStatus(ThingStatus.OFFLINE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        htu.close();
    }

    private void startAutomaticRefresh() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    boolean success = updateSensorData();
                    if (success) {
                        updateState(new ChannelUID(getThing().getUID(), CHANNEL_TEMPERATURE), getTemperature());
                        updateState(new ChannelUID(getThing().getUID(), CHANNEL_HUMIDITY), getHumidity());
                    }
                } catch (Exception e) {
                    logger.debug("Exception occurred during execution: {}", e.getMessage(), e);
                }
            }
        };

        final ScheduledFuture<?> refreshJob = scheduler.scheduleAtFixedRate(runnable, 0, refresh.intValue(),
                TimeUnit.SECONDS);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {

            boolean success = updateSensorData();
            if (success) {
                switch (channelUID.getId()) {
                    case CHANNEL_TEMPERATURE:
                        updateState(channelUID, getTemperature());
                        break;
                    case CHANNEL_HUMIDITY:
                        updateState(channelUID, getHumidity());
                    default:
                        logger.debug("Command received for an unknown channel: {}", channelUID.getId());
                        break;
                }
            }
        }
    }

    private State getHumidity() {
        if (humidity != 999) {
            return new DecimalType(humidity);
        }
        return UnDefType.UNDEF;
    }

    private State getTemperature() {
        if (temperature != 999) {
            return new DecimalType(temperature);
        }
        return UnDefType.UNDEF;
    }

    private synchronized boolean updateSensorData() {
        temperature = htu.readTemperature();
        humidity = htu.readHumidity();
        if (temperature != 999 && humidity != 999) {
            updateStatus(ThingStatus.ONLINE);
            return true;
        }
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                "Invalid reading from HTU21DF sensor");
        return false;
    }

}
