import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ro.foodApp.services.GroupTestController;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.dao.impl.objectify.GroupDAOImpl;
import ro.foodApp.entities.Group;

import java.util.ArrayList;
import java.util.List;


public class TaskQueueTest {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalTaskQueueTestConfig());


    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testTaskQueueSendEmails() {

        GroupDAO groupDao = Mockito.mock(GroupDAOImpl.class);
        Group group = Mockito.mock(Group.class);

        List<String> memberList = createMemberList();
        GroupTestController groupController = new GroupTestController(groupDao);
        LocalTaskQueue ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();

        doReturn(group).when(groupDao).getGroupByName("testGroup");
        doReturn(memberList).when(group).getMembers();

        QueueStateInfo qsi = ltq.getQueueStateInfo().get(QueueFactory.getDefaultQueue().getQueueName());
        assertEquals(0, qsi.getTaskInfo().size());

        groupController.sendEmails();

        qsi = ltq.getQueueStateInfo().get(QueueFactory.getDefaultQueue().getQueueName());
        assertEquals(1, qsi.getTaskInfo().size());

    }

    private List<String> createMemberList() {
        List<String> memberList = new ArrayList<String>();
        memberList.add("test@gmail.com");
        return memberList;
    }

}
