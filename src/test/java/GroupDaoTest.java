
import static org.junit.Assert.assertEquals;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Inject;
import com.googlecode.objectify.ObjectifyService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.di.ApplicationModule;
import ro.foodApp.entities.Group;

import javax.inject.Named;
import java.io.Closeable;
import java.io.IOException;

@Ignore
@RunWith(GuiceTestRunner.class)
@GuiceModules({ApplicationModule.class})
public class GroupDaoTest {

    static {
        ObjectifyService.register(Group.class);
    }

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private Closeable closeable;

    @Inject
    private GroupDAO groupDaoObjectify;

    @Inject
    @Named("entity")
    private GroupDAO groupDaoEntity;

    @Before
    public void setUp() {
        closeable = ObjectifyService.begin();
        helper.setUp();
    }

    @After
    public void tearDown() throws IOException {
        closeable.close();
        helper.tearDown();
    }

    @Test
    public void saveGroupObjectify() {
        Group group = TestUtils.createTestGroup("testGroupObjectify1", 1, 1);
        groupDaoObjectify.saveGroup(group);
        Group secondGroup = groupDaoObjectify.getGroupByName("testGroupObjectify1");
        assertEquals(group, secondGroup);
    }

    @Test
    public void saveGroupEntity() {
        Group group = TestUtils.createTestGroup("testGroup2", 1, 2);
        groupDaoEntity.saveGroup(group);
        Group secondGroup = groupDaoEntity.getGroupByName("testGroup2");
        assertEquals(group, secondGroup);
    }

    @Test
    public void addGroupMembersObjectify() {
        groupDaoObjectify.saveGroup(TestUtils.createTestGroup("testGroupObjectify1", 1, 0));
        Group group = groupDaoObjectify.getGroupByName("testGroupObjectify1");

        groupDaoObjectify.addMembers(TestUtils.createTestUserList("user", 4), group.getName());
        Group secondGroup = groupDaoObjectify.getGroupByName("testGroupObjectify1");

        assertEquals(group, secondGroup);
        assertEquals(4, secondGroup.getMembers().size());

    }

    @Test
    public void addGroupMembersEntity() {
        groupDaoEntity.saveGroup(TestUtils.createTestGroup("testGroupObjectify1", 1, 1));
        Group group = groupDaoEntity.getGroupByName("testGroupObjectify1");

        groupDaoEntity.addMembers(TestUtils.createTestUserList("user", 5), group.getName());
        Group secondGroup = groupDaoEntity.getGroupByName("testGroupObjectify1");

        assertEquals(group, secondGroup);
        assertEquals(6, secondGroup.getMembers().size());
    }


}