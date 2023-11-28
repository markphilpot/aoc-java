package com.markphilpot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Elf {
    public record ElfCalories(Integer index, Integer calories) {}

    public static ElfCalories findLeader(InputStream inputStream) {
        var scanner = new Scanner(inputStream).useDelimiter("\n");

        ElfCalories current = new ElfCalories(1, 0);
        ElfCalories currentLeader = null;

        while(scanner.hasNext()) {
            var line = scanner.next();

            if(line.isEmpty()) {
                // Elf is complete
                if(currentLeader == null || currentLeader.calories() < current.calories()) {
                    currentLeader = current;
                }
                current = new ElfCalories(current.index() + 1, 0);
            } else {
                var calories = Integer.parseInt(line);
                current = new ElfCalories(current.index(), current.calories() + calories);
            }
        }
        // Check last
        if(currentLeader == null || currentLeader.calories() < current.calories()) {
            currentLeader = current;
        }

        return currentLeader;
    }

    public static List<ElfCalories> findTopThree(InputStream inputStream) {
        var scanner = new Scanner(inputStream).useDelimiter("\n");

        List<ElfCalories> all = new ArrayList<>();
        ElfCalories current = new ElfCalories(1, 0);

        while(scanner.hasNext()) {
            var line = scanner.next();

            if(line.isEmpty()) {
                // Elf is complete
                all.add(current);
                current = new ElfCalories(current.index() + 1, 0);
            } else {
                var calories = Integer.parseInt(line);
                current = new ElfCalories(current.index(), current.calories() + calories);
            }
        }

        // Get Last
        all.add(current);

        return all.stream().sorted(Comparator.comparing(ElfCalories::calories).reversed()).limit(3).toList();
    }
}
