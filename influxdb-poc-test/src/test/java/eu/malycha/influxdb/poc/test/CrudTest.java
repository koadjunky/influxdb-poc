package eu.malycha.influxdb.poc.test;

import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.InfluxQLQuery;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.InfluxQLQueryApi;
import com.influxdb.query.InfluxQLQueryResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


class CrudTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudTest.class);

    static InfluxDBClient influxDBClient;
    static String org = "dev";
    static String bucket = "db0";

    @BeforeAll
    static void setUp() {
        char [] token = "admin-secret-token".toCharArray();
        influxDBClient = InfluxDBClientFactory.create("http://localhost:8086", token, org, bucket);
    }

    @AfterAll
    static void tearDown() {
        influxDBClient.close();
    }

    @Test
    void addRecord_synchronousBlocking() {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        Exposure exposure = Exposure.newBuilder()
            .withKey(UUID.randomUUID().toString())
            .withCurrency("USD")
            .withPortfolio("Pocket")
            .withTime(Instant.now())
            .withValue(new BigDecimal("1000"))
            .build();

        writeApi.writeMeasurement(WritePrecision.NS, exposure);
    }

    @Test
    void deleteRecord() {
        DeleteApi deleteApi = influxDBClient.getDeleteApi();

        OffsetDateTime start = OffsetDateTime.now().minus(1, ChronoUnit.YEARS);
        OffsetDateTime stop = OffsetDateTime.now();

        deleteApi.delete(start, stop, "currency = \"USD\"", bucket, org);
    }

    @Test
    void queryRecord() throws Exception {
        String flux = "from(bucket: \"db0\") |> range(start:0)";

        QueryApi queryApi = influxDBClient.getQueryApi();

        queryApi.queryRaw(flux, (cancellable, line) -> {
           LOGGER.info("Response: {}", line);
        });

        Thread.sleep(5000);
    }

    @Test
    void queryRecordKeys() throws Exception {
        String queryString = "show tag values from \"exposure\" with key=\"key\"";

        InfluxQLQuery query = new InfluxQLQuery(queryString, bucket).setPrecision(InfluxQLQuery.InfluxQLPrecision.MILLISECONDS);

        InfluxQLQueryApi queryApi = influxDBClient.getInfluxQLQueryApi();

        InfluxQLQueryResult result = queryApi.query(query, (columnName, rawValue, resultIndex, seriesName) -> switch (columnName) {
            case "time" -> Instant.ofEpochSecond(Long.parseLong(rawValue));
            default -> rawValue;
        });

        for (InfluxQLQueryResult.Result resultResult : result.getResults()) {
            LOGGER.info("Index: {}", resultResult.getIndex());
            for (InfluxQLQueryResult.Series series : resultResult.getSeries()) {
                LOGGER.info("Columns: {}", series.getColumns());
                for (InfluxQLQueryResult.Series.Record record : series.getValues()) {
                    LOGGER.info("Values: {}", record.getValueByKey("value"));
                }
            }
        }
    }
}
