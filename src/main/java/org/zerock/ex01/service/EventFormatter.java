package org.zerock.ex01.service;

import jdk.jfr.Event;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class EventFormatter implements Formatter<Event> {
    @Override
    public Event parse(String text, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public String print(Event object, Locale locale) {
        return null;
    }
}
