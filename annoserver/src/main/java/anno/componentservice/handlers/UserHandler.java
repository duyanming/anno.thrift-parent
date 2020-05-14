package anno.componentservice.handlers;

import anno.componentservice.events.UserEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserHandler {
    @EventListener(condition = "#user.name!=null")
    public void handleEvent(UserEvent user) throws Exception{
        System.out.println(user.getName());
        Thread.sleep(2000);
        System.out.println(user.getId());
    }
}
