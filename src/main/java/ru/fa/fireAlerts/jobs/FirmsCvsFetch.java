package ru.fa.fireAlerts.jobs;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.fa.fireAlerts.api.model.Fire;
import ru.fa.fireAlerts.api.repository.FireRepository;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
@AllArgsConstructor
public class FirmsCvsFetch {

    private final FireRepository fireRepository;

    //FIRMS API LINK
    private static final String FIRMS_URL = "https://firms.modaps.eosdis.nasa.gov/api/";
    private static final String COUNTRY = "country";

    /** Russian AREA request
     *  By all rights this deserves to be refactored.
     *  TODO: Refactor this.
     **/
    //@Scheduled(cron = "@hourly")
    @Scheduled(initialDelay = 1000,fixedRate = 5000)
    public void fetchRuFireData() throws IOException, InterruptedException, ParseException {
        LocalDate today = LocalDate.now();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); //Hate.

        // Initialise new connection client.
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(FIRMS_URL
                +COUNTRY+"/csv/"+"d984deaf88d96c52da828f97971ce95c"
                +"/"+"VIIRS_SNPP_NRT"
                +"/RUS"
                +"/"+ 1
                +"/"+today)).build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build().parse(csvBodyReader);

        for (CSVRecord record : records) {
            Fire newFire = new Fire();

            newFire.setAlertsAcquisitionDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            newFire.setLatitude(Double.parseDouble(record.get("latitude")));
            newFire.setLongitude(Double.parseDouble(record.get("longitude")));
            newFire.setSatellite(record.get("satellite"));
            newFire.setInstrument(record.get("instrument"));
            newFire.setBrightness(Double.parseDouble(record.get("bright_ti4")));
            newFire.setCountryId("RUS");
            newFire.setFirms_acq_time(dateFormatter.parse(record.get("acq_date")));

            fireRepository.save(newFire);
        }
    }
}
