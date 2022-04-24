package com.rent.rent.component;

import com.rent.rent.model.Landlord;
import com.rent.rent.model.ObjectForRent;
import com.rent.rent.model.Reservation;
import com.rent.rent.model.Tenant;
import com.rent.rent.repository.LandlordRepository;
import com.rent.rent.repository.ObjectForRentRepository;
import com.rent.rent.repository.ReservationRepository;
import com.rent.rent.repository.TenantRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Component
public class Loader implements CommandLineRunner {

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    ObjectForRentRepository objectForRentRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    LandlordRepository landlordRepository;

    @Value("${dev.h2.create.random.data.enabled:false}")
    private boolean devH2CreateRandomDataEnabled;

    @Override
    public void run(String... args) {
        if (devH2CreateRandomDataEnabled) {

            final int LANDLORD_COUNT = 10;
            final int TENANT_COUNT = 30;
            final int OBJECT_FOR_RENT_COUNT = 20;
            final int RESERVATION = 100;

            Faker faker = new Faker(new Locale("pl"));

            List<Landlord> landlordList = new ArrayList<>();
            IntStream.rangeClosed(1, LANDLORD_COUNT).forEach(value ->
                    landlordList.add(
                            Landlord.builder()
                                    .name(faker.name().fullName())
                                    .build())
            );
            landlordRepository.saveAll(landlordList);

            List<Tenant> tenantList = new ArrayList<>();
            IntStream.rangeClosed(1, TENANT_COUNT).forEach(value ->
                    tenantList.add(
                            Tenant.builder()
                                    .name(faker.name().fullName())
                                    .build())
            );
            tenantRepository.saveAll(tenantList);

            List<ObjectForRent> objectForRentList = new ArrayList<>();
            IntStream.rangeClosed(1, OBJECT_FOR_RENT_COUNT).forEach(value ->
                    objectForRentList.add(
                            ObjectForRent.builder()
                                    .name(faker.company().name())
                                    .unitPricePerDay(BigDecimal.valueOf(faker.number().numberBetween(50, 200) - 0.01))
                                    .surface((double) (faker.number().numberBetween(200, 1000) / 10))
                                    .description(faker.restaurant().description())
                                    .landlord(landlordList.get(new Random().nextInt(landlordList.size())))
                                    .build())
            );
            objectForRentRepository.saveAll(objectForRentList);

            List<Reservation> reservationList = new ArrayList<>();
            IntStream.rangeClosed(1, RESERVATION).forEach(value -> {
                while (true) {
                    LocalDate start = getRandomLocalDate();
                    LocalDate end = start.plusDays(faker.number().numberBetween(0, 13));
                    ObjectForRent objectForRent = objectForRentList.get(new Random().nextInt(objectForRentList.size()));

                    if (reservationList.stream().noneMatch(r -> r.getObjectForRent().equals(objectForRent) &&
                            start.compareTo(r.getEnd()) <= 0 && end.compareTo(r.getStart()) >= 0)) {
                        reservationList.add(
                                Reservation.builder()
                                        .start(start)
                                        .end(end)
                                        .tenant(tenantList.get(new Random().nextInt(tenantList.size())))
                                        .objectForRent(objectForRent)
                                        .cost(BigDecimal.valueOf(ChronoUnit.DAYS.between(start, end) + 1).multiply(objectForRent.getUnitPricePerDay()))
                                        .build());
                        break;
                    }
                }
            });
            reservationRepository.saveAll(reservationList);
        }
    }

    private LocalDate getRandomLocalDate() {
        long minDay = LocalDate.of(2022, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2022, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }
}
