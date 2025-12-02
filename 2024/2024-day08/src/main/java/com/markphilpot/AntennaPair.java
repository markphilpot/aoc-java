package com.markphilpot;

import java.util.List;
import java.util.Objects;

public record AntennaPair(Antenna a, Antenna b) {
  public List<Antinode> getAntinodes() {
    var rise = a.p().y() - b.p().y();
    var run = a.p().x() - b.p().x();

    return List.of(
            new Antinode(new Point(a.p().x() + run, a.p().y() + rise), a.signal()),
            new Antinode(new Point(b.p().x() - run, b.p().y() - rise), a.signal())
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AntennaPair that = (AntennaPair) o;
    return Objects.equals(a, that.a) && Objects.equals(b, that.b) || Objects.equals(a, that.b) && Objects.equals(b, that.a);
  }

  @Override
  public int hashCode() {
    return Objects.hash(a, b);
  }
}
