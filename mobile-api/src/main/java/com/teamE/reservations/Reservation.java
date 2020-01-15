package com.teamE.reservations;

import com.teamE.rooms.Room;
import com.teamE.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private LocalDateTime dateTime;

    private Duration duration;

    private int numberOfPeople;

    private boolean accepted;

    public BigDecimal getPrice() {
        Duration interval = this.getRoom().getConfiguration().getRentInterval();

        int intervalsCount = getIntervalsCount(interval);
        BigDecimal pricePerInterval = this.getRoom().getConfiguration().getPricePerInterval();
        return Objects.equals(null, pricePerInterval) ? BigDecimal.ZERO : pricePerInterval.multiply(BigDecimal.valueOf(intervalsCount));
    }

    private int getIntervalsCount(Duration interval) {
        Duration sum = interval;
        int intervalCount = 1;
        for (; sum.compareTo(this.duration) < 0; intervalCount++) {
            sum = sum.plus(interval);
        }
        return intervalCount;
    }

    public boolean switchAccepted() {
        this.accepted = !this.accepted;
        return accepted;
    }

}

