package ru.rsreu.labs.exchange.disruptor;

import com.lmax.disruptor.EventFactory;
import ru.rsreu.labs.exchange.queue.tasks.CreateOrderTask;

public class CreateOrderEvent {
    public final static EventFactory<CreateOrderEvent> EVENT_FACTORY
        = CreateOrderEvent::new;
    private CreateOrderTask task;

    public void setTask(CreateOrderTask task){
        this.task = task;
    }

    public CreateOrderTask getTask(){
        return task;
    }
}
