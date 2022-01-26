package com.team254.lib.geometry;

import com.team254.lib.util.Interpolable;

import io.github.oblarg.oblog.Loggable;

public interface State<S> extends Interpolable<S>, Loggable {
    double distance(final S other);

    boolean equals(final Object other);

    String toString();

    String toCSV();
}
