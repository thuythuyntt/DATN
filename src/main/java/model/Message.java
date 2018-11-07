/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.cloud.firestore.DocumentSnapshot;

/**
 *
 * @author thuy
 */
public class Message extends ModelBase{

    private String fromUserId;
    private String toUserId;
    private String text;
    private String datetime;

    public Message() {
    }

    public Message(String fromUserId, String toUserId, String text, String datetime) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.text = text;
        this.datetime = datetime;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public String getText() {
        return text;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getId() {
        return id;
    }

    @Override
    public void fromQueryDocument(DocumentSnapshot doc) {
        super.fromQueryDocument(doc);
        fromUserId = doc.getString("fromUserId");
        toUserId = doc.getString("toUserId");
        text = doc.getString("text");
        datetime = doc.getString("datetime");
    }

    @Override
    public String toString() {
        return "fromUserId: " + fromUserId + ", text: " + text + ", datetime: " + datetime; //To change body of generated methods, choose Tools | Templates.
    }
}
