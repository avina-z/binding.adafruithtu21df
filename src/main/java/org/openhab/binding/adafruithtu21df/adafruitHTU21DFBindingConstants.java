/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.adafruithtu21df;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link adafruitHTU21DFBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Avina - Initial contribution
 */
public class adafruitHTU21DFBindingConstants {

    public static final String BINDING_ID = "adafruithtu21df";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_ADAFRUITHTU21DF = new ThingTypeUID(BINDING_ID, "adafruithtu21df");

    // List of all Channel ids
    public final static String CHANNEL_TEMPERATURE = "temperature";
    public final static String CHANNEL_HUMIDITY = "humidity";

}
