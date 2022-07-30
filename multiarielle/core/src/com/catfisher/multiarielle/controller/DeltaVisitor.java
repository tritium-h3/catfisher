package com.catfisher.multiarielle.controller;

import com.catfisher.multiarielle.controller.delta.*;

public interface DeltaVisitor<Response> {
    Response visit(CharacterMoveDelta e);
    Response visit(CharacterAddDelta e);
    Response visit(CharacterRemoveDelta characterRemoveEvent);
    Response visit(EntityChangeDelta entityChangeDelta);
    Response visit(TileChangeDelta tileChangeDelta);
}
