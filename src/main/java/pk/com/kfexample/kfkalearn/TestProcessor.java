package pk.com.kfexample.kfkalearn;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;

import java.time.Duration;

public class TestProcessor implements Processor<String, String> {
    ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        context.schedule(Duration.ofSeconds(15), PunctuationType.WALL_CLOCK_TIME, timestamp -> {
                    //  System.out.printf("task id%s, offset=%s, par=%s", context.taskId(), context.offset(), context.partition());

                    System.out.println("For " + context.partition() + " inside the puncotate = " + timestamp);
                    //context.forward(new Record<String, String>("key1", "va1", timestamp), timestamp);
                }
        );
    }

    @Override
    public void process(String key, String value) {
        System.out.printf("\ntask id%s, offset=%s, par=%s, %s", context.taskId(), context.offset(), context.partition(), value);
    }

    @Override
    public void close() {

    }


}
