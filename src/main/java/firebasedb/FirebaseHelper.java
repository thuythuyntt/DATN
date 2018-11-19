/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firebasedb;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.annotations.Nullable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Message;
import model.User;
import util.*;

/**
 *
 * @author thuy
 */
public final class FirebaseHelper {

    public interface RoomMessageChangeListener {

        void onEvent(String toUserId, List<Message> list);

        void onEvent(String userA, String userB, List<Message> list);
    }

    private Firestore db = null;
    private User authUser;
//    private boolean isFirstLoad = true;

    private FirebaseHelper() {
        initFirebase();
    }

    private static FirebaseHelper instance = null;

    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public User getAuthUser() {
        return authUser;
    }

//    public void setIsFirstLoad(boolean isFirstLoad) {
//        this.isFirstLoad = isFirstLoad;
//    }
    private void initFirebase() {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("service-account.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("graduation-thesis-64adc.appspot.com")
                    .setDatabaseUrl("https://graduation-thesis-64adc.firebaseio.com/")
                    .build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
//            bucket = StorageClient.getInstance().bucket();
            System.out.println("FirebaseApp.initializeApp success");
        } catch (Exception ex) {
            System.out.println("FirebaseApp.initializeApp error");
            ex.printStackTrace();
        } finally {
            try {
                serviceAccount.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("FirebaseApp.initializeApp error");
            }
        }
    }

    public boolean checkLogin(String username, String password) {
        try {
            ApiFuture<QuerySnapshot> query = db.collection("account")
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password).get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            if (documents.isEmpty()) {
                return false;
            }
            QueryDocumentSnapshot doc = documents.get(0);
            authUser = new User();
            authUser.fromQueryDocument(doc);

            //lưu token ở firebase
            String token = Util.hash(authUser.getUsername() + authUser.getId());
            updateToken(token);

            //lưu file tmp
            Util.saveFile(token, Constants.TMP_FILE_NAME);

            System.out.println("checkLogin: " + authUser.toString());
            return true;
        } catch (Exception ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean checkAutoLogin(String token) {
        if (token == null || token.trim().length() == 0) {
            return false;
        }
        try {
            ApiFuture<QuerySnapshot> query = db.collection("account")
                    .whereEqualTo("token", token).get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            if (documents.isEmpty()) {
                System.out.println("checkAutoLogin false");
                return false;
            }
            QueryDocumentSnapshot doc = documents.get(0);
            authUser = new User();
            authUser.fromQueryDocument(doc);
            System.out.println("checkAutoLogin " + authUser.toString());
            return true;
        } catch (Exception ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public User getUserFromId(String id) {
        List<User> list = getListUsers();
        for (User u : list) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return new User();
    }

    public List<User> getListUsers() {
        List<User> list = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> query = db.collection("account").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            documents.forEach((doc) -> {
                User user = new User();
                user.fromQueryDocument(doc);
                list.add(user);
            });
            return list;
        } catch (Exception ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return list;
        }
    }

    public List<User> getListFriends() {
        List<User> listFriend = new ArrayList<User>();
        List<User> list = getListUsers();
        for (User u : list) {
            if (!u.getUsername().equals(authUser.getUsername())) {
                listFriend.add(u);
            }
        }
        return listFriend;
    }

    public List<User> getAllStudent() {
        List<User> listFriend = new ArrayList<User>();
        List<User> list = getListUsers();
        for (User u : list) {
            if (!u.getRole().equals(Constants.ROLE_TEACHER)) {
                listFriend.add(u);
            }
        }
        return listFriend;
    }

    public boolean sendMessage(Message message) {
        try {
            Map<String, String> data = new HashMap<>();
            data.put("fromUserId", message.getFromUserId());
            data.put("toUserId", message.getToUserId());
            data.put("text", message.getText());
            data.put("datetime", message.getDatetime());
            ApiFuture<DocumentReference> addedDocRef = db.collection("chat").add(data);
            return !addedDocRef.get().getId().isEmpty();
        } catch (InterruptedException ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void updatePersonInformation(String code,
            String dob,
            String faculty,
            String group,
            String fullname,
            String phone) {
        try {
            DocumentReference docRef = db.collection("account").document(authUser.getId());

            Map<String, Object> updates = new HashMap<>();
            updates.put("class", group);
            updates.put("code", code);
            updates.put("dob", dob);
            updates.put("faculty", faculty);
            updates.put("fullname", fullname);
            updates.put("phone", phone);

            ApiFuture<WriteResult> writeResult = docRef.update(updates);
            authUser.setGroup(group);
            authUser.setCode(code);
            authUser.setDob(dob);
            authUser.setFaculty(faculty);
            authUser.setFullname(fullname);
            authUser.setPhone(phone);

            System.out.println("Write result: " + writeResult.get());
        } catch (InterruptedException ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateToken(String token) {
        try {
            DocumentReference docRef = db.collection("account").document(authUser.getId());

            Map<String, Object> updates = new HashMap<>();
            updates.put("token", token);

            ApiFuture<WriteResult> writeResult = docRef.update(updates);
            authUser.setToken(token);

            System.out.println("Write result: " + writeResult.get());
        } catch (InterruptedException ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePassword(String newPassword) {
        try {
            DocumentReference docRef = db.collection("account").document(authUser.getId());

            Map<String, Object> updates = new HashMap<>();
            updates.put("password", newPassword);

            ApiFuture<WriteResult> writeResult = docRef.update(updates);
            authUser.setPassword(newPassword);

            System.out.println("Write result: " + writeResult.get());
        } catch (InterruptedException ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Đang bị double tin nhắn do có nhiều snapshotlistener cùng lắng nghe cùng một sự thay đổi:
    // khi click vài lần để chuyển tới cuộc trò chuyện nào đó, mỗi lần click là một lần tạo ra listener để lắng nghe sự thay đổi
    // => Tạo ra mảng chứa tin nhắn, chưa có thì thêm vào mảng và cập nhật lại giao diện chat.
    
    public void listenerGroupChatEvent(String toUserId, RoomMessageChangeListener listener) {
        mListGroupMessage = new ArrayList<>();
        db.collection("chat").whereEqualTo("toUserId", toUserId)
                .orderBy("datetime", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                            @Nullable FirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed:" + e);
                            return;
                        }
                        List<Message> lst = listenerEvent(snapshots);
                        List<Message> newMess = new ArrayList<Message>();
                        for (Message m : lst) {
                            if (!containsGroupMessage(m.getId())) {
                                newMess.add(m);
                                mListGroupMessage.add(m);
                            }
                        }
                        listener.onEvent(toUserId, newMess);
                    }
                });
    }
    public List<Message> mListSingleMessage;
    public List<Message> mListGroupMessage;

    public void getSingleMessage(String toUserId) {
        mListSingleMessage = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> query = db.collection("chat").whereEqualTo("fromUserId", authUser.getId()).whereEqualTo("toUserId", toUserId).get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            documents.forEach((doc) -> {
                Message m = new Message();
                m.fromQueryDocument(doc);
                mListSingleMessage.add(m);
            });

            ApiFuture<QuerySnapshot> query2 = db.collection("chat").whereEqualTo("fromUserId", toUserId).whereEqualTo("toUserId", authUser.getId()).get();
            QuerySnapshot querySnapshot2 = query2.get();
            List<QueryDocumentSnapshot> documents2 = querySnapshot2.getDocuments();
            documents2.forEach((doc) -> {
                Message m = new Message();
                m.fromQueryDocument(doc);
                mListSingleMessage.add(m);
            });

            Collections.sort(mListSingleMessage);

        } catch (Exception ex) {
            Logger.getLogger(FirebaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listenerSingleChatEvent(String toUserId, RoomMessageChangeListener listener) {
        getSingleMessage(toUserId);
        listener.onEvent(authUser.getId(), toUserId, mListSingleMessage);
        db.collection("chat").whereEqualTo("fromUserId", authUser.getId()).whereEqualTo("toUserId", toUserId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                    @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }
                List<Message> lst = listenerEvent(snapshots);
                List<Message> newMess = new ArrayList<Message>();
                for (Message m : lst) {
                    if (!containsSingleMessage(m.getId())) {
                        newMess.add(m);
                        mListSingleMessage.add(m);
                    }
                }
                listener.onEvent(authUser.getId(), toUserId, newMess);
            }
        });

        db.collection("chat").whereEqualTo("fromUserId", toUserId).whereEqualTo("toUserId", authUser.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                    @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed:" + e);
                    return;
                }
                List<Message> lst = listenerEvent(snapshots);
                List<Message> newMess = new ArrayList<Message>();
                for (Message m : lst) {
                    if (!containsSingleMessage(m.getId())) {
                        newMess.add(m);
                        mListSingleMessage.add(m);
                    }
                }
                listener.onEvent(toUserId, authUser.getId(), newMess);
            }
        });

    }

    private List<Message> listenerEvent(@Nullable QuerySnapshot snapshots) {
        List<Message> list = new ArrayList<>();
        for (DocumentChange dc : snapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                    Message m = new Message();
                    m.fromQueryDocument(dc.getDocument());
                    list.add(m);
                    System.out.println("New message: " + dc.getDocument().getData());
                    break;

                default:
                    break;
            }
        }
        return list;
    }

    public boolean containsSingleMessage(final String id) {
        return mListSingleMessage.stream().filter(o -> o.getId().equals(id)).findFirst().isPresent();
    }

    public boolean containsGroupMessage(final String id) {
        return mListGroupMessage.stream().filter(o -> o.getId().equals(id)).findFirst().isPresent();
    }
}
