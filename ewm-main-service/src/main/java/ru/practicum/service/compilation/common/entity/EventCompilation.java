package ru.practicum.service.compilation.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.service.event.common.entity.Event;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event_compilation", schema = "public")
public class EventCompilation {
    @EmbeddedId
    private EventCompilationId id;

    @ManyToOne
    @MapsId("compilationId")
    @JoinColumn(name = "compilation_id")
    private Compilation compilation;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private Event event;

    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    @Setter
    @Getter
    public static class EventCompilationId implements Serializable {
        @Column(name = "event_id")
        private Long eventId;

        @Column(name = "compilation_id")
        private Long compilationId;
    }
}
