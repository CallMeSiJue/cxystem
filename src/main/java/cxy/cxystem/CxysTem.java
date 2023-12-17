package cxy.cxystem;

import cxy.cxystem.netWork.NetworkHandler;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CxysTem implements ModInitializer {

    public static final String MOD_ID = "cxys_tem";
    private static final Logger log = LoggerFactory.getLogger(CxysTem.class);


    @Override
    public void onInitialize() {
        NetworkHandler.registerC2SPackets();
    }


}
