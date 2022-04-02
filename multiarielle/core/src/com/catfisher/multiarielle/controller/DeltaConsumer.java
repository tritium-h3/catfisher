package com.catfisher.multiarielle.controller;

import com.catfisher.multiarielle.controller.delta.Delta;

public interface DeltaConsumer<Response> {
    Response consume(Delta e);
}
