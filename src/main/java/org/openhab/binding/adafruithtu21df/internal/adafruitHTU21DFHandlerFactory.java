/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.adafruithtu21df.internal;

import static org.openhab.binding.adafruithtu21df.adafruitHTU21DFBindingConstants.THING_TYPE_ADAFRUITHTU21DF;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.adafruithtu21df.handler.adafruitHTU21DFHandler;

/**
 * The {@link adafruitHTU21DFHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Avina - Initial contribution
 */
public class adafruitHTU21DFHandlerFactory extends BaseThingHandlerFactory {

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .singleton(THING_TYPE_ADAFRUITHTU21DF);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_ADAFRUITHTU21DF)) {
            return new adafruitHTU21DFHandler(thing);
        }

        return null;
    }
}
