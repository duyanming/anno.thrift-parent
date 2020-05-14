package anno.componentservice.handlers;

import anno.componentservice.events.UserEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserHandler {
    @EventListener(condition = "#user.name!=null")
    public void handleEvent(UserEvent user) throws Exception{
        System.out.println(user.getName());
        System.out.println(user.getId());
    }
}
