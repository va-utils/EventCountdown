package com.vva.eventcountdown;

import java.io.Serializable;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MyEvent implements Serializable
{
    private long id;
    private String title;
    private String description;
    LocalDate localDate;

    long getId() { return id;}
    void setId(long id) { this.id = id;}

    long getEpochMillis()
    {
        localDate.toEpochDay();
        return 0;
    }

    String getTitle() {return title;}
    String getDescription() {return description;}
    String getLocalDateTimeString()
    {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return fmt.format(localDate);
    }

    Period getPeriod()
    {
        return Period.between(LocalDate.now(), localDate);
    }

    LocalDate getLocalDate() {return localDate;}

    void setTitle(String title)
    {
        this.title = title;
    }

    void setDescription(String description)
    {
        this.description = description;
    }

    void setLocalDate(int year, int mon, int day)
    {
        this.localDate = LocalDate.of(year,mon,day);
    }

    public MyEvent(long id, String title, String description, int year, int mon, int day)
    {
        setId(id);
        setTitle(title);
        setDescription(description);
        setLocalDate(year, mon, day);
    }
}
