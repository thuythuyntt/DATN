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
public class ModelBase {
    String id;
    
    public void fromQueryDocument(DocumentSnapshot doc) {
        id = doc.getId();
    }
}
