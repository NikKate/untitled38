package org.example;

import javafx.concurrent.Task;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class Main {

    public static BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);
    private static class Task implements Runnable {

        public final BlockingQueue<String> queue;
        public final char symbol;

        public Task(BlockingQueue<String> queue, char symbol) {
            this.queue = queue;
            this.symbol = symbol;
        }

    public static void main(String[] args) throws InterruptedException {
        Thread textGenerator = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String text = generateText("abc", 10000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread counterA = new Thread(new Task(queue1, 'a'));
        Thread counterB = new Thread(new Task(queue2, 'b'));
        Thread counterC = new Thread(new Task(queue3, 'c'));

        textGenerator.start();
        counterA.start();
        counterB.start();
        counterC.start();

        textGenerator.join();
        counterA.join();
        counterB.join();
        counterC.join();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

        @Override
        public void run() {
            int maxCount = 0;
            String maxString = "";
            for (int i = 0; i < 10000; i++) {
                try {
                    String text = this.queue.take();
                    int count = (int) IntStream.range(0, text.length()).filter(j -> text.charAt(j) == this.symbol).count();
                    if (count > maxCount) {
                        maxString = text;
                        maxCount = count;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.printf("Текст с максимальным количеством (%d) символов %s: %s\n", maxCount, this.symbol, maxString);
//

        }
    }

}


