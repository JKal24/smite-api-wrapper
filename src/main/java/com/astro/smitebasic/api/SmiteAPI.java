package com.astro.smitebasic.api;

import com.astro.smitebasic.db.session.SessionController;
import com.astro.smitebasic.info.ConnectionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class SmiteAPI implements CommandLineRunner {

    @Autowired
    private SessionController sessionController;

    @Value("${smite.api}")
    private String apiUri;

    @Value("${smite.dev-id}")
    private String devID;

    @Value("${smite.auth-key}")
    private String authKey;

    @Value("${smite.acc}")
    private String mainAcc;

    public String makeTimeStamp() {
        Instant instant = Instant.now();
        DateTimeFormatter formatterUTC = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.of("UTC"));
        return formatterUTC.format(instant);
    }

    public String makeSignature(String time) throws NoSuchAlgorithmException {
        String sig = devID + "createsession" + "D328B92A2C9A44FCB01854A300ACE310" + time;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((sig).getBytes());

        byte[] signature = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte x : signature) {
            sb.append(String.format("%02X", x).toLowerCase());
        }

        return sb.toString();
    }

    public String makeRequestUri(String... components) {
        return String.join("/", components);
    }

    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public ConnectionInfo runAPI(RestTemplate template) throws Exception {
        String timeStamp = makeTimeStamp();
        String createSession = makeRequestUri(apiUri, "createsessionJson", devID, makeSignature(timeStamp), timeStamp);
        ConnectionInfo currentInfo = template.getForObject(createSession, ConnectionInfo.class);
        return currentInfo;
    }

    @Override
    public void run(String... args) throws Exception {
        Iterable<ConnectionInfo> connectionInfos = sessionController.getConnections();
        String testTimeStamp = makeTimeStamp();
        for (ConnectionInfo connection : connectionInfos) {
            System.out.println(connection.getTimestamp());
            System.out.println(testTimeStamp);

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
            Date test = format.parse(testTimeStamp);
            Date old = format.parse(connection.getTimestamp());
            System.out.println(Math.abs(test.getTime() - old.getTime()));

        }
//        ConnectionInfo info = runAPI(restTemplate(new RestTemplateBuilder()));
//        System.out.println(info);
//        sessionController.addConnection(info);
    }
}
