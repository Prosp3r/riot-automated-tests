package mobilestests_ios;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_ios.RiotRoomPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests about Recents lists: badge, room name, room avatar, last event on room items.
 * @author jeangb
 */
@Listeners({ ScreenshotUtility.class })
public class RiotRoomsListItemsTests extends RiotParentTest{
private String roomNameTest="auto test list items";
private String riotUserDisplayName="riotuser11";
	/**
	 * Tests that some events are not displayed in the last event label on the rooms list.
	 * 1. Open room roomNameTest
	 * 2. Send a message
	 * 3. Change user avatar
	 * 4. Change user display name
	 * 5. Come back on the rooms list.
	 * Check that the last event is the msg sent in step 2.
	 * 6.  Set the old display name
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","checkuser"})
	public void roomItemExcludedLastEventsTest() throws InterruptedException{
		int randInt = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomMsg=(new StringBuilder("last event msg").append(randInt)).toString();
		String newDisplayName=riotUserDisplayName+"-";
		
		RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		//1. Open room roomNameTest
		roomsList.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects roomPage = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		//2. Send a message
		roomPage.sendAMessage(randomMsg);
		//3. Change user avatar
		roomPage.menuBackButton.click();
		roomsList.settingsButton.click();
		roomsList.changeAvatarFromSettings();
		//4. Change user display name
		roomsList.changeDisplayNameFromSettings(newDisplayName);
		roomsList.saveNavBarButton.click();
		//5. Come back on the rooms list.
		roomsList.backMenuButton.click();
		//Check that the last event is the msg sent in step 2
		Assert.assertEquals(roomsList.getLastEventByRoomName(roomNameTest,true), newDisplayName+": "+randomMsg);
		//6.  Set the old display name
		roomsList.settingsButton.click();
		roomsList.changeDisplayNameFromSettings(riotUserDisplayName);
		roomsList.saveNavBarButton.click();
		roomsList.backMenuButton.click();
	}
	
	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 * @throws YamlException 
	 * @throws FileNotFoundException 
	 */
	@BeforeGroups("checkuser")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver1(), riotUserDisplayName, Constant.DEFAULT_USERPWD);
	}
}
