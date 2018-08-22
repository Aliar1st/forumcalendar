package ru.forumcalendar.forumcalendar.domain;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class SpeakerActivityFieldBridge implements TwoWayFieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneoptions) {
        Activity activity = (Activity) value;
        luceneoptions.addFieldToDocument(
                name, Integer.toString(activity.getId()), document
        );
    }

    @Override
    public Object get(String name, Document document) {
        return document.getField(name);
    }

    @Override
    public String objectToString(Object activityId) {
        return Integer.toString((int) activityId);
    }
}
