/**
 * Code snippets that show how previous Android releases
 * supported Instant Messaging.
 */

package com.paad.chapter9gtalk;

import com.google.android.gtalkservice.GTalkServiceConstants;
import com.google.android.gtalkservice.GroupChatInvitation;
import com.google.android.gtalkservice.IChatListener;
import com.google.android.gtalkservice.IChatSession;
import com.google.android.gtalkservice.IGTalkConnection;
import com.google.android.gtalkservice.IGTalkService;
import com.google.android.gtalkservice.IGroupChatInvitationListener;
import com.google.android.gtalkservice.IImSession;
import com.google.android.gtalkservice.IRosterListener;
import com.google.android.gtalkservice.Presence;
import android.provider.Im.Contacts;
import android.provider.Telephony.Sms;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;

public class GTalkTest extends Activity {

  public static final String ACTION_OTA_ELIMINATE = "com.paad.ota_eliminate_action";
  
  private IGTalkService gtalkService;
  private IGTalkConnection gTalk = null;
  private IImSession imSession = null;
  
  @Override 
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
        
    // -- Binding to the GTalk Service
    bindGTalk();
                
    // -- Connecting to a GTalk Session
    try {
      IGTalkConnection gTalk = gtalkService.getDefaultConnection();
     	IImSession session = gTalk.getDefaultImSession();        	
		} catch (RemoteException e) {}
		
		// -- Presense
		
		Uri uri = android.provider.Im.Contacts.CONTENT_URI_CHAT_CONTACTS;
		Cursor c = managedQuery(uri, null, null, null, null);
		if (c.moveToFirst()) {
		  do {
		    String username = c.getString(c.getColumnIndexOrThrow(Contacts.USERNAME));
		    int presence = c.getInt(c.getColumnIndexOrThrow(Contacts.PRESENCE_STATUS));

		    if (presence == Contacts.AVAILABLE) {
		      // TODO: Do something
		    }
		  } while (c.moveToNext());
		}

		IRosterListener listener = new IRosterListener.Stub() {
			public void presenceChanged(String _contact) throws RemoteException {
        // TODO React to presence change.
			}

			public void rosterChanged() throws RemoteException {
				// TODO React to roster change.
			}

			public void selfPresenceChanged() throws RemoteException {
				// TODO React to user's presense change.
		  }
		};

		try {
  	  imSession.addRemoteRosterListener(listener);
 		  IChatSession chatSession = imSession.createChatSession("");
			imSession.approveSubscriptionRequest("joe", "nickname", null);
			imSession.declineSubscriptionRequest("joe");
			imSession.removeContact("whathaveyoudoneforme@lately.com");
				
			imSession.blockContact("scoble@microsoft.com");			

			Presence p = imSession.getPresence();

			String customMessage = "Developing applications for Android. Professionally";
			p.setStatus(Presence.Show.DND, customMessage);
			imSession.setPresence(p);
		} catch (RemoteException e) { }

		// -- Sending Data Messages
		
		Location location = null;
			
		Intent intent = new Intent(ACTION_OTA_ELIMINATE);		
		intent.putExtra("long", String.valueOf(location.getLatitude()));
		intent.putExtra("lat", String.valueOf(location.getLatitude()));
		intent.putExtra("target", "Sarah Conner");
		
		try {
			intent.putExtra("sender", gTalk.getUsername());
							
			String username = "T1000@sky.net";
			imSession.sendDataMessage(username, intent);
			chatSession.sendDataMessage(intent);
		} catch (RemoteException e) {}

		// -- Sending / Receiving Text Chats and Managing Chat Sessions
		try {
			String recipient = "mychattarget@gmail.com";
			IChatSession chatSession = imSession.getChatSession(recipient);
			if (chatSession == null)
			  chatSession = imSession.createChatSession(recipient);

			chatSession.sendChatMessage("Hello World!");
			
			IChatListener groupChatListener = new IChatListener.Stub() {
			  // Fired when a one-to-one chat becomes a group chat.
			  public void convertedToGroupChat(String oldJid, String groupChatRoom, long groupId) throws RemoteException {
			    // TODO Notify user that the conversation is now a group chat.
			  }

			  // Fired when a new person joins a chat room.
				public void participantJoined(String groupChatRoom, String nickname) throws RemoteException {
			    // TODO Notify user that a new participant has joined the conversation.
			  }

			  // Fired when a participant leaves a chat room.
			  public void participantLeft(String groupChatRoom, String nickname) throws RemoteException {
  	      // TODO Notify user a chat participant left.
				}

			  public void chatRead(String groupChatRoom) throws RemoteException { }

			  public void newMessageReceived(String from, String body) throws RemoteException { }

		    public void chatClosed(String groupChatRoom) throws RemoteException {}
		  };

      chatSession.addRemoteChatListener(groupChatListener);
      imSession.addRemoteChatListener(groupChatListener);

			String address = "";
			String password = "";
			String nickname = "Android Development";
			String[] contacts = { "bill", "fred" };
			imSession.createGroupChatSession(nickname, contacts);
			imSession.joinGroupChatSession(address, nickname, password);

			IGroupChatInvitationListener listener1 = new IGroupChatInvitationListener.Stub() {
			  public boolean onInvitationReceived(GroupChatInvitation _invite) throws RemoteException {
			    String address = _invite.getRoomAddress();
			    String password = _invite.getPassword();
			    String nickname = _invite.getInviter();
			    imSession.joinGroupChatSession(address, nickname, password);
			    return true;
			  } 
			};
			
   	  imSession.addGroupChatInvitationListener(listener1);
			
  		IChatListener chatListener = new IChatListener.Stub() {
		    // Fired when a one-to-one chat becomes a group chat.
	 	    public void convertedToGroupChat(String oldJid, String groupChatRoom, long groupId) {
		      // TODO Notify user that the conversation is now a group chat.
		    }

		    // Fired when a new person joins a chat room.
		    public void participantJoined(String groupChatRoom, String nickname) {
		      // TODO Notify user that a new participant has joined the conversation.
		    }

  		  // Fired when a participant leaves a chat room.
  		  public void participantLeft(String groupChatRoom, String nickname) {
  		    // TODO Notify user a chat participant left.
  		  }

  		  public void newMessageReceived(String from, String body) {}
  
  	    public void chatClosed(String arg0) throws RemoteException {}
  
        public void chatRead(String arg0) throws RemoteException {}
  		};	
    } 
    catch (DeadObjectException e) {} 
    catch (RemoteException e1) {}

    // -- Receiving Data Messages
    IntentFilter otaGTalkIntentReceiverFilter = new IntentFilter(ACTION_OTA_ELIMINATE);
		registerReceiver(otaGTalkIntentReceiver, otaGTalkIntentReceiverFilter);  
	}
		
  public class otaGTalkIntentReceiver extends BroadcastReceiver {
	  @Override
	  public void onReceive(Context _context, Intent _intent) {
	    if (_intent.getAction().equals(ACTION_OTA_ELIMINATE)) {
	      String sender = _intent.getStringExtra("sender");
	      String target = _intent.getStringExtra("target");

	      String lat = _intent.getStringExtra("lat");
	      String lng = _intent.getStringExtra("long");
	      Location location = new Location(LocationManager.GPS_PROVIDER);
	      location.setLatitude(Double.parseDouble(lat));
	      location.setLongitude(Double.parseDouble(lng));

	      // TODO Do something with the data transmitted.
	    }
	  }
  }

  private void bindGTalk() {
    Intent i = new Intent();

    i.setComponent(GTalkServiceConstants.GTALK_SERVICE_COMPONENT);
    bindService(i, gTalkConnection, 0);
  }
    
  private ServiceConnection gTalkConnection = new ServiceConnection() {
    // When the service connects, get the default GTalk session.
    public void onServiceConnected(ComponentName className, IBinder service) {
      IGTalkService gtalkService = IGTalkService.Stub.asInterface(service);
      try {
        gTalk = gtalkService.getDefaultConnection();
        imSession = gTalk.getDefaultImSession();
        
        IGTalkConnection gTalkUserTwo = gtalkService.getConnectionForUser("two");

        imSession.requestRoster();
      } catch (RemoteException e) {}
    }

    // When the service disconnects, clear the GTalk session.
    public void onServiceDisconnected(ComponentName className) {
      gTalk = null;  
      imSession = null;
    }
  };
}