package com.demo.aerztekasse.entity;

import java.time.DayOfWeek;
import java.util.List;

import com.demo.aerztekasse.entity.Deserializer.DayOfWeekDeserializer;
import com.demo.aerztekasse.entity.Serializer.DayOfWeekSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class DayOpening {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @JsonDeserialize(using = DayOfWeekDeserializer.class)
    @JsonSerialize(using = DayOfWeekSerializer.class)
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "opening_hours_id")
    private OpeningHours openingHours;

    @OneToMany(mappedBy = "dayOpening", cascade = CascadeType.ALL)
    private List<OpenInterval> intervals;
}