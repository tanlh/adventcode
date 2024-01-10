package _2023;

import util.Util;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day20 {

    static int TIMES = 1000;
    static AtomicInteger lowCount = new AtomicInteger(0);
    static AtomicInteger highCount = new AtomicInteger(0);
    static Map<String, Module> modules = new HashMap<>();
    static Queue<Module> processingModules = new LinkedList<>();
    static long times = 0L;

    public static void main(String[] args) {
        parseModuleConfig();

        for (int i = 0; i < TIMES; i++) {
            pressButton();
        }
        System.err.println("Part 1: " + (lowCount.addAndGet(TIMES) * highCount.get()));

        // Lazy, just clear and parse module config again :))
//        modules.clear();
//        parseModuleConfig();
//        while (true) {
//            times++;
//            pressButton();
//        }

        // After above run, I found that
        // zp, high from: ds, times: 3733
        // zp, high from: sb, times: 3797
        // zp, high from: hf, times: 3877
        // zp, high from: nd, times: 3917
        // zp receive all high at times = LCM of 3733, 3797, 3877, 3917
        System.err.println("Part 2: " + Util.findLCM(List.of(3733, 3797, 3877, 3917)));
    }

    private static void pressButton() {
        var broadcaster = modules.get("broadcaster");
        broadcaster.receivePulse("", false);

        while (!processingModules.isEmpty()) {
            var processingModule = processingModules.poll();
            processingModule.processPulseQueue();
        }
    }

    private static void parseModuleConfig() {
        var lines = Util.readFileToLines();
        for (var line : lines) {
            var parts = line.split(" -> ");
            var namePrefix = parts[0];
            var destinationNames = Util.splitLine(parts[1], ",");

            Module module;
            if (namePrefix.startsWith("%")) {
                module = new FlipFlopModule(namePrefix.substring(1));
            } else if (namePrefix.startsWith("&")) {
                module = new ConjunctionModule(namePrefix.substring(1));
            } else {
                module = new Module(namePrefix);
            }

            module.setDestinationNames(destinationNames);
            modules.put(module.getName(), module);
        }

        List<Module> outputModule = new ArrayList<>();

        for (var module : modules.values()) {
            module.getDestinationNames().forEach(dest -> {
                var destModule = modules.get(dest);
                if (destModule == null) {
                    destModule = new Module(dest);
                    outputModule.add(destModule);
                }

                module.addDestination(destModule);
            });
        }

        modules.values().stream().filter(module -> module instanceof ConjunctionModule)
            .forEach(module ->
                modules.values().stream()
                    .filter(m -> m.getDestinations().contains(module))
                    .forEach(m -> ((ConjunctionModule) module).initInput(m.getName()))
            );

        outputModule.forEach(module -> modules.put(module.getName(), module));
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    static class Module {
        final String name;
        List<String> destinationNames;
        List<Module> destinations = new ArrayList<>();
        Queue<Boolean> pulseQueue = new LinkedList<>();

        void addDestination(Module destination) {
            this.destinations.add(destination);
        }

        void receivePulse(String fromModule, boolean pulse) {
            pulseQueue.add(pulse);
            if (!processingModules.contains(this)) {
                processingModules.offer(this);
            }
        }

        void processPulseQueue() {
            pulseQueue.forEach(this::sendPulse);
            pulseQueue.clear();
        }

        void sendPulse(boolean pulse) {
            destinations.forEach(dest -> {
                lowCount.addAndGet(!pulse ? 1 : 0);
                highCount.addAndGet(pulse ? 1 : 0);
                if (dest.getName().equals("zp") && pulse) {
                    System.err.println("zp, high from: " + this.name + ", times: " + times);
                }

                dest.receivePulse(name, pulse);
            });
        }

        @Override
        public String toString() {
            return "{name=" + name + ", destinations=" +
                destinations.stream().map(Module::getName).collect(Collectors.joining(",")) + "}";
        }
    }

    static class FlipFlopModule extends Module {
        private boolean isOn = false;

        public FlipFlopModule(String name) {
            super(name);
        }

        @Override
        void sendPulse(boolean pulse) {
            if (!pulse) {
                isOn = !isOn;
                super.sendPulse(isOn);
            }
        }
    }

    static class ConjunctionModule extends Module {
        final Map<String, Boolean> inputStates = new HashMap<>();

        ConjunctionModule(String name) {
            super(name);
        }

        void initInput(String inputName) {
            this.inputStates.put(inputName, false);
        }

        @Override
        void receivePulse(String fromModule, boolean pulse) {
            super.receivePulse(fromModule, pulse);
            inputStates.put(fromModule, pulse);
        }

        @Override
        protected void sendPulse(boolean pulse) {
            var newPulse = !inputStates.values().stream().allMatch(p -> p);
            super.sendPulse(newPulse);
        }
    }
}
