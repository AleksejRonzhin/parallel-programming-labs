package ru.rsreu.labs.exchange.disruptor;

import com.lmax.disruptor.EventFactory;
import ru.rsreu.labs.exchange.queue.requests.CreateOrderRequest;

public class CreateOrderEvent {
    public final static EventFactory<CreateOrderEvent> EVENT_FACTORY
        = CreateOrderEvent::new;
    private CreateOrderRequest task;

    public void setTask(CreateOrderRequest task){
        this.task = task;
    }

    public CreateOrderRequest getTask(){
        return task;
    }
}
