package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestTerminal {
    private static final Logger log = LogManager.getLogger(TestTerminal.class);

    @Test
    public void testSamplePart1() {
        var inputStream = TestTerminal.class.getClassLoader().getResourceAsStream("sample.txt");

        var commands = Terminal.readStream(inputStream);

        var root = Filesystem.getRoot();
        Terminal.init(commands, root);

        log.info(root);

        assertThat(root.getSize(), is(48381165));

        var total = root.getAllDirs().stream()
                .filter(d -> d.getSize() <= 100000)
                .map(Filesystem.Directory::getSize)
                .reduce(0, Integer::sum);

        assertThat(total, is(95437));
    }

    @Test
    public void testInputPart1() {
        var inputStream = TestTerminal.class.getClassLoader().getResourceAsStream("input.txt");

        var commands = Terminal.readStream(inputStream);

        var root = Filesystem.getRoot();
        Terminal.init(commands, root);

        var total = root.getAllDirs().stream()
                .map(Filesystem.Directory::getSize)
                .filter(s -> s <= 100000)
                .reduce(0, Integer::sum);

        log.info(total);
        assertThat(total, is(1367870));
    }

    @Test
    public void testSamplePart2() {
        var inputStream = TestTerminal.class.getClassLoader().getResourceAsStream("sample.txt");

        var commands = Terminal.readStream(inputStream);

        var root = Filesystem.getRoot();
        Terminal.init(commands, root);

        var sizeOfRoot = root.getSize();

        assertThat(sizeOfRoot, is(48381165));

        var unused = 70000000 - sizeOfRoot;
        var target = 30000000 - unused;

        assertThat(target, is(8381165));

        var toDelete = root.getAllDirs().stream()
                .map(Filesystem.Directory::getSize)
                .filter(t -> t >= target)
                .sorted()
                .findFirst().get();

        assertThat(toDelete, is(24933642));
    }

    @Test
    public void testInputPart2() {
        var inputStream = TestTerminal.class.getClassLoader().getResourceAsStream("input.txt");

        var commands = Terminal.readStream(inputStream);

        var root = Filesystem.getRoot();
        Terminal.init(commands, root);

        var sizeOfRoot = root.getSize();
        var unused = 70000000 - sizeOfRoot;
        var target = 30000000 - unused;

        var toDelete = root.getAllDirs().stream()
                .map(Filesystem.Directory::getSize)
                .filter(t -> t >= target)
                .sorted()
                .findFirst().get();

        log.info(toDelete);
        assertThat(toDelete, is(549173));
    }
}
