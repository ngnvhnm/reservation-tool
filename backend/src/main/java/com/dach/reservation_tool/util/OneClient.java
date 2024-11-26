package com.dach.reservation_tool.util;

import com.dach.reservation_tool.conference.CONF_TYPE;
import com.dach.reservation_tool.conference.dto.ConferenceCreateDto;
import com.dach.reservation_tool.conference.dto.ConferenceUpdateDto;
import com.dach.reservation_tool.parkinglot.PARKINGLOT_NUMBER;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotCreateDto;
import com.dach.reservation_tool.parkinglot.dto.ParkinglotUpdateDto;
import com.github.caldav4j.methods.HttpDeleteMethod;
import com.github.caldav4j.methods.HttpPutMethod;
import com.github.caldav4j.model.request.CalendarRequest;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.Uid;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OneClient {

    private static final String CALDAV_BASE_URL = "https://caldav.one.com";

    @Value("${credentials.one.conference.og3.username}")
    private String conferenceOg3Username;
    @Value("${credentials.one.conference.og3.password}")
    private String conferenceOg3Password;
    @Value("${credentials.one.conference.og3.calPath}")
    private String conferenceOg3calPath;

    @Value("${credentials.one.parking.p12.username}")
    private String parking12Username;
    @Value("${credentials.one.parking.p12.password}")
    private String parking12Password;
    @Value("${credentials.one.parking.p12.calPath}")
    private String parking12CalPath;

    @Value("${credentials.one.parking.p13.username}")
    private String parking13Username;
    @Value("${credentials.one.parking.p13.password}")
    private String parking13Password;
    @Value("${credentials.one.parking.p13.calPath}")
    private String parking13CalPath;

    @Value("${credentials.one.parking.p14.username}")
    private String parking14Username;
    @Value("${credentials.one.parking.p14.password}")
    private String parking14Password;
    @Value("${credentials.one.parking.p14.calPath}")
    private String parking14CalPath;

    @Value("${credentials.one.parking.p15.username}")
    private String parking15Username;
    @Value("${credentials.one.parking.p15.password}")
    private String parking15Password;
    @Value("${credentials.one.parking.p15.calPath}")
    private String parking15CalPath;

    @Value("${credentials.one.parking.p16.username}")
    private String parking16Username;
    @Value("${credentials.one.parking.p16.password}")
    private String parking16Password;
    @Value("${credentials.one.parking.p16.calPath}")
    private String parking16CalPath;

    @Value("${credentials.one.parking.p37.username}")
    private String parking37Username;
    @Value("${credentials.one.parking.p37.password}")
    private String parking37Password;
    @Value("${credentials.one.parking.p37.calPath}")
    private String parking37CalPath;


    private Map<String, String> getCredentialsForConference(CONF_TYPE type) {
        if (type == CONF_TYPE.CONFERENCE_OG3) {
            var credMap = new HashMap<String, String>();
            credMap.put("username", conferenceOg3Username);
            credMap.put("password", conferenceOg3Password);
            credMap.put("calPath", conferenceOg3calPath);
            return credMap;
        }
        else return null;
    }

    private Map<String, String> getCredentialsForParking(PARKINGLOT_NUMBER type) {
        var credMap = new HashMap<String, String>();
        switch (type) {
            case PARKING_LOT12 -> {
                credMap.put("username", parking12Username);
                credMap.put("password", parking12Password);
                credMap.put("calPath", parking12CalPath);
            }
            case PARKING_LOT13 -> {
                credMap.put("username", parking13Username);
                credMap.put("password", parking13Password);
                credMap.put("calPath", parking13CalPath);
            }
            case PARKING_LOT14 -> {
                credMap.put("username", parking14Username);
                credMap.put("password", parking14Password);
                credMap.put("calPath", parking14CalPath);
            }
            case PARKING_LOT15 -> {
                credMap.put("username", parking15Username);
                credMap.put("password", parking15Password);
                credMap.put("calPath", parking15CalPath);
            }
            case PARKING_LOT16 -> {
                credMap.put("username", parking16Username);
                credMap.put("password", parking16Password);
                credMap.put("calPath", parking16CalPath);
            }
            case PARKING_LOT37 -> {
                credMap.put("username", parking37Username);
                credMap.put("password", parking37Password);
                credMap.put("calPath", parking37CalPath);
            }
            default -> {
                return null;
            }
        }

        return credMap;
    }

    public boolean createEventForConference(ConferenceCreateDto createDto, Uid uid, List<String> attendeeList, String title) {

        var credentials = getCredentialsForConference(createDto.conferenceType());
        if (credentials == null) {
            return false;
        }

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(URI.create(CALDAV_BASE_URL).getHost(), 443),
                new UsernamePasswordCredentials(credentials.get("username"), credentials.get("password"))
        );

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build()) {

            Date startDateConverted = Date.from(createDto.startTime().atZone(ZoneId.systemDefault()).toInstant());
            Date endDateConverted = Date.from(createDto.endTime().atZone(ZoneId.systemDefault()).toInstant());

            DateTime startDateTime = new DateTime(startDateConverted);
            DateTime endDateTime = new DateTime(endDateConverted);

            VEvent event = new VEvent(new DateTime(startDateTime), new DateTime(endDateTime), title);

            event.getProperties().add(uid);

            Organizer organizer = new Organizer(URI.create("mailto:" + credentials.get("username")));
            organizer.getParameters().add(PartStat.ACCEPTED);
            organizer.getParameters().add(new Cn(credentials.get("username")));
            event.getProperties().add(organizer);

            Attendee booker = new Attendee(URI.create("mailto:" + createDto.bookerEmail()));
            booker.getParameters().add(new Cn(createDto.bookerEmail()));
            booker.getParameters().add(PartStat.ACCEPTED);
            event.getProperties().add(booker);

            for (String attendeeMail : attendeeList) {
                Attendee attendee = new Attendee(URI.create("mailto:"+attendeeMail));
                attendee.getParameters().add(new Cn(attendeeMail));
                attendee.getParameters().add(PartStat.ACCEPTED);
                event.getProperties().add(attendee);
            }

            CalendarRequest calendarRequest = new CalendarRequest();
            calendarRequest.setCalendar(event);

            CalendarOutputter calendarOutputter = new CalendarOutputter();
            calendarOutputter.setValidating(true);

            String eventUrl = CALDAV_BASE_URL + credentials.get("calPath") + uid.getValue() + ".ics";

            HttpPutMethod putMethod = new HttpPutMethod(eventUrl, calendarRequest, calendarOutputter);

            try {
                HttpResponse response = httpClient.execute(putMethod);

                if (putMethod.succeeded(response)) {
                    System.out.println("Event created");
                    return true;

                } else {
                    System.out.println("Failed to create event.");
                    System.out.println("Status Code: " + response.getStatusLine().getStatusCode());
                    System.out.println("Status Message: " + response.getStatusLine().getReasonPhrase());
                }
            } catch (IOException e) {
                System.err.println("Error fetching events: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("Client Setup Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteEventForConference(Uid uid, CONF_TYPE type) {
        var credentials = getCredentialsForConference(type);
        if (credentials == null) {
            return false;
        }

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(URI.create(CALDAV_BASE_URL).getHost(), 443),
                new UsernamePasswordCredentials(credentials.get("username"), credentials.get("password"))
        );

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build()) {

            var deleteUrl = CALDAV_BASE_URL + credentials.get("calPath") + uid.getValue() + ".ics";

            HttpDeleteMethod deleteMethod = new HttpDeleteMethod(deleteUrl);

            try {
                HttpResponse response = httpClient.execute(deleteMethod);

                if (deleteMethod.succeeded(response)) {
                    System.out.println("Event deleted");
                    return true;
                }
                else {
                    System.out.println("Failed to create event.");
                    System.out.println("Status Code: " + response.getStatusLine().getStatusCode());
                    System.out.println("Status Message: " + response.getStatusLine().getReasonPhrase());
                }
            } catch (IOException e) {
                System.err.println("Error fetching events: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Client Setup Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean editEventForConference(ConferenceUpdateDto updateDto, List<String> attendeeList ,CONF_TYPE type, Uid uid, String bookerEmail, String title) {
        var credentials = getCredentialsForConference(type);
        if (credentials == null) {
            return false;
        }

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(URI.create(CALDAV_BASE_URL).getHost(), 443),
                new UsernamePasswordCredentials(credentials.get("username"), credentials.get("password"))
        );

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build()){

            Date startDateConverted = Date.from(updateDto.startTime().atZone(ZoneId.systemDefault()).toInstant());
            Date endDateConverted = Date.from(updateDto.endTime().atZone(ZoneId.systemDefault()).toInstant());

            DateTime startDateTime = new DateTime(startDateConverted);
            DateTime endDateTime = new DateTime(endDateConverted);

            VEvent event = new VEvent(new DateTime(startDateTime), new DateTime(endDateTime), title);

            event.getProperties().add(uid);

            if (attendeeList != null) {
                Organizer organizer = new Organizer(URI.create("mailto:" + credentials.get("username")));
                organizer.getParameters().add(PartStat.ACCEPTED);
                organizer.getParameters().add(new Cn(credentials.get("username")));
                event.getProperties().add(organizer);

                Attendee booker = new Attendee(URI.create("mailto:" + bookerEmail));
                booker.getParameters().add(new Cn(bookerEmail));
                booker.getParameters().add(PartStat.ACCEPTED);
                event.getProperties().add(booker);

                for (String attendeeMail : attendeeList) {
                    Attendee attendee = new Attendee(URI.create("mailto:"+attendeeMail));
                    attendee.getParameters().add(new Cn(attendeeMail));
                    attendee.getParameters().add(PartStat.ACCEPTED);
                    event.getProperties().add(attendee);
                }
            }



            CalendarRequest calendarRequest = new CalendarRequest();
            calendarRequest.setCalendar(event);

            CalendarOutputter calendarOutputter = new CalendarOutputter();
            calendarOutputter.setValidating(true);

            String eventUrl = CALDAV_BASE_URL + credentials.get("calPath") + uid.getValue() + ".ics";

            HttpPutMethod putMethod = new HttpPutMethod(eventUrl, calendarRequest, calendarOutputter);

            try {
                HttpResponse response = httpClient.execute(putMethod);

                if (putMethod.succeeded(response)) {
                    System.out.println("Event edited");
                    return true;
                } else {
                    System.out.println("Failed to edit event.");
                    System.out.println("Status Code: " + response.getStatusLine().getStatusCode());
                    System.out.println("Status Message: " + response.getStatusLine().getReasonPhrase());
                }
            } catch (IOException e) {
                System.err.println("Error fetching events: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Client Setup Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean createEventForParking(ParkinglotCreateDto createDto, Uid uid, String title) {
        var credentials = getCredentialsForParking(createDto.parkinglotNumber());
        if (credentials == null) {
            return false;
        }

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(URI.create(CALDAV_BASE_URL).getHost(), 443),
                new UsernamePasswordCredentials(credentials.get("username"), credentials.get("password"))
        );

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build()) {

            Date startDateConverted = Date.from(createDto.startTime().atZone(ZoneId.systemDefault()).toInstant());
            Date endDateConverted = Date.from(createDto.endTime().atZone(ZoneId.systemDefault()).toInstant());

            DateTime startDateTime = new DateTime(startDateConverted);
            DateTime endDateTime = new DateTime(endDateConverted);

            VEvent event = new VEvent(new DateTime(startDateTime), new DateTime(endDateTime), title);
            event.getProperties().add(uid);

            Organizer organizer = new Organizer(URI.create("mailto:" + credentials.get("username")));
            organizer.getParameters().add(PartStat.ACCEPTED);
            organizer.getParameters().add(new Cn(credentials.get("username")));
            event.getProperties().add(organizer);

            Attendee parkBooker = new Attendee(URI.create("mailto:" + createDto.bookerEmail()));
            parkBooker.getParameters().add(new Cn(createDto.bookerEmail()));
            parkBooker.getParameters().add(PartStat.ACCEPTED);
            event.getProperties().add(parkBooker);

            CalendarRequest calendarRequest = new CalendarRequest();
            calendarRequest.setCalendar(event);

            CalendarOutputter calendarOutputter = new CalendarOutputter();
            calendarOutputter.setValidating(true);

            String eventUrl = CALDAV_BASE_URL + credentials.get("calPath") + uid.getValue() + ".ics";

            HttpPutMethod putMethod = new HttpPutMethod(eventUrl, calendarRequest, calendarOutputter);

            try {
                HttpResponse response = httpClient.execute(putMethod);

                if (putMethod.succeeded(response)) {
                    System.out.println("Event created");
                    return true;
                } else {
                    System.out.println("Failed to create event.");
                    System.out.println("Status Code: " + response.getStatusLine().getStatusCode());
                    System.out.println("Status Message: " + response.getStatusLine().getReasonPhrase());
                }
            } catch (IOException e) {
                System.err.println("Error fetching events: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.err.println("Client Setup Error: " + e.getMessage());
        }

        return false;
    }

    public boolean deleteEventForParking(Uid uid, PARKINGLOT_NUMBER number) {
        var credentials = getCredentialsForParking(number);
        if (credentials == null) {
            return false;
        }

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(URI.create(CALDAV_BASE_URL).getHost(), 443),
                new UsernamePasswordCredentials(credentials.get("username"), credentials.get("password"))
        );

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build()) {

            var deleteUrl = CALDAV_BASE_URL + credentials.get("calPath") + uid.getValue() + ".ics";

            HttpDeleteMethod deleteMethod = new HttpDeleteMethod(deleteUrl);

            try {
                HttpResponse response = httpClient.execute(deleteMethod);

                if (deleteMethod.succeeded(response)) {
                    System.out.println("Event deleted");
                    return true;
                }
                else {
                    System.out.println("Failed to create event.");
                    System.out.println("Status Code: " + response.getStatusLine().getStatusCode());
                    System.out.println("Status Message: " + response.getStatusLine().getReasonPhrase());
                }
            } catch (IOException e) {
                System.err.println("Error fetching events: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Client Setup Error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean editEventForParking(ParkinglotUpdateDto updateDto, PARKINGLOT_NUMBER number, Uid uid, String title) {
        var credentials = getCredentialsForParking(number);
        if (credentials == null) {
            return false;
        }
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(URI.create(CALDAV_BASE_URL).getHost(), 443),
                new UsernamePasswordCredentials(credentials.get("username"), credentials.get("password"))
        );

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build()) {

            Date startDateConverted = Date.from(updateDto.startTime().atZone(ZoneId.systemDefault()).toInstant());
            Date endDateConverted = Date.from(updateDto.endTime().atZone(ZoneId.systemDefault()).toInstant());

            DateTime startDateTime = new DateTime(startDateConverted);
            DateTime endDateTime = new DateTime(endDateConverted);

            VEvent event = new VEvent(new DateTime(startDateTime), new DateTime(endDateTime), title);

            event.getProperties().add(uid);

            CalendarRequest calendarRequest = new CalendarRequest();
            calendarRequest.setCalendar(event);

            CalendarOutputter calendarOutputter = new CalendarOutputter();
            calendarOutputter.setValidating(true);

            String eventUrl = CALDAV_BASE_URL + credentials.get("calPath") + uid.getValue() + ".ics";

            HttpPutMethod putMethod = new HttpPutMethod(eventUrl, calendarRequest, calendarOutputter);

            try {
                HttpResponse response = httpClient.execute(putMethod);

                if (putMethod.succeeded(response)) {
                    System.out.println("Event created");
                    return true;
                } else {
                    System.out.println("Failed to create event.");
                    System.out.println("Status Code: " + response.getStatusLine().getStatusCode());
                    System.out.println("Status Message: " + response.getStatusLine().getReasonPhrase());
                }
            } catch (IOException e) {
                System.err.println("Error fetching events: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.err.println("Client Setup Error: " + e.getMessage());
        }
        return false;
    }



}
