/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.adafruithtu21df.internal.config;

import org.apache.commons.lang.StringUtils;

/**
 * Thing configuration from openHab.
 *
 * @author Alberto Avina - Initial contribution
 */
public class HTU21DFThingConfig {
    private String location;
    private Integer interval;
    private String thingUid;

    private Double toDouble(String value) {
        try {
            return Double.parseDouble(StringUtils.trimToNull(value));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Returns the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the interval.
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * Returns the thing uid as string.
     */
    public String getThingUid() {
        return thingUid;
    }

    /**
     * Sets the thing uid as string.
     */
    public void setThingUid(String thingUid) {
        this.thingUid = thingUid;
    }
}