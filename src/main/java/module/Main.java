package module;

import java.nio.file.Paths;

import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.contact.ContactJid;
import it.auties.whatsapp.model.contact.ContactJidProvider;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing WhatsApp API...");

        Whatsapp app = Whatsapp.webBuilder()
                .newConnection()
                .name("WhatsappAPI")
                .autodetectListeners(true)
                .acknowledgeMessages(false)
                .checkPatchMacks(true)
                .unregistered(QrHandler.toFile(Paths.get("qr.png"), QrHandler.ToFileConsumer.toTerminal()))
                .connect().join();

        try {
            System.out.println("WhatsApp API initialized!");
            ContactJidProvider contactJidProvider = ContactJid.of("+558599685687").toJid();
            System.out.println("Waiting to send message to " + contactJidProvider);
            Thread.sleep(30000);
            System.out.println("WhatsApp API READY TO SEND MESSAGE!");
            app.sendMessage(contactJidProvider, "Hello World!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}