import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

public class Reactor {
    @Test
    public void init() {
        Mono.empty();
        Flux.empty();

        Mono<Integer> mono = Mono.just(1);
        Flux<Integer> flux = Flux.just(1, 2, 3);

        Flux<Integer> fluxFromMono = mono.flux();
        Mono<Boolean> monoFromFlux = flux.any(s -> s.equals(1));
        Mono<Integer> integerMono = flux.elementAt(1);


    }

    @Test
    public void range() {
        Flux.range(1, 5).subscribe(System.out::println);
    }

    @Test
    public void fromIterable() {
        Flux.fromIterable(Arrays.asList(1, 2, 3)).subscribe(System.out::println);
    }

    @Test
    public void generator() throws InterruptedException {
        Flux.<String>generate(sink -> {
                    sink.next("Hello");
                })
                .delayElements(Duration.ofMillis(500))
                .take(4)
                .subscribe(System.out::println);

        Thread.sleep(4000);
    }

    @Test
    public void generator2() throws InterruptedException {
        Flux<Object> producer = Flux
                .generate(
                        () -> 2345,
                        (state, sink) -> {
                            sink.next("Hello " + state);
                            if (state == 2366) {
                                sink.complete();
                            }
                            return state + 3;
                        }
                );


        Flux.create(sink -> {
            producer.subscribe(new BaseSubscriber<Object>(){
                @Override
                protected void hookOnNext(Object value) {
                    sink.next(value);
                }

                @Override
                protected void hookOnComplete() {
                    sink.complete();
                }
            });
        }).subscribe(System.out::println);
    }

    @Test
    public void connect() {
        Flux<String> second =
                Flux
                        .just("World","coder")
                        .repeat();

        Flux<String> first =
                Flux
                        .just("Hello","reactive","lazy")
                        .zipWith(second, (l, r) -> String.format("%s %s!", l, r));

        first
                .subscribe(System.out::println);

    }

}
