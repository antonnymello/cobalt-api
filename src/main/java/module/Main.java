package module;

import java.nio.file.Paths;

import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.TextPreviewSetting;
import it.auties.whatsapp.api.Whatsapp;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing WhatsApp API...");

        Whatsapp.webBuilder()
                .newConnection()
                .name("WhatsappAPI")
                .autodetectListeners(true)
                .textPreviewSetting(TextPreviewSetting.ENABLED)
                .checkPatchMacks(true)
                .unregistered(QrHandler.toFile(Paths.get("qr.png"), QrHandler.ToFileConsumer.toTerminal()))
                .connectAwaitingLogout().join();
    }
}