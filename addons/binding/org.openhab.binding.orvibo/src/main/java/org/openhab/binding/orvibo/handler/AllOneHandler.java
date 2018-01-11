package org.openhab.binding.orvibo.handler;

import com.github.tavalin.orvibo.devices.AllOne;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link AllOneHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Grzegorz Szostak - Initial contribution
 */

public class AllOneHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(AllOneHandler.class);
    private AllOne allOne;
    public AllOneHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

    }
}
