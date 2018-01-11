/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.orvibo.internal.discovery;

import java.net.SocketException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.github.tavalin.orvibo.devices.DeviceType;
import com.github.tavalin.orvibo.devices.OrviboDevice;
import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.orvibo.OrviboBindingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tavalin.orvibo.OrviboClient;
import com.github.tavalin.orvibo.OrviboClient.OrviboDiscoveryListener;

/**
 * The {@link SocketDiscoveryService} class defines a service used
 * to discover S20 sockets on the local netowork.
 *
 * @author Daniel Walters - Initial contribution
 */
public class SocketDiscoveryService extends AbstractDiscoveryService implements OrviboDiscoveryListener {

    private final Logger logger = LoggerFactory.getLogger(SocketDiscoveryService.class);
    private static final int SEARCH_TIME = 60;
    private OrviboClient orviboClient;

    public SocketDiscoveryService() {
        super(getSupportedThingTypeUIDs(), SEARCH_TIME);
    }

    private static Set<ThingTypeUID> getSupportedThingTypeUIDs() {
        return Collections.singleton(OrviboBindingConstants.THING_TYPE_S20);
    }

    @Override
    protected void activate(Map<String, Object> configProperties) {
        try {
            orviboClient = OrviboClient.getInstance();
            super.activate(configProperties);
        } catch (SocketException ex) {
            logger.error("Error occurred while activating S20 discovery service: {}", ex.getMessage(), ex);
        }
    }

    @Override
    protected void startScan() {
        if (orviboClient != null) {
            logger.debug("starting manual scan");
            orviboClient.addDeviceDiscoveryListener(this);
            orviboClient.globalDiscovery();
//            for (final Socket socket : orviboClient.getAllSockets().values()) {
//                doThingDiscovered(socket);
//            }
        } else {
            logger.debug("Client not initialised");
        }
    }

    @Override
    protected void startBackgroundDiscovery() {
        if (orviboClient != null) {
            logger.debug("starting automatic background scan");
            orviboClient.addDeviceDiscoveryListener(this);
            orviboClient.globalDiscovery();
        } else {
            logger.debug("Client not initialised");
        }
    }

    @Override
    protected void stopBackgroundDiscovery() {
        orviboClient.removeDeviceDiscoveryListener(this);
    }

    private DiscoveryResult createDiscoveryResult(OrviboDevice device) {
        ThingUID thingUID = getUID(device);
        String label = device.getLabel();
        if (StringUtils.isBlank(label)) {
            label = "UNKNOWN_DEVICE_TYPE";
        }
        return DiscoveryResultBuilder.create(thingUID).withLabel(label)
                .withProperty(OrviboBindingConstants.CONFIG_PROPERTY_DEVICE_ID, device.getDeviceId()).build();
    }

    private ThingUID getUID(OrviboDevice device) {

        ThingUID thingUID = null;

        if(device.getDeviceType()== DeviceType.ALLONE) {
            thingUID = new ThingUID(OrviboBindingConstants.THING_TYPE_ALLONE, device.getDeviceId());
        } else if (device.getDeviceType() == DeviceType.SOCKET) {
            thingUID = new ThingUID(OrviboBindingConstants.THING_TYPE_S20, device.getDeviceId());
        }
        return thingUID;
    }

    private void doThingDiscovered(OrviboDevice device) {
        DiscoveryResult discoveryResult = createDiscoveryResult(device);
        thingDiscovered(discoveryResult);
    }

    @Override
    public void deviceDiscovered(OrviboDevice orviboDevice) {
        doThingDiscovered(orviboDevice);
    }
}
