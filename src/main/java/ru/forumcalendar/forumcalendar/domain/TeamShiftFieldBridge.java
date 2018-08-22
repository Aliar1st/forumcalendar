package ru.forumcalendar.forumcalendar.domain;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class TeamShiftFieldBridge implements TwoWayFieldBridge {
    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneoptions) {
        Shift shift = (Shift) value;
        luceneoptions.addFieldToDocument(
                name, Integer.toString(shift.getId()), document
        );
    }

    @Override
    public Object get(String name, Document document) {
        return document.getField(name);
    }

    @Override
    public String objectToString(Object shiftId) {
        return Integer.toString((int)shiftId);
    }
}